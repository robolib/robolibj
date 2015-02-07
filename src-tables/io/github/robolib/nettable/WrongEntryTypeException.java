package io.github.robolib.nettable;

import io.github.robolib.nettable.entry.NTEntryType;

/**
 * Throw to indicate that an attempt to put data to a table is illegal because
 * the specified key exists with a different data type than the put data type.
 */
public class WrongEntryTypeException extends RuntimeException {

    private static final long serialVersionUID = 243566072782797116L;

    /**
     * Creates a new TableKeyExistsWithDifferentTypeException
     * 
     * @param existingKey The name of the key which exists.
     * @param existingType The type of the key which exists.
     */
    public WrongEntryTypeException(String existingKey, NTEntryType existingType) {
        this(existingKey, existingType, "");
    }
    
    public WrongEntryTypeException(String existingKey, NTEntryType existingType, String message) {
        super("Illegal put - key '" + existingKey + "' exists with type '" + existingType + "'. "+message);
    }
}
