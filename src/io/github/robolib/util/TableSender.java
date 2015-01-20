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

package io.github.robolib.util;

import java.util.Vector;

import io.github.robolib.framework.RoboLibBot;
import io.github.robolib.framework.Sendable;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class TableSender {
    
    private static TableSender m_instance;
    
//    private static Vector<Sendable> m_userSends = new Vector<Sendable>();
    private static Vector<Sendable> m_framework = new Vector<Sendable>();
    
    public static final TableSender getInstance(){
        return m_instance == null ? m_instance = new TableSender() : m_instance;
    }
    
    private TableSender(){}
    
    public void runFramework(){
        m_framework.forEach(send -> send.updateTable());
    }
    
    public void runUser(){
        
    }
    
//    public static void addSendable(Sendable sendable, String tableName){
//        
//    }
    
    public static void addFramework(Sendable sendable, String tableName){
        sendable.initTable(CommonFunctions.getTable(RoboLibBot.getRobotTable(), tableName));
        m_framework.addElement(sendable);
        
    }

}
