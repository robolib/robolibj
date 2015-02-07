package edu.wpi.first.wpilibj.networktables.type;

/**
 *
 */
public abstract class ComplexEntryType extends NTEntryType{
    
    protected ComplexEntryType(byte id, String name){
            super(id, name);
    }
    
    public abstract Object internalizeValue(String key, Object externalRepresentation, Object currentInteralValue);
    public abstract void exportValue(String key, Object internalData, Object externalRepresentation);
    
}
