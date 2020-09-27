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

package io.github.robolib.module.actuator;

import static io.github.robolib.util.Common.allocateInt;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.jni.HALUtil;
import io.github.robolib.jni.SolenoidJNI;
import io.github.robolib.lang.ResourceAllocationException;
import io.github.robolib.util.log.Logger;

/**
 * The Class SolenoidBase.
 *
 * @author noriah <vix@noriah.dev>
 */
public abstract class SolenoidBase {

    /**
     * The Enum Channel.
     *
     * @author noriah <vix@noriah.dev>
     */
    public static enum SolenoidChannel {

        /** Solenoid channel 0 on first module. */
        Channel0,

        /** Solenoid channel 1 on first module. */
        Channel1,

        /** Solenoid channel 2 on first module. */
        Channel2,

        /** Solenoid channel 3 on first module. */
        Channel3,

        /** Solenoid channel 4 on first module. */
        Channel4,

        /** Solenoid channel 5 on first module. */
        Channel5,

        /** Solenoid channel 6 on first module. */
        Channel6,

        /** Solenoid channel 7 on first module. */
        Channel7,

        /** Solenoid channel 0 on second module. */
        Channel8,

        /** Solenoid channel 1 on second module. */
        Channel9,

        /** Solenoid channel 2 on second module. */
        Channel10,

        /** Solenoid channel 3 on second module. */
        Channel11,

        /** Solenoid channel 4 on second module. */
        Channel12,

        /** Solenoid channel 5 on second module. */
        Channel13,

        /** Solenoid channel 6 on second module. */
        Channel14,

        /** Solenoid channel 7 on second module. */
        Channel15;
    }

    /**
     * The Enum Value.
     */
    public static enum Value {

        /** The off. */
        OFF,

        /** The on. */
        ON,

        /** The forward. */
        FORWARD,

        /** The reverse. */
        REVERSE;
    }

    /** Keep track of already used channels. */
    private static final boolean USED_CHANNELS[] = new boolean[SolenoidChannel.values().length];

    /** Ports for blacklist checking */
    private static final ByteBuffer MODULE_PORTS[] = new ByteBuffer[2];

    /** The Constant kSolenoidOff. */
    protected static final byte SOLENOID_OFF = (byte) 0x00;

    /** The Constant kSolenoidOn. */
    protected static final byte SOLENOID_ON = (byte) 0xFF;

    /**
     * Inits the module.
     *
     * @param channel the channel
     * @return the byte buffer
     */
    protected synchronized static final ByteBuffer initChannel(SolenoidChannel channel) {
        allocateChannel(channel);
        IntBuffer status = allocateInt();
        byte ch = (byte) (channel.ordinal() / 8);
        ByteBuffer port = SolenoidJNI.getPortWithModule(ch, (byte) channel.ordinal());
        port = SolenoidJNI.initializeSolenoidPort(port, status);
        HALUtil.checkStatus(status);

        if (MODULE_PORTS[ch] == null)
            MODULE_PORTS[ch] = port;
        return port;
    }

    /**
     * Free channel.
     *
     * @param channel the channel
     */
    protected synchronized static final void freeChannel(SolenoidChannel channel) {
        unallocateChannel(channel);
    }

    /**
     * Allocate a Solenoid channel.
     *
     * @param channel the Solenoid channel to allocate
     */
    private synchronized static final void allocateChannel(SolenoidChannel channel) {

        if (USED_CHANNELS[channel.ordinal()] == true)
            throw new ResourceAllocationException("Solenoid channel '" + channel.name() + "' already in use.");

        USED_CHANNELS[channel.ordinal()] = true;
    }

    /**
     * Free a Solenoid channel.
     *
     * @param channel the Solenoid channel to free
     */
    private synchronized static final void unallocateChannel(SolenoidChannel channel) {

        if (USED_CHANNELS[channel.ordinal()] == false)
            Logger.get(SolenoidBase.class, "Solenoid")
                    .error("Solenoid Channel '" + channel.name() + "' was not allocated. How did you get here?");

        USED_CHANNELS[channel.ordinal()] = false;
    }

    /**
     * Reads complete solenoid blacklist for all 8 solenoids as a single byte.
     *
     * If a solenoid is shorted, it is added to the blacklist and disabled until
     * power cycle, or until faults are cleared.
     *
     * @see #clearAllPCMStickyFaults(int)
     *
     * @param module
     * @return The solenoid blacklist of all 8 solenoids on the module.
     */
    public final byte getPCMSolenoidBlacklist(int module) {
        IntBuffer status = allocateInt();
        byte value = SolenoidJNI.getPCMSolenoidBlackList(MODULE_PORTS[module], status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * @param module
     * @return true if PCM sticky fault is set : The common highside solenoid
     *         voltage rail is too low, most likely a solenoid channel is shorted.
     */
    public final boolean getPCMSolenoidVoltageStickyFault(int module) {
        IntBuffer status = allocateInt();
        boolean value = SolenoidJNI.getPCMSolenoidVoltageStickyFault(MODULE_PORTS[module], status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * @param module
     * @return true if PCM is in fault state : The common highside solenoid voltage
     *         rail is too low, most likely a solenoid channel is shorted.
     */
    public final boolean getPCMSolenoidVoltageFault(int module) {
        IntBuffer status = allocateInt();
        boolean value = SolenoidJNI.getPCMSolenoidVoltageFault(MODULE_PORTS[module], status);
        HALUtil.checkStatus(status);
        return value;
    }

    /**
     * Clear ALL sticky faults inside PCM that Compressor is wired to.
     *
     * If a sticky fault is set, then it will be persistently cleared. Compressor
     * drive maybe momentarily disable while flags are being cleared. Care should be
     * taken to not call this too frequently, otherwise normal compressor
     * functionality may be prevented.
     *
     * If no sticky faults are set then this call will have no effect.
     *
     * @param module
     */
    public final void clearAllPCMStickyFaults(int module) {
        IntBuffer status = allocateInt();
        SolenoidJNI.clearAllPCMStickyFaults(MODULE_PORTS[module], status);
        HALUtil.checkStatus(status);
    }

    /**
     * Sets the.
     *
     * @param port  the port
     * @param value the value
     */
    protected static final void set(ByteBuffer port, byte value) {
        IntBuffer status = allocateInt();
        SolenoidJNI.setSolenoid(port, value, status);
        HALUtil.checkStatus(status);
    }

    /**
     *
     * @param port
     * @return the value of the solenoid
     */
    protected static final boolean get(ByteBuffer port) {
        IntBuffer status = allocateInt();
        byte out = SolenoidJNI.getSolenoid(port, status);
        HALUtil.checkStatus(status);
        return out == SOLENOID_ON;
    }

    /**
     * Sets the.
     *
     * @param value the value
     */
    public abstract void set(Value value);

    /**
     *
     * @return get the value of the solenoid
     */
    public abstract Value get();

}
