package pro.pantrypilot.db.classes.recipe;

import org.junit.jupiter.api.Test;
import pro.pantrypilot.db.DatabaseConnectionManager;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;
class RecipeDatabaseTest {

    private void initTest() {
        IngredientsDatabase.initializeIngredientsDatabase();

        String clearRecipeTableSQL = "DELETE FROM pantry_pilot.recipes;";
        String clearIngredientTableSQL = "DELETE FROM pantry_pilot.ingredients;";
        String clearRecipeIngredientTableSQL = "DELETE FROM pantry_pilot.recipe_ingredients;";

        String insertRecipeSQL = "INSERT INTO pantry_pilot.recipes (recipeID, title, instructions, thumbnailUrl, rating) VALUES (1, 'Tiramisu Blondies', 'Preheat the oven to 350 degrees...', 'https://www.allrecipes.com/thmb/7RTcK0893zgtVf7Gg6LWmew6g_U=/750x0/filters:no_upscale():max_bytes(150000):strip_icc()/8781917_Tiramisu-Blondies_Kim-Shupe_4x3-38045a8e3d5944b6abe149ca8b02d16d.jpg', 0);";
        String insertIngredientSQL = "INSERT INTO pantry_pilot.ingredients (ingredientID, ingredientName) VALUES (1, 'semisweet chocolate chips');";
        String insertRecipeIngredientSQL = "INSERT INTO pantry_pilot.recipe_ingredients (recipeID, ingredientID, quantity, unit) VALUES (1, 1, 1, 'cup');";

        DatabaseConnectionManager.initializeDatabase();
        try {
            Connection conn = DatabaseConnectionManager.getConnection();
            conn.createStatement().execute(clearRecipeTableSQL);
            conn.createStatement().execute(clearIngredientTableSQL);
            conn.createStatement().execute(clearRecipeIngredientTableSQL);
            conn.createStatement().execute(insertRecipeSQL);
            conn.createStatement().execute(insertIngredientSQL);
            conn.createStatement().execute(insertRecipeIngredientSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void initializeRecipeDatabase() {
        RecipeDatabase.initializeRecipeDatabase();
        assertTrue(true);
    }

    @Test
    void getRecipesNoIngredients() {
        RecipeDatabase.initializeRecipeDatabase();
        initTest();
        assertEquals("Tiramisu Blondies", RecipeDatabase.getRecipesNoIngredients().get(0).getTitle());
    }

    @Test
    void getRecipesWithIngredients() {
        RecipeDatabase.initializeRecipeDatabase();
        initTest();
        assertEquals("Tiramisu Blondies", RecipeDatabase.getRecipesWithIngredients().get(0).getTitle());
        assertEquals("semisweet chocolate chips", RecipeDatabase.getRecipesWithIngredients().get(0).getIngredients().get(0).getIngredientName());
        assertEquals(1, RecipeDatabase.getRecipesWithIngredients().get(0).getIngredients().get(0).getQuantity());
        assertEquals("cup", RecipeDatabase.getRecipesWithIngredients().get(0).getIngredients().get(0).getUnit());
        assertEquals("Preheat the oven to 350 degrees...", RecipeDatabase.getRecipesWithIngredients().get(0).getInstructions());
    }

    @Test
    void getRecipeWithIngredients() {
        RecipeDatabase.initializeRecipeDatabase();
        initTest();
        assertEquals("Tiramisu Blondies", RecipeDatabase.getRecipeWithIngredients(1).getTitle());
        assertEquals("semisweet chocolate chips", RecipeDatabase.getRecipeWithIngredients(1).getIngredients().get(0).getIngredientName());
        assertEquals(1, RecipeDatabase.getRecipeWithIngredients(1).getIngredients().get(0).getQuantity());
        assertEquals("cup", RecipeDatabase.getRecipeWithIngredients(1).getIngredients().get(0).getUnit());
        assertEquals("Preheat the oven to 350 degrees...", RecipeDatabase.getRecipeWithIngredients(1).getInstructions());
    }
}