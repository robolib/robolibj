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

package org.warriors2583.robolib.control;

import org.warriors2583.robolib.robot.Robot;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 *
 * @author Austin Reuland
 */
public class NetTableController extends JoystickAdapter {

    private final ITable m_table;
    private Axis m_axes[];
    private Button m_btns[];
    
    /**
     * {@inheritDoc}
     */
    public class NetTableAxis implements Axis {

        private int m_invert = 1;
        private double m_deadBand = 0.00;
        private final int m_channel;
        
        public NetTableAxis(int channel){
            m_channel = channel;
            m_table.putNumber("axis-" + channel, 0.00);
        }
        
        /**
         * {@inheritDoc}
         */
        public double get(){
            double out = m_table.getNumber("axis-" + m_channel, 0.00) * m_invert;
            return (Math.abs(out) <= m_deadBand ? 0 : out);
        }

        /**
         * {@inheritDoc}
         */
        public void invert(){
            m_invert = 0 - m_invert;
        }

        /**
         * {@inheritDoc}
         */
        public void setDeadband(double value){
            m_deadBand = value;
        }
    }
    
    /**
     * {@inheritDoc}
     */
    public class NetTableButton implements Button {
        
        private boolean m_invert = false;
        private final int m_channel;
        
        public NetTableButton(int channel){
            m_channel = channel;
            m_table.putBoolean("button-" + channel, false);
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean get() {
            return m_table.getBoolean("button-" + m_channel, false) & !m_invert;
        }

        /**
         * {@inheritDoc}
         */
        public void invert() {
            m_invert = !m_invert;
        }   
    }
    
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
     * @param numAxes Number of Axes to add to the Joystick
     * @param numBtns Number of Buttons to add to the Joystick
     */
    public NetTableController(String name, int numAxes, int numBtns){
        
        super(numAxes, numBtns);        
        m_axes = new NetTableAxis[numAxes];
        m_btns = new NetTableButton[numBtns];
        
        m_table = Robot.getRobotTable().getSubTable("Joystick").getSubTable(name);
        for(int i = 0; i < numAxes; i++)
            m_axes[i] = new NetTableAxis(i + 1);
        
        for(int i = 0; i < numBtns; i++)
            m_btns[i] = new NetTableButton(i + 1);
    }

    /**
     * {@inheritDoc}
     * @return a {@link NetTableAxis} instance of the requested Axis
     * @see NetTableAxis
     */
    public Axis getAxis(int axis) {
        checkAxis(axis);
        return m_axes[axis - 1];
    }

    /**
     * {@inheritDoc}
     * @return a {@link NetTableButton} instance of the requested Button
     * @see NetTableButton
     */
    public Button getButton(int btn) {
        checkButton(btn);
        return m_btns[btn - 1];
    }

    /**
     * Get the direction of the vector formed by the joystick and its origin
     * in radians
     * 
     * From WPILibJ package edu.wpi.first.wpilibj.Joystick
     *
     * @return The direction of the vector in radians
     */
    public double getDirectionRadians() {
        return Math.atan2(getX(), getY());
    }
}