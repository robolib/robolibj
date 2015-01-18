package io.github.robolib.communication;

public class UsageReporting {

	public static void report(int resource, int instanceNumber, int i) {
		report(resource, instanceNumber, i, "");
	}
	
	public static void report(int resource, int instanceNumber) {
		report(resource, instanceNumber, 0, "");
	}

	public static void report(int resource, int instanceNumber, int i,
			String string) {
		FRCNetworkCommunicationsLibrary.UsageReportingReport((byte)resource, (byte) instanceNumber, (byte) i, string);
	}

}
