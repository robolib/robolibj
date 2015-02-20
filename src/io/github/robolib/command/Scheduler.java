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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import io.github.robolib.identifier.NamedSendable;
import io.github.robolib.identifier.Trigger.ButtonScheduler;
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.nettable.ITable;
import io.github.robolib.nettable.entry.NumberArray;
import io.github.robolib.nettable.entry.StringArray;
import io.github.robolib.util.log.ILogger;
import io.github.robolib.util.log.Logger;

/**
 * The Class Scheduler.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class Scheduler implements NamedSendable {
    
    private static class NullCommand extends Command{
        protected void initialize() {}
        protected void execute(){}
        protected boolean isFinished(){return false;}
        protected void end(){}
        protected void interrupted(){}
    }
    
    /** The m_instance. */
    private static Scheduler m_instance;
    
    /** The m_log. */
    private static final ILogger m_log = Logger.get(Scheduler.class);
    
    /** The m_table. */
    private static ITable m_table;
    
    /** The m_adding. */
    private static boolean m_adding = false;
    
    /** The m_disabled. */
    private static boolean m_disabled = false;
    
    /** The m_running commands changed. */
    private static boolean m_runningCommandsChanged;
    
    /** The m_disabled counter. */
    private static byte m_disabledCounter = 0;
    
    private static boolean m_running = false;
    
    /** The m_subsystems. */
    private static final Vector<Subsystem> m_subsystems = new Vector<Subsystem>();
    
    /** The m_buttons. */
    private static final Vector<ButtonScheduler> m_buttons = new Vector<ButtonScheduler>();
    
    /** The m_additions. */
    private static final Vector<Command> m_additions = new Vector<Command>();
    
    private static final Vector<Binding> m_binds = new Vector<Binding>();
    
    private static final Command m_firstCommand = new NullCommand();
    
    private static final Command m_lastCommand = new NullCommand();
    
    public synchronized static void initialize(){
        if(m_instance != null)
            throw new IllegalStateException("Scheduler already initialized.");
        
        m_instance = new Scheduler();
        UsageReporting.report(UsageReporting.ResourceType_Command, UsageReporting.Command_Scheduler);
        
    }
    
    /**
     * Gets the single instance of Scheduler.
     *
     * @return single instance of Scheduler
     */
    public static Scheduler getInstance(){
        synchronized(m_instance){
            return m_instance;
        }
    }
    
    /**
     * Instantiates a new scheduler.
     */
    private Scheduler(){
        
        m_firstCommand.m_nextCommand = m_lastCommand;
        m_lastCommand.m_previousCommand = m_firstCommand;
//        initTable(NetworkTable.getTable("Scheduler"));
//        SmartDashboard.putData(this);
    }
    
    
    /**
     * Adds the.
     *
     * @param command the command
     */
    public static void add(Command command){
        if(command != null)
            m_additions.addElement(command);
    }
    
    public static void addBind(Binding sink){
        m_binds.addElement(sink);
    }
    
    /**
     * Adds the button.
     *
     * @param btn the btn
     */
    public static void addButton(ButtonScheduler btn){
        if(btn != null)
            m_buttons.addElement(btn);
    }
    
    /**
     * Adds a command immediately to the {@link Scheduler}. This should only be
     * called in the {@link Scheduler#run()} loop. Any command with conflicting
     * requirements will be removed, unless it is uninterruptable. Giving
     * <code>null</code> does nothing.
     *
     * @param command the {@link Command} to add
     */
    protected static void add_internal(Command command){
        if(command == null) return;
        
        if(m_adding){
            m_log.warn("Cannot start command from cancel. Ignoring '" + command + "'");
            return;
        }
        
        if(!command.m_running){
            List<Subsystem> requires = command.getRequirements();
            if(requires.stream().anyMatch(Subsystem::getCurrentCommandNotInterruptable))
                return;
            
            m_adding = true;

            Command cmd;
            for(Subsystem system : requires){
                if((cmd = system.getCurrentCommand()) != null){
                    cmd.cancel();
                    cmd.getRequirements().forEach(Subsystem::nullifyCurrentCommand);
                    cmd.removed();
                }
                system.setCurrentCommand(command);
            }
            
            command.m_previousCommand = m_lastCommand.m_previousCommand;
            command.m_nextCommand = m_lastCommand;
            m_lastCommand.m_previousCommand = command;

            m_adding = false;
            
            m_runningCommandsChanged = true;
            command.startRunning();
        }
    }
    
    /**
     * Runs a single iteration of the loop. This method should be called often
     * in order to have a functioning {@link Command} system. The loop has five
     * stages:
     *
     * <ol> <li> Poll the Buttons </li> <li> Execute/Remove the Commands </li>
     * <li> Send values to SmartDashboard </li> <li> Add Commands </li> <li> Add
     * Defaults </li> </ol>
     */
    public static void run(){
        if(m_running){
            m_log.warn("Scheduler already running");
            return;
        }
        
        m_running = true;
        m_runningCommandsChanged = false;
        
        if(m_disabled){
            if(++m_disabledCounter >= 32){
                m_log.warn("Scheduler is being called, but is disabled.");
                m_disabledCounter = 0;
            }
            return;
        }
        
        m_buttons.forEach(ButtonScheduler::execute);
        
        m_binds.forEach(Binding::updateValue);
        
        Command cmd = null;
        Command nextCmd = m_firstCommand.m_nextCommand;
        
//        if(m_table != null){
//            m_table.retrieveValue("Cancel", toCancel);
//            
//            commands.setSize(0);
//            ids.setSize(0);
//            
//            while(nextCmd != m_lastCommand){
//                cmd = nextCmd;
//                nextCmd = cmd.m_nextCommand;
//                if
//                if(!cmd.run()){
//                    cmd.getRequirements().forEach(Subsystem::nullifyCurrentCommand);
//                    cmd.removed();
//
//                    m_runningCommandsChanged = true;
//                }else{
//                    commands.add(cmd.getName());
//                    ids.add(cmd.hashCode());
//                }
//            }
//
//            toCancel.setSize(0);
//            m_table.putValue("Cancel", toCancel);
//            
//            if(m_runningCommandsChanged){
//                m_table.putValue("Names", commands);
//                m_table.putValue("Ids", ids);
//            }
//            
//        }else{
//            
            while(nextCmd != m_lastCommand){
                cmd = nextCmd;
                nextCmd = cmd.m_nextCommand;
                if(!cmd.run()){
                    cmd.getRequirements().forEach(Subsystem::nullifyCurrentCommand);
                    cmd.removed();

                    m_runningCommandsChanged = true;
                }
            }
//        }
        
        cmd = null;
        nextCmd = null;
        
        m_additions.forEach(Scheduler::add_internal);
        m_additions.removeAllElements();
        
        m_subsystems.forEach(Subsystem::iterationRun);
        
//        updateTable();
        m_running = false;
    }
    
    /**
     * Registers a {@link Subsystem} to this {@link Scheduler}, so that the
     * {@link Scheduler} might know if a default {@link Command} needs to be
     * run. All {@link Subsystem Subsystems} should call this.
     *
     * @param system the system
     */
    static void registerSubsystem(Subsystem system){
        if(system != null)
            m_subsystems.addElement(system);
    }
    
    /**
     * Removes the all.
     */
    public static void removeAll(){
        Command cmd;
        Command nextCmd = m_firstCommand.m_nextCommand;
        while(nextCmd != m_lastCommand){
            cmd = nextCmd;
            nextCmd = cmd.m_nextCommand;
            cmd.removed();
        }
        
        cmd = null;
        nextCmd = null;
        
        for(Iterator<Subsystem> iter = m_subsystems.iterator(); iter.hasNext();){
            iter.next().nullifyCurrentCommand();
        }
    }
    
    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    public static void setEnabled(boolean enabled){
        m_disabled = !enabled;
    }
    
    /**
     * Gets the type.
     *
     * @return the type
     */
    public String getType(){
        return "Scheduler";
    }
    
    /** The commands. */
    private static StringArray commands;
    
    /** The to cancel. */
    private static NumberArray ids, toCancel;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getName(){
        return "Scheduler";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getSmartDashboardType() {
        return "Scheduler";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initTable(ITable subtable){
        m_table = subtable;
        commands = new StringArray();
        ids = new NumberArray();
        toCancel = new NumberArray();
        
        m_table.putValue("Names", commands);
        m_table.putValue("Ids", ids);
        m_table.putValue("Cancel", toCancel);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable() {
        return m_table;
    }
    
//    public static void updateTable(){
//        if(m_table != null){
//            m_table.retrieveValue("Cancel", toCancel);
//
//            if(toCancel.size() > 0){
//                m_commandList.forEach(cmd -> {
//                    for(int i = 0; i < toCancel.size(); i++){
//                        if(cmd.hashCode() == toCancel.get(i)){
//                            cmd.cancel();
//                            return;
//                        }
//                    }
//                });
//            }
//            toCancel.setSize(0);
//            m_table.putValue("Cancel", toCancel);
//        }
//    }
}
