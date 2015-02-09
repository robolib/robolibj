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
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import io.github.robolib.control.Trigger.ButtonScheduler;
import io.github.robolib.identifier.NamedSendable;
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.nettable.ITable;
import io.github.robolib.nettable.NetworkTable;
import io.github.robolib.nettable.entry.NumberArray;
import io.github.robolib.nettable.entry.StringArray;
import io.github.robolib.util.log.ILogger;
import io.github.robolib.util.log.Logger;

/**
 * The Class Scheduler.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Scheduler implements NamedSendable {
    
    /** The m_instance. */
    private static Scheduler m_instance;
    
    /** The m_log. */
    private static final ILogger LOG = Logger.get(Scheduler.class);
    
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
    
    /** The m_subsystems. */
    private static final Vector<Subsystem> SUBSYSTEMS = new Vector<Subsystem>();
    
    /** The m_buttons. */
    private static final Vector<ButtonScheduler> BUTTONS = new Vector<ButtonScheduler>();
    
    /** The m_additions. */
    private static final Vector<Command> ADDITIONS = new Vector<Command>();
    
    /** The m_command list. */
    private static final List<Command> COMMAND_LIST = new LinkedList<Command>();
    
    public synchronized static final void initialize(){
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
    public synchronized static Scheduler getInstance(){
        return m_instance;
    }
    
    /**
     * Instantiates a new scheduler.
     */
    private Scheduler(){
        initTable(NetworkTable.getTable("Scheduler"));
    }
    
    
    /**
     * Adds the.
     *
     * @param command the command
     */
    public static final void add(Command command){
        if(command != null)
            ADDITIONS.addElement(command);
    }
    
    /**
     * Adds the button.
     *
     * @param btn the btn
     */
    public static final void addButton(ButtonScheduler btn){
        if(btn != null)
            BUTTONS.addElement(btn);
    }
    
    /**
     * Adds a command immediately to the {@link Scheduler}. This should only be
     * called in the {@link Scheduler#run()} loop. Any command with conflicting
     * requirements will be removed, unless it is uninterruptable. Giving
     * <code>null</code> does nothing.
     *
     * @param command the {@link Command} to add
     */
    protected static final void add_internal(Command command){
        if(command == null) return;
        
        if(m_adding){
            LOG.warn("Cannot start command from cancel. Ignoring '" + command + "'");
            return;
        }
        
        if(!COMMAND_LIST.contains(command)){
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
                    COMMAND_LIST.remove(cmd);
                }
                system.setCurrentCommand(command);
            }

            m_adding = false;
            
            COMMAND_LIST.add(command);
            
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
    public static final void run(){
        m_runningCommandsChanged = false;
        
        if(m_disabled){
            if(++m_disabledCounter >= 32){
                LOG.warn("Scheduler is being called, but is disabled.");
                m_disabledCounter = 0;
            }
            return;
        }
        
        BUTTONS.forEach(ButtonScheduler::execute);
        
        Command cmd = null;
        for(Iterator<Command> iter = COMMAND_LIST.iterator();iter.hasNext();){
            cmd = iter.next();
            if(!cmd.run()){
                cmd.getRequirements().forEach(Subsystem::nullifyCurrentCommand);
                cmd.removed();
                iter.remove();
                m_runningCommandsChanged = true;
            }
        }

//        iter = m_additions.iterator();
//        for(;iter.hasNext();){
//            add_impl(iter.next());
//        }
        
        ADDITIONS.forEach(Scheduler::add_internal);
        ADDITIONS.removeAllElements();
        
        SUBSYSTEMS.forEach(Subsystem::iterationRun);
        
        updateTable();
    }
    
    /**
     * Registers a {@link Subsystem} to this {@link Scheduler}, so that the
     * {@link Scheduler} might know if a default {@link Command} needs to be
     * run. All {@link Subsystem Subsystems} should call this.
     *
     * @param system the system
     */
    static final void registerSubsystem(Subsystem system){
        if(system != null)
            SUBSYSTEMS.addElement(system);
    }
    
    /**
     * Removes the all.
     */
    public static final void removeAll(){
        Command cmd;
        for(Iterator<Command> iter = COMMAND_LIST.iterator(); iter.hasNext();){
            cmd = iter.next();            
            cmd.removed();
            iter.remove();
        }
        SUBSYSTEMS.forEach(Subsystem::nullifyCurrentCommand);
    }
    
    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    public static final void setEnabled(boolean enabled){
        m_disabled = !enabled;
    }
    
    /**
     * Gets the type.
     *
     * @return the type
     */
    public final String getType(){
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
    public final String getName(){
        return "Scheduler";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSmartDashboardType() {
        return "Scheduler";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final void initTable(ITable subtable){
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
    public final ITable getTable() {
        return m_table;
    }
    
    public static void updateTable(){
        if(m_table != null){
            m_table.retrieveValue("Cancel", toCancel);

            if(toCancel.size() > 0){
                COMMAND_LIST.forEach(cmd -> {
                    for(int i = 0; i < toCancel.size(); i++){
                        if(cmd.hashCode() == toCancel.get(i)){
                            cmd.cancel();
                            return;
                        }
                    }
                });
            }
            toCancel.setSize(0);
            m_table.putValue("Cancel", toCancel);
        }
        
        if(m_runningCommandsChanged){
            commands.setSize(0);
            ids.setSize(0);
            COMMAND_LIST.forEach(cmd -> {
                commands.add(cmd.getName());
                ids.add(cmd.hashCode());
            });
            m_table.putValue("Names", commands);
            m_table.putValue("Ids", ids);
        }
    }
}
