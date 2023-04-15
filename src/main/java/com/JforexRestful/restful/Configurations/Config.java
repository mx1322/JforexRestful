package com.JforexRestful.restful.Configurations;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String PROPERTIES_FILE = "src/main/resources/application.properties";
    private Properties properties;

    public Config() {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        this.properties = new Properties();
        try (InputStream input = new FileInputStream(PROPERTIES_FILE)) {
            if (input == null) {
                System.err.println("Failed to find " + PROPERTIES_FILE + " on the classpath");
                System.exit(1);
            }
            properties.load(input);
        } catch (IOException ex) {
            System.err.println("Failed to read " + PROPERTIES_FILE + ": " + ex.getMessage());
            System.exit(1);
        }
    }

    public String getJnlpUrl() {
        return properties.getProperty("jnlpUrl");
    }

    public String getUserName() {
        return properties.getProperty("username");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }

    public void printProperties() {
        System.out.println("Properties:");
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            System.out.println("\t" + key + " = " + value);
        }
    }

    public static void main(String[] args) {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        System.out.println(PROPERTIES_FILE);
        Config config = new Config();
        System.out.println("jnlpUrl: " + config.getJnlpUrl());
        System.out.println("username: " + config.getUserName());
        System.out.println("password: " + config.getPassword());
        config.printProperties();
    }
}
