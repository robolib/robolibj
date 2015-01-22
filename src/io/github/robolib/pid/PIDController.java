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

/**
 * 
 * @author noriah Reuland <vix@noriah.dev>
 */
public class PIDController {
	
	public interface Tolerance {
		public boolean onTarget();
	}

    public class NullTolerance implements Tolerance {

        @Override
    public boolean onTarget() {
            throw new RuntimeException("No tolerance value set when using PIDController.onTarget()");
        }
    }

	private static final double kDefaultPeriod = 0.05;
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
	private double m_period = kDefaultPeriod;
	PIDSource m_source;
	PIDOutput m_output;
	java.util.Timer m_controlLoop;
	private boolean m_freed = false;
	private boolean m_usingPercentTolerance;
	
	/*public synchronized void setPercentTolerance(final double percentage) {
        m_tolerance = () -> (Math.abs(getError()) < percentage / 100
                * (m_maxInput - m_minInput));
    }
	
	public synchronized void setAbsoluteTolerance(final double absValue){
		m_tolerance = () -> (Math.abs(getError()) < absValue);
	}*/
	
	
}
