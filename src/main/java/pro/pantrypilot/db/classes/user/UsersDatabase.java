package pro.pantrypilot.db.classes.user;

import pro.pantrypilot.db.DatabaseConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UsersDatabase {

    public static void initializeUserDatabase(){

        String createUsersTableSQL = "CREATE TABLE IF NOT EXISTS users (\n"
                + "    userID CHAR(36) PRIMARY KEY DEFAULT (UUID()),\n"  // Auto-generate UUID
                + "    username VARCHAR(50) NOT NULL UNIQUE,\n"          // Unique usernames
                + "    email VARCHAR(100) NOT NULL UNIQUE,\n"            // Unique emails
                + "    passwordHash VARCHAR(255) NOT NULL,\n"            // Hashed password
                + "    salt VARCHAR(255) NOT NULL UNIQUE,\n"                    // Salt for password hashing
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

    public static boolean createUser(User user) {
        String createUserSQL = "INSERT INTO users (username, email, passwordHash, salt) VALUES ('"
                + user.getUsername() + "', '"
                + user.getEmail() + "', '"
                + user.getPasswordHash() + "', '"
                + user.getSalt() + "');";
        try {
            int rowsAffected = DatabaseConnectionManager.getConnection()
                    .createStatement()
                    .executeUpdate(createUserSQL);
            return rowsAffected > 0; // Returns true if a row was inserted
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Returns false if an exception occurred
        }
    }


    public static User getUser(String username) {
        String getUserSQL = "SELECT * FROM users WHERE username = '" + username + "';";
        try (Statement statement = DatabaseConnectionManager.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(getUserSQL)) {

            if (resultSet.next()) {  // Move cursor to the first row
                return new User(resultSet);
            } else {
                return null; // No user found with the given username
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


}
