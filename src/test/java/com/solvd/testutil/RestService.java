package com.solvd.testutil;

public class RestService {

    private static final ConfigReader config = new ConfigReader("config.properties");

    public static String getBaseUrl() {
        return config.getProperty("URL");
    }

    public static String getToken() {
        return config.getProperty("TOKEN");
    }

    public static String getGraphUrl() {
        return config.getProperty("GRAPH_URL");
    }
}