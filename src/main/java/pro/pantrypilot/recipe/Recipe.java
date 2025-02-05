package pro.pantrypilot.recipe;

import pro.pantrypilot.db.classes.ingredients.RecipeIngredient;

import java.util.ArrayList;

public class Recipe {

    private final int recipeID;
    private final String title;
    private final String thumbnailUrl;
    private final String instructions;
    private final ArrayList<RecipeIngredient> ingredients;
    private final float rating;


    public Recipe(int recipeID, String title, String thumbnailUrl, String instructions, ArrayList<RecipeIngredient> ingredients, float rating) {
        this.recipeID = recipeID;
        this.title = title;
        this.thumbnailUrl = thumbnailUrl;
        this.instructions = instructions;
        this.ingredients = ingredients;
        this.rating = rating;
    }
}
