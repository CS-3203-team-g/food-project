package pro.pantrypilot.endpoints.api.recipes;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.db.classes.recipe.Recipe;
import pro.pantrypilot.db.classes.recipe.RecipeDatabase;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class GetRecipeWithIngredients implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetRecipeWithIngredients.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            logger.debug("Invalid request method: {}", exchange.getRequestMethod());
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        String query = exchange.getRequestURI().getQuery();
        int recipeID;
        try {
            recipeID = Integer.parseInt(query.split("=")[1]);
        } catch (Exception e) {
            logger.error("Invalid recipe ID format: {}", query);
            exchange.sendResponseHeaders(400, -1); // Bad Request
            return;
        }

        Recipe recipe = RecipeDatabase.getRecipeWithIngredients(recipeID);
        if (recipe == null) {
            logger.debug("Recipe not found for ID: {}", recipeID);
            exchange.sendResponseHeaders(404, -1); // Not Found
            return;
        }

        Gson gson = new Gson();
        String jsonResponse = gson.toJson(recipe);

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}