package io.github.robolib.communication;

import io.github.robolib.hal.JNIWrapper;

public class UsageReporting extends JNIWrapper {

    public static final int kResourceType_Controller = 0;
    public static final int kResourceType_Module = 1;
    public static final int kResourceType_Language = 2;
    public static final int kResourceType_CANPlugin = 3;
    public static final int kResourceType_Accelerometer = 4;
    public static final int kResourceType_ADXL345 = 5;
    public static final int kResourceType_AnalogChannel = 6;
    public static final int kResourceType_AnalogTrigger = 7;
    public static final int kResourceType_AnalogTriggerOutput = 8;
    public static final int kResourceType_CANJaguar = 9;
    public static final int kResourceType_Compressor = 10;
    public static final int kResourceType_Counter = 11;
    public static final int kResourceType_Dashboard = 12;
    public static final int kResourceType_DigitalInput = 13;
    public static final int kResourceType_DigitalOutput = 14;
    public static final int kResourceType_DriverStationCIO = 15;
    public static final int kResourceType_DriverStationEIO = 16;
    public static final int kResourceType_DriverStationLCD = 17;
    public static final int kResourceType_Encoder = 18;
    public static final int kResourceType_GearTooth = 19;
    public static final int kResourceType_Gyro = 20;
    public static final int kResourceType_I2C = 21;
    public static final int kResourceType_Framework = 22;
    public static final int kResourceType_Jaguar = 23;
    public static final int kResourceType_Joystick = 24;
    public static final int kResourceType_Kinect = 25;
    public static final int kResourceType_KinectStick = 26;
    public static final int kResourceType_PIDController = 27;
    public static final int kResourceType_Preferences = 28;
    public static final int kResourceType_PWM = 29;
    public static final int kResourceType_Relay = 30;
    public static final int kResourceType_RobotDrive = 31;
    public static final int kResourceType_SerialPort = 32;
    public static final int kResourceType_Servo = 33;
    public static final int kResourceType_Solenoid = 34;
    public static final int kResourceType_SPI = 35;
    public static final int kResourceType_Task = 36;
    public static final int kResourceType_Ultrasonic = 37;
    public static final int kResourceType_Victor = 38;
    public static final int kResourceType_Button = 39;
    public static final int kResourceType_Command = 40;
    public static final int kResourceType_AxisCamera = 41;
    public static final int kResourceType_PCVideoServer = 42;
    public static final int kResourceType_SmartDashboard = 43;
    public static final int kResourceType_Talon = 44;
    public static final int kResourceType_HiTechnicColorSensor = 45;
    public static final int kResourceType_HiTechnicAccel = 46;
    public static final int kResourceType_HiTechnicCompass = 47;
    public static final int kResourceType_SRF08 = 48;
    
    public static final int kLanguage_LabVIEW = 1;
    public static final int kLanguage_CPlusPlus = 2;
    public static final int kLanguage_Java = 3;
    public static final int kLanguage_Python = 4;
    public static final int kCANPlugin_BlackJagBridge = 1;
    public static final int kCANPlugin_2CAN = 2;
    public static final int kFramework_Iterative = 1;
    public static final int kFramework_Simple = 2;
    public static final int kRobotDrive_ArcadeStandard = 1;
    public static final int kRobotDrive_ArcadeButtonSpin = 2;
    public static final int kRobotDrive_ArcadeRatioCurve = 3;
    public static final int kRobotDrive_Tank = 4;
    public static final int kRobotDrive_MecanumPolar = 5;
    public static final int kRobotDrive_MecanumCartesian = 6;
    public static final int kDriverStationCIO_Analog = 1;
    public static final int kDriverStationCIO_DigitalIn = 2;
    public static final int kDriverStationCIO_DigitalOut = 3;
    public static final int kDriverStationEIO_Acceleration = 1;
    public static final int kDriverStationEIO_AnalogIn = 2;
    public static final int kDriverStationEIO_AnalogOut = 3;
    public static final int kDriverStationEIO_Button = 4;
    public static final int kDriverStationEIO_LED = 5;
    public static final int kDriverStationEIO_DigitalIn = 6;
    public static final int kDriverStationEIO_DigitalOut = 7;
    public static final int kDriverStationEIO_FixedDigitalOut = 8;
    public static final int kDriverStationEIO_PWM = 9;
    public static final int kDriverStationEIO_Encoder = 10;
    public static final int kDriverStationEIO_TouchSlider = 11;
    public static final int kADXL345_SPI = 1;
    public static final int kADXL345_I2C = 2;
    public static final int kCommand_Scheduler = 1;
    public static final int kSmartDashboard_Instance = 1;
    
    
	public static void report(int resource, int instanceNumber, int i) {
		report(resource, instanceNumber, i, "");
	}
	
	public static void report(int resource, int instanceNumber) {
		report(resource, instanceNumber, 0, "");
	}

	public static void report(int resource, int instanceNumber, int i, String string) {
	    
		UsageReportingReport((byte)resource, (byte) instanceNumber, (byte) i, string);
	}
	
	/**
     * Original signature : <code>uint32_t FRC_NetworkCommunication_nUsageReporting_report(uint8_t, uint8_t, uint8_t, const char*)</code><br>
     * <i>native declaration : src\main\include\NetworkCommunication\UsageReporting.h:120</i>
     * @param resource 
     * @param instanceNumber 
     * @param context 
     * @param feature 
     * @return int
     */
    public static native int UsageReportingReport(byte resource, byte instanceNumber, byte context, String feature);

}
