package pro.pantrypilot.db.classes.recipe;

public class Ingredient {

    private final int ingredientID;
    private final String ingredientName;

    public Ingredient(int ingredientID, String ingredientName) {
        this.ingredientID = ingredientID;
        this.ingredientName = ingredientName;
    }

    public int getIngredientID() {
        return ingredientID;
    }

    public String getIngredientName() {
        return ingredientName;
    }

}
