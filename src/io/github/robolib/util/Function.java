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

package io.github.robolib.util;

/**
 * The Class Function.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public final class Function {
    
    /**
     * Instantiates a new function.
     *
     * @throws IllegalAccessException the illegal access exception
     */
    private Function() throws IllegalAccessException {
        
    }
    
    /**
     * The Interface VoidFunction.
     */
    public static interface VoidFunction{
        
        /**
         * Func.
         */
        public void Func();
    }
    
    /**
     * The Interface DoubleFunction.
     */
    public static interface DoubleFunction{
        
        /**
         * Func.
         *
         * @return the double
         */
        public double Func();
    }
    
    /**
     * The Interface BooleanFunction.
     */
    public static interface BooleanFunction{
        
        /**
         * Func.
         *
         * @return true, if successful
         */
        public boolean Func();
    }
}