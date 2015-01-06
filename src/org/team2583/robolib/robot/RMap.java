/*
 * Copyright (c) 2015 Westwood Robotics <code.westwoodrobotics@gmail.com>.
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

package org.team2583.robolib.robot;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;
import java.util.Hashtable;
import org.team2583.robolib.util.ConstantRunner;

/**
 * The Class RMap.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class RMap {
    
    /** The Constant strVals. */
    private static final Hashtable strVals = new Hashtable();
    
    /** The Constant boolVals. */
    private static final Hashtable boolVals = new Hashtable();
    
    /** The Constant intVals. */
    private static final Hashtable intVals = new Hashtable();
    
    /** The Constant NETTABLE_MAP_NAME. */
    public static final String NETTABLE_MAP_NAME = "RMap";
    
    /** The Constant NETTABLE_MAP_STRINGS_NAME. */
    public static final String NETTABLE_MAP_STRINGS_NAME = "Strings";
    
    /** The Constant NETTABLE_MAP_BOOLEANS_NAME. */
    public static final String NETTABLE_MAP_BOOLEANS_NAME = "Booleans";
    
    /** The Constant NETTABLE_MAP_INTEGERS_NAME. */
    public static final String NETTABLE_MAP_INTEGERS_NAME = "Integers";
    
    /** The Constant runner. */
    public static final ConstantRunner runner = new ConstantRunner("RMap.ini");
    
    /** The Constant m_intTable. */
    private static final ITable m_strTable, m_boolTable, m_intTable;
    
    /** The Constant m_intListener. */
    private static final ITableListener m_strListener, m_boolListener, m_intListener;
    
    static{
        m_strTable = RoboLibBot.getRobotTable().getSubTable(NETTABLE_MAP_NAME).getSubTable(NETTABLE_MAP_STRINGS_NAME);
        m_boolTable = RoboLibBot.getRobotTable().getSubTable(NETTABLE_MAP_NAME).getSubTable(NETTABLE_MAP_BOOLEANS_NAME);
        m_intTable = RoboLibBot.getRobotTable().getSubTable(NETTABLE_MAP_NAME).getSubTable(NETTABLE_MAP_INTEGERS_NAME);
        
        m_strListener = new ITableListener(){
            public void valueChanged(ITable table, String key, Object value, boolean isNew){
                strVals.put(key, value);
            }
        };
        m_strTable.addTableListener(m_strListener);
        
        m_boolListener = new ITableListener(){
            public void valueChanged(ITable table, String key, Object value, boolean isNew){
                boolVals.put(key, value);                
            }
        };
        m_boolTable.addTableListener(m_boolListener);
        
        m_intListener = new ITableListener(){
            public void valueChanged(ITable table, String key, Object value, boolean isNew){
                intVals.put(key, value);                
            }
        };
        m_intTable.addTableListener(m_intListener);
    }
    

}