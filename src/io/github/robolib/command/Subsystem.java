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

import io.github.robolib.identifier.NamedSendable;
import io.github.robolib.nettable.ITable;

// TODO: Auto-generated Javadoc
/**
 * The Class Subsystem.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class Subsystem implements NamedSendable {

    
    /** The m_initialized default command. */
    private boolean m_initializedDefaultCommand = false;
    
    /** The m_current command. */
    private Command m_currentCommand;
    
    /** The m_current command changed. */
    private boolean m_currentCommandChanged;
    
    /** The m_default command. */
    private Command m_defaultCommand;
    
    /** The m_name. */
    private final String m_name;

    private ITable m_table;
    
    /** The m_all subsystems. */
//    private static Vector<Subsystem> m_allSubsystems = new Vector<Subsystem>();
    
    /**
     * Instantiates a new subsystem.
     *
     * @param name the name
     */
    public Subsystem(String name){
        m_name = name;
        Scheduler.registerSubsystem(this);
        m_currentCommandChanged = true;
    }
    
    /**
     * Instantiates a new subsystem.
     */
    public Subsystem(){
        m_name = getClass().getName().substring(getClass().getName().lastIndexOf('.') + 1);
        Scheduler.registerSubsystem(this);
        m_currentCommandChanged = true;
    }
    
    /**
     * Inits the default command.
     */
    protected abstract void initDefaultCommand();
    
    /**
     * Sets the default command.
     *
     * @param command the new default command
     */
    protected void setDefaultCommand(Command command){
        if(command == null){
            m_defaultCommand = null;
        }else{
            if(!command.getRequirements().contains(this))
                throw new IllegalStateException("A default command must require the subsystem");
            
            m_defaultCommand = command;
        }
        
        if (m_table != null) {
            if (m_defaultCommand != null) {
                m_table.putBoolean("hasDefault", true);
                m_table.putString("default", m_defaultCommand.getName());
            } else {
                m_table.putBoolean("hasDefault", false);
            }
        }
    }
    
    /**
     * Gets the default command.
     *
     * @return the default command
     */
    protected Command getDefaultCommand(){
        if(!m_initializedDefaultCommand){
            m_initializedDefaultCommand = true;
            initDefaultCommand();
        }
        return m_defaultCommand;
    }
    
    /**
     * Sets the current command.
     *
     * @param command the new current command
     */
    void setCurrentCommand(Command command){
        m_currentCommand = command;
        m_currentCommandChanged = true;
    }
    
    void nullifyCurrentCommand(){
        m_currentCommand = null;
        m_currentCommandChanged = true;
    }
    
    /**
     * Confirm command.
     */
    void confirmCommand(){
        if(m_currentCommandChanged){
            m_currentCommandChanged = false;
            if(m_table != null){
                if(m_currentCommand != null){
                    m_table.putBoolean("hasCommand", true);
                    m_table.putString("command", m_currentCommand.getName());
                }else{
                    m_table.putBoolean("hasCommand", false);
                }
            }
        }
    }
    
    void iterationRun(){
        if(m_currentCommand == null){
            Scheduler.add_internal(getDefaultCommand());
        }
        confirmCommand();
    }
    
    /**
     * Gets the current command.
     *
     * @return the current command
     */
    public Command getCurrentCommand(){
        return m_currentCommand;
    }
    
    public boolean getCurrentCommandNotInterruptable(){
        if(m_currentCommand == null){
            return false;
        }
        return !m_currentCommand.isInterruptible();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString(){
        return m_name;
    }
    
    public String getSmartDashboardType() {
        return "Subsystem";
    }

    public void initTable(ITable table) {
        m_table = table;
        if(table!=null) {
            if (m_defaultCommand != null) {
                m_table.putBoolean("hasDefault", true);
                m_table.putString("default", m_defaultCommand.getName());
            } else {
                table.putBoolean("hasDefault", false);
            }
            if (m_currentCommand != null) {
                m_table.putBoolean("hasCommand", true);
                m_table.putString("command", m_currentCommand.getName());
            } else {
                m_table.putBoolean("hasCommand", false);
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    public ITable getTable() {
        return m_table;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName(){
        return m_name;
    }
    
}
