package io.github.robolib.jni;

public class AccelerometerJNI extends JNIWrapper {
	public static native void setAccelerometerActive(boolean active);

	public static native void setAccelerometerRange(int range);

	public static native double getAccelerometerX();

	public static native double getAccelerometerY();

	public static native double getAccelerometerZ();
}
