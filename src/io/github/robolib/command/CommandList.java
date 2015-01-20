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

import java.util.Hashtable;
import java.util.Iterator;

/**
 * The Class CommandList.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class CommandList implements Iterable<Command> {
    
    /**
     * The Class ListElement.
     */
    class ListElement{
        
        /** The last. */
        ListElement last;
        
        /** The next. */
        ListElement next;
        
        /** The m_command. */
        Command m_command;
        
        /**
         * Instantiates a new list element.
         *
         * @param command the command
         * @param lastOne the last one
         */
        ListElement(Command command, ListElement lastOne){
            m_command = command;
            last = lastOne;
        }
    }
    
    /** The c to le. */
    private Hashtable<Command, ListElement> cToLE = new Hashtable<>();
    
    /** The first element. */
    private ListElement firstElement = new ListElement(null, null);
    
    /** The current element. */
    private ListElement currentElement = firstElement;
    
    /**
     * Instantiates a new command list.
     */
    CommandList(){}
    
    /**
     * Adds the.
     *
     * @param command the command
     */
    void add(Command command){
        ListElement elm = new ListElement(command, currentElement);
        currentElement.next = elm;
        currentElement = elm;
        cToLE.put(command, elm);
    }
    
    /**
     * Removes the.
     *
     * @param command the command
     */
    void remove(Command command){
        ListElement le = cToLE.get(command);
        if(le == currentElement){
            currentElement = le.last;
            currentElement.next = null;
        }else{
            le.last.next = le.next;
            le.next.last = le.last;
        }
        cToLE.remove(command);
        le = null;
    }
    
    /**
     * Contains.
     *
     * @param command the command
     * @return true, if successful
     */
    boolean contains(Command command){
        return cToLE.containsKey(command);
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<Command> iterator() {
        return new Iterator<Command>(){
            ListElement current = firstElement;
            ListElement next = current.next;
            public boolean hasNext() {
                return next != null;
            }

            public Command next() {
                current = next;
                next = current.next;
                return current.m_command;
            }
        };
    }

}
