/*
 * Copyright (c) 2014 Westwood Robotics code.westwoodrobotics@gmail.com.
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

package org.warriors2583.robolib.command;

import edu.wpi.first.wpilibj.command.Command;
import org.warriors2583.robolib.SS_Dashboard;

/**
 * Updates the Dashboard
 * @author Austin Reuland
 */
public class C_UpdateDashboard extends Command {

    public C_UpdateDashboard() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super("C_UpdateDashboard");
        requires(SS_Dashboard.getInstance());
        setRunWhenDisabled(true);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        SS_Dashboard.update();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
