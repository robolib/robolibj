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

package io.github.robolib.exception;

/**
 * An exception for use with robot code.
 * 
 * throw this exception when something goes wrong with the robot that does
 * not fall under any other exception type.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 * @since 0.1.0
 */
public final class RobotException extends RuntimeException {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3748667178724340857L;

    /**
     * Instantiates a new robot exception.
     *
     * @param msg the msg
     */
    public RobotException(String msg){
        super(msg);
    }
}
