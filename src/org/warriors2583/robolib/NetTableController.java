/*
 * Copyright (c) 2014 Westwood Robotics code.westwoodrobotics@gmail.com.
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

package org.warriors2583.robolib;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.parsing.IInputOutput;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 *
 * @author Austin Reuland
 */
public class NetTableController extends GenericHID implements IInputOutput {

    private final ITable m_table;
    private final int m_buttons, m_axes;
    
    /**
     * Create a NetworkJoystick Instance
     * @param name Name of the Joystick in the Robot/Joystick Table
     */
    public NetTableController(String name){
        this(name, 6, 12);
    }
    
    /**
     * Create a NetworkJoystick Instance
     * @param name Name of the Joystick in the Robot/Joystick Table
     * @param numBtns Number of Buttons to add to the Joystick
     */
    public NetTableController(String name, int numBtns){
        this(name, 6, numBtns);
    }
    
    /**
     * Create a NetworkJoystick Instance
     * @param name Name of the Joystick in the Robot/Joystick Table
     * @param numAxes Number of Axes to add to the Joystick
     * @param numBtns Number of Buttons to add to the Joystick
     */
    public NetTableController(String name, int numAxes, int numBtns){
        
        m_table = Robot.getRobotTable().getSubTable("Joystick").getSubTable(name);
        m_buttons = numBtns;
        m_axes = numAxes;
        for(int i = 0; i < numAxes; i++)
            m_table.putNumber("axis-" + i + 1, 0.00);
        
        for(int i = 0; i < numBtns; i++)
            m_table.putBoolean("button-" + i + 1, false);
    }
    
    public double getX(Hand hand) {
        return m_table.getNumber("axis-" + ( 1 + hand.value * 3), 0.00);
    }

    public double getY(Hand hand) {
        return m_table.getNumber("axis-" + ( 2 + hand.value * 3), 0.00);
    }

    public double getZ(Hand hand) {
        return m_table.getNumber("axis-" + ( 3 + hand.value * 3), 0.00);
    }

    public double getTwist() {
        return m_table.getNumber("axis-4", 0.00);
    }

    public double getThrottle() {
        return m_table.getNumber("axis-5", 0.00);
    }

    public double getRawAxis(int which) {
        if(which > m_axes || which < 1){
            throw new IndexOutOfBoundsException("Index: " + which + ", Size: " + m_axes);
        }
        return m_table.getNumber("axis-" + which, 0.00);
    }

    public boolean getTrigger(Hand hand) {
        return m_table.getBoolean("button-1", false);
    }

    public boolean getTop(Hand hand) {
        
        return m_table.getBoolean("button-2", false);
    }

    public boolean getBumper(Hand hand) {
        return m_table.getBoolean("button-3", false);
    }

    public boolean getRawButton(int button) {
        if(button > m_buttons || button < 1){
            throw new IndexOutOfBoundsException("Index: " + button + ", Size: " + m_buttons);
        }
        return m_table.getBoolean("button-" + button, false);
    }

}