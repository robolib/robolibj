package io.github.robolib.nettable.entry;

import java.io.DataOutputStream;
import java.io.IOException;

import io.github.robolib.nettable.NTConnection;
import io.github.robolib.nettable.entry.NTEntryStore.TableListenerManager;

/**
 * An entry in a network table
 */
public class NTTableEntry {
    /**
     * the id that represents that an id is unknown for an entry
     */
    public static final char UNKNOWN_ID = (char)0xFFFF;

    private char m_id;
    private char m_sequenceNumber;
    /**
     * the name of the entry
     */
    public final String m_name;
    /**
     * the type of the entry
     */
    private NTEntryType m_type;
    private Object m_value;
    private volatile boolean m_isNew = true;
    private volatile boolean m_isDirty = false;

    /**
     * Create a new entry with the given name, type, value, an unknown id and a sequence number of 0
     * @param name
     * @param type
     * @param value
     */
    public NTTableEntry(final String name, final NTEntryType type, final Object value){
        this(UNKNOWN_ID, name, (char)0, type, value);
    }
    /**
     * Create a new entry with the given id, name, sequence number, type and value
     * @param id
     * @param name
     * @param sequenceNumber
     * @param type
     * @param value
     */
    public NTTableEntry(final char id, final String name, final char sequenceNumber, final NTEntryType type, final Object value){
        m_id = id;
        m_name = name;
        m_sequenceNumber = sequenceNumber;
        m_type = type;
        m_value = value;
    }

    /**
     * @return the id of the entry
     */
    public char getId() {
        return m_id;
    }
    /**
     * @return the current value of the entry
     */
    public Object getValue(){
        return m_value;
    }
    /**
     * @return the type of the entry
     */
    public NTEntryType getType(){
        return m_type;
    }
    private static final char HALF_OF_CHAR = 32768;
    /**
     * set the value of the entry if the given sequence number is greater that the current sequence number
     * @param newSequenceNumber the sequence number of the incoming entry
     * @param newValue the new value
     * @return true if the value was set
     */
    public boolean putValue(final char newSequenceNumber, final Object newValue) {
        if( (m_sequenceNumber < newSequenceNumber && newSequenceNumber - m_sequenceNumber < HALF_OF_CHAR)
                || (m_sequenceNumber > newSequenceNumber && m_sequenceNumber - newSequenceNumber > HALF_OF_CHAR) ){
            m_value = newValue;
            m_sequenceNumber = newSequenceNumber;
            return true;
        }
        return false;
    }
    /**
     * force a value and new sequence number upon an entry
     * @param newSequenceNumber
     * @param newValue
     */
    public void forcePut(final char newSequenceNumber, final Object newValue) {
        m_value = newValue;
        m_sequenceNumber = newSequenceNumber;
    }
    /**
     * force a value and new sequence number upon an entry, Will also set the type of the entry
     * @param newSequenceNumber
     * @param type
     * @param newValue
     */
    public void forcePut(final char newSequenceNumber, final NTEntryType type, final Object newValue) {
        m_type = type;
        forcePut(newSequenceNumber, newValue);
    }
    
    
    public void makeDirty() {
        m_isDirty = true;
    }
    public void makeClean() {
        m_isDirty = false;
    }
    public boolean isDirty(){
        return m_isDirty;
    }

    /**
     * Send the value of the entry over the output stream
     * @param os
     * @throws IOException
     */
    public void sendValue(final DataOutputStream os) throws IOException{
        m_type.sendValue(m_value, os);
    }

    /**
     * @return the current sequence number of the entry
     */
    public char getSequenceNumber() {
        return m_sequenceNumber;
    }
    /**
     * Sets the id of the entry
     * @param id the id of the entry
     * @throws IllegalStateException if the entry already has a known id
     */
    public void setId(final char id) throws IllegalStateException{
        if(this.m_id!=UNKNOWN_ID)
            throw new IllegalStateException("Cannot set the Id of a table entry that already has a valid id");
        m_id = id;
    }
    /**
     * clear the id of the entry to unknown
     */
    public void clearId() {
        m_id = UNKNOWN_ID;
    }
    
    public void send(NTConnection connection) throws IOException {
        connection.sendEntryAssignment(this);
    }
    public void fireListener(TableListenerManager listenerManager) {//TODO determine best way to handle complex data
        listenerManager.fireTableListeners(m_name, m_value, m_isNew);
        m_isNew = false;
    }

    public String toString(){
        return "Network Table " + m_type.name
                + " entry: " + m_name + ": "
                + (int)getId() + " - " + (int)getSequenceNumber()
                + " - " + getValue();
    }

    
    
}
