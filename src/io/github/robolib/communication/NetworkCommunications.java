package io.github.robolib.communication;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import io.github.robolib.hal.JNIWrapper;

public class NetworkCommunications extends JNIWrapper {

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
		public static final int kTargetClass_FRC2_Analog = (int)NetworkCommunications.tTargetClass.kTargetClass_FRC2 | (int)NetworkCommunications.tModuleType.kModuleType_Analog;
		public static final int kTargetClass_FRC2_Digital = (int)NetworkCommunications.tTargetClass.kTargetClass_FRC2 | (int)NetworkCommunications.tModuleType.kModuleType_Digital;
		public static final int kTargetClass_FRC2_Solenoid = (int)NetworkCommunications.tTargetClass.kTargetClass_FRC2 | (int)NetworkCommunications.tModuleType.kModuleType_Solenoid;
		public static final int kTargetClass_FamilyMask = 0xF0;
		public static final int kTargetClass_ModuleMask = 0x0F;
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
	 * @param resource one of the values in the UsageReporting above (max value 51).
	 * @param instanceNumber an index that identifies the resource instance.
	 * @param context an optional additional context number for some cases (such as module number).  Set to 0 to omit.
	 * @param feature a string to be included describing features in use on a specific resource.  Setting the same resource more than once allows you to change the feature string.<br>
	 * Original signature : <code>uint32_t report(UsageReporting, uint8_t, uint8_t, const char*)</code><br>
	 * <i>native declaration : src\main\include\NetworkCommunication\UsageReporting.h:113</i>
	 * @return int
	 */
	public static native int report(int resource, byte instanceNumber, byte context, String feature);
	
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
	public static native void NetworkCommunicationReserve();

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
