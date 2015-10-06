/*
 * Copyright (c) 2015 noriah Reuland <vix@noriah.dev>.
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

package io.github.robolib;


import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.jni.HALUtil;
import io.github.robolib.jni.NetworkCommunications;
import io.github.robolib.modes.GameMode;
import io.github.robolib.module.RoboRIO;
import io.github.robolib.module.hid.Joystick;
import io.github.robolib.util.Common;
import io.github.robolib.util.TableSender;
import io.github.robolib.util.log.ILogger;
import io.github.robolib.util.log.Logger;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 *
 */
public final class DriverStation implements Runnable {

    private final Thread m_thread;
    private final Object m_dataSem;
    private static final Object m_jsSem = new Object();
    private volatile boolean m_thread_keepAlive;
    private volatile boolean m_thread_exit_error;
    private boolean m_newControlData;
    private final ByteBuffer m_packetDataAvailableMutex;
    private final ByteBuffer m_packetDataAvailableSem;
    private final ILogger m_log;

    /** The Constant COUNT_JOYSTICKS. */
    public static final int COUNT_JOYSTICKS = 6;
    
    /** The m_joystick axes. */
    private static final short m_joystickAxes[][] =
            new short[COUNT_JOYSTICKS][NetworkCommunications.MAX_JS_AXES];
    
    /** The m_joystick po vs. */
    private static final short m_joystickPOVs[][] =
            new short[COUNT_JOYSTICKS][NetworkCommunications.MAX_JS_POVS];
    
    /** The m_joystick buttons. */
    private static final int m_joystickBtns[] = new int[COUNT_JOYSTICKS];
    
    /** The m_joystick buttons count. */
    private static final byte m_joystickNBtn[] = new byte[COUNT_JOYSTICKS];

    private static DriverStation m_instance;
    
    protected static void initialize(){
        if(m_instance != null)
            throw new IllegalStateException("DriverStation already Initialized");
        m_instance = new DriverStation();
    }
    
    public static DriverStation getInstance(){
        return m_instance;
    }
    
    /**
     * 
     */
    private DriverStation(){
        m_log = Logger.get(DriverStation.class);
        m_log.info("Interface initializing");
        m_dataSem = new Object();
        m_packetDataAvailableMutex = HALUtil.initializeMutexNormal();
        m_packetDataAvailableSem = HALUtil.initializeMultiWait();
        NetworkCommunications.setNewDataSem(m_packetDataAvailableSem);
        
        //WOO lookie here. Lambda functions ^_^
        m_thread = new Thread(this, "DriverStation JSThread");
        m_thread.setPriority(8);
        m_thread.setDaemon(true);
    }
    
    /**
     * Task run by the Robot to get DS data.
     */
    public void run(){
        m_log.info("Communications thread started.");
        byte safetyCounter = 0;
        byte tableCounter = 0;
        ByteBuffer countBuffer = ByteBuffer.allocateDirect(1);
        synchronized(m_dataSem){
            m_dataSem.notifyAll();
        }
        
        while (m_thread_keepAlive){
            HALUtil.takeMultiWait(m_packetDataAvailableSem, m_packetDataAvailableMutex, 0);
            synchronized(m_jsSem){
                
                m_joystickAxes[0] = NetworkCommunications.HALGetJoystickAxes((byte) 0x00);
                m_joystickPOVs[0] = NetworkCommunications.HALGetJoystickPOVs((byte) 0x00);
                m_joystickBtns[0] = NetworkCommunications.HALGetJoystickButtons((byte) 0x00, countBuffer);
                m_joystickNBtn[0] = countBuffer.get();
                countBuffer.clear();
                
                m_joystickAxes[1] = NetworkCommunications.HALGetJoystickAxes((byte) 0x01);
                m_joystickPOVs[1] = NetworkCommunications.HALGetJoystickPOVs((byte) 0x01);
                m_joystickBtns[1] = NetworkCommunications.HALGetJoystickButtons((byte) 0x01, countBuffer);
                m_joystickNBtn[1] = countBuffer.get();
                countBuffer.clear();
                
                m_joystickAxes[2] = NetworkCommunications.HALGetJoystickAxes((byte) 0x02);
                m_joystickPOVs[2] = NetworkCommunications.HALGetJoystickPOVs((byte) 0x02);
                m_joystickBtns[2] = NetworkCommunications.HALGetJoystickButtons((byte) 0x02, countBuffer);
                m_joystickNBtn[2] = countBuffer.get();
                countBuffer.clear();
                
                m_joystickAxes[3] = NetworkCommunications.HALGetJoystickAxes((byte) 0x03);
                m_joystickPOVs[3] = NetworkCommunications.HALGetJoystickPOVs((byte) 0x03);
                m_joystickBtns[3] = NetworkCommunications.HALGetJoystickButtons((byte) 0x03, countBuffer);
                m_joystickNBtn[3] = countBuffer.get();
                countBuffer.clear();
                
                m_joystickAxes[4] = NetworkCommunications.HALGetJoystickAxes((byte) 0x04);
                m_joystickPOVs[4] = NetworkCommunications.HALGetJoystickPOVs((byte) 0x04);
                m_joystickBtns[4] = NetworkCommunications.HALGetJoystickButtons((byte) 0x04, countBuffer);
                m_joystickNBtn[4] = countBuffer.get();
                countBuffer.clear();
                
                m_joystickAxes[5] = NetworkCommunications.HALGetJoystickAxes((byte) 0x05);
                m_joystickPOVs[5] = NetworkCommunications.HALGetJoystickPOVs((byte) 0x05);
                m_joystickBtns[5] = NetworkCommunications.HALGetJoystickButtons((byte) 0x05, countBuffer);
                m_joystickNBtn[5] = countBuffer.get();
                countBuffer.clear();
                
                m_newControlData = true;
            }
            
            synchronized(m_dataSem){
                m_dataSem.notifyAll();
            }
            
            if(++tableCounter >= 2){
                TableSender.getInstance().runFramework();
                tableCounter = 0;
            }

            if(++safetyCounter >= 4){
                SafetyManager.check();
                safetyCounter = 0;
            }
        }
        if(m_thread_exit_error)
            m_log.severe("Communications thread ended!");
    }

    private static double m_nextComplainTime = 0.0;
    
    protected static void complainJoystickMissing(String msg){
        synchronized(m_instance) {
            double c = RoboRIO.getFPGATimestamp();
            if(c > m_nextComplainTime){
                Logger.get(Joystick.class).error(msg);
                m_nextComplainTime = c + 5.0;
            }
        }
        
    }

    /**
     * Gets the stick axis.
     *
     * @param stick the stick
     * @param axis the axis
     * @return the stick axis
     */
    public static double getStickAxis(Joystick.JSID stick, int axis){
        synchronized(m_jsSem){
            if(m_joystickAxes[stick.ordinal()].length <= axis){
                complainJoystickMissing("Axis '" + axis + "' on stick '" + stick + "' is invalid. Is it plugged in?");
                return 0.0;
            }
            
            double value = m_joystickAxes[stick.ordinal()][axis];
            if(value < 0){
                return value / 128.0;
            }else{
                return value / 127.0;
            }
        }
    }
    
    /**
     * Gets the stick button.
     *
     * @param stick the stick
     * @param button the button
     * @return the stick button
     */
    public static boolean getStickButton(Joystick.JSID stick, int button){
        synchronized(m_jsSem){    
            if(m_joystickNBtn[stick.ordinal()] <= button){
                complainJoystickMissing("Button '" + button + "' on stick '" + stick + "' is invalid. Is it plugged in?");
                return false;
            }
            
            return ((1 << button) & m_joystickBtns[stick.ordinal()]) != 0;
        }
    }
    
    /**
     * Gets the stick pov.
     *
     * @param stick the stick
     * @param pov the pov
     * @return the stick pov
     */
    public static int getStickPOV(Joystick.JSID stick, int pov){
        synchronized(m_jsSem){
            if(m_joystickPOVs[stick.ordinal()].length <= pov){
                complainJoystickMissing("Button '" + pov + "' on stick '" + stick + "' is invalid. Is it plugged in?");
                return 0;
            }
            
            return m_joystickPOVs[stick.ordinal()][pov];
        }
    }
    
    protected void startThread(){
        if(!m_thread.isAlive()){
            m_thread_keepAlive = true;
            m_thread_exit_error = true;
            m_thread.start();
        }
    }
    
    public void exitNoError(){
        m_thread_keepAlive = false;
        m_thread_exit_error = false;
    }
    
    public void exit(){
        m_thread_keepAlive = false;
    }
    
    /**
     * Wait for new data from the driver station.
     */
    public void waitForData() {
        waitForData(0);
    }

    /**
     * Wait for new data or for timeout, which ever comes first.  If timeout is
     * 0, wait for new data only.
     *
     * @param timeout The maximum time in milliseconds to wait.
     */
    public void waitForData(long timeout) {
        synchronized (m_dataSem) {
            try {
                m_dataSem.wait(timeout);
            } catch (InterruptedException ex) {
            }
        }
    }
    
    /**
     * Has a new control packet from the driver station arrived since the last time this function was called?
     * @return True if the control data has been updated since the last call.
     */
    protected boolean isNewControlData() {
        synchronized(this){
            boolean result = m_newControlData;
            m_newControlData = false;
            return result;
        }
    }
    
    private int m_modeInt = 0;
    private int m_modeNewInt = 0;
    
    
    protected boolean hasModeChanged(){
        m_modeNewInt = NetworkCommunications.HALGetRobotStatus();
        if(m_modeNewInt != m_modeInt){
            m_modeInt = m_modeNewInt;
            return true;
        }
        return false;
    }
    
    /**
     * Determine if the Robot is currently disabled.
     *
     * @return True if the Robot is currently disabled by the field controls.
     */
    public static boolean isDisabled() {
        return (NetworkCommunications.HALGetRobotStatus() & 1) == 0;
    }

    /**
     * Determine if the Robot is currently enabled.
     *
     * @return True if the Robot is currently enabled by the field controls.
     */
    public static boolean isEnabled() {
        return (NetworkCommunications.HALGetRobotStatus() & 1) != 0;
    }

    /**
     * Determine if the robot is currently in Autonomous mode.
     *
     * @return True if the robot is currently operating Autonomously as
     * determined by the field controls.
     */
    public static boolean isAutonomous() {
        return (NetworkCommunications.HALGetRobotStatus() & 2) != 0;
    }

    /**
     * Determine if the robot is currently in Test mode.
     *
     * @return True if the robot is currently operating in Test mode as
     * determined by the driver station.
     */
    public static boolean isTest() {
        return (NetworkCommunications.HALGetRobotStatus() & 4) != 0;
    }
    
    /**
     * Determine if the robot is currently Emergency stopped.
     * 
     * @return True if the robot is currently emergency stopped.
     */
    public static boolean isEStopped(){
        return (NetworkCommunications.HALGetRobotStatus() & 8) != 0;
    }
    
    /**
     * Check if the Field Management System is attached to the DS
     * 
     * @return True if the FMS is attached
     */
    public static boolean isFMSAttached(){
        return (NetworkCommunications.HALGetRobotStatus() & 16) != 0;
    }
    
    /**
     * Check if the Driver Station is attached to the robot
     * 
     * @return True if we have a driver station
     */
    public static boolean isDSAttached(){
        return (NetworkCommunications.HALGetRobotStatus() & 32) != 0;
    }

    /**
     * Determine if the robot is currently in Operator Control mode.
     *
     * @return True if the robot is currently operating in Tele-Op mode as
     * determined by the field controls.
     */
    public static boolean isOperatorControl() {
        int word = NetworkCommunications.HALGetRobotStatus();
        return ((word & 6) == 0) && ((word & 1) != 0);
    }
    
    /**
     * Check on the overall status of the system.
     *
     * @return Is the system active (i.e. PWM motor outputs, etc. enabled)?
     */
    public static boolean isSysActive() {
        IntBuffer status = Common.allocateInt();
        boolean retVal = NetworkCommunications.HALGetSystemActive(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Check if the system is browned out.
     * 
     * @return True if the system is browned out
     */
    public static boolean isBrownedOut() {
        IntBuffer status = Common.allocateInt();
        boolean retVal = NetworkCommunications.HALGetBrownedOut(status);
        HALUtil.checkStatus(status);
        return retVal;
    }
    
    /**
     * Get the current driver station indicated mode.
     * @return the driver station mode as a {@link GameMode}
     */
    public static GameMode getGameMode(){
        if(isDisabled())
            return GameMode.DISABLED;
        else if(isTest())
            return GameMode.TEST;
        else if(isAutonomous())
            return GameMode.AUTON;
        else
            return GameMode.TELEOP;
    }

    /**
     * Report an error to the Driver Station
     * 
     * @param err
     */
    public static void reportError(String err){
        if(isDSAttached())
            NetworkCommunications.HALSetErrorData(err);
    }
}
