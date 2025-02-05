package pro.pantrypilot.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.config.ConfigurationManager;
import pro.pantrypilot.db.classes.ingredients.IngredientsDatabase;
import pro.pantrypilot.db.classes.session.SessionsDatabase;
import pro.pantrypilot.db.classes.user.UsersDatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnectionManager.class);

    private static Connection conn;

    private static final String DB_URL = ConfigurationManager.getProperty("database.url");
    private static final String DB_USERNAME = ConfigurationManager.getProperty("database.username");
    private static final String DB_PASSWORD = ConfigurationManager.getProperty("database.password");

    public static void connectToDatabase() {

        try {
            logger.info("Loading Database Driver");
            Class.forName("org.mariadb.jdbc.Driver");
            logger.info("Successfully loaded database driver");
        } catch (ClassNotFoundException e) {
            logger.error("Error loading database driver", e);
        }


        try {
            logger.info("Connecting to Database");
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            logger.info("Successfully connected to database");
        } catch (SQLException e) {
            logger.error("Error connecting to database", e);
            throw new RuntimeException("Error connecting to database", e);
        }

    }

    public static Connection getConnection() {
        return conn;
    }

    public static void initializeDatabase() {

        UsersDatabase.initializeUserDatabase();
        SessionsDatabase.initializeSessionsDatabase();
        IngredientsDatabase.initializeIngredientsDatabase();

    }
}
