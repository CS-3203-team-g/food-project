package pro.pantrypilot.config;

import pro.pantrypilot.Main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigurationManager {

    private static Properties properties = new Properties();

    public static void loadConfig() {
        try {
            InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/config.properties"));
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        if(properties.isEmpty()) {
            loadConfig();
        }
        return properties.getProperty(key);
    }

    public static int getIntProperty(String key) {
        if(properties.isEmpty()) {
            loadConfig();
        }
        return Integer.parseInt(properties.getProperty(key));
    }

    public static boolean getBooleanProperty(String key) {
        if(properties.isEmpty()) {
            loadConfig();
        }
        return Boolean.parseBoolean(properties.getProperty(key));
    }

    public static double getDoubleProperty(String key) {
        if(properties.isEmpty()) {
            loadConfig();
        }
        return Double.parseDouble(properties.getProperty(key));
    }
}
