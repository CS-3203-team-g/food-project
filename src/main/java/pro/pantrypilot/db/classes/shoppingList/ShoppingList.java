package pro.pantrypilot.db.classes.shoppingList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.endpoints.api.shoppingLists.GetShoppingLists;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ShoppingList {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingList.class);

    private int shoppingListID;
    private final String userID;
    private final String name;
    private Timestamp createdAt;
    private Timestamp lastUpdated;
    private ArrayList<ShoppingListIngredient> shoppingListIngredients;


    public ShoppingList(int shoppingListID, String userID, String name, Timestamp createdAt, Timestamp lastUpdated) {
        this.shoppingListID = shoppingListID;
        this.userID = userID;
        this.name = name;
        this.createdAt = createdAt;
        this.lastUpdated = lastUpdated;
    }

    public ShoppingList(String userID, String name) {
        this.userID = userID;
        this.name = name;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.lastUpdated = new Timestamp(System.currentTimeMillis());
    }

    public ShoppingList(ResultSet rs){
        try {
            this.shoppingListID = rs.getInt("shoppingListID");
            this.userID = rs.getString("userID");
            this.name = rs.getString("shoppingListName");
            this.createdAt = rs.getTimestamp("createdAt");
            this.lastUpdated = rs.getTimestamp("lastUpdated");
        } catch (SQLException e) {
            logger.error("Error constructing ShoppingList from ResultSet", e);
            throw new RuntimeException(e);
        }
    }

    public int getShoppingListID() {
        return shoppingListID;
    }

    public void setShoppingListID(int shoppingListID) {
        this.shoppingListID = shoppingListID;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public ArrayList<ShoppingListIngredient> getShoppingListIngredients() {
        return shoppingListIngredients;
    }

    public void setShoppingListIngredients(ArrayList<ShoppingListIngredient> shoppingListIngredients) {
        this.shoppingListIngredients = shoppingListIngredients;
    }
}
