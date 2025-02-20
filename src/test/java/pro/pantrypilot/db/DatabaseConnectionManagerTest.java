package pro.pantrypilot.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;

class DatabaseConnectionManagerTest {

    @Test
    void testGetConnection() {
        try {
            System.out.println("TESTING");
            Connection conn = DatabaseConnectionManager.getConnection();
            System.out.println("TESTING");
            assertNotNull(conn, "Connection should not be null");
            System.out.println("TESTING");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("AKSJHBDKASJD");
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.getClass());
            fail("Should not throw exception: " + e.getMessage());
        }
    }
}
