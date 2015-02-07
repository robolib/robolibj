package edu.wpi.first.wpilibj.networktables.connection;

import java.io.IOException;

import edu.wpi.first.wpilibj.networktables.server.ConnectionAdapter;
import edu.wpi.first.wpilibj.networktables.thread.PeriodicRunnable;


/**
 * A periodic thread that repeatedly reads from a connection
 * @author Mitchell
 *
 */
public class ConnectionMonitorThread implements PeriodicRunnable{
	private final ConnectionAdapter m_adapter;
	private final NetworkTableConnection m_connection;

	/**
	 * create a new monitor thread
	 * @param adapter
	 * @param connection
	 */
	public ConnectionMonitorThread(final ConnectionAdapter adapter, final NetworkTableConnection connection) {
		m_adapter = adapter;
		m_connection = connection;
	}

	public void run() throws InterruptedException {
		try{
			m_connection.read(m_adapter);
		} catch(BadMessageException e){
			m_adapter.badMessage(e);
		} catch(IOException e){
			m_adapter.ioException(e);
		}
	}
}