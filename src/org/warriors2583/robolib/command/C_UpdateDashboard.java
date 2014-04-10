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
