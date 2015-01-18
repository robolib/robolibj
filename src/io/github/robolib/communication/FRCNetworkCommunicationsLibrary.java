package io.github.robolib.communication;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import io.github.robolib.hal.JNIWrapper;

public class FRCNetworkCommunicationsLibrary extends JNIWrapper {

	public static interface tModuleType {
		public static final int kModuleType_Unknown = 0x00;
		public static final int kModuleType_Analog = 0x01;
		public static final int kModuleType_Digital = 0x02;
		public static final int kModuleType_Solenoid = 0x03;
	};

	public static interface tTargetClass {
		public static final int kTargetClass_Unknown = 0x00;
		public static final int kTargetClass_FRC1 = 0x10;
		public static final int kTargetClass_FRC2 = 0x20;
		public static final int kTargetClass_FRC2_Analog = (int)FRCNetworkCommunicationsLibrary.tTargetClass.kTargetClass_FRC2 | (int)FRCNetworkCommunicationsLibrary.tModuleType.kModuleType_Analog;
		public static final int kTargetClass_FRC2_Digital = (int)FRCNetworkCommunicationsLibrary.tTargetClass.kTargetClass_FRC2 | (int)FRCNetworkCommunicationsLibrary.tModuleType.kModuleType_Digital;
		public static final int kTargetClass_FRC2_Solenoid = (int)FRCNetworkCommunicationsLibrary.tTargetClass.kTargetClass_FRC2 | (int)FRCNetworkCommunicationsLibrary.tModuleType.kModuleType_Solenoid;
		public static final int kTargetClass_FamilyMask = 0xF0;
		public static final int kTargetClass_ModuleMask = 0x0F;
	};

	public static interface tResourceType {
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
	};

	public static interface tInstances {
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
	};
	
	public static final int kFRC_NetworkCommunication_DynamicType_DSEnhancedIO_Input = 17;
	public static final int kFRC_NetworkCommunication_DynamicType_Kinect_Vertices1 = 21;
	public static final int SYS_STATUS_DATA_SIZE = 44;
	public static final int kFRC_NetworkCommunication_DynamicType_Kinect_Custom = 25;
	public static final int kFRC_NetworkCommunication_DynamicType_Kinect_Vertices2 = 23;
	public static final int kFRC_NetworkCommunication_DynamicType_Kinect_Header = 19;
	public static final int kFRC_NetworkCommunication_DynamicType_Kinect_Joystick = 24;
	public static final int IO_CONFIG_DATA_SIZE = 32;
	public static final int kMaxModuleNumber = 2;
	public static final int kFRC_NetworkCommunication_DynamicType_DSEnhancedIO_Output = 18;
	public static final int kFRC_NetworkCommunication_DynamicType_Kinect_Extra2 = 22;
	public static final int kFRC_NetworkCommunication_DynamicType_Kinect_Extra1 = 20;
	public static final int USER_DS_LCD_DATA_SIZE = 128;
	public static final int kUsageReporting_version = 1;
	public static final int USER_STATUS_DATA_SIZE = (984 - 32 - 44);
	/**
	 * Original signature : <code>uint32_t FRC_NetworkCommunication_nAICalibration_getLSBWeight(const uint32_t, const uint32_t, int32_t*)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\AICalibration.h:7</i>
	 * @param aiSystemIndex 
	 * @param channel 
	 * @param status 
	 * @return 
	 */
	public static native int AICalibrationGetLSBWeight(int aiSystemIndex, int channel, Integer status);
	/**
	 * Original signature : <code>int32_t FRC_NetworkCommunication_nAICalibration_getOffset(const uint32_t, const uint32_t, int32_t*)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\AICalibration.h:8</i>
	 * @param aiSystemIndex 
	 * @param channel 
	 * @param status 
	 * @return 
	 */
	public static native int AICalibrationGetOffset(int aiSystemIndex, int channel, Integer status);
	/**
	 * Original signature : <code>tTargetClass getTargetClass()</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\LoadOut.h:25</i>
	 * @return 
	 */
	public static native int getTargetClass();
	/**
	 * Original signature : <code>uint32_t FRC_NetworkCommunication_nLoadOut_getModulePresence(uint32_t, uint8_t)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\LoadOut.h:32</i>
	 * @param moduleType 
	 * @param moduleNumber 
	 * @return 
	 */
	public static native int LoadOutGetModulePresence(int moduleType, byte moduleNumber);
	/**
	 * Original signature : <code>uint32_t FRC_NetworkCommunication_nLoadOut_getTargetClass()</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\LoadOut.h:33</i>
	 * @return 
	 */
	public static native int LoadOutGetTargetClass();
	/**
	 * Report the usage of a resource of interest.
	 *
	 * @param resource one of the values in the tResourceType above (max value 51).
	 * @param instanceNumber an index that identifies the resource instance.
	 * @param context an optional additional context number for some cases (such as module number).  Set to 0 to omit.
	 * @param feature a string to be included describing features in use on a specific resource.  Setting the same resource more than once allows you to change the feature string.<br>
	 * Original signature : <code>uint32_t report(tResourceType, uint8_t, uint8_t, const char*)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\UsageReporting.h:113</i>
	 * @return int
	 */
	public static native int report(int resource, byte instanceNumber, byte context, String feature);
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
	/**
	 * Original signature : <code>void getFPGAHardwareVersion(uint16_t*, uint32_t*)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\FRCComm.h:124</i>
	 * @param fpgaVersion 
	 * @param fpgaRevision 
	 */
	public static native void getFPGAHardwareVersion(ShortBuffer fpgaVersion, IntBuffer fpgaRevision);
	/**
	 * Original signature : <code>int setErrorData(const char*, int, int)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\FRCComm.h:136</i>
	 * @param errors 
	 * @param errorsLength 
	 * @param wait_ms 
	 * @return int
	 */
	public static native int setErrorData(String errors, int errorsLength, int wait_ms);
	/**
	 * Original signature : <code>int overrideIOConfig(const char*, int)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\FRCComm.h:138</i>
	 * @param ioConfig 
	 * @param wait_ms 
	 * @return 
	 */
	public static native int overrideIOConfig(String ioConfig, int wait_ms);
	/**
	 * Original signature : <code>void setNewDataSem(pthread_mutex_t*)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\FRCComm.h:147</i>
	 * @param mutexId 
	 */
	public static native void setNewDataSem(ByteBuffer mutexId);
	/**
	 * Original signature : <code>void signalResyncActionDone()</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\FRCComm.h:150</i>
	 */
	public static native void signalResyncActionDone();
	/**
	 * this uint32_t is really a LVRefNum<br>
	 * Original signature : <code>void setNewDataOccurRef(uint32_t)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\FRCComm.h:154</i>
	 * @param refnum 
	 */
	public static native void setNewDataOccurRef(int refnum);
	/**
	 * Original signature : <code>void setResyncOccurRef(uint32_t)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\FRCComm.h:156</i>
	 * @param refnum 
	 */
	public static native void setResyncOccurRef(int refnum);
	/**
	 * Original signature : <code>void FRC_NetworkCommunication_getVersionString(char*)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\FRCComm.h:159</i>
	 * @param version 
	 */
	public static native void GetVersionString(ByteBuffer version);
	public static native void ObserveUserProgramStarting();
	public static native void ObserveUserProgramDisabled();
	public static native void ObserveUserProgramAutonomous();
	public static native void ObserveUserProgramTeleop();
	public static native void ObserveUserProgramTest();
	public static native void FRCNetworkCommunicationReserve();

	public static native int HALGetRobotStatus();
	public static native int HALGetAllianceStation();

	public static int kMaxJoystickAxes = 12;
	public static int kMaxJoystickPOVs = 12;
	public static native short[] HALGetJoystickAxes(byte joystickNum);
	public static native short[] HALGetJoystickPOVs(byte joystickNum);
	public static native int HALGetJoystickButtons(byte joystickNum, ByteBuffer count);
	public static native int HALSetJoystickOutputs(byte joystickNum, int outputs, short leftRumble, short rightRumble);
	public static native float HALGetMatchTime();
	public static native boolean HALGetSystemActive(IntBuffer status);
	public static native boolean HALGetBrownedOut(IntBuffer status);
	
	public static native int HALSetErrorData(String error);
}
