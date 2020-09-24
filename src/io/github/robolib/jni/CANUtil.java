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

package io.github.robolib.jni;

import io.github.robolib.lang.CANInvalidBufferException;
import io.github.robolib.lang.CANMessageNotAllowedException;
import io.github.robolib.lang.CANMessageNotFoundException;
import io.github.robolib.lang.CANNotInitializedException;

/**
 *
 *
 * @author noriah <vix@noriah.dev>
 */
public class CANUtil {

    // FRC Error codes
    static final int ERR_CANSessionMux_InvalidBuffer = -44086;
    static final int ERR_CANSessionMux_MessageNotFound = -44087;
    static final int ERR_CANSessionMux_NotAllowed = -44088;
    static final int ERR_CANSessionMux_NotInitialized = -44089;

    public static void checkStatus(int status, int messageID) throws
    CANInvalidBufferException, CANMessageNotAllowedException,
    CANNotInitializedException {
        switch (status) {
        case NIRioStatus.kRioStatusSuccess:
            return;
        case ERR_CANSessionMux_InvalidBuffer:
        case NIRioStatus.kRIOStatusBufferInvalidSize:
            throw new CANInvalidBufferException();
        case ERR_CANSessionMux_MessageNotFound:
        case NIRioStatus.kRIOStatusOperationTimedOut:
            throw new CANMessageNotFoundException();
        case ERR_CANSessionMux_NotAllowed:
        case NIRioStatus.kRIOStatusFeatureNotSupported:
            throw new CANMessageNotAllowedException("MessageID = " + Integer.toString(messageID));
        case ERR_CANSessionMux_NotInitialized:
        case NIRioStatus.kRIOStatusResourceNotInitialized:
            throw new CANNotInitializedException();
        default:
            throw new RuntimeException("Fatal status code detected:  " + Integer.toString(status));
        }
    }
}
