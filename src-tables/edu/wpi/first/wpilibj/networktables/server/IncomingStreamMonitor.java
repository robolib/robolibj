package edu.wpi.first.wpilibj.networktables.server;

import java.io.IOException;

import edu.wpi.first.wpilibj.networktables.NetworkTableEntryStore;
import edu.wpi.first.wpilibj.networktables.stream.SimpleIOStream;
import edu.wpi.first.wpilibj.networktables.stream.SocketServerStreamProvider;
import edu.wpi.first.wpilibj.networktables.thread.NTThread;
import edu.wpi.first.wpilibj.networktables.thread.NTThreadManager;
import edu.wpi.first.wpilibj.networktables.thread.PeriodicRunnable;
import edu.wpi.first.wpilibj.networktables.type.NetworkTableEntryTypeManager;

/**
 * Thread that monitors for incoming connections
 * 
 * @author Mitchell
 *
 */
public class IncomingStreamMonitor implements PeriodicRunnable{

	private final SocketServerStreamProvider m_streamProvider;
	private final NTServer m_incomingListener;
	private final NetworkTableEntryStore m_entryStore;
	private final ServerConnectionList m_adapterListener;

	private NTThread m_monitorThread;
	private NTThreadManager m_threadManager;
	private final NetworkTableEntryTypeManager m_typeManager;
	
	/**
	 * Create a new incoming stream monitor
	 * @param streamProvider the stream provider to retrieve streams from
	 * @param entryStore the entry store for the server
	 * @param transactionPool transaction pool for the server
	 * @param incomingListener the listener that is notified of new connections
	 * @param adapterListener the listener that will listen to adapter events
	 * @param threadManager the thread manager used to create the incoming thread and provided to the Connection Adapters
	 */
	public IncomingStreamMonitor(
	        final SocketServerStreamProvider streamProvider,
	        final NetworkTableEntryStore entryStore,
			final NTServer incomingListener,
			final ServerConnectionList adapterListener,
			final NetworkTableEntryTypeManager typeManager,
			final NTThreadManager threadManager){
	    
		m_streamProvider = streamProvider;
		m_entryStore = entryStore;
		m_incomingListener = incomingListener;
		m_adapterListener = adapterListener;
		m_typeManager = typeManager;
		m_threadManager = threadManager;
	}
	
	/**
	 * Start the monitor thread
	 */
	public void start(){
		if(m_monitorThread!=null)
			stop();
		m_monitorThread = m_threadManager.newBlockingPeriodicThread(this, "Server Incoming Stream Monitor Thread");
	}
	/**
	 * Stop the monitor thread
	 */
	public void stop(){
		if(m_monitorThread!=null)
			m_monitorThread.stop();
	}
	
	public void run(){
		SimpleIOStream newStream = null;
		try {
			newStream = m_streamProvider.accept();
			if(newStream!=null){
				ConnectionAdapter connectionAdapter = new ConnectionAdapter(newStream, m_entryStore, m_entryStore, m_adapterListener, m_typeManager, m_threadManager);
				m_incomingListener.onNewConnection(connectionAdapter);
			}
		} catch (IOException e) {
			//could not get a new stream for some reason. ignore and continue
		}
	}
	
	
	
}
