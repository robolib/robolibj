package io.github.robolib.nettable.entry;

/**
 *
 */
public class BooleanArray extends ArrayData{

    private static final byte BOOLEAN_ARRAY_RAW_ID = 0x10;
    public static final ArrayEntryType TYPE = new ArrayEntryType(BOOLEAN_ARRAY_RAW_ID, NTEntryTypes.BOOLEAN, BooleanArray.class);


    public BooleanArray(){
        super(TYPE);
    }
    
    public boolean get(int index){
        return ((Boolean)getAsObject(index)).booleanValue();
    }
    public void set(int index, boolean value){
        _set(index, value?Boolean.TRUE:Boolean.FALSE);
    }
    public void add(boolean value){
        _add(value?Boolean.TRUE:Boolean.FALSE);
    }
}
