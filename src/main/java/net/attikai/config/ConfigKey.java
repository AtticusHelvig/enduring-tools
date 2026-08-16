package net.attikai.config;

import java.util.HashMap;
import java.util.Map;

public enum ConfigKey {
    EFFICIENCY_MULTIPLIER ("broken.multiplier.efficiency"),
    DAMAGE_MULTIPLIER ("broken.multiplier.damage");

    private final String propertyName;
    private static final Map<String, ConfigKey> reverse;

    static {
        reverse = new HashMap<>();
        for (ConfigKey key : ConfigKey.values()) {
            reverse.put(key.propertyName(), key);
        }
    }

    ConfigKey(String propertyName) {
        this.propertyName = propertyName;
    }

    public String propertyName() {
        return this.propertyName;
    }

    public static ConfigKey from(String propertyName) {
        return reverse.get(propertyName);
    }
}
