/*
 * Copyright (c) 2014 noriah vix@noriah.dev.
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

package org.warriors2583.robolib;

//import com.sun.squawk.util.MathUtils;
import org.warriors2583.robolib.command.C_UpdateDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//import org.warriors2583.frc2014.RMap;
//import org.warriors2583.frc2014.common.SS_Compressor;

/**
 * Dashboard Control Class
 * @author noriah Reuland
 */
public class SS_Dashboard extends Subsystem {// implements RMap {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    
    //private static final String m_memMax;
    private static final SS_Dashboard m_instance = new SS_Dashboard();

    public static SS_Dashboard getInstance(){
        return m_instance;
    }

    static{
        //m_memMax = String.valueOf((MathUtils.round(((double)Runtime.getRuntime().totalMemory()/1048576) * 1e2))/1e2) + "M";
    }
    
    public static void update(){
        //SmartDashboard.putBoolean(DASH_COMPRESSOR_RUNNING, SS_Compressor.isRunning());
        //double memFree = ((MathUtils.round(((double)Runtime.getRuntime().freeMemory()/1048576) * 1e2))/1e2);
        //SmartDashboard.putString(DASH_MEMORY_STATUS, memFree + "M/" + m_memMax);
    }

    private SS_Dashboard(){
        super("SS_Dashboard");
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new C_UpdateDashboard());
    }
}
