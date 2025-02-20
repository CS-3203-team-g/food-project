package pro.pantrypilot.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;

class DatabaseConnectionManagerTest {

    @Test
    void testGetConnection() {
        try {
            Connection conn = DatabaseConnectionManager.getConnection();
            assertNotNull(conn, "Connection should not be null");
            conn.close();
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
}
