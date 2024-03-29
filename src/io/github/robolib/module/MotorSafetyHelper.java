/*
 * Copyright (c) 2015-2020 noriah <vix@noriah.dev>.
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

package io.github.robolib.module;

import io.github.robolib.RobotState;
import io.github.robolib.SafetyManager;
import io.github.robolib.util.log.Logger;

// TODO: Auto-generated Javadoc
/**
 * The Class MotorSafetyHelper.
 *
 * @author noriah <vix@noriah.dev>
 */
public final class MotorSafetyHelper {

    /** The m_safety object. */
    private MotorSafety m_safetyObject;

    /** The m_enabled. */
    private boolean m_enabled;

    /** The m_expiration. */
    private double m_expiration;

    /** The m_stop time. */
    private double m_stopTime;

    /**
     * Instantiates a new motor safety helper.
     *
     * @param object the object
     */
    public MotorSafetyHelper(MotorSafety object) {
        m_safetyObject = object;
        m_stopTime = RoboRIO.getFPGATimestamp();
        m_expiration = MotorSafety.SAFETY_TIMEOUT_DEFAULT;
        m_enabled = false;
    }

    /**
     * Feed.
     */
    public void feed() {
        m_stopTime = RoboRIO.getFPGATimestamp() + m_expiration;
    }

    /**
     * Enable safety.
     *
     * @param enabled the enabled
     */
    public void setSafetyEnabled(boolean enabled) {
        m_enabled = enabled;
    }

    /**
     * Checks if is safety enabled.
     *
     * @return true, if is safety enabled
     */
    public boolean isSafetyEnabled() {
        return m_enabled;
    }

    /**
     * Sets the expiration.
     *
     * @param exp the new expiration
     */
    public void setExpiration(double exp) {
        m_expiration = exp;
    }

    /**
     * Gets the expiration.
     *
     * @return the expiration
     */
    public double getExpiration() {
        return m_expiration;
    }

    /**
     * Checks if is alive.
     *
     * @return true, if is alive
     */
    public boolean isAlive() {
        return !m_enabled || m_stopTime > RoboRIO.getFPGATimestamp();
    }

    /**
     * Check.
     */
    public void check() {
        if (!m_enabled || RobotState.isDisabled() || RobotState.isTest())
            return;
        if (m_stopTime < RoboRIO.getFPGATimestamp()) {
            Logger.get(SafetyManager.class)
                    .warn(m_safetyObject.getDescription() + "... Output not updated often enough.");

            m_safetyObject.stopMotor();
        }
    }

}
