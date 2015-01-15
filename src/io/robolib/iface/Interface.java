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

package io.robolib.iface;

import java.util.HashMap;
import java.util.Map;

import io.robolib.exception.ResourceAllocationException;
import io.robolib.util.log.Logger;

/**
 * The Class Interface.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public abstract class Interface {

    /** The maximum number of digital channels. */
    public static final int kMaxDigitalChannels = 26;

    /** The maximum number of analog input channels. */
    public static final int kMaxAnalogInputChannels = 8;

    /** The maximum number of analog output channels. */
    public static final int kMaxAnalogOutputChannels = 2;

    /** The maximum number of pwm channels. */
    public static final int kMaxPWMChannels = 20;

    /** The maximum number of relay channels. */
    public static final int kMaxRelayChannels = 4;

    /**
     * Check the given digital channel for validity
     *
     * @param channel the channel to check
     */
    protected static void checkDigitalChannel(final int channel){
        if(channel < 0 || channel > kMaxDigitalChannels){
            throw new IndexOutOfBoundsException("Bad Digital Channel");
        }
    }

    /**
     * Check the given analog input channel for validity
     *
     * @param channel the channel to check
     */
    protected static void checkAnalogInputChannel(final int channel){
        if(channel < 0 || channel > kMaxAnalogInputChannels){
            throw new IndexOutOfBoundsException("Bad Analog Input Channel");
        }
    }

    /**
     * Check the given analog output channel for validity
     *
     * @param channel the channel to check
     */
    protected static void checkAnalogOutputChannel(final int channel){
        if(channel < 0 || channel > kMaxAnalogOutputChannels){
            throw new IndexOutOfBoundsException("Bad Analog Output Channel");
        }
    }

    /**
     * Check the given pwm channel for validity
     *
     * @param channel the channel to check
     */
    protected static void checkPWMChannel(final int channel){
        if(channel < 0 || channel > kMaxPWMChannels){
            throw new IndexOutOfBoundsException("Bad PWM Channel");
        }
    }

    /**
     * Check the given relay channel for validity
     *
     * @param channel the channel to check
     */
    protected static void checkRelayChannel(final int channel){
        if(channel < 0 || channel > kMaxRelayChannels){
            throw new IndexOutOfBoundsException("Bad Relay Channel");
        }
    }

    /** The InterfaceType for this Interface. */
    protected final InterfaceType m_ifaceType;

    /** Keep a mapping of MXP pins to InterfaceTypes. */
    private static Map<Integer, InterfaceType> m_mxpMap = new HashMap<Integer, InterfaceType>();

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
            if(m_mxpMap.containsKey(pin) || !m_mxpMap.get(pin).equals(m_ifaceType)){
                throw new ResourceAllocationException("MXP pin '" + pin + "' already allocated as '" + m_mxpMap.get(pin).name() + "'.");
            }else{
                m_mxpMap.put(pin, m_ifaceType);
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
            if(m_mxpMap.containsKey(pin)){
                if(m_mxpMap.get(pin).equals(m_ifaceType)){
                    m_mxpMap.remove(pin);
                }else{
                    Logger.get(Interface.class).warn("Attempt to release MXP pin '" + pin + "' (" + m_mxpMap.get(pin).name() + ")  failed. Type");
                    Logger.get(Interface.class).warn("Allocated Type: " + m_mxpMap.get(pin).name() + ", Releasing type: " + m_ifaceType.name() + ".");
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
