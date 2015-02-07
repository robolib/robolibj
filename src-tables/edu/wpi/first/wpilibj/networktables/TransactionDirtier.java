package edu.wpi.first.wpilibj.networktables;


/**
 * A transaction receiver that marks all Table entries as dirty in the entry store. Entries will not be passed to the continuing receiver if they are already dirty
 * 
 * @author Mitchell
 *
 */
public class TransactionDirtier implements OutgoingEntryReceiver {
	private final OutgoingEntryReceiver m_continuingReceiver;
	
	public TransactionDirtier(final OutgoingEntryReceiver continuingReceiver) {
		this.m_continuingReceiver = continuingReceiver;
	}



	public void offerOutgoingAssignment(NTTableEntry entry) {
		if(entry.isDirty())
			return;
		entry.makeDirty();
		m_continuingReceiver.offerOutgoingAssignment(entry);
	}




	public void offerOutgoingUpdate(NTTableEntry entry) {
		if(entry.isDirty())
			return;
		entry.makeDirty();
		m_continuingReceiver.offerOutgoingUpdate(entry);
	}

}
