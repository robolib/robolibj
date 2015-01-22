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

package io.github.robolib.exception;

/**
 * Exception indicating that the Jaguar CAN Driver layer refused to send a
 * restricted message ID to the CAN bus.
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public class CANMessageNotAllowedException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = 2755736457012620920L;

    public CANMessageNotAllowedException(String msg) {
        super(msg);
    }
}
