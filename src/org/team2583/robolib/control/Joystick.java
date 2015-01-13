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

package org.team2583.robolib.control;

import org.team2583.robolib.communication.FRCNetworkCommunicationsLibrary;
import org.team2583.robolib.communication.UsageReporting;
import org.team2583.robolib.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import org.team2583.robolib.util.log.Logger;

/**
 * The RoboLibJ main Joystick.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class Joystick extends ControllerBase {   

    
    public static enum Stick{
        Stick0,
        Stick1,
        Stick2,
        Stick3,
        Stick4,
        Stick5;
    }
    
    
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
            m_channel = channel;
        }
        
        /**
         * {@inheritDoc}
         */
        public boolean get() {
            return getStickButton(m_port, m_channel);
        }
    }
    
    public static final int kNumJoysticks = 6;
    
    private static short m_joystickAxes[][] = new short[kNumJoysticks][FRCNetworkCommunicationsLibrary.kMaxJoystickAxes];
    private static short m_joystickPOVs[][] = new short[kNumJoysticks][FRCNetworkCommunicationsLibrary.kMaxJoystickPOVs];
    private static int m_joystickButtons[] = new int[kNumJoysticks];
    private static byte m_joystickButtonsCount[] = new byte[kNumJoysticks];
    
    protected static final void setJoystickData(int stick, short[] axes, short[] povs, int buttons, byte numBtns){
        synchronized(m_joystickAxes){
            m_joystickAxes[stick] = axes;
        }
        synchronized(m_joystickPOVs){
            m_joystickPOVs[stick] = povs;
        }
        synchronized(m_joystickButtons){
            m_joystickButtons[stick] = buttons;
        }
        synchronized(m_joystickButtonsCount){
            m_joystickButtonsCount[stick] = numBtns;
        }
    }
    
    protected synchronized static double getStickAxis(Stick stick, int axis){
        if(m_joystickAxes[stick.ordinal()].length <= axis){
            Logger.get(ControllerBase.class, "Joystick").error("Joystick Axis '" + axis + "' on stick '" + stick + "' is invalid. Is it plugged in?");
            return 0.0;
        }
        
        double value = m_joystickAxes[stick.ordinal()][axis];
        if(value < 0){
            return value / 128.0;
        }else{
            return value / 127.0;
        }
    }
    
    protected synchronized static boolean getStickButton(Stick stick, int button){
        if(m_joystickButtonsCount[stick.ordinal()] <= button){
            Logger.get(ControllerBase.class, "Joystick").error("Joystick Button '" + button + "' on stick '" + stick + "' is invalid. Is it plugged in?");
            return false;
        }
        
        return ((0x1 << button) & m_joystickButtons[stick.ordinal()]) != 0;
    }
    
    protected static void checkStick(int stick){
        if(stick < 0 || stick > kNumJoysticks){
            throw new RuntimeException("Invalid Joystick '" + stick + "'.");
        }
    }
    
    protected Stick m_port;    
    
    /**
     * The RoboLibJ main Joystick.
     * @param port The Joystick Number
     */
    public Joystick(final Stick port){
        this(port, 6, 12);
        UsageReporting.report(tResourceType.kResourceType_Joystick, port.ordinal());
    }
    
    public Joystick(final Stick port, int numAxes, int numBtns){
        super(numAxes, numBtns);

        m_port = port;
        m_axes = new JoystickAxis[numAxes];
        for(int i = 0; i < numAxes; i++){
            m_axes[i] = new JoystickAxis(i);
        }
        
        m_btns = new JoystickButton[numBtns];
        for(int i = 0; i < numBtns; i++){
            m_btns[i] = new JoystickButton(i);
        }
    }

    /**
     * {@inheritDoc}
     * @return a {@link JoystickAxis} instance of the requested Axis
     * @see JoystickAxis
     */
    public Axis getAxis(int axis) {
        checkAxis(axis);
        return m_axes[axis];
    }
    
    /**
     * {@inheritDoc}
     * @return a {@link JoystickButton} instance of the requested Button
     * @see JoystickButton
     */
    public Button getButton(int btn) {
        checkButton(btn);
        return m_btns[btn];
    }

}