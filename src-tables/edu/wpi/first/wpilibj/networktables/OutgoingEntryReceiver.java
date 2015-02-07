package edu.wpi.first.wpilibj.networktables;



public interface OutgoingEntryReceiver {
	OutgoingEntryReceiver NULL = new OutgoingEntryReceiver() {
		public void offerOutgoingUpdate(NTTableEntry entry) {
		}
		public void offerOutgoingAssignment(NTTableEntry entry) {
		}
	};
	
	public void offerOutgoingAssignment(NTTableEntry entry);
	public void offerOutgoingUpdate(NTTableEntry entry);
}
