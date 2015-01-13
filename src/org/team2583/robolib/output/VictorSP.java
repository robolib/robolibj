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

package org.team2583.robolib.output;

import org.team2583.robolib.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import org.team2583.robolib.communication.UsageReporting;
import org.team2583.robolib.util.PDP.PowerChannel;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * VEX Robotics Victor SP Speed Controller
 * 
 * @author Austin Reuland <amreuland@gmail.com>
 *
 */
public class VictorSP extends PWMController {
    
    /**
     * Instantiates a new VictorSP motor controller.
     *
     * @param channel the pwm channel this controller operates on
     */
    public VictorSP(PWMChannel channel) {
        this(channel, "PWM VictorSP Ch" + channel.ordinal(), null);
    }
    
    /**
     * Instantiates a new VictorSP motor controller.
     * Giving a description helps with debugging. It will be used in log outputs.
     *
     * @param channel the pwm channel this controller operates on
     * @param desc the description of this motor controller
     */
    public VictorSP(PWMChannel channel, String desc){
        this(channel, desc, null);
    }
    
    /**
     * Instantiates a new VictorSP motor controller.
     * Giving a description helps with debugging. It will be used in log outputs.
     * Giving a power channel will help with power logging. It will be shown as this
     * controllers power channel on power log outputs.
     *
     * @param channel the pwm channel this controller operates on
     * @param desc the description of this motor controller
     * @param pwChannel The PDP(Power Distribution Panel) Channel
     */
    public VictorSP(PWMChannel channel, String desc, PowerChannel pwChannel){
        super(channel, desc, pwChannel);
        setBounds(2.004, 1.52, 1.50, 1.48, 0.997);
        LiveWindow.addActuator("VictorSP", channel.ordinal(), this);
        UsageReporting.report(tResourceType.kResourceType_Talon, channel.ordinal());
    }

}
