package org.warriors2583.lib;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * Delays the a Command Group
 * @author noriah Reuland
 */
public class C_Delay extends Command {

    private final double m_sec;
    private final Timer m_timer;
    
    public C_Delay(double sec) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super("C_Delay");
        this.m_sec = sec;
        m_timer = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return m_timer.get() >= m_sec;
    }

    // Called once after isFinished returns true
    protected void end() {
        m_timer.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        end();
    }
}
