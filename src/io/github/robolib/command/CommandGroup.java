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
import java.util.Iterator;
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
    
    /** The m_commands. */
    final List<Command> m_commands = new ArrayList<Command>();
    
    /** The m_children. */
    final Vector<Command> m_children = new Vector<Command>();
    
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
    public final void addSequential(Command command){
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
    public final void addSequential(Command command, double timeout){
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
    public final void addParallel(Command command){
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
    public final void addParallel(Command command, double timeout){
        addCommand(command, BRANCH_CHILD, timeout);
    }
    
    /**
     * Adds the command.
     *
     * @param command the command
     * @param state the state
     * @param timeout the timeout
     */
    private final void addCommand(Command command, byte state, double timeout){
        synchronized(this){
            if(m_locked)
                throw new IllegalStateException("Cannot add new command to command group after being started or added to a command group.");
            if(command == null) return;
            if(timeout < -1) throw new IllegalArgumentException("Timeout cannot be less than -1");
            
            
            synchronized(command){
                command.setParent(this);
                command.m_state = state;
                command.getRequirements().forEach(this::requires);
                if(timeout > -1)
                    command.setTimeout(timeout);
            }
            m_commands.add(command);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    final void _initialize(){
        m_currentCommandIndex = -1;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    final void _execute(){
        Command entry = null;
        Command cmd = null;
        boolean firstRun = false;
        if(m_currentCommandIndex == -1){
            firstRun = true;
            m_currentCommandIndex = 0;
        }
        
        while(m_currentCommandIndex < m_commands.size()){
            if(cmd != null){
                if(cmd.isTimedOut()) cmd._cancel();
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
            
            entry = m_commands.get(m_currentCommandIndex);
            cmd = null;
            
            switch(entry.m_state){
            case IN_SEQUENCE:
                cmd = entry;
                if(firstRun){
                    cmd.startRunning();
                    cancelConflicts(cmd);
                }
                firstRun = false;
                break;
            case BRANCH_PEER:
                m_currentCommandIndex++;
                entry.start();
                break;
            case BRANCH_CHILD:
                m_currentCommandIndex++;
                cancelConflicts(entry);
                entry.startRunning();
                m_children.addElement(entry);
                break;
            }
            
            m_children.forEach(child -> {
                if(child.isTimedOut()) child._cancel();
                if(!child.run()){
                    child.removed();
                    m_children.remove(child);
                }
            });
                    
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    final void _end(){
        Command cmd;
        if(m_currentCommandIndex != -1 && m_currentCommandIndex < m_commands.size()){
            cmd = m_commands.get(m_currentCommandIndex);
            cmd._cancel();
            cmd.removed();
        }
        cmd = null;
        
        Iterator<Command> iter = m_children.iterator();
        while(iter.hasNext()){
            cmd = iter.next();
            cmd._cancel();
            cmd.removed();
            iter.remove();
        }
        iter = null;
        cmd = null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    final void _interrupted(){
        _end();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected final boolean isFinished(){
        return m_currentCommandIndex >= m_commands.size() && m_children.isEmpty();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected final void initialize(){}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected final void execute(){}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected final void end(){}
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected final void interrupted(){}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean isInterruptible(){
        synchronized(this){
            if(!super.isInterruptible())
                return false;
            
            if(m_currentCommandIndex != -1 && m_currentCommandIndex < m_commands.size()){
                if(!m_commands.get(m_currentCommandIndex).isInterruptible())
                    return false;
            }
            
            for(Command m : m_children){
                if(!m.isInterruptible())
                    return false;
            }
        }
        
        return true;
    }
    
    protected static final Command Wait(double timeout){
        return new WaitCommand(timeout);
    }
    
    /**
     * Cancel conflicts.
     *
     * @param command the command
     */
    private final void cancelConflicts(Command command){
        Command cmd;
        List<Subsystem> requires = command.getRequirements();
        Iterator<Command> iter = m_children.iterator();
        while(iter.hasNext()){
            cmd = iter.next();
            if(cmd.requiresAny(requires)){
                cmd._cancel();
                cmd.removed();
                iter.remove();
            }
        }
        iter = null;
        cmd = null;
        requires = null;
    }
}
