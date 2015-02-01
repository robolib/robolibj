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

import java.util.Vector;

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.control.Trigger.ButtonScheduler;
import io.github.robolib.framework.RoboLibBot;
import io.github.robolib.util.Sendable;
import io.github.robolib.util.log.ILogger;
import io.github.robolib.util.log.Logger;

import edu.wpi.first.wpilibj.networktables2.type.NumberArray;
import edu.wpi.first.wpilibj.networktables2.type.StringArray;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The Class Scheduler.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Scheduler implements Sendable {
    
    /** The m_instance. */
    private static Scheduler m_instance;
    
    /** The m_log. */
    private final ILogger m_log;
    
    /** The m_table. */
    private ITable m_table;
    
    /** The m_adding. */
    private boolean m_adding = false;
    
    /** The m_disabled. */
    private boolean m_disabled = false;
    
    /** The m_running commands changed. */
    private boolean m_runningCommandsChanged;
    
    /** The m_disabled counter. */
    private byte m_disabledCounter = 0;
    
    /** The m_subsystems. */
    private Vector<Subsystem> m_subsystems = new Vector<Subsystem>();
    
    /** The m_buttons. */
    private Vector<ButtonScheduler> m_buttons = new Vector<ButtonScheduler>();
    
    /** The m_additions. */
    private Vector<Command> m_additions = new Vector<Command>();
    
    /** The m_command list. */
    private CommandList m_commandList = new CommandList();
    
    /**
     * Gets the single instance of Scheduler.
     *
     * @return single instance of Scheduler
     */
    public synchronized static Scheduler getInstance(){
        return m_instance == null ? m_instance = new Scheduler() : m_instance;
    }
    
    /**
     * Instantiates a new scheduler.
     */
    private Scheduler(){
        UsageReporting.report(UsageReporting.kResourceType_Compressor, UsageReporting.kCommand_Scheduler);
        m_log = Logger.get(Scheduler.class);
        initTable(RoboLibBot.getRobotTable().getSubTable("Scheduler"));
    }
    
    
    /**
     * Adds the.
     *
     * @param command the command
     */
    public void add(Command command){
        if(commands != null)
            m_additions.addElement(command);
    }
    
    /**
     * Adds the button.
     *
     * @param btn the btn
     */
    public void addButton(ButtonScheduler btn){
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
    private void add_impl(Command command){
        if(command == null) return;
        
        if(m_adding){
            m_log.warn("Cannot start command from cancel. Ignoring '" + command + "'");
            return;
        }
        
        if(!m_commandList.contains(command)){
            Vector<Subsystem> requires = command.getRequirements();
            if(requires.stream().anyMatch(system -> system.getCurrentCommand() != null &&
                    system.getCurrentCommand().isInterruptible())) return;
            
            m_adding = true;
            requires.forEach(system -> {
                if(system.getCurrentCommand() != null){
                    system.getCurrentCommand().cancel();
                    remove(system.getCurrentCommand());
                }
                system.setCurrentCommand(command);
            });
            m_adding = false;
            
            m_commandList.add(command);
            
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
    public void run(){
        m_runningCommandsChanged = false;
        
        if(m_disabled){
            if(++m_disabledCounter >= 4){
                m_log.warn("Scheduler is being called, but is disabled.");
                m_disabledCounter = 0;
            }
            return;
        }
        
        m_buttons.forEach(ButtonScheduler::execute);
        
        m_commandList.forEach(cmd -> {
            if(!cmd.run()){
                remove(cmd);
                m_runningCommandsChanged = true;
            }
        });
        
        m_additions.forEach(this::add_impl);
        m_additions.removeAllElements();
        
        m_subsystems.forEach(system -> {
            if(system.getCurrentCommand() == null)
                add_impl(system.getDefaultCommand());
            system.confirmCommand();
        });
        
        updateTable();
    }
    
    /**
     * Registers a {@link Subsystem} to this {@link Scheduler}, so that the
     * {@link Scheduler} might know if a default {@link Command} needs to be
     * run. All {@link Subsystem Subsystems} should call this.
     *
     * @param system the system
     */
    void registerSubsystem(Subsystem system){
        if(system != null)
            m_subsystems.addElement(system);
    }
    
    /**
     * Removes the {@link Command} from the {@link Scheduler}.
     *
     * @param command the command to remove
     */
    void remove(Command command){
        if(command == null || !m_commandList.contains(command)) return;
        
        m_commandList.remove(command);
        command.getRequirements().forEach(system -> system.setCurrentCommand(null));
        
        command.removed();
    }
    
    /**
     * Removes the all.
     */
    public void removeAll(){
        m_commandList.forEach(this::remove);
    }
    
    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    public void setEnabled(boolean enabled){
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
    private StringArray commands;
    
    /** The to cancel. */
    private NumberArray ids, toCancel;
    
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
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTable(){
        if(m_table != null){
            m_table.retrieveValue("Cancel", toCancel);

            if(toCancel.size() > 0){
                m_commandList.forEach(cmd -> {
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
            m_commandList.forEach(cmd -> {
                commands.add(cmd.getName());
                ids.add(cmd.hashCode());
            });
            m_table.putValue("Names", commands);
            m_table.putValue("Ids", ids);
        }
    }
}
