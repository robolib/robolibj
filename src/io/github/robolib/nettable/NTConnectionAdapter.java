package io.github.robolib.nettable;

import java.io.EOFException;
import java.io.IOException;

import io.github.robolib.nettable.entry.NTEntryStore;
import io.github.robolib.nettable.entry.NTEntryTypeManager;
import io.github.robolib.nettable.entry.NTTableEntry;

/**
 * Object that adapts messages from a client to the server
 */
public class NTConnectionAdapter {
    
    private final NTEntryStore m_entryStore;
    private final NTConnectionList m_adapterListener;
    public final NTConnection m_connection;
    private final NTThread m_readThread;
    
    private NTConnectionState m_connectionState;
    
    private void gotoState(NTConnectionState newState){
        if(m_connectionState != newState){
            System.out.println(this + " entered connection state: " + newState);
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
    public NTConnectionAdapter(
            final NTSocketStream stream,
            final NTEntryStore entryStore,
            final NTConnectionList adapterListener,
            final NTEntryTypeManager typeManager) {
        m_connection = new NTConnection(stream, typeManager);
        m_entryStore = entryStore;
        m_adapterListener = adapterListener;
        
        gotoState(NTConnectionState.GOT_CONNECTION_FROM_CLIENT);
        m_readThread = NTThread.newBlockingPeriodicThread(() -> {
            try{
                m_connection.read(this);
            } catch(BadMessageException e){
                badMessage(e);
            } catch(IOException e){
                ioException(e);
            }
        }, "Server Connection Reader Thread");
    }
    
    /**
     * called if a bad message exception is thrown
     * @param e
     */
    public void badMessage(BadMessageException e) {
            gotoState(new NTConnectionState.Error(e));
            m_adapterListener.close(this, true);
    }
    
    /**
     * called if an io exception is thrown
     * @param e
     */
    public void ioException(IOException e) {
        if(e instanceof EOFException)
            gotoState(NTConnectionState.CLIENT_DISCONNECTED);
        else
            gotoState(new NTConnectionState.Error(e));
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
    public void keepAlive() throws IOException {}

    /**
     * Called when the connection receives a client hello message
     * @param protocolRevision
     * @throws IOException
     */
    public void clientHello(char protocolRevision) throws IOException {
        if(m_connectionState != NTConnectionState.GOT_CONNECTION_FROM_CLIENT)
            throw new BadMessageException("A server should not receive a client hello after it has already connected/entered an error state");
        if(protocolRevision != NTConnection.PROTOCOL_REVISION){
            m_connection.sendProtocolVersionUnsupported();
            throw new BadMessageException("Client Connected with bad protocol revision: 0x"+Integer.toHexString(protocolRevision));
        }else{
            m_entryStore.sendServerHello(m_connection);
            gotoState(NTConnectionState.CONNECTED_TO_CLIENT);
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
        m_entryStore.offerIncomingAssignment(entry);
    }

    public void offerIncomingUpdate(NTTableEntry entry, char sequenceNumber, Object value) {
        m_entryStore.offerIncomingUpdate(entry, sequenceNumber, value);
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
            if(m_connectionState == NTConnectionState.CONNECTED_TO_CLIENT)
                m_connection.sendEntryAssignment(entry);
        } catch (IOException e) {
            ioException(e);
        }
    }
    public void offerOutgoingUpdate(NTTableEntry entry) {
        try {
            if(m_connectionState == NTConnectionState.CONNECTED_TO_CLIENT)
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
    public NTConnectionState getConnectionState() {
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
