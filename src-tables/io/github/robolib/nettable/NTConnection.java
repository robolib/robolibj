package io.github.robolib.nettable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import io.github.robolib.nettable.entry.NTEntryType;
import io.github.robolib.nettable.entry.NTEntryTypeManager;
import io.github.robolib.nettable.entry.NTTableEntry;

/**
 * An abstraction for the NetworkTable protocol
 */
public class NTConnection {
	public static final char PROTOCOL_REVISION = 0x0200;
        
	private final Object WRITE_LOCK = new Object();

	private final DataInputStream m_is;
	private final DataOutputStream m_os;
	public final NTSocketStream m_stream;
	private final NTEntryTypeManager m_typeManager;
	private boolean m_isValid;

	
	public NTConnection(NTSocketStream stream, NTEntryTypeManager typeManager){
		m_stream = stream;
		m_typeManager = typeManager;
		m_is = new DataInputStream(new BufferedInputStream(stream.getInputStream()));
		m_os = new DataOutputStream(new BufferedOutputStream(stream.getOutputStream()));
		m_isValid = true;
	}

	public void close() {
		if(m_isValid){
			m_isValid = false;
			m_stream.close();
		}
	}

	private void sendMessageHeader(int messageType) throws IOException{
       synchronized(WRITE_LOCK){
           m_os.writeByte(messageType);
       }
	}
	public void flush() throws IOException{
        synchronized(WRITE_LOCK){
            m_os.flush();
        }
	}



	public void sendKeepAlive() throws IOException {
        synchronized(WRITE_LOCK){
    		sendMessageHeader(NTMessageType.KEEP_ALIVE);
    		flush();
        }
	}

	public void sendClientHello() throws IOException {
        synchronized(WRITE_LOCK){
    		sendMessageHeader(NTMessageType.CLIENT_HELLO);
    		m_os.writeChar(PROTOCOL_REVISION);
    		flush();
        }
	}
	
	public void sendServerHelloComplete() throws IOException {
	    synchronized(WRITE_LOCK){
    		sendMessageHeader(NTMessageType.SERVER_HELLO_COMPLETE);
    		flush();
        }
	}

	public void sendProtocolVersionUnsupported() throws IOException {
        synchronized(WRITE_LOCK){
    		sendMessageHeader(NTMessageType.PROTOCOL_VERSION_UNSUPPORTED);
    		m_os.writeChar(PROTOCOL_REVISION);
    		flush();
        }
	}

	public void sendEntryAssignment(NTTableEntry entry) throws IOException {
        synchronized(WRITE_LOCK){
    		sendMessageHeader(NTMessageType.ENTRY_ASSIGNMENT);
    		m_os.writeUTF(entry.m_name);
    		m_os.writeByte(entry.getType().id);
    		m_os.writeChar(entry.getId());
    		m_os.writeChar(entry.getSequenceNumber());
    		entry.sendValue(m_os);
        }
	}
	
	public void sendEntryUpdate(NTTableEntry entry) throws IOException {
        synchronized(WRITE_LOCK){
    		sendMessageHeader(NTMessageType.FIELD_UPDATE);
    		m_os.writeChar(entry.getId());
    		m_os.writeChar(entry.getSequenceNumber());
    		entry.sendValue(m_os);
        }
	}

	public void read(NTConnectionAdapter adapter) throws IOException {
		try{
			int messageType = m_is.readByte();
			switch(messageType){
			case NTMessageType.KEEP_ALIVE:
				adapter.keepAlive();
				return;
			case NTMessageType.CLIENT_HELLO: 
				char protocolRevision0 = m_is.readChar();
				adapter.clientHello(protocolRevision0);
				return;
			case NTMessageType.SERVER_HELLO_COMPLETE:
				adapter.serverHelloComplete();
				return;
			case NTMessageType.PROTOCOL_VERSION_UNSUPPORTED:
				char protocolRevision1 = m_is.readChar();
				adapter.protocolVersionUnsupported(protocolRevision1);
				return;
			case NTMessageType.ENTRY_ASSIGNMENT:
				String entryName = m_is.readUTF();
                byte typeId = m_is.readByte();
				NTEntryType entryType = m_typeManager.getType(typeId);
				if(entryType == null)
					throw new BadMessageException("Unknown data type: 0x"+Integer.toHexString((int)typeId));
				char entryId1 = m_is.readChar();
				char entrySequenceNumber = m_is.readChar();
				Object value = entryType.readValue(m_is);
				adapter.offerIncomingAssignment(new NTTableEntry(entryId1, entryName, entrySequenceNumber, entryType, value));
				return;
			case NTMessageType.FIELD_UPDATE:
				char entryId2 = m_is.readChar();
				char entrySequenceNumber2 = m_is.readChar();
				NTTableEntry entry = adapter.getEntry(entryId2);
				if(entry == null)
					throw new BadMessageException("Received update for unknown entry id: " + (int)entryId2);
				Object value2 = entry.getType().readValue(m_is);
				
				adapter.offerIncomingUpdate(entry, entrySequenceNumber2, value2);
				return;
			
			default:
				throw new BadMessageException("Unknown Network Table Message Type: " + messageType);
			}
		} catch(IOException e){
			throw e;
		}
	}

}
