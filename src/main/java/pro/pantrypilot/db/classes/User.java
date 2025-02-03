package pro.pantrypilot.db.classes;

import java.util.UUID;
import java.sql.Timestamp;

public class User {
    private String userID;
    private String username;
    private String email;
    private String passwordHash;
    private Timestamp createdAt;
    private Timestamp lastLogin;
    private boolean isActive;

    // Constructor for new users (auto-generates userID)
    public User(String username, String email, String passwordHash) {
        this.userID = UUID.randomUUID().toString(); // Auto-generate UUID in Java
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.isActive = true; // Default value
        this.createdAt = new Timestamp(System.currentTimeMillis()); // Set current timestamp
        this.lastLogin = null; // Not logged in yet
    }

    // Constructor for loading existing users from the database
    public User(String userID, String username, String email, String passwordHash,
                Timestamp createdAt, Timestamp lastLogin, boolean isActive) {
        this.userID = userID;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
        this.isActive = isActive;
    }

    // Getters and Setters
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Method to update last login timestamp
    public void updateLastLogin() {
        this.lastLogin = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return "User{" +
                "userID='" + userID + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", lastLogin=" + lastLogin +
                ", isActive=" + isActive +
                '}';
    }
}
