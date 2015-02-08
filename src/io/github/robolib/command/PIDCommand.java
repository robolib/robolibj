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

package io.github.robolib.command;

import io.github.robolib.identifier.PIDSink;
import io.github.robolib.identifier.PIDSource;
import io.github.robolib.identifier.Sendable;
import io.github.robolib.module.controller.PIDController;
import io.github.robolib.nettable.ITable;

/**
 * 
 *
 * @author noriah Reuland <vix@noriah.dev>
 */
public abstract class PIDCommand extends Command implements Sendable {
    
    private PIDController m_controller;
    
    private PIDSink m_sink = this::usePIDOutput;
    
    private PIDSource m_source = this::returnPIDInput;
    
    /**
     * Instantiates a {@link PIDCommand} that will use the given p, i and d values.
     * @param name the name of the command
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     */
    public PIDCommand(String name, double p, double i, double d){
        super(name);
        m_controller = new PIDController(p, i, d, m_source, m_sink);
    }
    
    /**
     * Instantiates a {@link PIDCommand} that will use the given p, i and d values.  It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param name the name
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param period the time (in seconds) between calculations
     */
    public PIDCommand(String name, double p, double i, double d, double period){
        super(name);
        m_controller = new PIDController(p, i, d, m_source, m_sink, period);
    }
    
    /**
     * Instantiates a {@link PIDCommand} that will use the given p, i and d values.
     * It will use the class name as its name.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     */
    public PIDCommand(double p, double i, double d){
        m_controller = new PIDController(p, i, d, m_source, m_sink);
    }
    
    /**
     * Instantiates a {@link PIDCommand} that will use the given p, i and d values.
     * It will use the class name as its name..
     * It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param period the time (in seconds) between calculations
     */
    public PIDCommand(double p, double i, double d, double period){
        m_controller = new PIDController(p, i, d, m_source, m_sink, period);
    }
    
    /**
     * Returns the {@link PIDController} used by this {@link PIDCommand}.
     * Use this if you would like to fine tune the pid loop.
     *
     * @return the {@link PIDController} used by this {@link PIDCommand}
     */
    protected PIDController getController(){
        return m_controller;
    }
    
    @Override
    void initialize_impl(){
        m_controller.enable();
    }
    
    @Override
    void end_impl(){
        m_controller.disable();
    }
    
    @Override
    void interrupted_impl(){
        m_controller.disable();
    }
    
    /**
     * Adds the given value to the setpoint.
     * If {@link PIDCommand#setInputRange(double, double) setInputRange(...)} was used,
     * then the bounds will still be honored by this method.
     * @param deltaSetpoint the change in the setpoint
     */
    public void setSetpointRelative(double deltaSetpoint){
        setSetpoint(getSetpoint() + deltaSetpoint);
    }
    
    /**
     * Sets the setpoint to the given value.  If {@link PIDCommand#setInputRange(double, double) setInputRange(...)}
     * was called,
     * then the given setpoint
     * will be trimmed to fit within the range.
     * @param setpoint the new setpoint
     */
    protected void setSetpoint(double point){
        m_controller.setSetpoint(point);
    }
    
    /**
     * Returns the setpoint.
     * @return the setpoint
     */
    protected double getSetpoint(){
        return m_controller.getSetpoint();
    }
    
    /**
     * Returns the current position
     * @return the current position
     */
    protected double getPosition(){
        return returnPIDInput();
    }
    
    /**
     * Sets the maximum and minimum values expected from the input and setpoint.
     *
     * @param min the minimum value expected from the input and setpoint
     * @param max the maximum value expected from the input and setpoint
     */
    protected void setInputRange(double min, double max){
        m_controller.setInputRange(min, max);
    }
    
    /**
     * Returns the input for the pid loop.
     *
     * <p>It returns the input for the pid loop, so if this command was based
     * off of a gyro, then it should return the angle of the gyro</p>
     *
     * <p>All subclasses of {@link PIDCommand} must override this method.</p>
     *
     * <p>This method will be called in a different thread then the {@link Scheduler} thread.</p>
     *
     * @return the value the pid loop should use as input
     */
    protected abstract double returnPIDInput();
    
    /**
     * Uses the value that the pid loop calculated.  The calculated value is the "output" parameter.
     * This method is a good time to set motor values, maybe something along the lines of <code>driveline.tankDrive(output, -output)</code>
     *
     * <p>All subclasses of {@link PIDCommand} must override this method.</p>
     *
     * <p>This method will be called in a different thread then the {@link Scheduler} thread.</p>
     *
     * @param output the value the pid loop calculated
     */
    protected abstract void usePIDOutput(double output);
    
    public String getSmartDashboardType(){
        return "PIDCommand";
    }
    
    public void initTable(ITable table){
        m_controller.initTable(table);
        super.initTable(table);
    }
    
}
