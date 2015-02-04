package io.github.robolib.jni;


public class UsageReporting extends JNIWrapper {

    public static final byte ResourceType_Controller = 0;
    public static final byte ResourceType_Module = 1;
    public static final byte ResourceType_Language = 2;
    public static final byte ResourceType_CANPlugin = 3;
    public static final byte ResourceType_Accelerometer = 4;
    public static final byte ResourceType_ADXL345 = 5;
    public static final byte ResourceType_AnalogChannel = 6;
    public static final byte ResourceType_AnalogTrigger = 7;
    public static final byte ResourceType_AnalogTriggerOutput = 8;
    public static final byte ResourceType_CANJaguar = 9;
    public static final byte ResourceType_Compressor = 10;
    public static final byte ResourceType_Counter = 11;
    public static final byte ResourceType_Dashboard = 12;
    public static final byte ResourceType_DigitalInput = 13;
    public static final byte ResourceType_DigitalOutput = 14;
    public static final byte ResourceType_DriverStationCIO = 15;
    public static final byte ResourceType_DriverStationEIO = 16;
    public static final byte ResourceType_DriverStationLCD = 17;
    public static final byte ResourceType_Encoder = 18;
    public static final byte ResourceType_GearTooth = 19;
    public static final byte ResourceType_Gyro = 20;
    public static final byte ResourceType_I2C = 21;
    public static final byte ResourceType_Framework = 22;
    public static final byte ResourceType_Jaguar = 23;
    public static final byte ResourceType_Joystick = 24;
    public static final byte ResourceType_Kinect = 25;
    public static final byte ResourceType_KinectStick = 26;
    public static final byte ResourceType_PIDController = 27;
    public static final byte ResourceType_Preferences = 28;
    public static final byte ResourceType_PWM = 29;
    public static final byte ResourceType_Relay = 30;
    public static final byte ResourceType_RobotDrive = 31;
    public static final byte ResourceType_SerialPort = 32;
    public static final byte ResourceType_Servo = 33;
    public static final byte ResourceType_Solenoid = 34;
    public static final byte ResourceType_SPI = 35;
    public static final byte ResourceType_Task = 36;
    public static final byte ResourceType_Ultrasonic = 37;
    public static final byte ResourceType_Victor = 38;
    public static final byte ResourceType_Button = 39;
    public static final byte ResourceType_Command = 40;
    public static final byte ResourceType_AxisCamera = 41;
    public static final byte ResourceType_PCVideoServer = 42;
    public static final byte ResourceType_SmartDashboard = 43;
    public static final byte ResourceType_Talon = 44;
    public static final byte ResourceType_HiTechnicColorSensor = 45;
    public static final byte ResourceType_HiTechnicAccel = 46;
    public static final byte ResourceType_HiTechnicCompass = 47;
    public static final byte ResourceType_SRF08 = 48;
    public static final byte Language_LabVIEW = 1;
    public static final byte Language_CPlusPlus = 2;
    public static final byte Language_Java = 3;
    public static final byte Language_Python = 4;
    public static final byte CANPlugin_BlackJagBridge = 1;
    public static final byte CANPlugin_2CAN = 2;
    public static final byte Framework_Iterative = 1;
    public static final byte Framework_Simple = 2;
    public static final byte RobotDrive_ArcadeStandard = 1;
    public static final byte RobotDrive_ArcadeButtonSpin = 2;
    public static final byte RobotDrive_ArcadeRatioCurve = 3;
    public static final byte RobotDrive_Tank = 4;
    public static final byte RobotDrive_MecanumPolar = 5;
    public static final byte RobotDrive_MecanumCartesian = 6;
    public static final byte DriverStationCIO_Analog = 1;
    public static final byte DriverStationCIO_DigitalIn = 2;
    public static final byte DriverStationCIO_DigitalOut = 3;
    public static final byte DriverStationEIO_Acceleration = 1;
    public static final byte DriverStationEIO_AnalogIn = 2;
    public static final byte DriverStationEIO_AnalogOut = 3;
    public static final byte DriverStationEIO_Button = 4;
    public static final byte DriverStationEIO_LED = 5;
    public static final byte DriverStationEIO_DigitalIn = 6;
    public static final byte DriverStationEIO_DigitalOut = 7;
    public static final byte DriverStationEIO_FixedDigitalOut = 8;
    public static final byte DriverStationEIO_PWM = 9;
    public static final byte DriverStationEIO_Encoder = 10;
    public static final byte DriverStationEIO_TouchSlider = 11;
    public static final byte ADXL345_SPI = 1;
    public static final byte ADXL345_I2C = 2;
    public static final byte Command_Scheduler = 1;
    public static final byte SmartDashboard_Instance = 1;
    
    
	public static void report(byte resource, int instanceNumber, int i) {
		report(resource, instanceNumber, i, "");
	}
	
	public static void report(byte resource, int instanceNumber) {
		report(resource, instanceNumber, 0, "");
	}

	public static void report(byte resource, int instanceNumber, int i, String string) {
	    
		UsageReportingReport(resource, (byte) instanceNumber, (byte) i, string);
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
