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
 * Exception indicating that the CAN driver layer has not been initialized.
 * This happens when an entry-point is called before a CAN driver plugin
 * has been installed.
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class CANNotInitializedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 7943821576920330903L;

}
