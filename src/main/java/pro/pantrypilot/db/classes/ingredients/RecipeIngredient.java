package pro.pantrypilot.db.classes.ingredients;

public class RecipeIngredient {

    private final int recipeID;
    private final int ingredientID;
    private final int quantity;
    private final String unit;


    public RecipeIngredient(int recipeID, int ingredientID, int quantity, String unit) {
        this.recipeID = recipeID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
        this.unit = unit;
    }


    public int getRecipeID() {
        return recipeID;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }
}
