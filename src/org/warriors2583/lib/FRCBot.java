package org.warriors2583.lib;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

/**
 *
 * @author Austin Reuland
 */
public class FRCBot extends IterativeRobot {
    
    public static final String NETTABLE_CURRENT_MODE = "mode";
    public static final String NETTABLE_CURRENT_MODE_STRING = "mode-string";
    
    private static String m_name;
    private static String m_version;
    private static ITable m_table;
    //private static RMap m_map = null;
    private String m_map = null;
    
    private static Compressor m_compressor = null;
    
    public FRCBot(){
        this("Default", "1.0");
    }

    /**
     * FRCBot Class Method
     * @param name Name of the Robot
     * @param version Version number of the robot
     */
    public FRCBot(String name, String version){
        super();
        m_name = name;
        m_version = version;
    }

    /**
     * Send a message to the Console, and to the Dashboard.
     * @param msg The message to be sent.
     */
    public static void debug(String msg){
        System.out.println(msg);
    }

    /**
     * Send out an error to the console. Crash the Robot
     * Lets hope we dont get too many of these
     * @param msg The message to be sent with the RuntimeException
     */
    public static void error(String msg){
        throw new RuntimeException(msg);
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

    private static void initTable(){
        debug("Initializing Robot Network Table");
        m_table = NetworkTable.getTable("Robot");
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
    
    public void init(){
        debug("Default FRCBot.init() method... Overload me!");
    }
    public void initAutonomous(){
        debug("Default FRCBot.initAutonomous() method... Overload me!");
    }
    public void autonomous(){
        debug("Default FRCBot.autonomous() method... Overload me!");
    }
    public void initTeleop(){
        debug("Default FRCBot.initTeleop() method... Overload me!");
    }
    public void teleop(){
        debug("Default FRCBot.teleop() method... Overload me!");
    }
    public void initDisabled(){
        debug("Default FRCBot.initDisabled() method... Overload me!");
    }
    public void disabled(){
        debug("Default FRCBot.disabled() method... Overload me!");
    }
    public void initTest(){
        debug("Default FRCBot.initTest() method... Overload me!");
    }
    public void test(){
        debug("Default FRCBot.test() method... Overload me!");
    }
    
    @Override
    public void robotInit(){
        debug("FRCBot Library Version 3.11");
        debug("Starting " + m_name);
        //if(m_map == null){
        //    error("you must call FRCBot.setRMap() in the Class Constructor");
        //}
        initTable();
        init();
        debug(m_name + ", Verion " + m_version + " Initiated");
        debug("Robot Initiated");
    }

    @Override
    public void autonomousInit(){
        System.gc();
        startCompressor();
        m_table.putNumber(NETTABLE_CURRENT_MODE, 2.0);
        m_table.putString(NETTABLE_CURRENT_MODE_STRING, "autonomous");
        autonomousInit();
    }

    @Override
    public void autonomousPeriodic(){
        Scheduler.getInstance().run();
        autonomous();
    }

    @Override
    public void disabledInit(){
        System.gc();
        stopCompressor();
        m_table.putNumber(NETTABLE_CURRENT_MODE, 1.0);
        m_table.putString(NETTABLE_CURRENT_MODE_STRING, "disabled");
        disabledInit();
    }

    @Override
    public void disabledPeriodic(){
        Scheduler.getInstance().run();
        disabled();
    }

    @Override
    public void teleopInit(){
        System.gc();
        startCompressor();
        m_table.putNumber(NETTABLE_CURRENT_MODE, 3.0);
        m_table.putString(NETTABLE_CURRENT_MODE_STRING, "teleop");
        teleopInit();
    }

    @Override
    public void teleopPeriodic(){
        Scheduler.getInstance().run();
        teleop();
    }

    @Override
    public void testInit(){
        System.gc();
        startCompressor();
        m_table.putNumber(NETTABLE_CURRENT_MODE, 4.0);
        m_table.putString(NETTABLE_CURRENT_MODE_STRING, "test");
        testInit();
    }

    @Override
    public void testPeriodic(){
        LiveWindow.run();
        Scheduler.getInstance().run();
        test();
    }

}