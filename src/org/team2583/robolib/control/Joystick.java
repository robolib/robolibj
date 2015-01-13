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

package org.team2583.robolib.control;

import org.team2583.robolib.communication.UsageReporting;
import org.team2583.robolib.communication.FRCNetworkCommunicationsLibrary.tResourceType;

/**
 * The RoboLibJ main Joystick.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class Joystick extends JoystickBase {
    
    
    
    /** The m_port. */
    private final int m_port;
    
    

    /**
     * The Class JoystickAxis.
     */
    private class JoystickAxis implements Axis {

        /** The m_invert. */
        private int m_invert = 1;
        
        /** The m_dead band. */
        private double m_deadBand = 0.00;
        
        /** The m_channel. */
        private final int m_channel;
        

        /**
         * Instantiates a new joystick axis.
         *
         * @param axis the axis
         */
        public JoystickAxis(int axis){
            m_channel = axis;
        }
        
        /**
         * {@inheritDoc}
         */
        public double get(){
            double out = getStickAxis(m_port, m_channel) * m_invert;
            return (Math.abs(out) >= m_deadBand ? out : 0D);
            //return DSInfo.getStickAxis(m_port, m_channel) * m_invert;
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
     * The Class JoystickButton.
     */
    private class JoystickButton implements Button {
        
        /** The m_channel. */
        private final int m_channel;
        
        /**
         * Instantiates a new joystick button.
         *
         * @param channel the channel
         */
        public JoystickButton(int channel){
            m_channel = channel - 1;
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean get() {
            return getStickButton(m_port, m_channel);
        }
    }
    
    /**
     * The RoboLibJ main Joystick.
     * @param port The Joystick Number
     */
    public Joystick(final int port){
        super(6, 12);
        m_port = port;
        
        m_axes = new JoystickAxis[]{
            new JoystickAxis(1),
            new JoystickAxis(2),
            new JoystickAxis(3),
            new JoystickAxis(4),
            new JoystickAxis(5),
            new JoystickAxis(6)
        };
        
        m_btns = new JoystickButton[]{
            new JoystickButton(1),
            new JoystickButton(2),
            new JoystickButton(3),
            new JoystickButton(4),
            new JoystickButton(5),
            new JoystickButton(6),
            new JoystickButton(7),
            new JoystickButton(8),
            new JoystickButton(9),
            new JoystickButton(10),
            new JoystickButton(11),
            new JoystickButton(12)
        };
        
        UsageReporting.report(tResourceType.kResourceType_Joystick, port);
    }

    /**
     * {@inheritDoc}
     * @return a {@link JoystickAxis} instance of the requested Axis
     * @see JoystickAxis
     */
    public Axis getAxis(int axis) {
        checkAxis(axis);
        return m_axes[axis - 1];
    }
    
    /**
     * {@inheritDoc}
     * @return a {@link JoystickButton} instance of the requested Button
     * @see JoystickButton
     */
    public Button getButton(int btn) {
        checkButton(btn);
        return m_btns[btn - 1];
    }

}