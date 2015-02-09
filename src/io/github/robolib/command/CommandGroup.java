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

import java.util.List;
import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class CommandGroup.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class CommandGroup extends Command {
    
    /** The Constant IN_SEQUENCE. */
    private static final byte IN_SEQUENCE = 0;
    
    /** The Constant BRANCH_PEER. */
    private static final byte BRANCH_PEER = 1;
    
    /** The Constant BRANCH_CHILD. */
    private static final byte BRANCH_CHILD = 2;
    
    /**
     * The Class CommandEntry.
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    private final class CommandEntry {
        
        /** The m_command. */
        Command m_command;
        
        /** The m_state. */
        byte m_state;
        
        /** The m_timeout. */
        double m_timeout;
        
        /**
         * Instantiates a new command entry.
         *
         * @param command the command
         * @param state the state
         * @param timeout the timeout
         */
        CommandEntry(Command command, byte state, double timeout){
            m_command = command;
            m_state = state;
            m_timeout = timeout;
        }
        
        /**
         * Checks if is timed out.
         *
         * @return true, if is timed out
         */
        boolean isTimedOut(){
            if(m_timeout == -1){
                return false;
            }else{
                double time = m_command.timeSinceInitialized();
                return time == 0 ? false : time >= m_timeout;
            }
        }
    }

    /** The m_commands. */
    Vector<CommandEntry> m_commands = new Vector<CommandEntry>();
    
    /** The m_children. */
    Vector<CommandEntry> m_children = new Vector<CommandEntry>();
    
    /** The m_current command index. */
    int m_currentCommandIndex = -1;
    
    /**
     * Instantiates a new command group.
     */
    public CommandGroup(){}
    
    /**
     * Instantiates a new command group.
     *
     * @param name the name
     */
    public CommandGroup(String name){
        super(name);
    }
    
    /**
     * Adds a new {@link Command Command} to the group.  The {@link Command Command} will be started after
     * all the previously added {@link Command Commands}.
     * 
     * <p>Note that any requirements the given {@link Command Command} has will be added to the
     * group.  For this reason, a {@link Command Command's} requirements can not be changed after
     * being added to a group.</p>
     * 
     * <p>It is recommended that this method be called in the constructor.</p>
     *
     * @param command The {@link Command Command} to be added
     */
    public synchronized final void addSequential(Command command){
        addCommand(command, IN_SEQUENCE, -1);
    }

    /**
     * Adds a new {@link Command Command} to the group with a given timeout.
     * The {@link Command Command} will be started after all the previously added commands.
     * A timeout of -1 means it will not time out. This is the same a omitting the timeout value.
     * 
     * <p>Once the {@link Command Command} is started, it will be run until it finishes or the time
     * expires, whichever is sooner.  Note that the given {@link Command Command} will have no
     * knowledge that it is on a timer.</p>
     * 
     * <p>Note that any requirements the given {@link Command Command} has will be added to the
     * group.  For this reason, a {@link Command Command's} requirements can not be changed after
     * being added to a group.</p>
     * 
     * <p>It is recommended that this method be called in the constructor.</p>
     *
     * @param command The {@link Command Command} to be added
     * @param timeout The timeout (in seconds)
     */
    public synchronized final void addSequential(Command command, double timeout){
        addCommand(command, IN_SEQUENCE, timeout);
    }
    
    /**
     * Adds a new child {@link Command} to the group.  The {@link Command} will be started after
     * all the previously added {@link Command Commands}.
     * 
     * <p>Instead of waiting for the child to finish, a {@link CommandGroup} will have it
     * run at the same time as the subsequent {@link Command Commands}.  The child will run until either
     * it finishes, a new child with conflicting requirements is started, or
     * the main sequence runs a {@link Command} with conflicting requirements.  In the latter
     * two cases, the child will be canceled even if it says it can't be
     * interrupted.</p>
     * 
     * <p>Note that any requirements the given {@link Command Command} has will be added to the
     * group.  For this reason, a {@link Command Command's} requirements can not be changed after
     * being added to a group.</p>
     * 
     * <p>It is recommended that this method be called in the constructor.</p>
     *
     * @param command The command to be added
     */
    public synchronized final void addParallel(Command command){
        addCommand(command, BRANCH_CHILD, -1);
    }
    
    /**
     * Adds a new child {@link Command} to the group with the given timeout.  The {@link Command} will be started after
     * all the previously added {@link Command Commands}.
     * A timeout of -1 means it will not time out. This is the same a omitting the timeout value.
     * 
     * <p>Once the {@link Command Command} is started, it will run until it finishes, is interrupted,
     * or the time expires, whichever is sooner.  Note that the given {@link Command Command} will have no
     * knowledge that it is on a timer.</p>
     * 
     * <p>Instead of waiting for the child to finish, a {@link CommandGroup} will have it
     * run at the same time as the subsequent {@link Command Commands}.  The child will run until either
     * it finishes, the timeout expires, a new child with conflicting requirements is started, or
     * the main sequence runs a {@link Command} with conflicting requirements.  In the latter
     * two cases, the child will be canceled even if it says it can't be
     * interrupted.</p>
     * 
     * <p>Note that any requirements the given {@link Command Command} has will be added to the
     * group.  For this reason, a {@link Command Command's} requirements can not be changed after
     * being added to a group.</p>
     * 
     * <p>It is recommended that this method be called in the constructor.</p>
     *
     * @param command The command to be added
     * @param timeout The timeout (in seconds)
     */
    public synchronized final void addParallel(Command command, double timeout){
        addCommand(command, BRANCH_CHILD, timeout);
    }
    
    /**
     * Adds the command.
     *
     * @param command the command
     * @param state the state
     * @param timeout the timeout
     */
    private synchronized final void addCommand(Command command, byte state, double timeout){
        validate("Cannot add new command to command group");
        if(command == null) return;
        if(timeout < -1) throw new IllegalArgumentException("Timeout cannot be less than -1");
        
        command.setParent(this);
        m_commands.addElement(new CommandEntry(command, state, timeout));
        command.getRequirements().forEach(this::requires);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void initialize_impl(){
        m_currentCommandIndex = -1;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void execute_impl(){
        CommandEntry entry = null;
        Command cmd = null;
        boolean firstRun = false;
        if(m_currentCommandIndex == -1){
            firstRun = true;
            m_currentCommandIndex = 0;
        }
        
        while(m_currentCommandIndex < m_commands.size()){
            if(cmd != null){
                if(entry.isTimedOut()) cmd.cancel_impl();
                if(cmd.run()){
                    break;
                }else{
                    cmd.removed();
                    m_currentCommandIndex++;
                    firstRun = true;
                    cmd = null;
                    continue;
                }
            }
            
            entry = m_commands.elementAt(m_currentCommandIndex);
            cmd = null;
            
            switch(entry.m_state){
            case IN_SEQUENCE:
                cmd = entry.m_command;
                if(firstRun){
                    cmd.startRunning();
                    cancelConflicts(cmd);
                }
                firstRun = false;
                break;
            case BRANCH_PEER:
                m_currentCommandIndex++;
                entry.m_command.start();
                break;
            case BRANCH_CHILD:
                m_currentCommandIndex++;
                cancelConflicts(entry.m_command);
                entry.m_command.startRunning();
                m_children.addElement(entry);
                break;
            }
            
            m_children.forEach(ce -> {
                Command child = ce.m_command;
                if(ce.isTimedOut()) child.cancel_impl();
                if(!child.run()){
                    child.removed();
                    m_children.remove(ce);
                }
            });
                    
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void end_impl(){
        if(m_currentCommandIndex != -1 && m_currentCommandIndex < m_commands.size()){
            Command cmd = m_commands.elementAt(m_currentCommandIndex).m_command;
            cmd.cancel_impl();
            cmd.removed();
        }
        
        m_children.forEach(ce -> {
            ce.m_command.cancel_impl();
            ce.m_command.removed();
        });
        m_children.removeAllElements();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    void interrupted_impl(){
        end_impl();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isFinished(){
        return m_currentCommandIndex >= m_commands.size() && m_children.isEmpty();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialize(){}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(){}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void end(){}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void interrupted(){}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized boolean isInterruptible(){
        if(!super.isInterruptible())
            return false;
        
        if(m_currentCommandIndex != -1 && m_currentCommandIndex < m_commands.size()){
            if(!m_commands.get(m_currentCommandIndex).m_command.isInterruptible())
                return false;
        }
        
        for(CommandEntry m : m_children){
            if(!m.m_command.isInterruptible())
                return false;
        }
        
        return true;
    }
    
    /**
     * Cancel conflicts.
     *
     * @param command the command
     */
    private void cancelConflicts(Command command){
        m_children.forEach(ce -> {
            List<Subsystem> requires = command.getRequirements();
            if(requires.stream().anyMatch(system -> ce.m_command.doesRequire(system))){
                ce.m_command.cancel_impl();
                ce.m_command.removed();
                m_children.remove(ce);
            }
        });
    }
    
    
    
}
