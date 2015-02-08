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

package io.github.robolib;

import static io.github.robolib.util.CommonFunctions.getLE4IntBuffer;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.robolib.control.Joystick;
import io.github.robolib.jni.HALUtil;
import io.github.robolib.jni.NetworkCommunications;
import io.github.robolib.robot.GameMode;
import io.github.robolib.util.TableSender;
import io.github.robolib.util.log.ILogger;
import io.github.robolib.util.log.Logger;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 *
 */
public final class DriverStation {

    private Thread m_thread;
    private final Object m_dataSem;
    private volatile boolean m_thread_keepAlive;
    private volatile boolean m_thread_exit_error;
    private boolean m_newControlData;
    private final ByteBuffer m_packetDataAvailableMutex;
    private final ByteBuffer m_packetDataAvailableSem;
    private final ILogger m_log;

    private static DriverStation m_instance = null;
    
    public static DriverStation getInstance(){
        return m_instance == null ? m_instance = new DriverStation() : m_instance;
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
        m_thread = new Thread(() -> commTask(), "DriverStation JSThread");
        m_thread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
        m_thread.setDaemon(true);
        
    }
    
    /**
     * Task run by the Robot to get DS data.
     */
    private void commTask(){
        m_log.info("Communications thread started.");
        byte safetyCounter = 0;
        byte tableCounter = 0;
        ByteBuffer countBuffer = ByteBuffer.allocateDirect(1);
        short[] axes;
        short[] povs;
        int btns;
        synchronized(m_dataSem){
            m_dataSem.notifyAll();
        }
        
        while (m_thread_keepAlive){
            HALUtil.takeMultiWait(m_packetDataAvailableSem, m_packetDataAvailableMutex, 0);
            synchronized(this){
                
                axes = NetworkCommunications.HALGetJoystickAxes((byte) 0x00);
                povs = NetworkCommunications.HALGetJoystickPOVs((byte) 0x00);
                btns = NetworkCommunications.HALGetJoystickButtons((byte) 0x00, countBuffer);
                Joystick.setJoystickData(0x00, axes, povs, btns, countBuffer.get());
                countBuffer.clear();
                
                axes = NetworkCommunications.HALGetJoystickAxes((byte) 0x01);
                povs = NetworkCommunications.HALGetJoystickPOVs((byte) 0x01);
                btns = NetworkCommunications.HALGetJoystickButtons((byte) 0x01, countBuffer);
                Joystick.setJoystickData(0x01, axes, povs, btns, countBuffer.get());
                countBuffer.clear();
                
                axes = NetworkCommunications.HALGetJoystickAxes((byte) 0x02);
                povs = NetworkCommunications.HALGetJoystickPOVs((byte) 0x02);
                btns = NetworkCommunications.HALGetJoystickButtons((byte) 0x02, countBuffer);
                Joystick.setJoystickData(0x02, axes, povs, btns, countBuffer.get());
                countBuffer.clear();
                
                axes = NetworkCommunications.HALGetJoystickAxes((byte) 0x03);
                povs = NetworkCommunications.HALGetJoystickPOVs((byte) 0x03);
                btns = NetworkCommunications.HALGetJoystickButtons((byte) 0x03, countBuffer);
                Joystick.setJoystickData(0x03, axes, povs, btns, countBuffer.get());
                countBuffer.clear();
                
                axes = NetworkCommunications.HALGetJoystickAxes((byte) 0x04);
                povs = NetworkCommunications.HALGetJoystickPOVs((byte) 0x04);
                btns = NetworkCommunications.HALGetJoystickButtons((byte) 0x04, countBuffer);
                Joystick.setJoystickData(0x04, axes, povs, btns, countBuffer.get());
                countBuffer.clear();
                
                axes = NetworkCommunications.HALGetJoystickAxes((byte) 0x05);
                povs = NetworkCommunications.HALGetJoystickPOVs((byte) 0x05);
                btns = NetworkCommunications.HALGetJoystickButtons((byte) 0x05, countBuffer);
                Joystick.setJoystickData(0x05, axes, povs, btns, countBuffer.get());
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
    protected synchronized boolean isNewControlData() {
        boolean result = m_newControlData;
        m_newControlData = false;
        return result;
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
        IntBuffer status = getLE4IntBuffer();
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
        IntBuffer status = getLE4IntBuffer();
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
