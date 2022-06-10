package com.mcrebels.additionalblocks.additionalblocks.util;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

public class PersistentDataSpace {

    private final NamespacedKey namespacedKey;
    private final PersistentDataType<?, ?> dataType;

    public <T, Z> PersistentDataSpace(NamespacedKey namespacedKey, PersistentDataType<T, Z> dataType) {
        this.namespacedKey = namespacedKey;
        this.dataType = dataType;
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public PersistentDataType<?, ?> getDataType() {
        return dataType;
    }

}