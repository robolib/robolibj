package edu.wpi.first.wpilibj.networktables.server;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.networktables.NTTableEntry;

/**
 * A list of connections that the server currently has
 */
public class ConnectionList {
	private List<ConnectionAdapter> connections = new ArrayList<ConnectionAdapter>();
	private final Object connectionsLock = new Object();
	
	/**
	 * Add a connection to the list
	 * @param connection
	 */
	public void add(final ConnectionAdapter connection){
		synchronized(connectionsLock){
			connections.add(connection);
		}
	}
	
	/**
     * Called when a connection adapter has been closed
     * @param connectionAdapter the adapter that was closed
     */
	public void close(ConnectionAdapter connectionAdapter, boolean closeStream) {
		synchronized(connectionsLock){
			if(connections.remove(connectionAdapter)){
				System.out.println("Close: "+connectionAdapter);
				connectionAdapter.shutdown(closeStream);
			}
		}
	}
	/**
	 * close all connections and remove them
	 */
	public void closeAll() {
		synchronized(connectionsLock){
			while(connections.size()>0){
				close((ConnectionAdapter)connections.get(0), true);
			}
		}
	}
	
	public void offerOutgoingAssignment(NTTableEntry entry) {
		synchronized(connectionsLock){
			for(int i = 0; i<connections.size(); ++i){
				((ConnectionAdapter)connections.get(i)).offerOutgoingAssignment(entry);
			}
		}
	}
	public void offerOutgoingUpdate(NTTableEntry entry) {
		synchronized(connectionsLock){
			for(int i = 0; i<connections.size(); ++i){
				((ConnectionAdapter)connections.get(i)).offerOutgoingUpdate(entry);
			}
		}
	}
	public void flush() {
		synchronized(connectionsLock){
			for(int i = 0; i<connections.size(); ++i){//TODO iterate over as array
				((ConnectionAdapter)connections.get(i)).flush();
			}
		}
	}
	public void ensureAlive() {
		synchronized(connectionsLock){
			for(int i = 0; i<connections.size(); ++i){//TODO iterate over as array
				((ConnectionAdapter)connections.get(i)).ensureAlive();
			}
		}
	}
}
