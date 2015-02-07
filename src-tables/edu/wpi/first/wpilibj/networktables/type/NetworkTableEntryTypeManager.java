package edu.wpi.first.wpilibj.networktables.type;

import java.util.HashMap;
import java.util.Map;

public class NetworkTableEntryTypeManager {
	private final Map<Byte, NetworkTableEntryType> typeMap = new HashMap<Byte, NetworkTableEntryType>();
	
	public NetworkTableEntryType getType(byte id){
		return typeMap.get(id);
	}
	
	void registerType(NetworkTableEntryType type){
		typeMap.put(type.id, type);
	}
	
	public NetworkTableEntryTypeManager(){
		DefaultEntryTypes.registerTypes(this);
	}
}
