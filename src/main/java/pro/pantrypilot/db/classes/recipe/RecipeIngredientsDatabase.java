package pro.pantrypilot.db.classes.recipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.db.DatabaseConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RecipeIngredientsDatabase {

    private static final Logger logger = LoggerFactory.getLogger(RecipeIngredientsDatabase.class);

    public static void initializeRecipeIngredientsDatabase() {
        String createRecipeIngredientsTableSQL = "CREATE TABLE IF NOT EXISTS recipe_ingredients (\n"
                + "    recipeID INT NOT NULL,\n"
                + "    ingredientID INT NOT NULL,\n"
                + "    quantity INT NOT NULL,\n"
                + "    unit VARCHAR(50),\n"
                + "    PRIMARY KEY (recipeID, ingredientID),\n"
                + "    FOREIGN KEY (recipeID) REFERENCES recipes(recipeID) ON DELETE CASCADE,\n"
                + "    FOREIGN KEY (ingredientID) REFERENCES ingredients(ingredientID) ON DELETE CASCADE\n"
                + ");";
        try {
            Statement stmt = DatabaseConnectionManager.getConnection().createStatement();
            stmt.execute(createRecipeIngredientsTableSQL);
        } catch (SQLException e) {
            logger.error("Error creating recipe_ingredients table", e);
            throw new RuntimeException(e);
        }
    }
}