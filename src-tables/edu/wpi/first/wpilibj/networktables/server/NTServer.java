package edu.wpi.first.wpilibj.networktables.server;

import io.github.robolib.util.log.Logger;

import edu.wpi.first.wpilibj.networktables.NetworkTableEntryStore;
import edu.wpi.first.wpilibj.networktables.NTNode;
import edu.wpi.first.wpilibj.networktables.TransactionDirtier;
import edu.wpi.first.wpilibj.networktables.WriteManager;
import edu.wpi.first.wpilibj.networktables.stream.SocketServerStreamProvider;
import edu.wpi.first.wpilibj.networktables.thread.DefaultThreadManager;
import edu.wpi.first.wpilibj.networktables.thread.NTThreadManager;
import edu.wpi.first.wpilibj.networktables.type.NetworkTableEntryTypeManager;

/**
 * A server node in NetworkTables 2.0
 * 
 * @author Mitchell
 *
 */
public class NTServer extends NTNode {
	private final IncomingStreamMonitor m_streamMonitor;
	private final WriteManager m_writeManager;
	private final SocketServerStreamProvider m_streamProvider;
	private final ServerConnectionList m_connectionList;

	/**
	 * Create a NetworkTable Server
	 * 
	 * @param streamProvider
	 * @param threadManager
	 * @param transactionPool
	 */
	public NTServer(
	        final SocketServerStreamProvider streamProvider,
	        final NetworkTableEntryTypeManager typeManager,
	        final NTThreadManager threadManager){
		NetworkTableEntryStore entryStore = new NetworkTableEntryStore(this);
		init(entryStore);
		m_streamProvider = streamProvider;
		
		m_connectionList = new ServerConnectionList();
		m_writeManager = new WriteManager(m_connectionList, threadManager, entryStore, Long.MAX_VALUE);

		m_streamMonitor = new IncomingStreamMonitor(streamProvider, entryStore,
		        this, m_connectionList,
		        typeManager, threadManager);
		
		entryStore.setIncomingReceiver(new TransactionDirtier(m_writeManager));
		entryStore.setOutgoingReceiver(new TransactionDirtier(m_writeManager));
		
		m_streamMonitor.start();
		m_writeManager.start();
	}
	/**
	 * Create a NetworkTable Server
	 * 
	 * @param streamProvider
	 */
	public NTServer(final SocketServerStreamProvider streamProvider){
		this(streamProvider, new NetworkTableEntryTypeManager(), new DefaultThreadManager());
	}
	
	public void close(){
		try {
			m_streamMonitor.stop();
			m_writeManager.stop();
			m_connectionList.closeAll();
			Thread.sleep(1000);//To get around bug where an error will occur in select if the socket server is closed before all sockets finish closing
			m_streamProvider.close();
			Thread.sleep(1000);
		} catch (Exception e) {
		    Logger.get(this).fatal("Network Tables failure", e);
		}
	}

	/**
     * 
     * Called on create of a new connection
     * @param connectionAdapter the server connection adapter
     */
	public void onNewConnection(ConnectionAdapter connectionAdapter) {
		m_connectionList.add(connectionAdapter);
	}

	
	public boolean isConnected() {
		return true;
	}

	
	public boolean isServer() {
		return true;
	}

}