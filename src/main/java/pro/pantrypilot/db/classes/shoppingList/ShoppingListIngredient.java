package pro.pantrypilot.db.classes.shoppingList;

public class ShoppingListIngredient {

    private final int shoppingListID;
    private final int ingredientID;
    private final int quantity;
    private final String unit;
    private String ingredientName;

    public ShoppingListIngredient(int shoppingListID, int ingredientID, int quantity, String unit) {
        this.shoppingListID = shoppingListID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
        this.unit = unit;
    }

    public ShoppingListIngredient(int shoppingListID, int ingredientID, int quantity, String unit, String ingredientName) {
        this.shoppingListID = shoppingListID;
        this.ingredientID = ingredientID;
        this.quantity = quantity;
        this.unit = unit;
        this.ingredientName = ingredientName;
    }

    public int getShoppingListID() {
        return shoppingListID;
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

    public String getIngredientName() {
        return ingredientName;
    }

    @Override
    public String toString() {
        return "ShoppingListIngredient{" +
                "shoppingListID=" + shoppingListID +
                ", ingredientID=" + ingredientID +
                ", quantity=" + quantity +
                ", unit='" + unit + '\'' +
                ", ingredientName='" + ingredientName + '\'' +
                '}';
    }
}