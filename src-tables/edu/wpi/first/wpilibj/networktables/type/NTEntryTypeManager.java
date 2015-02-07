package edu.wpi.first.wpilibj.networktables.type;

import java.util.HashMap;
import java.util.Map;

public class NTEntryTypeManager {
	private final Map<Byte, NTEntryType> typeMap = new HashMap<Byte, NTEntryType>();
	
	public NTEntryType getType(byte id){
		return typeMap.get(id);
	}
	
	void registerType(NTEntryType type){
		typeMap.put(type.id, type);
	}
	
	public NTEntryTypeManager(){
		NTEntryTypes.registerTypes(this);
	}
}
