/*
 * Copyright (c) 2015 noriah <vix@noriah.dev>.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 */

package io.github.robolib.command;

import java.util.ArrayList;
import java.util.List;

import io.github.robolib.DriverStation;
import io.github.robolib.identifier.NamedSendable;
import io.github.robolib.module.RoboRIO;
import io.github.robolib.nettable.ITable;
import io.github.robolib.nettable.ITableListener;

/**
 * The Class Command.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class Command implements NamedSendable {
    
    /** The m_name. */
    private String m_name;
    
    /** The m_start time. */
    private double m_startTime = -1;
    
    /** The m_timeout. */
    private double m_timeout = -1;
    
    /** The m_initialized. */
    private boolean m_initialized = false;
    
    /** The m_requirements. */
    private List<Subsystem> m_requirements;
    
    /** The m_running. */
    protected boolean m_running = false;
    
    /** The m_interruptible. */
    private boolean m_interruptible = true;
    
    /** The m_canceled. */
    private boolean m_canceled = false;
    
    /** The m_locked. */
    protected boolean m_locked = false;
    
    /** The m_run when disabled. */
    private boolean m_runWhenDisabled = false;
    
    /** The m_cancel on mode switched */
    private boolean m_cancelOnModeSwitched = true;
    
    protected Command m_previousCommand;
    
    protected Command m_nextCommand;
    
    /** The m_parent. */
    private CommandGroup m_parent;

    /** The m_table. */
    private ITable m_table;
    
    protected byte m_state;
    
    
    /**
     * Instantiates a new command.
     */
    public Command(){
        m_name = getClass().getCanonicalName();
    }
    
    public Command(Subsystem system){
        this();
        requires(system);
    }
    
    /**
     * Instantiates a new command.
     *
     * @param name the name
     */
    public Command(String name){
        if(name == null) throw new IllegalArgumentException("Name must not be null.");
        m_name = name;
    }
    
    /**
     * Instantiates a new command.
     *
     * @param timeout the timeout
     */
    public Command(double timeout){
        this();
        setTimeout(timeout);
    }
    
    /**
     * Instantiates a new command.
     *
     * @param name the name
     * @param timeout the timeout
     */
    public Command(String name, double timeout){
        this(name);
        setTimeout(timeout);
    }
    
    /**
     * Gets the name.
     *
     * @return the name
     */
    @Override
    public String getName(){
        return m_name;
    }
    
    /**
     * Sets the timeout. A value of -1 means no time out
     *
     * @param timeout the new timeout
     */
    protected final void setTimeout(double timeout){
        if(timeout < -1)
            throw new IllegalArgumentException("Timeout can not be less than -1.");
        synchronized (this) {
            m_timeout = timeout;
        }
    }
    
    /**
     * Time since initialized.
     *
     * @return the double
     */
    public final double timeSinceInitialized(){
        synchronized(this){
            return m_startTime < 0 ? 0 : RoboRIO.getFPGATimestamp() - m_startTime;
        }
    }
    
    /**
     * Requires.
     *
     * @param subsys the subsys
     */
    protected void requires(Subsystem subsys){
        if(m_locked)
            throw new IllegalStateException("Cannot add new requirement to command after being started or added to a command group.");
        if(subsys == null) return;
        if(m_requirements == null){
            synchronized(this){
                m_requirements = new ArrayList<Subsystem>(1);
            }
        }
        synchronized (m_requirements) {
            m_requirements.add(subsys);
        }
    }
    
    /**
     * Removed.
     */
    void removed(){
        synchronized(this){
            if(m_initialized){
                if(isCanceled()){
                    interrupted();
                    _interrupted();
                }else{
                    end();
                    _end();
                }
            }
            m_initialized = false;
            m_canceled = false;
            m_running = false;
            
            if(m_table != null){
                m_table.putBoolean("running", false);
            }
        }
    }
    
    /**
     * Run.
     *
     * @return true, if successful
     */
    boolean run(){
        synchronized(this){
            if(!m_runWhenDisabled && m_parent == null && DriverStation.isDisabled()) cancel();
            
            if(isCanceled()) return false;
            
            if(!m_initialized){
                m_initialized = true;
                startTiming();
                _initialize();
                initialize();
            }
            
            _execute();
            execute();
            
            return !isFinished();
        }
    }
    
    /**
     * The initialize method is called the first time this Command is run after
     * being started.
     */
    protected abstract void initialize();
    
    /**
     * Initialize_impl.
     */
    void _initialize(){}
    
    /**
     * The execute method is called repeatedly until this Command either finishes
     * or is canceled.
     */
    protected abstract void execute();
    
    /**
     * Execute_impl.
     */
    void _execute(){}
    
    /**
     * Returns whether this command is finished.
     * If it is, then the command will be removed
     * and {@link Command#end() end()} will be called.
     *
     * <p>It may be useful for a team to reference the {@link Command#isTimedOut() isTimedOut()} method
     * for time-sensitive commands.</p>
     * @return whether this command is finished.
     * @see Command#isTimedOut() isTimedOut()
     */
    protected abstract boolean isFinished();
    
    /**
     * Called when the command ended peacefully.  This is where you may want
     * to wrap up loose ends, like shutting off a motor that was being used
     * in the command.
     */
    protected abstract void end();
    
    /**
     * End_impl.
     */
    void _end(){}
    
    /**
     * Called when the command ends because somebody called {@link Command#cancel() cancel()}
     * or another command shared the same requirements as this one, and booted
     * it out.
     *
     * <p>This is where you may want
     * to wrap up loose ends, like shutting off a motor that was being used
     * in the command.</p>
     *
     * <p>Generally, it is useful to simply call the {@link Command#end() end()} method
     * within this method</p>
     */
    protected abstract void interrupted();
    
    /**
     * Interrupted_impl.
     */
    void _interrupted(){}
    
    /**
     * Start timing.
     */
    private void startTiming(){
        m_startTime = RoboRIO.getFPGATimestamp();
    }
    
    /**
     * Checks if is timed out.
     *
     * @return true, if is timed out
     */
    protected boolean isTimedOut(){
        synchronized(this){
            return m_timeout != -1 && (m_startTime < 0 ? 0 : RoboRIO.getFPGATimestamp() - m_startTime) >= m_timeout;
        }
    }
    
    /**
     * Gets the requirements.
     *
     * @return the requirements
     */
    List<Subsystem> getRequirements(){
        synchronized(this){
            return m_requirements == null ?
                    m_requirements = new ArrayList<Subsystem>(1) : m_requirements;
        }
    }

    /**
     * Sets the parent.
     *
     * @param parent the new parent
     */
    void setParent(CommandGroup parent){
        synchronized(this){
            if(m_parent != null)
                throw new IllegalStateException("Cannot change CommandGroup of command that is in a command group.");
            
            m_locked = true;
            m_parent = parent;
            if(m_table != null)
                m_table.putBoolean("isParented", true);
        }
    }
    
    
    /**
     * Start.
     */
    public void start(){
        synchronized(this){
            m_locked = true;
            if(m_parent != null)
                throw new IllegalStateException("Cannot start command that is in a command group.");
            
            Scheduler.add(this);
        }
    }
    
    /**
     * Start running.
     */
    void startRunning(){
        synchronized(this){
            m_running = true;
            m_startTime = -1;
            if(m_table != null) m_table.putBoolean("running", true);
        }
    }
    
    /**
     * Checks if is running.
     *
     * @return true, if is running
     */
    public boolean isRunning(){
        synchronized(this){
            return m_running;
        }
    }
    
    /**
     * Cancel.
     */
    public void cancel(){
        synchronized(this){
            if(m_parent != null)
                throw new IllegalStateException("Cannot stop command that is in a command group.");
        }
        _cancel();
    }
    
    /**
     * Cancel_impl.
     */
    void _cancel(){
        synchronized(this){
            if(m_running)
                m_canceled = true;
        }
    }
    
    /**
     * Checks if is canceled.
     *
     * @return true, if is canceled
     */
    public boolean isCanceled(){
        synchronized(this){
            return m_canceled;
        }
    }
    
    /**
     * Checks if is interruptible.
     *
     * @return true, if is interruptible
     */
    public boolean isInterruptible(){
        synchronized(this){
            return m_interruptible;
        }
    }
    
    /**
     * Sets the interruptible.
     *
     * @param interruptible the new interruptible
     */
    public void setInterruptible(boolean interruptible){
        synchronized(this){
            m_interruptible = interruptible;
        }
    }
    
    /**
     * Does require.
     *
     * @param system the system
     * @return true, if successful
     */
    public boolean doesRequire(Subsystem system){
        if(m_requirements == null)
            return false;
        synchronized(m_requirements){
            return m_requirements.contains(system);
        }
    }
    
    /**
     * Returns the {@link CommandGroup} that this command is a part of.
     * Will return null if this {@link Command} is not in a group.
     * @return the {@link CommandGroup} that this command is a part of (or null if not in group)
     */
    public CommandGroup getGroup(){
        synchronized(m_parent){
            return m_parent;
        }
    }
    
    /**
     * Sets whether or not this {@link Command} should run when the robot is disabled.
     *
     * <p>By default a command will not run when the robot is disabled, and will in fact be canceled.</p>
     * @param run whether or not this command should run when the robot is disabled
     */
    public void setRunWhenDisabled(boolean run) {
        m_runWhenDisabled = run;
    }
    
    /**
     * Returns whether or not this {@link Command} will run when the robot is disabled, or if it will cancel itself.
     * @return whether or not this {@link Command} will run when the robot is disabled, or if it will cancel itself
     */
    public boolean willRunWhenDisabled(){
        return m_runWhenDisabled;
    }
    
    /**
     * Sets whether or not this {@link Command} should continue running when the robot switches mode.
     * 
     * <p>By default a command will cancel when the mode is switched</p>
     * @param cancel whether or not this command should cancel when the robot switches mode.
     */
    public void setCancelOnModeSwitch(boolean cancel){
        m_cancelOnModeSwitched = cancel;
    }
    
    /**
     * Returns whether or not this {@link Command} will cancel when the robot switches modes.
     * @return whether or not this {@link Command} will cancel when the robot switches modes.
     */
    public boolean willCancelOnModeSwitch(){
        return m_cancelOnModeSwitched;
    }
    
    /**
     * The string representation for a {@link Command} is by default its name.
     * @return the string representation of this object
     */
    @Override
    public String toString(){
        return m_name;
    }
    
    public String getSmartDashboardType() {
        return "Command";
    }
    
    private ITableListener listener = new ITableListener() {
        public void valueChanged(ITable table, String key, Object value, boolean isNew) {
            if (((Boolean) value).booleanValue()) {
                start();
            } else {
                cancel();
            }
        }
    };
    
    public void initTable(ITable table) {
        if(m_table!=null)
            m_table.removeTableListener(listener);
        m_table = table;
        if(table!=null) {
            table.putString("name", getName());
            table.putBoolean("running", isRunning());
            table.putBoolean("isParented", m_parent != null);
            table.addTableListener("running", listener, false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }
    
    

}
