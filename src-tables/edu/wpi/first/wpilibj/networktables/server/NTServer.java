package edu.wpi.first.wpilibj.networktables.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import io.github.robolib.util.log.Logger;

import edu.wpi.first.wpilibj.networktables.ITableListener;
import edu.wpi.first.wpilibj.networktables.NTEntryStore;
import edu.wpi.first.wpilibj.networktables.NTEntryStore.TableListenerManager;
import edu.wpi.first.wpilibj.networktables.NTTableEntry;
import edu.wpi.first.wpilibj.networktables.NTThread;
import edu.wpi.first.wpilibj.networktables.NTThread.NTRunnable;
import edu.wpi.first.wpilibj.networktables.TableKeyExistsWithDifferentTypeException;
import edu.wpi.first.wpilibj.networktables.TableKeyNotDefinedException;
import edu.wpi.first.wpilibj.networktables.WriteManager;
import edu.wpi.first.wpilibj.networktables.type.ComplexData;
import edu.wpi.first.wpilibj.networktables.type.ComplexEntryType;
import edu.wpi.first.wpilibj.networktables.type.NTEntryType;
import edu.wpi.first.wpilibj.networktables.type.NTEntryTypeManager;
import edu.wpi.first.wpilibj.networktables.type.NTEntryTypes;

/**
 * A server node in NetworkTables 2.0
 */
public class NTServer implements NTRunnable, TableListenerManager {
    
	private final WriteManager m_writeManager;
	private final ConnectionList m_connectionList;
	private final NTEntryStore m_entryStore;
	private final NTEntryTypeManager m_typeManager;
	private final ServerSocket m_server;

    private final List<ITableListener> m_tableListeners;

	private NTThread m_monitorThread;

	/**
	 * Create a NetworkTable Server
	 * 
	 * @param streamProvider
	 * @param threadManager
	 * @param transactionPool
	 * @throws IOException 
	 */
	public NTServer() throws IOException {
		m_entryStore = new NTEntryStore(this);
		m_tableListeners = new ArrayList<ITableListener>();
		
		m_server = new ServerSocket(1735);
		
		m_connectionList = new ConnectionList();
		m_writeManager = new WriteManager(m_connectionList, m_entryStore, Long.MAX_VALUE);

		m_typeManager = new NTEntryTypeManager();
		m_entryStore.setInOutReceiver(m_writeManager);
		
		start();
		m_writeManager.start();
	}

    /**
     * @return a new SimpleIOStream normally from a server
     * @throws IOException
     */
    public SocketStream acceptConnection() throws IOException {
            Socket socket = m_server.accept();
            return new SocketStream(socket);
    }
	
	/**
     * close all networking activity related to this node
     */
	public void close(){
		try {
			stop();
			m_writeManager.stop();
			m_connectionList.closeAll();
			Thread.sleep(1000);
			m_server.close();
			Thread.sleep(1000);
		} catch (Exception e) {
		    Logger.get(this).fatal("Network Tables failure", e);
		}
	}

	/**
	 * Start the monitor thread
	 */
	public void start(){
		if(m_monitorThread != null)
			stop();
		m_monitorThread = NTThread.newBlockingPeriodicThread(this, "Server Incoming Stream Monitor Thread");
	}

	/**
	 * Stop the monitor thread
	 */
	public void stop(){
		if(m_monitorThread!=null)
			m_monitorThread.stop();
	}
	
	public void run(){
		SocketStream newStream = null;
		try {
		    newStream = new SocketStream(m_server.accept());
			if(newStream != null){
				ConnectionAdapter connectionAdapter = new ConnectionAdapter(
				        newStream,
				        m_entryStore,
				        m_connectionList,
				        m_typeManager);
				
				onNewConnection(connectionAdapter);
			}
		} catch(IOException e){}
	}

	/**
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
    /**
     * @return the entry store used by this node
     */
    public NTEntryStore getEntryStore(){
        return m_entryStore;
    }
    
    public void putBoolean(String name, boolean value){
        putValue(name, NTEntryTypes.BOOLEAN, value?Boolean.TRUE:Boolean.FALSE);
    }
    
    public boolean getBoolean(String name) throws TableKeyNotDefinedException{
        NTTableEntry entry = m_entryStore.getEntry(name);
        if(entry == null)
            throw new TableKeyNotDefinedException(name);
        return ((Boolean)entry.getValue()).booleanValue();
    }

    public void putDouble(String name, double value){
        putValue(name, NTEntryTypes.DOUBLE, new Double(value));//TODO don't make a new double every time
    }
    
    public double getDouble(String name) throws TableKeyNotDefinedException{
        NTTableEntry entry = m_entryStore.getEntry(name);
        if(entry == null)
            throw new TableKeyNotDefinedException(name);
        return ((Double)entry.getValue()).doubleValue();
    }

    public void putString(String name, String value){
        putValue(name, NTEntryTypes.STRING, value);
    }
    
    public String getString(String name) throws TableKeyNotDefinedException{
        NTTableEntry entry = m_entryStore.getEntry(name);
        if(entry == null)
            throw new TableKeyNotDefinedException(name);
        return ((String)entry.getValue());
    }
        
    public void putComplex(String name, ComplexData value){
        putValue(name, value.getType(), value);
    }
        
    public void retrieveValue(String name, Object externalData) throws TableKeyNotDefinedException{
        synchronized(m_entryStore){
            NTTableEntry entry = m_entryStore.getEntry(name);
            if(entry == null)
                throw new TableKeyNotDefinedException(name);
            NTEntryType entryType = entry.getType();
            if(!(entryType instanceof ComplexEntryType))
                throw new TableKeyExistsWithDifferentTypeException(name, entryType, "Is not a complex data type");
            ComplexEntryType complexType = (ComplexEntryType)entryType;
            complexType.exportValue(name, entry.getValue(), externalData);
        }
    }
    
    public void putValue(String name, Object value) throws IllegalArgumentException{
        if(value instanceof Double){
                putValue(name, NTEntryTypes.DOUBLE, value);
        } else if (value instanceof String){
                putValue(name, NTEntryTypes.STRING, value);
        } else if(value instanceof Boolean){
                putValue(name, NTEntryTypes.BOOLEAN, value);
        } else if(value instanceof ComplexData){
                putValue(name, ((ComplexData)value).getType(), value);
        } else if(value == null) {
            throw new NullPointerException("Cannot put a null value into networktables");
        } else {
            throw new IllegalArgumentException("Invalid Type");
        }
    }
    
    /**
     * Put a value with a specific network table type
     * @param name the name of the entry to associate with the given value
     * @param type the type of the entry
     * @param value the actual value of the entry
     */
    public void putValue(String name, NTEntryType type, Object value){
        if(type instanceof ComplexEntryType){
            synchronized(m_entryStore){//must sync because use get
                ComplexEntryType entryType = (ComplexEntryType)type;
                NTTableEntry entry = m_entryStore.getEntry(name);
                if(entry!=null)
                    m_entryStore.putOutgoing(entry, entryType.internalizeValue(entry.m_name, value, entry.getValue()));
                else
                    m_entryStore.putOutgoing(name, type, entryType.internalizeValue(name, value, null));
            }
        }
        else
            m_entryStore.putOutgoing(name, type, value);
    }
    
    public void putValue(NTTableEntry entry, Object value){
        if(entry.getType() instanceof ComplexEntryType){
            synchronized(m_entryStore){//must sync because use get
                ComplexEntryType entryType = (ComplexEntryType)entry.getType();
                m_entryStore.putOutgoing(entry, entryType.internalizeValue(entry.m_name, value, entry.getValue()));
            }
        }
        else
            m_entryStore.putOutgoing(entry, value);
    }
    
    public Object getValue(String name) throws TableKeyNotDefinedException{//TODO don't allow get of complex types
        synchronized(m_entryStore){
            NTTableEntry entry = m_entryStore.getEntry(name);
            if(entry == null) throw new TableKeyNotDefinedException(name);
            return entry.getValue();
        }
    }
    
    /**
     * @param key the key to check for existence
     * @return true if the table has the given key
     */
    public boolean containsKey(final String key){
        return m_entryStore.getEntry(key)!=null;
    }

    
    public void addTableListener(ITableListener listener, boolean immediateNotify) {
        m_tableListeners.add(listener);
        if(immediateNotify)
            m_entryStore.notifyEntries(null, listener);
    }
    
    public void removeTableListener(ITableListener listener) {
        m_tableListeners.remove(listener);
    }
    
    public void fireTableListeners(String key, Object value, boolean isNew){
        for(int i = 0; i < m_tableListeners.size(); ++i)
            ((ITableListener)m_tableListeners.get(i)).valueChanged(null, key, value, isNew);
    }

}