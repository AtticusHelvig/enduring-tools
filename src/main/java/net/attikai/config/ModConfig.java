package net.attikai.config;

import net.attikai.EnduringTools;
import org.jspecify.annotations.NonNull;

import java.util.Properties;

public class ModConfig {
    public float EFFICIENCY_MULTIPLIER = 0.125f;
    public float DAMAGE_MULTIPLIER = 0.125f;

    public ModConfig() {}

    public void load() {
        Properties user = ConfigIO.load(EnduringTools.MOD_ID);
        if (user != null) {
            apply(user);
            return;
        }
        EnduringTools.LOGGER.info("No configuration file detected, creating");
        create();
    }

    public void create() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Configuration for Enduring Tools\n");
        sb.append("# Broken Tool Efficiency (Mining Speed) Multiplier\n");
        sb.append(ConfigKey.EFFICIENCY_MULTIPLIER.propertyName());
        sb.append(" = ");
        sb.append(EFFICIENCY_MULTIPLIER);
        sb.append("\n# Broken Tool Damage Multiplier\n");
        sb.append(ConfigKey.DAMAGE_MULTIPLIER.propertyName());
        sb.append(" = ");
        sb.append(DAMAGE_MULTIPLIER);

        ConfigIO.store(EnduringTools.MOD_ID, sb.toString());
    }

    private void apply(@NonNull Properties properties) {
        EFFICIENCY_MULTIPLIER = validateFloat(properties, ConfigKey.EFFICIENCY_MULTIPLIER, EFFICIENCY_MULTIPLIER);
        DAMAGE_MULTIPLIER = validateFloat(properties, ConfigKey.DAMAGE_MULTIPLIER, DAMAGE_MULTIPLIER);
        validate(properties);
    }

    private float validateFloat(@NonNull Properties properties, ConfigKey key, float def) {
        String propertyName = key.propertyName();
        String value = properties.getProperty(propertyName);

        if (value == null) {
            EnduringTools.LOGGER.warn("Missing value for key '{}', using default", propertyName);
            return def;
        }

        float result = def;
        try {
            result = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            EnduringTools.LOGGER.warn("'{}' requires a float, but received '{}', ignoring", propertyName, value);
        }
        return result;
    }

    private void validate(@NonNull Properties properties) {
        for (String propertyName : properties.stringPropertyNames()) {
            if (ConfigKey.from(propertyName) == null) {
                EnduringTools.LOGGER.warn("Unknown configuration key '{}', ignoring", propertyName);
            }
        }
    }
}
