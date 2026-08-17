package net.attikai.config;

import net.attikai.EnduringTools;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigIO {
    private static final String EXTENSION = ".properties";

    public static Properties load(String filename) {
        return loadOrDefault(filename, null);
    }

    public static Properties loadOrDefault(String filename, Properties def) {
        // find/open the file
        Path path = FabricLoader.getInstance().getConfigDir();
        if (path == null) {
            EnduringTools.LOGGER.warn("Failed to access config directory (load)");
            return def;
        }
        File file = path.resolve(filename + EXTENSION).toFile();

        if (!file.exists()) {
            EnduringTools.LOGGER.warn("Config file missing");
            return def;
        }
        // create a stream from the file
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            EnduringTools.LOGGER.warn("Somehow the config file doesn't exist even though we just checked");
        }
        if (stream == null) {
            EnduringTools.LOGGER.warn("Input stream was null");
            return def;
        }

        // parse the stream
        Properties properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            EnduringTools.LOGGER.warn("Failed to parse the config file");
        }

        try {
            stream.close();
        } catch (IOException e) {
            EnduringTools.LOGGER.warn("Couldn't close the things");
        }

        return properties;
    }

    public static boolean store(String filename, String content) {
        Path path = FabricLoader.getInstance().getConfigDir();
        if (path == null) {
            EnduringTools.LOGGER.warn("Failed to access config directory (store)");
            return false;
        }
        path = path.resolve(filename + EXTENSION);
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            EnduringTools.LOGGER.error("Failed to store config file");
            return false;
        }
        return true;
    }
}
