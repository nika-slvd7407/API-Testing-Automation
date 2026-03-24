package com.solvd.testutil;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private final Properties properties = new Properties();

    public ConfigReader(String configFileName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(configFileName)) {
            if (is == null) throw new RuntimeException("File not found: " + configFileName);
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("Error loading config", e);
        }
    }

    public String getProperty(String key) { return properties.getProperty(key); }
}
