package edu.wpi.first.wpilibj.networktables.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import edu.wpi.first.wpilibj.networktables.NTTableEntry;
import edu.wpi.first.wpilibj.networktables.NetworkTableMessageType;
import edu.wpi.first.wpilibj.networktables.server.ConnectionAdapter;
import edu.wpi.first.wpilibj.networktables.stream.SimpleIOStream;
import edu.wpi.first.wpilibj.networktables.type.NetworkTableEntryType;
import edu.wpi.first.wpilibj.networktables.type.NetworkTableEntryTypeManager;


/**
 * An abstraction for the NetworkTable protocol
 * 
 * @author mwills
 *
 */
public class NetworkTableConnection {
	public static final char PROTOCOL_REVISION = 0x0200;
        
	private final Object WRITE_LOCK = new Object();

	private final DataInputStream m_is;
	private final DataOutputStream m_os;
	/**
	 * the raw stream that is used in this connection
	 */
	public final SimpleIOStream m_stream;
	private final NetworkTableEntryTypeManager m_typeManager;
	private boolean isValid;

	
	public NetworkTableConnection(SimpleIOStream stream, NetworkTableEntryTypeManager typeManager){
		m_stream = stream;
		m_typeManager = typeManager;
		m_is = new DataInputStream(new BufferedInputStream(stream.getInputStream()));
		m_os = new DataOutputStream(new BufferedOutputStream(stream.getOutputStream()));
		isValid = true;
	}

	public void close() {
		if(isValid){
			isValid = false;
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
		sendMessageHeader(NetworkTableMessageType.KEEP_ALIVE);
		flush();
            }
	}

	public void sendClientHello() throws IOException {
            synchronized(WRITE_LOCK){
		sendMessageHeader(NetworkTableMessageType.CLIENT_HELLO);
		m_os.writeChar(PROTOCOL_REVISION);
		flush();
            }
	}
	
	public void sendServerHelloComplete() throws IOException {
            synchronized(WRITE_LOCK){
		sendMessageHeader(NetworkTableMessageType.SERVER_HELLO_COMPLETE);
		flush();
            }
	}

	public void sendProtocolVersionUnsupported() throws IOException {
            synchronized(WRITE_LOCK){
		sendMessageHeader(NetworkTableMessageType.PROTOCOL_VERSION_UNSUPPORTED);
		m_os.writeChar(PROTOCOL_REVISION);
		flush();
            }
	}

	
	
	public void sendEntryAssignment(NTTableEntry entry) throws IOException {
            synchronized(WRITE_LOCK){
		sendMessageHeader(NetworkTableMessageType.ENTRY_ASSIGNMENT);
		m_os.writeUTF(entry.name);
		m_os.writeByte(entry.getType().id);
		m_os.writeChar(entry.getId());
		m_os.writeChar(entry.getSequenceNumber());
		entry.sendValue(m_os);
            }
	}
	public void sendEntryUpdate(NTTableEntry entry) throws IOException {
            synchronized(WRITE_LOCK){
		sendMessageHeader(NetworkTableMessageType.FIELD_UPDATE);
		m_os.writeChar(entry.getId());
		m_os.writeChar(entry.getSequenceNumber());
		entry.sendValue(m_os);
            }
	}

	
	
	public void read(ConnectionAdapter adapter) throws IOException {
		try{
			int messageType = m_is.readByte();
			switch(messageType){
			case NetworkTableMessageType.KEEP_ALIVE:
				adapter.keepAlive();
				return;
			case NetworkTableMessageType.CLIENT_HELLO:
			{
				char protocolRevision = m_is.readChar();
				adapter.clientHello(protocolRevision);
				return;
			}
			case NetworkTableMessageType.SERVER_HELLO_COMPLETE:
			{
				adapter.serverHelloComplete();
				return;
			}
			case NetworkTableMessageType.PROTOCOL_VERSION_UNSUPPORTED:
			{
				char protocolRevision = m_is.readChar();
				adapter.protocolVersionUnsupported(protocolRevision);
				return;
			}
			case NetworkTableMessageType.ENTRY_ASSIGNMENT:
			{
				String entryName = m_is.readUTF();
                                byte typeId = m_is.readByte();
				NetworkTableEntryType entryType = m_typeManager.getType(typeId);
				if(entryType==null)
					throw new BadMessageException("Unknown data type: 0x"+Integer.toHexString((int)typeId));
				char entryId = m_is.readChar();
				char entrySequenceNumber = m_is.readChar();
				Object value = entryType.readValue(m_is);
				adapter.offerIncomingAssignment(new NTTableEntry(entryId, entryName, entrySequenceNumber, entryType, value));
				return;
			}
			case NetworkTableMessageType.FIELD_UPDATE:
			{
				char entryId = m_is.readChar();
				char entrySequenceNumber = m_is.readChar();
				NTTableEntry entry = adapter.getEntry(entryId);
				if(entry==null)
					throw new BadMessageException("Received update for unknown entry id: "+(int)entryId);
				Object value = entry.getType().readValue(m_is);
				
				adapter.offerIncomingUpdate(entry, entrySequenceNumber, value);
				return;
			}
			default:
				throw new BadMessageException("Unknown Network Table Message Type: "+messageType);
			}
		} catch(IOException e){
			throw e;
		}
	}

}
