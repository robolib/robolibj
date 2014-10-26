/*
 * Copyright (c) 2014 noriah <vix@noriah.dev>.
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

package org.team2583.robolib.exception;

/**
 * An exception for use with robot code.
 * 
 * throw this exception when something goes wrong with the robot that does
 * not fall under any other exception type.
 * @since 0.1.0
 * @author noriah Reuland <vix@noriah.dev>
 */
public final class RobotException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3748667178724340857L;

    public RobotException(String msg){
        super(msg);
    }
}
