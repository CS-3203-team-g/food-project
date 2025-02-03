package pro.pantrypilot.db.classes;

import pro.pantrypilot.db.DatabaseConnectionManager;

import java.sql.SQLException;

public class UsersDatabase {

    public static void initializeUserDatabase(){

        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (\n"
                + "    userID CHAR(36) PRIMARY KEY DEFAULT (UUID()),\n"  // Auto-generate UUID
                + "    username VARCHAR(50) NOT NULL UNIQUE,\n"          // Unique usernames
                + "    email VARCHAR(100) NOT NULL UNIQUE,\n"            // Unique emails
                + "    passwordHash VARCHAR(255) NOT NULL,\n"            // Hashed password
                + "    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n" // Auto timestamp
                + "    lastLogin TIMESTAMP NULL DEFAULT NULL,\n"         // Nullable, updated on login
                + "    isActive BOOLEAN DEFAULT TRUE\n"                  // Active status flag
                + ");";
        try {
            DatabaseConnectionManager.getConnection().createStatement().execute(createUsersTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static void createUser(User user) {
        String createUserSQL = "INSERT INTO users (username, email, passwordHash) VALUES ('" + user.getUsername() + "', '" + user.getEmail() + "', '" + user.getPasswordHash() + "');";
        try {
            DatabaseConnectionManager.getConnection().createStatement().execute(createUserSQL);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static User getUser(String username) {
        String getUserSQL = "SELECT * FROM users WHERE username = '" + username + "';";
        try {
            return new User(DatabaseConnectionManager.getConnection().createStatement().executeQuery(getUserSQL));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
