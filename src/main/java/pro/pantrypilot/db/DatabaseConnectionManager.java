package pro.pantrypilot.db;

import pro.pantrypilot.config.ConfigurationManager;
import pro.pantrypilot.db.classes.UsersDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static Connection conn;

    private static final String DB_URL = ConfigurationManager.getProperty("database.url");
    private static final String DB_USERNAME = ConfigurationManager.getProperty("database.username");
    private static final String DB_PASSWORD = ConfigurationManager.getProperty("database.password");

    public static void connectToDatabase() {

        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static Connection getConnection() {
        return conn;
    }

    public static void initializeDatabase() {

        UsersDatabase.initializeUserDatabase();

    }
}
