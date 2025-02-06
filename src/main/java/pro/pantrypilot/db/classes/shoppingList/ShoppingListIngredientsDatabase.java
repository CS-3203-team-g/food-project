package pro.pantrypilot.db.classes.shoppingList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.db.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingListIngredientsDatabase {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingListIngredientsDatabase.class);

    public static void initializeShoppingListIngredientsDatabase() {
        String createShoppingListIngredientsTableSQL = "CREATE TABLE IF NOT EXISTS shopping_list_ingredients (\n"
                + "    shoppingListID INT NOT NULL,\n"
                + "    ingredientID INT NOT NULL,\n"
                + "    quantity INT NOT NULL,\n"
                + "    unit VARCHAR(50),\n"
                + "    PRIMARY KEY (shoppingListID, ingredientID),\n"
                + "    FOREIGN KEY (shoppingListID) REFERENCES shopping_lists(shoppingListID) ON DELETE CASCADE,\n"
                + "    FOREIGN KEY (ingredientID) REFERENCES ingredients(ingredientID) ON DELETE CASCADE\n"
                + ");";
        try {
            Statement stmt = DatabaseConnectionManager.getConnection().createStatement();
            stmt.execute(createShoppingListIngredientsTableSQL);
        } catch (SQLException e) {
            logger.error("Error creating shopping_list_ingredients table", e);
            throw new RuntimeException(e);
        }
    }

    public static boolean addIngredientToShoppingList(int shoppingListID, int ingredientID, int quantity, String unit) {
        String addIngredientSQL = "INSERT INTO shopping_list_ingredients (shoppingListID, ingredientID, quantity, unit) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = DatabaseConnectionManager.getConnection().prepareStatement(addIngredientSQL)) {
            preparedStatement.setInt(1, shoppingListID);
            preparedStatement.setInt(2, ingredientID);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, unit);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error adding ingredient to shopping list", e);
            return false;
        }
    }

    public static List<ShoppingListIngredient> getIngredientsForShoppingList(int shoppingListID) {
        String getIngredientsSQL = "SELECT * FROM shopping_list_ingredients WHERE shoppingListID = ?";
        List<ShoppingListIngredient> ingredients = new ArrayList<>();
        try (PreparedStatement preparedStatement = DatabaseConnectionManager.getConnection().prepareStatement(getIngredientsSQL)) {
            preparedStatement.setInt(1, shoppingListID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int ingredientID = resultSet.getInt("ingredientID");
                    int quantity = resultSet.getInt("quantity");
                    String unit = resultSet.getString("unit");
                    ShoppingListIngredient ingredient = new ShoppingListIngredient(shoppingListID, ingredientID, quantity, unit);
                    ingredients.add(ingredient);
                }
            }
        } catch (SQLException e) {
            logger.error("Error retrieving ingredients for shoppingListID: " + shoppingListID, e);
        }
        return ingredients;
    }

    public static boolean removeIngredientFromShoppingList(int shoppingListID, int ingredientID, int quantity, String unit) {
        String removeIngredientSQL = "DELETE FROM shopping_list_ingredients WHERE shoppingListID = ? AND ingredientID = ? AND quantity = ? AND unit = ? LIMIT 1";
        try (PreparedStatement preparedStatement = DatabaseConnectionManager.getConnection().prepareStatement(removeIngredientSQL)) {
            preparedStatement.setInt(1, shoppingListID);
            preparedStatement.setInt(2, ingredientID);
            preparedStatement.setInt(3, quantity);
            preparedStatement.setString(4, unit);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error("Error removing ingredient from shopping list", e);
            return false;
        }
    }
}