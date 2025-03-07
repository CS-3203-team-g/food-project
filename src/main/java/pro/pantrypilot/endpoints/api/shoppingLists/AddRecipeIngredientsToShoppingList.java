package pro.pantrypilot.endpoints.api.shoppingLists;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.db.classes.session.Session;
import pro.pantrypilot.db.classes.session.SessionsDatabase;
import pro.pantrypilot.db.classes.shoppingList.ShoppingList;
import pro.pantrypilot.db.classes.shoppingList.ShoppingListIngredientsDatabase;
import pro.pantrypilot.db.classes.shoppingList.ShoppingListsDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class AddRecipeIngredientsToShoppingList implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(AddRecipeIngredientsToShoppingList.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            logger.debug("Invalid request method: {}", exchange.getRequestMethod());
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        // Read the request body
        String requestBody;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            requestBody = reader.lines().collect(Collectors.joining("\n"));
        }

        // Parse the request body
        addRecipeIngredientsToShoppingListRequest addIngredientRequest = new Gson().fromJson(requestBody, addRecipeIngredientsToShoppingListRequest.class);

        // Validate sessionID
        Session session = SessionsDatabase.getSession(addIngredientRequest.sessionID);
        if (session == null) {
            logger.debug("Invalid sessionID: {}", addIngredientRequest.sessionID);
            exchange.sendResponseHeaders(403, -1); // Forbidden
            return;
        }

        // Check if the user owns the shopping list
        ShoppingList shoppingList = ShoppingListsDatabase.getShoppingListWithoutIngredients(addIngredientRequest.shoppingListID);
        if (shoppingList == null) {
            logger.debug("Shopping list not found for ID: {}", addIngredientRequest.shoppingListID);
            exchange.sendResponseHeaders(404, -1); // Not Found
            return;
        }

        if (!shoppingList.getUserID().equals(session.getUserID())) {
            logger.debug("User does not own the shopping list: {}", addIngredientRequest.shoppingListID);
            exchange.sendResponseHeaders(403, -1); // Forbidden
            return;
        }

        // Add the recipe ingredients to the shopping list
        boolean success = ShoppingListIngredientsDatabase.addIngredientsToShoppingList(addIngredientRequest.recipeID, addIngredientRequest.shoppingListID);

        if (success) {
            exchange.sendResponseHeaders(200, -1); // OK
        } else {
            exchange.sendResponseHeaders(400, -1); // Bad Request
        }
    }

    // Inner class to represent the request payload
    private static class addRecipeIngredientsToShoppingListRequest {
        int shoppingListID;
        int recipeID;
        String sessionID;
    }
}
