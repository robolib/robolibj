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
public abstract class PIDSubsystem extends Subsystem implements Sendable {
    
    final PIDController m_controller;
    
    final PIDSink m_sink = this::usePIDOutput;
    final PIDSource m_source = this::returnPIDInput;

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * @param name the name
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     */
    public PIDSubsystem(String name, double p, double i, double d){
        super(name);
        m_controller = new PIDController(p, i, d, m_source, m_sink);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * @param name the name
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param f the feed forward value
     */
    public PIDSubsystem(String name, double p, double i, double d, double f){
        super(name);
        m_controller = new PIDController(p, i, d, f, m_source, m_sink);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.  It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param name the name
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param period the time (in seconds) between calculations
     */
    public PIDSubsystem(String name, double p, double i, double d, double f, double period){
        super(name);
        m_controller = new PIDController(p, i, d, f, m_source, m_sink, period);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * It will use the class name as its name.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     */
    public PIDSubsystem(double p, double i, double d){
        m_controller = new PIDController(p, i, d, m_source, m_sink);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * It will use the class name as its name.
     * It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param f the feed forward coefficient
     * @param period the time (in seconds) between calculations
     */
    public PIDSubsystem(double p, double i, double d, double f){
        m_controller = new PIDController(p, i, d, f, m_source, m_sink);
    }

    /**
     * Instantiates a {@link PIDSubsystem} that will use the given p, i and d values.
     * It will use the class name as its name.
     * It will also space the time
     * between PID loop calculations to be equal to the given period.
     * @param p the proportional value
     * @param i the integral value
     * @param d the derivative value
     * @param period the time (in seconds) between calculations
     */
    public PIDSubsystem(double p, double i, double d, double f, double period){
        m_controller = new PIDController(p, i, d, f, m_source, m_sink, period);
    }
    
    /**
     * Returns the {@link PIDController} used by this {@link PIDSubsystem}.
     * Use this if you would like to fine tune the pid loop.
     *
     * @return the {@link PIDController} used by this {@link PIDSubsystem}
     */
    public final PIDController getPIDController() {
        return m_controller;
    }
    
    /**
     * Adds the given value to the setpoint.
     * If {@link PIDSubsystem#setInputRange(double, double) setInputRange(...)} was used,
     * then the bounds will still be honored by this method.
     * @param deltaSetpoint the change in the setpoint
     */
    public final void setSetpointRelative(double deltaSetpoint) {
        setSetpoint(getPosition() + deltaSetpoint);
    }
    
    /**
     * Sets the setpoint to the given value.  If {@link PIDSubsystem#setInputRange(double, double) setInputRange(...)}
     * was called,
     * then the given setpoint
     * will be trimmed to fit within the range.
     * @param point the new setpoint
     */
    public final void setSetpoint(double point) {
        m_controller.setSetpoint(point);
    }
    
    /**
     * Returns the setpoint.
     * @return the setpoint
     */
    public final double getSetpoint() {
        return m_controller.getSetpoint();
    }
    
    /**
     * Returns the current position
     * @return the current position
     */
    public final double getPosition() {
        return returnPIDInput();
    }
    
    /**
     * Sets the maximum and minimum values expected from the input and setpoint.
     *
     * @param min the minimum value expected from the input and setpoint
     * @param max the maximum value expected from the input and setpoint
     */
    public final void setInputRange(double min, double max){
        m_controller.setInputRange(min, max);
    }
    
    /**
     * Sets the maximum and minimum values to write.
     *
     * @param min the minimum value to write to the output
     * @param max the maximum value to write to the output
     */
    public final void setOutputRange(double min, double max){
        m_controller.setOutputRange(min, max);
    }
    
    /**
     * Set the absolute error which is considered tolerable for use with
     * OnTarget. The value is in the same range as the PIDInput values.
     * @param t the absolute tolerance
     */
     public final void setAbsoluteTolerance(double t) {
         m_controller.setAbsoluteTolerance(t);
     }

     /**
     * Set the percentage error which is considered tolerable for use with
     * OnTarget. (Value of 15.0 == 15 percent)
     * @param p the percent tolerance
     */
     public final void setPercentTolerance(double p) {
         m_controller.setPercentTolerance(p);
     }

     /**
      * Return true if the error is within the percentage of the total input range,
      * determined by setTolerance. This assumes that the maximum and minimum input
      * were set using setInput.
      * @return true if the error is less than the tolerance
      */
     public final boolean onTarget() {
         return m_controller.onTarget();
     }

     /**
      * Returns the input for the pid loop.
      *
      * <p>It returns the input for the pid loop, so if this Subsystem was based
      * off of a gyro, then it should return the angle of the gyro</p>
      *
      * <p>All subclasses of {@link PIDSubsystem} must override this method.</p>
      *
      * @return the value the pid loop should use as input
      */
     protected abstract double returnPIDInput();

     /**
      * Uses the value that the pid loop calculated.  The calculated value is the "output" parameter.
      * This method is a good time to set motor values, maybe something along the lines of <code>driveline.tankDrive(output, -output)</code>
      *
      * <p>All subclasses of {@link PIDSubsystem} must override this method.</p>
      *
      * @param output the value the pid loop calculated
      */
     protected abstract void usePIDOutput(double output);

     /**
      * Enables the internal {@link PIDController}
      */
     public final void enable() {
         m_controller.enable();
     }

     /**
      * Disables the internal {@link PIDController}
      */
     public final void disable() {
         m_controller.disable();
     }

     public final String getSmartDashboardType() {
         return "PIDSubsystem";
     }
     
     public final void initTable(ITable table) {
         m_controller.initTable(table);
         super.initTable(table);
     }

}
