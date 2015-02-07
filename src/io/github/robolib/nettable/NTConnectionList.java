package io.github.robolib.nettable;

import java.util.ArrayList;
import java.util.List;

import io.github.robolib.nettable.entry.NTTableEntry;

/**
 * A list of connections that the server currently has
 */
public class NTConnectionList {
    private List<NTConnectionAdapter> connections = new ArrayList<NTConnectionAdapter>();
    private final Object connectionsLock = new Object();
    
    /**
     * Add a connection to the list
     * @param connection
     */
    public void add(final NTConnectionAdapter connection){
        synchronized(connectionsLock){
            connections.add(connection);
        }
    }
    
    /**
     * Called when a connection adapter has been closed
     * @param connectionAdapter the adapter that was closed
     */
    public void close(NTConnectionAdapter connectionAdapter, boolean closeStream) {
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
                close((NTConnectionAdapter)connections.get(0), true);
            }
        }
    }
    
    public void offerOutgoingAssignment(NTTableEntry entry) {
        synchronized(connectionsLock){
            for(int i = 0; i<connections.size(); ++i){
                ((NTConnectionAdapter)connections.get(i)).offerOutgoingAssignment(entry);
            }
        }
    }
    public void offerOutgoingUpdate(NTTableEntry entry) {
        synchronized(connectionsLock){
            for(int i = 0; i<connections.size(); ++i){
                ((NTConnectionAdapter)connections.get(i)).offerOutgoingUpdate(entry);
            }
        }
    }
    public void flush() {
        synchronized(connectionsLock){
            for(int i = 0; i<connections.size(); ++i){//TODO iterate over as array
                ((NTConnectionAdapter)connections.get(i)).flush();
            }
        }
    }
    public void ensureAlive() {
        synchronized(connectionsLock){
            for(int i = 0; i<connections.size(); ++i){//TODO iterate over as array
                ((NTConnectionAdapter)connections.get(i)).ensureAlive();
            }
        }
    }
}
