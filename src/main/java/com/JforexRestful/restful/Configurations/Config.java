package com.JforexRestful.restful.Configurations;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String PROPERTIES_FILE = "application.properties";
    private Properties properties;

    public Config() {
        this.properties = new Properties();
        try {
            Resource resource = new ClassPathResource(PROPERTIES_FILE);
            InputStream input = resource.getInputStream();
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
        Config config = new Config();
        config.printProperties();
    }
}
