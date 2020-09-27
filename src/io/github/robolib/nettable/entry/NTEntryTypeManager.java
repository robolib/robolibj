package io.github.robolib.nettable.entry;

import java.util.HashMap;
import java.util.Map;

public class NTEntryTypeManager {
    private final Map<Byte, NTEntryType> typeMap = new HashMap<Byte, NTEntryType>();

    public NTEntryType getType(byte id) {
        return typeMap.get(id);
    }

    void registerType(NTEntryType type) {
        typeMap.put(type.id, type);
    }

    public NTEntryTypeManager() {
        NTEntryTypes.registerTypes(this);
    }
}
