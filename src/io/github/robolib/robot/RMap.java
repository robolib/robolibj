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

package io.github.robolib.robot;

import java.util.HashMap;
import java.util.Map;

import io.github.robolib.iface.AnalogIO;
import io.github.robolib.iface.DigitalIO;
import io.github.robolib.output.Jaguar;
import io.github.robolib.output.SpeedController;
import io.github.robolib.output.Talon;
import io.github.robolib.output.TalonSRX;
import io.github.robolib.output.Victor;
import io.github.robolib.output.VictorSP;
import io.github.robolib.pneumatic.DoubleSolenoid;
import io.github.robolib.pneumatic.Solenoid;
import io.github.robolib.pneumatic.SolenoidBase;
import io.github.robolib.util.ConstantRunner;
import io.github.robolib.util.log.Logger;


/**
 * The Class RMap.
 *
 * @author Austin Reuland <amreuland@gmail.com>
 */
public class RMap {
    
    public RMap(){
        
        
    }
    
    private static final Map<String, Class<? extends SpeedController>> map_SpeedController = new HashMap<>();
    
    private static final Map<String, Class<? extends SolenoidBase>> map_Solenoid = new HashMap<>();
    
    private static final Map<String, Class<? extends DigitalIO>> map_DigitalIO = new HashMap<>();
    
    static{
        addSpeedControllerType("Talon", Talon.class);
        addSpeedControllerType("TalonSRX", TalonSRX.class);
        addSpeedControllerType("Victor", Victor.class);
        addSpeedControllerType("VictorSP", VictorSP.class);
        addSpeedControllerType("Jaguar", Jaguar.class);
        
        addSolenoidType("Solenoid", Solenoid.class);
        addSolenoidType("SingleSolenoid", Solenoid.class);
        addSolenoidType("DoubleSolenoid", DoubleSolenoid.class);
        
        map_DigitalIO.put("INPUT", DigitalIO.class);
        map_DigitalIO.put("OUTPUT", DigitalIO.class);
        
        
    }
    
    
 
    /** The Constant runner. */
    public static final ConstantRunner runner = new ConstantRunner("RMap.ini");
    
    public static final void addSpeedControllerType(String key, Class<? extends SpeedController> type){
        key = key.toUpperCase();
        if(map_SpeedController.containsKey(key)){
            Logger.get(RMap.class).warn("SpeedController map already contains key '" + key + "'");
        }else{
            map_SpeedController.put(key, type);
        }
        
    }
    
    public static final void addSolenoidType(String key, Class<? extends SolenoidBase> type){
        key = key.toUpperCase();
        if(map_Solenoid.containsKey(key)){
            Logger.get(RMap.class).warn("Solenoid map already contains key '" + key + "'");
        }else{
            map_Solenoid.put(key, type);
        }
    }
    
    public static final void addDigitalIOType(String key, Class<? extends DigitalIO> type){
        key = key.toUpperCase();
        
    }
    
    public static final void addAnalogType(String key, Class<? extends AnalogIO> type){
        key = key.toUpperCase();
        
    }
    
    public static SpeedController getNewSpeedController(String key){
        key = key.toUpperCase();
        
        
        return null;
    }
    

}