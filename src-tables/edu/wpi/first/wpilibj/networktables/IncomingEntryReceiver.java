package edu.wpi.first.wpilibj.networktables;



public interface IncomingEntryReceiver {
	IncomingEntryReceiver NULL = new IncomingEntryReceiver() {
		public void offerIncomingUpdate(NTTableEntry entry, char entrySequenceNumber, Object value) {
		}
		public void offerIncomingAssignment(NTTableEntry entry) {
		}
	};
	
	
	public void offerIncomingAssignment(NTTableEntry entry);
	public void offerIncomingUpdate(NTTableEntry entry, char entrySequenceNumber, Object value);
}
