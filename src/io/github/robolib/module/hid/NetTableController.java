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

import io.github.robolib.RoboLib;
import io.github.robolib.nettable.ITable;

/**
 * The Class NetTableController.
 *
 * @author noriah <vix@noriah.dev>
 */
public final class NetTableController extends GenericHID {

    /** The m_table. */
    private final ITable m_table;

    /**
     * The Class NetTableAxis.
     *
     * @author noriah <vix@noriah.dev>
     */
    public final class NetTableAxis implements HIDAxis {

        /** The m_invert. */
        private boolean m_invert = false;

        /** The m_dead band. */
        private double m_deadBand = 0.00;

        /** The m_channel. */
        private final int m_channel;

        /**
         * Instantiates a new net table axis.
         *
         * @param channel the channel
         */
        public NetTableAxis(int channel){
            m_channel = channel;
            m_table.putNumber("axis-" + channel, 0.00);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double get(){
            double out = m_table.getNumber("axis-" + m_channel, 0.00);
            out = (Math.abs(out) <= m_deadBand ? 0 : out);
            return m_invert ? -out : out;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setInverted(boolean inverted){
            m_invert = inverted;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setDeadband(double value){
            m_deadBand = value;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setRampEnd(double end) {
            // TODO Auto-generated method stub

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setBacklash(double value) {
            // TODO Auto-generated method stub

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setFineControl(double value) {
            // TODO Auto-generated method stub

        }
    }

    /**
     * The Class NetTableButton.
     *
     * @author noriah <vix@noriah.dev>
     */
    public final class NetTableButton implements HIDButton {

        /** The m_invert. */
        private boolean m_invert = false;

        /** The m_channel. */
        private final int m_channel;

        /**
         * Instantiates a new net table button.
         *
         * @param channel the channel
         */
        public NetTableButton(int channel){
            m_channel = channel;
            m_table.putBoolean("button-" + channel, false);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getState() {
            return m_table.getBoolean("button-" + m_channel, false) & !m_invert;
        }
    }

    /**
     * Create a NetworkJoystick Instance.
     *
     * @param name Name of the Joystick in the RoboLibBot/Joystick Table
     */
    public NetTableController(String name){
        this(name, 6, 12);
    }

    /**
     * Create a NetworkJoystick Instance.
     *
     * @param name Name of the Joystick in the RoboLibBot/Joystick Table
     * @param numAxes Number of Axes to add to the Joystick
     * @param numBtns Number of Buttons to add to the Joystick
     */
    public NetTableController(String name, int numAxes, int numBtns){
        super(numAxes, numBtns);

        m_table = RoboLib.getRobotTable().getSubTable("Joystick").getSubTable(name);
        for(int i = 0; i < numAxes; i++)
            m_axes[i] = new NetTableAxis(i + 1);

        for(int i = 0; i < numBtns; i++)
            m_btns[i] = new NetTableButton(i + 1);
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
