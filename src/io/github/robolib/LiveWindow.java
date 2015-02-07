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

package io.github.robolib;

import java.util.Hashtable;
import java.util.Vector;

import io.github.robolib.command.Scheduler;
import io.github.robolib.identifier.LiveWindowSendable;
import io.github.robolib.nettable.ITable;
import io.github.robolib.nettable.NetworkTable;
import io.github.robolib.util.log.ILogger;
import io.github.robolib.util.log.Logger;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class LiveWindow {
    
    private static Vector<LiveWindowSendable> m_sensors = new Vector<LiveWindowSendable>();
    
    private static Hashtable<LiveWindowSendable, LWComponent> m_components = new Hashtable<LiveWindowSendable, LWComponent>();
    
    private static ITable m_table;
    
    private static ITable m_statusTable;
    
    private static ILogger m_log = Logger.get(LiveWindow.class);
    
    private static boolean m_lwEnabled = false;
    
    private static boolean m_firstTime = true;
    
    private static void initLWComponents(){
        m_log.debug("Initializing the components first time");
        m_table = NetworkTable.getTable("LiveWindow");
        m_statusTable = m_table.getSubTable("~STATUS~");
        m_components.forEach((LiveWindowSendable s, LWComponent c) -> {
            String subsystem = c.m_subsystem;
            String name = c.m_name;
            m_log.debug("Initializing table for '" + subsystem + "' '" + name + "'");
            m_table.getSubTable("subsystem").putString("~TYPE~", "LW Subsystem");
            ITable table = m_table.getSubTable(subsystem).getSubTable(name);
            table.putString("~TYPE~", s.getSmartDashboardType());
            table.putString("Name", name);
            table.putString("Subsystem", subsystem);
            s.initTable(table);
            if(c.m_isSensor) m_sensors.addElement(s);
        });
    }
    
    public static void setEnabled(boolean enabled){
        if(m_lwEnabled != enabled){
            if(enabled){
                m_log.debug("Starting live window mode");
                if(m_firstTime){
                    initLWComponents();
                    m_firstTime = false;
                }
                Scheduler.getInstance().setEnabled(false);
                Scheduler.getInstance().removeAll();
                m_components.keySet().forEach(s -> s.startLiveWindowMode());
            }else{
                m_log.debug("Stopping live window mode.");
                m_components.keySet().forEach(s -> s.stopLiveWindowMode());
                Scheduler.getInstance().setEnabled(true);
            }
            m_lwEnabled = enabled;
            m_statusTable.putBoolean("LW Enabled", enabled);
        }
    }
    
    public static void run(){
        m_sensors.forEach(sensor -> sensor.updateTable());
    }
    
    public static void addSensor(String subsystem, String name, LiveWindowSendable component){
        m_components.put(component, new LWComponent(subsystem, name, true));
    }
    
    public static void addSensor(String itemType, int channel, LiveWindowSendable component){
        addSensor("Ungrouped", itemType + "[" + channel + "]", component);
        if (m_sensors.contains(component)) {
            m_sensors.removeElement(component);
        }
        m_sensors.addElement(component);
    }
    
    public static void addActuator(String subsystem, String name, LiveWindowSendable component){
        m_components.put(component, new LWComponent(subsystem, name, false));
    }
    
    public static void addActuator(String itemType, int channel, LiveWindowSendable component){
        addActuator("Ungrouped", itemType + "[" + channel + "]", component);
    }
}

/**
 * Class for each live window senable before it is processed
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
class LWComponent {
    
    String m_subsystem;
    String m_name;
    boolean m_isSensor;
    
    LWComponent(String s, String n, boolean i){
        m_subsystem = s;
        m_name = n;
        m_isSensor = i;
    }
    
}