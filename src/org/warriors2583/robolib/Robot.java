package org.warriors2583.robolib;

import com.sun.squawk.*;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.communication.FRCControl;
import edu.wpi.first.wpilibj.communication.UsageReporting;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 * The main Class for the RoboLibJ Library.
 * It extends and IterativeRobot, and takes care of all the methods.
 * It also handles any compressor the robot may have, it handles Loading the
 * robot and sending NetworkTable values about the current state.
 * @author noriah Reuland
 */
public class Robot extends RobotBase {
    
    public static final String NETTABLE_CURRENT_MODE = "mode";
    public static final String NETTABLE_CURRENT_MODE_STRING = "mode-string";
    
    public static final int MODE_ERROR = -1;
    public static final int MODE_NOMODE = 0;
    public static final int MODE_DISABLED = 1;
    public static final int MODE_TEST = 2;
    public static final int MODE_AUTONOMOUS = 3;
    public static final int MODE_TELEOP = 4;
    private static int m_mode;

    private static String m_name;
    private static String m_version;
    private static ITable m_table;
    private static boolean m_debug = true;
    private static boolean m_run = true;
    
    //private static RMap m_map = null;
    private String m_map = null;
    
    private static Compressor m_compressor = null;
    
    public Robot(){
        this("Default", "1.0");
    }

    /**
     * FRCBot Class Method
     * @param name Name of the Robot
     * @param version Version number of the robot
     */
    public Robot(String name, String version){
        m_name = name;
        m_version = version;
        m_mode = 1;
    }

    /**
     * Send a message to the Console, and to the Dashboard.
     * @param msg The message to be sent.
     */
    public static void msg(String msg){
        System.out.println(msg);
    }

    /**
     * Send a message to the Console, and to the Dashboard.
     * @param msg The message to be sent.
     */
    public static void debug(String msg){
        if(m_debug)
            System.out.println(msg);
    }

    /**
     * Enable or Disable Debug Messages
     * @param debug Enable or Disable
     */
    public static void enableDebug(boolean debug){
        m_debug = debug;
    }

    /**
     * Send out an error to the console. Crash the Robot.
     * Lets hope we don't get too many of these
     * @param msg The message to be sent with the RuntimeException
     */
    public static void error(String msg){
        m_run = false;
        throw new RuntimeException(msg);
    }
    
    private int getNewMode(){
        int mode = 0;
        mode += (isDisabled() ? MODE_DISABLED : 0);
        mode += (isTest() ? MODE_TEST : 0);
        mode += (isAutonomous() ? MODE_AUTONOMOUS : 0);
        mode += (isOperatorControl() ? MODE_TELEOP : 0);
        return mode;
    }

    public static int getMode(){
        return m_mode;
    }

    private void initMode(int mode){
        System.gc();
        switch(mode){
            case MODE_DISABLED:
                LiveWindow.setEnabled(false);
                stopCompressor();
                m_table.putNumber(NETTABLE_CURRENT_MODE, MODE_DISABLED);
                m_table.putString(NETTABLE_CURRENT_MODE_STRING, "disabled");
                disabledInit();

            case MODE_TEST:
                LiveWindow.setEnabled(true);
                startCompressor();
                m_table.putNumber(NETTABLE_CURRENT_MODE, MODE_TEST);
                m_table.putString(NETTABLE_CURRENT_MODE_STRING, "test");
                testInit();
            
            case MODE_AUTONOMOUS:
                LiveWindow.setEnabled(false);
                startCompressor();
                m_table.putNumber(NETTABLE_CURRENT_MODE, MODE_AUTONOMOUS);
                m_table.putString(NETTABLE_CURRENT_MODE_STRING, "autonomous");
                autonomousInit();

            case MODE_TELEOP:
                LiveWindow.setEnabled(false);
                startCompressor();
                m_table.putNumber(NETTABLE_CURRENT_MODE, MODE_TELEOP);
                m_table.putString(NETTABLE_CURRENT_MODE_STRING, "teleop");
                teleopInit();

            default:
                LiveWindow.setEnabled(false);
                stopCompressor();
                m_table.putNumber(NETTABLE_CURRENT_MODE, MODE_ERROR);
                m_table.putString(NETTABLE_CURRENT_MODE_STRING, "ERROR");
                error("Unknown Mode in FRCBot.initMode()");
        }
    }
    
    private void runMode(int mode){
        if(mode != m_mode){
            initMode(mode);
            m_mode = mode;
        }
        if(m_ds.isNewControlData()){
            switch(mode){

                case MODE_DISABLED:
                    FRCControl.observeUserProgramDisabled();
                    disabledPeriodic();

                case MODE_TEST:
                    FRCControl.observeUserProgramTest();
                    testPeriodic();

                case MODE_AUTONOMOUS:
                    FRCControl.observeUserProgramAutonomous();
                    autonomousPeriodic();

                case MODE_TELEOP:
                    FRCControl.observeUserProgramTeleop();
                    teleopPeriodic();

                default:
                    error("Shit");

            Scheduler.getInstance().run();
            }
        }
    }
    
    @Override
    public void startCompetition() {
        debug("FRCBot Library Version 3.11");
        debug("Starting " + m_name);
        UsageReporting.report(UsageReporting.kResourceType_Framework, UsageReporting.kFramework_Iterative);
        //if(m_map == null){
        //    error("you must call FRCBot.setRMap() in the Class Constructor");
        //}
        debug("Initializing Robot Network Table");
        m_table = NetworkTable.getTable("Robot");
        
        //Run User initialization
        robotInit();        
        LiveWindow.setEnabled(false);

        msg(m_name + ", Verion " + m_version + " Initiated");
        while(m_run){
            runMode(getNewMode());

            m_ds.waitForData();
        }

        
        
    }
    
    /**
     * Add a Compressor to the System
     * @param compressor_switch
     * @param compressor_relay 
     */
    protected static void compressor(int compressor_switch, int compressor_relay){
        debug("Adding Compressor on Relay Port " + compressor_relay + " DIO Port " + compressor_switch);
        m_compressor = new Compressor(compressor_switch, compressor_relay);
    }

    private static void startCompressor(){
        if(m_compressor != null){
            m_compressor.start();
        }
    }
    
    private static void stopCompressor(){
        if(m_compressor != null){
            m_compressor.stop();
        }
    }
    
    public static ITable getRobotTable(){
        return m_table;
    }

    /**
     * Enable or Disable the Watchdog 
     * @param enabled
     */
    protected static void enableWatchdog(boolean enabled){
        if(enabled){
            debug("Enabling Watchdog");
            Watchdog.getInstance().setEnabled(true);
        }else{
            debug("Disabling Watchdog");
            Watchdog.getInstance().setEnabled(false);
        }
    }
    
    public void robotInit(){
        debug("Default FRCBot.robotInit() method... Overload me!");
    }

    public void disabledInit(){
        debug("Default FRCBot.disabledInit() method... Overload me!");
    }

    public void testInit(){
        debug("Default FRCBot.testInit() method... Overload me!");   
    }
    
    public void autonomousInit(){
        debug("Default FRCBot.autonomousInit() method... Overload me!");
    }

    public void teleopInit(){
        debug("Default FRCBot.teleopInit() method... Overload me!");
    }

    public void disabledPeriodic(){
        debug("Default FRCBot.disabledPeriodic() method... Overload me!");
        Timer.delay(0.001);
    }

    public void testPeriodic(){
        debug("Default FRCBot.testPeriodic() method... Overload me!");
        Timer.delay(0.001);
    }

    public void autonomousPeriodic(){
        debug("Default FRCBot.autonomousPeriodic() method... Overload me!");
        Timer.delay(0.001);
    }

    public void teleopPeriodic(){
        debug("Default FRCBot.teleopPeriodic() method... Overload me!");
        Timer.delay(0.001);
    }


}