package pro.pantrypilot.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationManagerTest {

    @Test
    void getProperty() {

        ConfigurationManager.loadConfig();
        String property = ConfigurationManager.getProperty("server.port");
        assertNotNull(property);

    }

    @Test
    void getIntProperty() {

        ConfigurationManager.loadConfig();
        int intProperty = ConfigurationManager.getIntProperty("server.port");
        assertNotEquals(0, intProperty);

    }
}