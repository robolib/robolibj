/*
 * Copyright (c) 2015-2020 noriah <vix@noriah.dev>.
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

package io.github.robolib.module.hid;

import io.github.robolib.DriverStation;
import io.github.robolib.jni.NetworkCommunications;
import io.github.robolib.jni.UsageReporting;
import io.github.robolib.util.MathUtils;
import io.github.robolib.util.log.Logger;

/**
 * The RoboLibJ main Joystick.
 *
 * @author noriah <vix@noriah.dev>
 */
public class Joystick extends GenericHID {


    /**
     * The Enum JSID.
     *
     * @author noriah <vix@noriah.dev>
     */
    public static enum JSID{

        /** The first Joystick input device. */
        JS0,

        /** The second Joystick input device. */
        JS1,

        /** The thrid Joystick input device. */
        JS2,

        /** The fourth Joystick input device. */
        JS3,

        /** The fifth Joystick input device. */
        JS4,

        /** The sixth Joystick input device. */
        JS5;
    }


    /**
     * The Class JoystickAxis.
     *
     * @author noriah <vix@noriah.dev>
     */
    protected final class JoystickAxis implements HIDAxis {

        /** The m_invert. */
        private boolean m_inverted = false;

        /** The m_dead band. */
        private double m_deadband = 0.0;

        private double m_backlash = 0.0;

        private double m_fineConrol = 0.75;

        private double m_rampEnd = 0.5;

        private double m_m1;

        private double m_m2;

        /** The m_channel. */
        private final int m_channel;

        /**
         * Instantiates a new joystick axis.
         *
         * @param axis the axis
         */
        public JoystickAxis(int axis){
            m_channel = axis;
            calculate();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double get(){
            double out = DriverStation.getStickAxis(m_port, m_channel);
            double x = Math.abs(out);

            if(x < m_deadband)
                x = 0.0;
            else if(x < m_rampEnd)
                x = m_backlash + m_m1 * (x - m_deadband);
            else
                x = m_fineConrol + m_m2 * (x - m_rampEnd);

            if(out < 0)
                x = -x;
//            double x = Math.abs(out) < m_deadband ? 0 : out;
            return m_inverted ? -x : x;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setInverted(boolean inverted){
            m_inverted = inverted;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setDeadband(double value){
            m_deadband = value;
            calculate();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setRampEnd(double end){
            m_rampEnd = end;
            calculate();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setBacklash(double value){
            m_backlash = value;
            calculate();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setFineControl(double control){
            m_fineConrol = control;
            calculate();
        }

        private void calculate(){
            m_m1 = (m_fineConrol - m_backlash) / (m_rampEnd - m_deadband);

            m_m2 = (1 - m_fineConrol) / (1 - m_rampEnd);
        }
    }

    /**
     * The Class JoystickButton.
     *
     * @author noriah <vix@noriah.dev>
     */
    protected final class JoystickButton implements HIDButton {

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
        @Override
        public boolean getState() {
            return DriverStation.getStickButton(m_port, m_channel);
        }
    }

//    private static final EnumMap<JSID, ? extends Joystick> m_stickMap =

    /**
     * Check stick.
     *
     * @param stick the stick
     */
    protected static void checkStick(int stick){
        if(stick < 0 || stick > 6){
            throw new RuntimeException("Invalid Joystick '" + stick + "'.");
        }
    }

    /** The m_port. */
    private final JSID m_port;

    /** The m_portByte. */
    private final byte m_portByte;

    /** The m_outputs. */
    private int m_outputs;

    private short m_leftRumble;
    private short m_rightRumble;

    /**
     * Instantiates a new joystick.
     *
     * @param port the port
     * @param numAxes the num axes
     * @param numBtns the num btns
     */
    public Joystick(final JSID port, int numAxes, int numBtns){
        super(numAxes, numBtns);

        m_port = port;
        m_portByte = (byte)port.ordinal();
        for(int i = 0; i < numAxes; i++){
            m_axes[i] = new JoystickAxis(i);
        }

        for(int i = 0; i < numBtns; i++){
            m_btns[i] = new JoystickButton(i);
        }
        UsageReporting.report(UsageReporting.ResourceType_Joystick, port.ordinal());
    }

    public Joystick getStick(JSID port, int numAxes, int numBtns){
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getPOV(int pov){
        return DriverStation.getStickPOV(m_port, pov);
    }

    /**
     * Get the Port JSID that this Joystick is on
     * @return the stick this joystick is on
     */
    public final JSID getStickPort(){
        return m_port;
    }

    /**
     * Rumble control enum
     *
     * @author noriah <vix@noriah.dev>
     */
    public static enum RumbleSide{
        LEFT,
        RIGHT,
        BOTH;
    }

    /**
     * Set the rumble output for the joystick. The DS currently supports 2 rumble values,
     * left rumble and right rumble
     * @param type Which rumble value to set
     * @param value The normalized value (0 to 1) to set the rumble to
     */
    public final void setRumble(RumbleSide type, float value) {
        short rVal = (short)(MathUtils.clamp(value, 0F, 1F) * 65535);
        switch(type){
        case LEFT:
            m_leftRumble = rVal;
            break;
        case RIGHT:
            m_rightRumble = rVal;
            break;
        case BOTH:
            m_leftRumble = rVal;
            m_rightRumble = rVal;
            break;
        }
        NetworkCommunications.HALSetJoystickOutputs(m_portByte, m_outputs, m_leftRumble, m_rightRumble);
    }

    public final void setOutput(int outputNumber, boolean value){
        if(MathUtils.inBounds(outputNumber, 0, 31)){
            m_outputs = (m_outputs & ~(1 << outputNumber)) | ((value?1:0) << outputNumber);
            NetworkCommunications.HALSetJoystickOutputs(m_portByte, m_outputs, m_leftRumble, m_rightRumble);
        }else{
            Logger.get(Joystick.class).warn("No such Output number '" + outputNumber + "' on joysticks.");
        }

    }

    public final void setOutputs(int value){
        m_outputs = value;
        NetworkCommunications.HALSetJoystickOutputs(m_portByte, m_outputs, m_leftRumble, m_rightRumble);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableModule() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableModule() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getModuleEnabled() {
        // TODO Auto-generated method stub
        return true;
    }

}
