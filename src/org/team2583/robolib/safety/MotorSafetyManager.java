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

package org.team2583.robolib.safety;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class MotorSafetyManager.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public final class MotorSafetyManager {
    
    /** The m_safety objects. */
    private static List<MotorSafetyHelper> m_safetyObjects = new ArrayList<MotorSafetyHelper>();
    
    /**
     * Adds the motor.
     *
     * @param object the object
     * @return the motor safety helper
     */
    public static MotorSafetyHelper addMotor(MotorSafety object){
        MotorSafetyHelper msh = new MotorSafetyHelper(object);
        m_safetyObjects.add(msh);
        return msh;
    }
    
    /**
     * Check.
     */
    public static void check(){
        for(MotorSafetyHelper msh : m_safetyObjects){
            msh.check();
        }
    }

}
