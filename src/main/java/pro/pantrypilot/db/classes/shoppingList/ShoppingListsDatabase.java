package pro.pantrypilot.db.classes.shoppingList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.db.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListsDatabase {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingListsDatabase.class);

    public static void initializeShoppingListsDatabase() {
        String createShoppingListsTableSQL = "CREATE TABLE IF NOT EXISTS shopping_lists (\n"
                + "    shoppingListID INT AUTO_INCREMENT PRIMARY KEY,\n"
                + "    shoppingListName VARCHAR(255) NOT NULL,\n"
                + "    userID CHAR(36) NOT NULL,\n"
                + "    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
                + "    lastUpdated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n"
                + "    FOREIGN KEY (userID) REFERENCES users(userID) ON DELETE CASCADE\n"
                + ");";
        try {
            Statement stmt = DatabaseConnectionManager.getConnection().createStatement();
            stmt.execute(createShoppingListsTableSQL);
        } catch (SQLException e) {
            logger.error("Error creating shopping_lists table", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean createShoppingList(String shoppingListName, String userID) {
        String createShoppingListSQL = "INSERT INTO shopping_lists (shoppingListName, userID) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = DatabaseConnectionManager.getConnection().prepareStatement(createShoppingListSQL)) {
            preparedStatement.setString(1, shoppingListName);
            preparedStatement.setString(2, userID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error creating shopping list", e);
            return false;
        }
    }

    public static List<ShoppingList> getShoppingListsByUser(String userID) {
        String getShoppingListsSQL = "SELECT shoppingListName, lastUpdated, shoppingListID, userID, createdAt FROM shopping_lists WHERE userID = ? ORDER BY lastUpdated DESC";
        List<ShoppingList> shoppingLists = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseConnectionManager.getConnection().prepareStatement(getShoppingListsSQL)) {
            preparedStatement.setString(1, userID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    ShoppingList shoppingList = new ShoppingList(resultSet);
                    shoppingLists.add(shoppingList);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving shopping lists for userID: " + userID, e);
        }
        return shoppingLists;
    }

    public static boolean updateShoppingListName(int shoppingListID, String newShoppingListName) {
        String updateShoppingListSQL = "UPDATE shopping_lists SET shoppingListName = ? WHERE shoppingListID = ?";
        try (PreparedStatement preparedStatement = DatabaseConnectionManager.getConnection().prepareStatement(updateShoppingListSQL)) {
            preparedStatement.setString(1, newShoppingListName);
            preparedStatement.setInt(2, shoppingListID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error updating shopping list name for shoppingListID: " + shoppingListID, e);
            return false;
        }
    }

    public static boolean deleteShoppingList(int shoppingListID) {
        String deleteShoppingListSQL = "DELETE FROM shopping_lists WHERE shoppingListID = ?";
        try (PreparedStatement preparedStatement = DatabaseConnectionManager.getConnection().prepareStatement(deleteShoppingListSQL)) {
            preparedStatement.setInt(1, shoppingListID);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error deleting shopping list with shoppingListID: " + shoppingListID, e);
            return false;
        }
    }

    public static ShoppingList getShoppingListWithIngredients(int shoppingListID) {
        String sql = "SELECT sl.shoppingListID, sl.shoppingListName, sl.userID, sl.createdAt, sl.lastUpdated, " +
                "si.ingredientID, si.quantity, si.unit, i.ingredientName " +
                "FROM shopping_lists sl " +
                "JOIN shopping_list_ingredients si ON sl.shoppingListID = si.shoppingListID " +
                "JOIN ingredients i ON si.ingredientID = i.ingredientID " +
                "WHERE sl.shoppingListID = ?";

        ShoppingList shoppingList = null;
        try {
            Connection conn = DatabaseConnectionManager.getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, shoppingListID);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        if (shoppingList == null) {
                            String shoppingListName = rs.getString("shoppingListName");
                            String userID = rs.getString("userID");
                            Timestamp createdAt = rs.getTimestamp("createdAt");
                            Timestamp lastUpdated = rs.getTimestamp("lastUpdated");
                            shoppingList = new ShoppingList(shoppingListID, userID, shoppingListName, createdAt, lastUpdated);
                            shoppingList.setShoppingListIngredients(new ArrayList<>());
                        }
                        int ingredientID = rs.getInt("ingredientID");
                        int quantity = rs.getInt("quantity");
                        String unit = rs.getString("unit");
                        String ingredientName = rs.getString("ingredientName");
                        ShoppingListIngredient ingredient = new ShoppingListIngredient(shoppingListID, ingredientID, quantity, unit, ingredientName);
                        shoppingList.getShoppingListIngredients().add(ingredient);
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving shopping list with ingredients for shoppingListID: " + shoppingListID, e);
        }
        return shoppingList;
    }
}