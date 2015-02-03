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

package io.github.robolib.pid;

import java.util.Timer;
import java.util.TimerTask;

import io.github.robolib.communication.UsageReporting;
import io.github.robolib.util.livewindow.LiveWindowSendable;

import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PIDController implements LiveWindowSendable {
	
    /**
     * Interface for representing the error and tolerance
     * of the PID
     *
     * @author noriah Reuland <vix@noriah.dev>
     */
	public interface Tolerance {
		public boolean onTarget();
	}
	
	/**
	 * The task each PIDController runs
	 *
	 * @author noriah Reuland <vix@noriah.dev>
	 */
	private class PIDTask extends TimerTask {
	    private PIDController m_controller;
	    public PIDTask(PIDController controller){
	        m_controller = controller;
	        
	    }
	    @Override
        public void run(){
	        m_controller.calculate();
	    }
	}

	public static final double kDefaultPeriod = 0.05;
	private static int m_instances = 0;
	
	private double m_P;
	private double m_I;
	private double m_D;
	private double m_F;
	private double m_maxOutput = 1.0;
	private double m_minOutput = -1.0;
	private double m_maxInput = 0;
	private double m_minInput = 0;
	private boolean m_continuous = false;
	private boolean m_enabled = false;
	private double m_prevError = 0.0;
	private double m_totalError = 0.0;
	private Tolerance m_tolerance;
	private double m_setpoint = 0.0;
	private double m_error = 0.0;
	private double m_result= 0.0;
//	private double m_period = kDefaultPeriod;
	PIDSource m_source;
	PIDSink m_output;
	Timer m_controlLoop;
//	private boolean m_freed = false;
//	private boolean m_usingPercentTolerance;
	
	public PIDController(double P, double I, double D, double F,
	        PIDSource source, PIDSink output, double period){
	    m_controlLoop = new java.util.Timer();
	    
	    m_P = P;
	    m_I = I;
	    m_D = D;
	    m_F = F;
	    
	    m_source = source;
	    m_output = output;
//	    m_period = period;
	    
	    m_controlLoop.schedule(new PIDTask(this), 0L, (long) (period * 1000));
	    
	    UsageReporting.report(UsageReporting.ResourceType_PIDController, ++m_instances);
	    m_tolerance = () -> {
	        throw new RuntimeException("No tolerance value set when using PIDController.onTarget()");
	    };
	}
	
	public void free(){
	    m_controlLoop.cancel();
	    synchronized (this){
//	        m_freed = true;
	        m_output = null;
	        m_source = null;
	        m_controlLoop = null;
        }
	    if(m_source != null)
	        m_table.removeTableListener(m_listener);
	}
	
	/**
     * Read the input, calculate the output accordingly, and write to the output.
     * This should only be called by the PIDTask
     * and is created during initialization.
     */
	private void calculate(){
	    boolean enabled;
        PIDSource pidInput;

        synchronized (this) {
            if (m_source == null) {
                return;
            }
            if (m_output == null) {
                return;
            }
            enabled = m_enabled; // take snapshot of these values...
            pidInput = m_source;
        }

        if (enabled) {
          double input;
            double result;
            PIDSink pidOutput = null;
            synchronized (this){
              input = pidInput.pidGet();
            }
            synchronized (this) {
                m_error = m_setpoint - input;
                if (m_continuous) {
                    if (Math.abs(m_error)
                            > (m_maxInput - m_minInput) / 2) {
                        if (m_error > 0) {
                            m_error = m_error - m_maxInput + m_minInput;
                        } else {
                            m_error = m_error
                                      + m_maxInput - m_minInput;
                        }
                    }
                }

                if (m_I != 0) {
                    double potentialIGain = (m_totalError + m_error) * m_I;
                    if (potentialIGain < m_maxOutput) {
                        if (potentialIGain > m_minOutput) {
                            m_totalError += m_error;
                        } else {
                            m_totalError = m_minOutput / m_I;
                        }
                    } else {
                        m_totalError = m_maxOutput / m_I;
                    }
                }

                m_result = m_P * m_error + m_I * m_totalError + m_D * (m_error - m_prevError) + m_setpoint * m_F;
                m_prevError = m_error;

                if (m_result > m_maxOutput) {
                    m_result = m_maxOutput;
                } else if (m_result < m_minOutput) {
                    m_result = m_minOutput;
                }
                pidOutput = m_output;
                result = m_result;
            }

            pidOutput.pidWrite(result);
        }
	}
	
	/**
     * Set the PID Controller gain parameters.
     * Set the proportional, integral, and differential coefficients.
     * @param p Proportional coefficient
     * @param i Integral coefficient
     * @param d Differential coefficient
     */
	public synchronized void setPID(double p, double i, double d){
	    m_P = p;
	    m_I = i;
	    m_D = d;
	    
	    if(m_table != null){
	        m_table.putNumber("P", p);
	        m_table.putNumber("I", i);
	        m_table.putNumber("D", d);
	    }
	}
	
	/**
	    * Set the PID Controller gain parameters.
	    * Set the proportional, integral, and differential coefficients.
	    * @param p Proportional coefficient
	    * @param i Integral coefficient
	    * @param d Differential coefficient
	    * @param f Feed forward coefficient
	    */
	public synchronized void setPID(double p, double i, double d, double f){
        m_P = p;
        m_I = i;
        m_D = d;
        m_F = f;
        
        if(m_table != null){
            m_table.putNumber("P", p);
            m_table.putNumber("I", i);
            m_table.putNumber("D", d);
            m_table.putNumber("F", f);
        }
    }
	
	/**
     * Get the Proportional coefficient
     * @return proportional coefficient
     */
	public synchronized double getP(){ return m_P; }
	
	/**
     * Get the Integral coefficient
     * @return integral coefficient
     */
	public synchronized double getI(){ return m_I; }
	
	/**
     * Get the Differential coefficient
     * @return differential coefficient
     */
	public synchronized double getD(){ return m_D; }
	
	/**
     * Get the Feed forward coefficient
     * @return feed forward coefficient
     */
	public synchronized double getF(){ return m_F; }
	
	/**
     * Return the current PID result
     * This is always centered on zero and constrained the the max and min outs
     * @return the latest calculated output
     */
	public synchronized double get(){ return m_result; }

	/**
     *  Set the PID controller to consider the input to be continuous,
     *  Rather then using the max and min in as constraints, it considers them to
     *  be the same point and automatically calculates the shortest route to
     *  the setpoint.
     * @param continuous Set to true turns on continuous, false turns off continuous
     */
	public synchronized void setContinuous(boolean continuous){
	    m_continuous = continuous;
	}
	
	/**
     *  Set the PID controller to consider the input to be continuous,
     *  Rather then using the max and min in as constraints, it considers them to
     *  be the same point and automatically calculates the shortest route to
     *  the setpoint.
     */
	public synchronized void setContinuous(){ setContinuous(true); }
	
	/**
     * Sets the maximum and minimum values expected from the input and setpoint.
     *
     * @param min the minimum value expected from the input
     * @param max the maximum value expected from the input
     */
	public synchronized void setInputRange(double min, double max){
	    if(min > max) throw new IllegalArgumentException("Min cannot be larger than Max");
	    m_minInput = min;
	    m_maxInput = max;
	    setSetpoint(m_setpoint);
	}
	
	/**
     * Sets the minimum and maximum values to write.
     *
     * @param min the minimum percentage to write to the output
     * @param max the maximum percentage to write to the output
     */
	public synchronized void setOutputRange(double min, double max){
	    if(min > max) throw new IllegalArgumentException("Min cannot be larger than Max");
	    m_minOutput = min;
	    m_maxOutput = max;
	}
	
	/**
     * Set the setpoint for the PIDController
     * @param point the desired setpoint
     */
	public synchronized void setSetpoint(double point){
	    if (m_maxInput > m_minInput){
            if(point > m_maxInput)
                m_setpoint = m_maxInput;
            else if (point < m_minInput)
                m_setpoint = m_minInput;
            else
                m_setpoint = point;
            
        }else{
            m_setpoint = point;
        }

        if (m_table != null)
            m_table.putNumber("setpoint", m_setpoint);
	}
	
	/**
     * Returns the current setpoint of the PIDController
     * @return the current setpoint
     */
	public synchronized double getSetpoint(){ return m_setpoint; }
	
	/**
     * Returns the current difference of the input from the setpoint
     * @return the current error
     */
	public synchronized double getError(){
	    return getSetpoint() - m_source.pidGet();
	}
	
	/**
	 * Set the PID tolerance using a Tolerance object.
     * Tolerance can be specified as a percentage of the range or as an absolute
     * value. The Tolerance object encapsulates those options in an object. Use it by
     * creating the type of tolerance that you want to use: setTolerance(new PIDController.AbsoluteTolerance(0.1))
     * @param tolerance a tolerance object of the right type, e.g. PercentTolerance or AbsoluteTolerance
     */
	public synchronized void setTolerance(Tolerance tolerance){
	    m_tolerance = tolerance;
	}
	
	/**
     * Set the percentage error which is considered tolerable for use with
     * OnTarget. (Input of 15.0 = 15 percent)
     * @param percentage percent error which is tolerable
     */
	public synchronized void setPercentTolerance(final double percentage) {
        m_tolerance = () -> (Math.abs(getError()) < percentage / 100
                * (m_maxInput - m_minInput));
    }
	
	/**
     * Set the absolute error which is considered tolerable for use with
     * OnTarget.
     * @param absValue absolute error which is tolerable in the units of the input object
     */
	public synchronized void setAbsoluteTolerance(final double absValue){
		m_tolerance = () -> (Math.abs(getError()) < absValue);
	}

    /**
     * Return true if the error is within the percentage of the total input range,
     * determined by setTolerance. This assumes that the maximum and minimum input
     * were set using setInput.
     * @return true if the error is less than the tolerance
     */
    public synchronized boolean onTarget(){ return m_tolerance.onTarget(); }
	
    /**
     * Begin running the PIDController
     */
    public synchronized void enable(){
        m_enabled = true;
        if(m_table != null)
            m_table.putBoolean("enabled", true);
    }
    
    /**
     * Stop running the PIDController, this sets the output to zero before stopping.
     */
    public synchronized void disable() {
        m_output.pidWrite(0);
        m_enabled = false;

        if (m_table != null) {
            m_table.putBoolean("enabled", false);
        }
    }
    
    /**
     * Return true if PIDController is enabled.
     */
    public synchronized boolean isEnabled(){ return m_enabled; }
    
    /**
     * Reset the previous error,, the integral term, and disable the controller.
     */
    public synchronized void reset(){
        disable();
        m_prevError = 0;
        m_totalError = 0;
        m_result = 0;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getSmartDashboardType() {
        return "PIDController";
    }
    
	private ITable m_table;
	private final ITableListener m_listener = (ITable table, String key,
	        Object value, boolean isNew) -> {
        if (key.equals("P") || key.equals("I") || key.equals("D") || key.equals("F")) {
            if (getP() != table.getNumber("p", 0.0) || getI() != table.getNumber("I", 0.0) || getD() != table.getNumber("d", 0.0) || getF() != table.getNumber("F", 0.0))
                setPID(table.getNumber("P", 0.0), table.getNumber("I", 0.0), table.getNumber("D", 0.0), table.getNumber("F", 0.0));
        } else if (key.equals("setpoint")) {
            if (getSetpoint() != ((Double) value).doubleValue())
                setSetpoint(((Double) value).doubleValue());
        } else if (key.equals("enabled")) {
            if (isEnabled() != ((Boolean) value).booleanValue()) {
                if (((Boolean) value).booleanValue()) {
                enable();
                } else {
                    disable();
                }
            }
        }
    };
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initTable(ITable table) {
        if(m_table!=null)
            m_table.removeTableListener(m_listener);
        m_table = table;
        if(m_table!=null) {
            table.putNumber("p", getP());
            table.putNumber("i", getI());
            table.putNumber("d", getD());
            table.putNumber("f", getF());
            table.putNumber("setpoint", getSetpoint());
            table.putBoolean("enabled", isEnabled());
            table.addTableListener(m_listener, false);
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ITable getTable(){
        return m_table;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void updateTable(){}

    /**
     * {@inheritDoc}
     */
    @Override
    public void startLiveWindowMode() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public void stopLiveWindowMode() {}
}
