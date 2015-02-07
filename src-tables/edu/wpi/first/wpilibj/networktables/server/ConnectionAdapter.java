package edu.wpi.first.wpilibj.networktables.server;

import java.io.EOFException;
import java.io.IOException;

import edu.wpi.first.wpilibj.networktables.IncomingEntryReceiver;
import edu.wpi.first.wpilibj.networktables.NTTableEntry;
import edu.wpi.first.wpilibj.networktables.NetworkTableEntryStore;
import edu.wpi.first.wpilibj.networktables.connection.BadMessageException;
import edu.wpi.first.wpilibj.networktables.connection.ConnectionMonitorThread;
import edu.wpi.first.wpilibj.networktables.connection.NetworkTableConnection;
import edu.wpi.first.wpilibj.networktables.stream.SimpleIOStream;
import edu.wpi.first.wpilibj.networktables.thread.NTThread;
import edu.wpi.first.wpilibj.networktables.thread.NTThreadManager;
import edu.wpi.first.wpilibj.networktables.type.NetworkTableEntryTypeManager;

/**
 * Object that adapts messages from a client to the server
 * 
 * @author Mitchell
 *
 */
public class ConnectionAdapter implements IncomingEntryReceiver {
	
	private final NetworkTableEntryStore m_entryStore;
	private final IncomingEntryReceiver m_transactionReceiver;
	private final ServerConnectionList m_adapterListener;
	/**
	 * the connection this adapter uses
	 */
	public final NetworkTableConnection m_connection;
	private final NTThread m_readThread;
	
	private ConnectionState m_connectionState;
	
	private void gotoState(ConnectionState newState){
		if(m_connectionState!=newState){
			System.out.println(this+" entered connection state: "+newState);
			m_connectionState = newState;
		}
	}

	/**
	 * Create a server connection adapter for a given stream
	 * 
	 * @param stream
	 * @param transactionPool
	 * @param entryStore
	 * @param transactionReceiver
	 * @param adapterListener
	 * @param threadManager
	 */
	public ConnectionAdapter(final SimpleIOStream stream, final NetworkTableEntryStore entryStore,
	        final IncomingEntryReceiver transactionReceiver, final ServerConnectionList adapterListener,
	        final NetworkTableEntryTypeManager typeManager, final NTThreadManager threadManager) {
		m_connection = new NetworkTableConnection(stream, typeManager);
		m_entryStore = entryStore;
		m_transactionReceiver = transactionReceiver;
		m_adapterListener = adapterListener;
		
		gotoState(ConnectionState.GOT_CONNECTION_FROM_CLIENT);
		m_readThread = threadManager.newBlockingPeriodicThread(new ConnectionMonitorThread(this, m_connection), "Server Connection Reader Thread");
	}
	
	/**
     * called if a bad message exception is thrown
     * @param e
     */
	public void badMessage(BadMessageException e) {
            gotoState(new ConnectionState.Error(e));
            m_adapterListener.close(this, true);
	}
	
	/**
     * called if an io exception is thrown
     * @param e
     */
	public void ioException(IOException e) {
            if(e instanceof EOFException)
                gotoState(ConnectionState.CLIENT_DISCONNECTED);
            else
                gotoState(new ConnectionState.Error(e));
            m_adapterListener.close(this, false);
	}
	
	
	/**
	 * stop the read thread and close the stream
	 */
	public void shutdown(boolean closeStream) {
		m_readThread.stop();
                if(closeStream)
		m_connection.close();
	}

	/**
     * Called when the connection receives a keep alive message
     * @throws IOException
     */
	public void keepAlive() throws IOException {
		//just let it happen
	}

	/**
     * Called when the connection receives a client hello message
     * @param protocolRevision
     * @throws IOException
     */
	public void clientHello(char protocolRevision) throws IOException {
		if(m_connectionState!=ConnectionState.GOT_CONNECTION_FROM_CLIENT)
			throw new BadMessageException("A server should not receive a client hello after it has already connected/entered an error state");
                if(protocolRevision!=NetworkTableConnection.PROTOCOL_REVISION){
                    m_connection.sendProtocolVersionUnsupported();
                    throw new BadMessageException("Client Connected with bad protocol revision: 0x"+Integer.toHexString(protocolRevision));
                }
                else{
                    m_entryStore.sendServerHello(m_connection);
                    gotoState(ConnectionState.CONNECTED_TO_CLIENT);
                }
	}

	/**
     * Called when the connection receives a protocol unsupported message
     * @param protocolRevision the protocol version the server reported it supports
     * @throws IOException
     */
	public void protocolVersionUnsupported(char protocolRevision) throws IOException {
		throw new BadMessageException("A server should not receive a protocol version unsupported message");
	}

	public void serverHelloComplete() throws IOException {
		throw new BadMessageException("A server should not receive a server hello complete message");
	}

	public void offerIncomingAssignment(NTTableEntry entry) {
		m_transactionReceiver.offerIncomingAssignment(entry);
	}

	public void offerIncomingUpdate(NTTableEntry entry, char sequenceNumber, Object value) {
		m_transactionReceiver.offerIncomingUpdate(entry, sequenceNumber, value);
	}

	/**
     * get an entry (used by a connection when filling an update entry
     * @param id
     * @return the entry or null if the entry does not exist
     */
	public NTTableEntry getEntry(char id) {
		return m_entryStore.getEntry(id);
	}

	public void offerOutgoingAssignment(NTTableEntry entry) {
		try {
			if(m_connectionState==ConnectionState.CONNECTED_TO_CLIENT)
				m_connection.sendEntryAssignment(entry);
		} catch (IOException e) {
			ioException(e);
		}
	}
	public void offerOutgoingUpdate(NTTableEntry entry) {
		try {
			if(m_connectionState==ConnectionState.CONNECTED_TO_CLIENT)
				m_connection.sendEntryUpdate(entry);
		} catch (IOException e) {
			ioException(e);
		}
	}


	public void flush() {
		try {
			m_connection.flush();
		} catch (IOException e) {
			ioException(e);
		}
	}

	/**
	 * @return the state of the connection
	 */
	public ConnectionState getConnectionState() {
            return m_connectionState;
	}

	public void ensureAlive() {
            try {
                    m_connection.sendKeepAlive();
            } catch (IOException e) {
                    ioException(e);
            }
	}

}
