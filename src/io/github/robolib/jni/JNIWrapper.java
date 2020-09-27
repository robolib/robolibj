package io.github.robolib.jni;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

//
// base class for all JNI wrappers
//
public class JNIWrapper {
	static boolean libraryLoaded = false;
	static File jniLibrary = null;
	static {
		try {
			if (!libraryLoaded) {
				// create temporary file
				jniLibrary = File.createTempFile("librobolibJNI", ".so");
				// flag for delete on exit
				jniLibrary.deleteOnExit();

				byte[] buffer = new byte[1024];

				int readBytes;

				InputStream is = JNIWrapper.class.getResourceAsStream("/linux-arm/librobolibJNI.so");

				OutputStream os = new FileOutputStream(jniLibrary);

				try {
					while ((readBytes = is.read(buffer)) != -1) {
						os.write(buffer, 0, readBytes);
					}

				} finally {
					os.close();
					is.close();
				}

				libraryLoaded = true;
			}

			System.load(jniLibrary.getAbsolutePath());
		} catch (Exception ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public static native ByteBuffer getPortWithModule(byte module, byte pin);

	public static native ByteBuffer getPort(byte pin);
}
