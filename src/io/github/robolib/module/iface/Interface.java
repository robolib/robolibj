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

package io.github.robolib.module.iface;

import io.github.robolib.lang.ResourceAllocationException;
import io.github.robolib.util.log.Logger;

/**
 * The Class Interface.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class Interface {
    
    /**
     * Enum representation of interfaces.
     * 
     * @author noriah Reuland <vix@noriah.dev>
     */
    protected static enum InterfaceType {
        ANALOG,
        DIGITALIO,
        I2C,
        PWM,
        RELAY,
        SERIAL,
        SPI;
    }

    /** The maximum number of digital channels. */
    public static final int MAX_DIGITAL_CHANNELS = 26;

    /** The maximum number of analog input channels. */
    public static final int MAX_ANALOG_IN_CHANNELS = 8;

    /** The maximum number of analog output channels. */
    public static final int MAX_ANALOG_OUT_CHANNELS = 2;

    /** The maximum number of pwm channels. */
    public static final int MAX_PWM_CHANNELS = 20;

    /** The maximum number of relay channels. */
    public static final int MAX_RELAY_CHANNELS = 4;

    /**
     * Check the given digital channel for validity
     *
     * @param channel the channel to check
     */
    protected static final void checkDigitalChannel(final int channel){
        if(channel < 0 || channel > MAX_DIGITAL_CHANNELS){
            throw new IndexOutOfBoundsException("Bad Digital Channel");
        }
    }

    /**
     * Check the given analog input channel for validity
     *
     * @param channel the channel to check
     */
    protected static final void checkAnalogInputChannel(final int channel){
        if(channel < 0 || channel > MAX_ANALOG_IN_CHANNELS){
            throw new IndexOutOfBoundsException("Bad AnalogIO Input Channel");
        }
    }

    /**
     * Check the given analog output channel for validity
     *
     * @param channel the channel to check
     */
    protected static final void checkAnalogOutputChannel(final int channel){
        if(channel < 0 || channel > MAX_ANALOG_OUT_CHANNELS){
            throw new IndexOutOfBoundsException("Bad AnalogIO Output Channel");
        }
    }

    /**
     * Check the given pwm channel for validity
     *
     * @param channel the channel to check
     */
    protected static final void checkPWMChannel(final int channel){
        if(channel < 0 || channel > MAX_PWM_CHANNELS){
            throw new IndexOutOfBoundsException("Bad PWM Channel");
        }
    }

    /**
     * Check the given relay channel for validity
     *
     * @param channel the channel to check
     */
    protected static final void checkRelayChannel(final int channel){
        if(channel < 0 || channel > MAX_RELAY_CHANNELS){
            throw new IndexOutOfBoundsException("Bad Relay Channel");
        }
    }

    /** The InterfaceType for this Interface. */
    protected final InterfaceType m_ifaceType;

    /** Keep a mapping of MXP pins to InterfaceTypes. */
    private static final InterfaceType[] m_mxpArr = new InterfaceType[34];

    /**
     * Instantiates a new interface.
     *
     * @param iType the InterfaceType for this Interface
     */
    protected Interface(InterfaceType iType){
        m_ifaceType = iType;
    }

    /**
     * Allocate an MXP pin
     *
     * @param pin the pin we are allocating.
     */
    protected final void allocateMXPPin(int pin){
        if(pin > 0){
            if(m_mxpArr[pin] != null){
                throw new ResourceAllocationException("MXP pin '" + pin + "' already allocated as '" + m_mxpArr[pin].name() + "'.");
            }else{
                m_mxpArr[pin] = m_ifaceType;
            }
        }
    }

    /**
     * Free an MXP pin
     *
     * @param pin the pin we are allocating.
     */
    protected final void freeMXPPin(int pin){
        if(pin > 0){
            if(m_mxpArr[pin] != null){
                if(m_mxpArr[pin] == m_ifaceType){
                    m_mxpArr[pin] = null;
                }else{
                    Logger.get(Interface.class).warn("Attempt to release MXP pin '" + pin + "' (" + m_mxpArr[pin].name() + ")  failed. Type");
                    Logger.get(Interface.class).warn("Allocated Type: " + m_mxpArr[pin].name() + ", Releasing type: " + m_ifaceType.name() + ".");
                }
            }else{
                Logger.get(Interface.class).warn("MXP pin '" + pin + "' Was not allocated. Should have been type: '" + m_ifaceType.name() + "'.");
            }
        }
    }

    /**
     * Gets the interface type.
     *
     * @return the interface type for this interface
     */
    public final InterfaceType getInterfaceType(){
        return m_ifaceType;
    }

}
