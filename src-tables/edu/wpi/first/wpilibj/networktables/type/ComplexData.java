package edu.wpi.first.wpilibj.networktables.type;

/**
 *
 */
public class ComplexData {
    private final ComplexEntryType m_type;
    public ComplexData(ComplexEntryType type){
        m_type = type;
    }
    
    public ComplexEntryType getType() {
        return m_type;
    }
}
