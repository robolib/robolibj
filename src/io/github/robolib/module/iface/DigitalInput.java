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

package io.github.robolib.module.iface;

import static io.github.robolib.util.Common.allocateInt;

import java.nio.IntBuffer;

import io.github.robolib.identifier.BooleanSource;
import io.github.robolib.identifier.Trigger;
import io.github.robolib.jni.DIOJNI;
import io.github.robolib.jni.HALUtil;

/**
 *
 * @author noriah <vix@noriah.dev>
 */
public class DigitalInput extends DigitalIO implements Trigger, BooleanSource {

    public DigitalInput(DigitalChannel channel) {
        super(channel, Direction.IN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getState() {
        IntBuffer status = allocateInt();
        boolean value = DIOJNI.getDIO(m_port, status) == 0;
        HALUtil.checkStatus(status);
        return value;
    }
}
