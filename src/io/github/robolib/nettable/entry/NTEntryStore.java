package io.github.robolib.nettable.entry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import io.github.robolib.nettable.ITable;
import io.github.robolib.nettable.ITableListener;
import io.github.robolib.nettable.NTConnection;
import io.github.robolib.nettable.NTWriteManager;
import io.github.robolib.nettable.WrongEntryTypeException;

/**
 * An entry store that handles storing entries and applying transactions
 */
public class NTEntryStore {
    private final Map<Character, NTTableEntry> m_idEntries = new HashMap<Character, NTTableEntry>();
    private final Hashtable<String, NTTableEntry> m_namedEntries = new Hashtable<String, NTTableEntry>();
    
    private final TableListenerManager m_listenerManager;

    private NTWriteManager m_receiverOut;
    private NTWriteManager m_receiverIn;

    private char m_nextId = (char)0;
    
    public NTEntryStore(TableListenerManager listenerManager){
        m_listenerManager = listenerManager;
    }
    
    /**
     * Get an entry based on it's id
     * @param entryId the id f the entry to look for
     * @return the entry or null if the entry does not exist
     */
    public NTTableEntry getEntry(final char entryId){
        synchronized(this){
            return m_idEntries.get(entryId);
        }
    }
    /**
     * Get an entry based on it's name
     * @param name the name of the entry to look for
     * @return the entry or null if the entry does not exist
     */
    public NTTableEntry getEntry(String name){
        synchronized(this){ 
            return m_namedEntries.get(name);
        }
    }
    /**
     * Get an entry based on it's name
     * @param m_name the name of the entry to look for
     * @return the entry or null if the entry does not exist
     */
    public Set<String> keys(){
        synchronized(this){
            return m_namedEntries.keySet();
        }
    }
    
    /**
     * Remove all entries
         * NOTE: This method should not be used with applications which cache entries which would lead to unknown results
         * This method is for use in testing only
     */
    public void clearEntries() {
        synchronized (this) {
            m_idEntries.clear();
            m_namedEntries.clear();
        }
    }

    /**
     * clear the id's of all entries
     */
    public void clearIds() {
        synchronized(this){
            m_idEntries.clear();
            m_namedEntries.values().forEach(NTTableEntry::clearId);
        }
    }
    
    public void setInOutReceiver(final NTWriteManager receiver){
        m_receiverOut = receiver;
        m_receiverIn = receiver;
    }
    
    public boolean addEntry(NTTableEntry newEntry){
        synchronized(this){
            NTTableEntry entry = m_namedEntries.get(newEntry.m_name);

            if(entry==null){
                newEntry.setId(m_nextId++);
                m_idEntries.put(newEntry.getId(), newEntry);
                m_namedEntries.put(newEntry.m_name, newEntry);
                return true;
            }
            return false;
        }
    }

    public boolean updateEntry(NTTableEntry entry, char sequenceNumber, Object value){
        synchronized(this){
            if(entry.putValue(sequenceNumber, value))
                return true;
            return false;
        }
    }
        
    /**
     * Check if two objects are equal doing a deep equals of arrays
     * This method assumes that o1 and o2 are of the same type (if one is an object array the other one is also)
     * @param o1
     * @param o2 
     */
    private static boolean valuesEqual(Object o1, Object o2){
        if(o1 instanceof Object[]){
            Object[] a1 = (Object[])o1;
            Object[] a2 = (Object[])o2;
            if(a1.length!=a2.length)
                return false;
            for(int i = 0; i<a1.length; ++i)
                if(!valuesEqual(a1[i], a2[i]))
                    return false;
            return true;
        }
        return o1!=null?o1.equals(o2):o2==null;
    }
    
    /**
     * Stores the given value under the given name and queues it for 
     * transmission to the server.
     * 
     * @param name The name under which to store the given value.
     * @param type The type of the given value.
     * @param value The value to store.
     * @throws WrongEntryTypeException Thrown if an 
     *  entry already exists with the given name and is of a different type.
     */
    public void putOutgoing(String name, NTEntryType type, Object value)
            throws WrongEntryTypeException{
        synchronized(this){
            NTTableEntry tableEntry = m_namedEntries.get(name);
            if(tableEntry == null){
                //TODO validate type
                tableEntry = new NTTableEntry(name, type, value);
                if(addEntry(tableEntry)){
                    tableEntry.fireListener(m_listenerManager);
                    m_receiverOut.offerOutgoingAssignment(tableEntry);
                }
            }else{
                if(tableEntry.getType().id != type.id)
                    throw new WrongEntryTypeException(name, tableEntry.getType());
                if(!valuesEqual(value, tableEntry.getValue())){
                    if(updateEntry(tableEntry, (char)(tableEntry.getSequenceNumber()+1), value)){
                        m_receiverOut.offerOutgoingUpdate(tableEntry);
                    }
                    tableEntry.fireListener(m_listenerManager);
                }
            }
        }
    }

    public void putOutgoing(NTTableEntry tableEntry, Object value){
        synchronized(this){
            //TODO Validate type
            if(!valuesEqual(value, tableEntry.getValue())){
                if(updateEntry(tableEntry, (char)(tableEntry.getSequenceNumber()+1), value)){
                    m_receiverOut.offerOutgoingUpdate(tableEntry);
                }
                tableEntry.fireListener(m_listenerManager);
            }
        }
    }
    

    public void offerIncomingAssignment(NTTableEntry entry) {
        synchronized(this){
            NTTableEntry tableEntry = m_namedEntries.get(entry.m_name);
            if(addEntry(entry)){
                if(tableEntry == null)
                    tableEntry = entry;
                tableEntry.fireListener(m_listenerManager);
                m_receiverIn.offerOutgoingAssignment(tableEntry);
            }
        }
    }

    public void offerIncomingUpdate(NTTableEntry entry, char sequenceNumber, Object value) {
        synchronized(this){
            if(updateEntry(entry, sequenceNumber, value)){
                entry.fireListener(m_listenerManager);
                m_receiverIn.offerOutgoingUpdate(entry);
            }
        }
    }

    /**
     * Send all entries in the entry store as entry assignments in a single transaction
     * @param connection
     * @throws IOException
     */
    public void sendServerHello(final NTConnection connection) throws IOException {
        synchronized(this){
            for(NTTableEntry entry : m_namedEntries.values()){
                connection.sendEntryAssignment(entry);
            }
            connection.sendServerHelloComplete();
            connection.flush();
        }
    }
    
    /**
     * Called to say that a listener should notify the listener manager of all of the entries
     * @param listener
     * @param table 
     */
    public void notifyEntries(final ITable table, final ITableListener listener) {
        synchronized(this){
            m_namedEntries.values().forEach(entry -> listener.valueChanged(
                    table, entry.m_name, entry.getValue(), true));
        }
    }

    /**
     * An object that handles firing Table Listeners
     * @author Mitchell
     *
     */
    public interface TableListenerManager {
        /**
         * Called when the object should fire it's listeners
         * @param key
         * @param value
         * @param isNew
         */
        void fireTableListeners(String key, Object value, boolean isNew);
    }
}
