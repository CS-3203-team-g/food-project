package pro.pantrypilot.endpoints.api.shoppingLists;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.db.classes.session.Session;
import pro.pantrypilot.db.classes.session.SessionsDatabase;
import pro.pantrypilot.db.classes.shoppingList.ShoppingListsDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CreateShoppingList implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(CreateShoppingList.class);

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
        CreateListRequest createListRequest = new Gson().fromJson(requestBody, CreateListRequest.class);

        // Validate sessionID
        Session session = SessionsDatabase.getSession(createListRequest.sessionID);
        if (session == null) {
            logger.debug("Invalid sessionID: {}", createListRequest.sessionID);
            exchange.sendResponseHeaders(401, -1); // Unauthorized
            return;
        }

        // Create the shopping list
        boolean success = ShoppingListsDatabase.createShoppingList(createListRequest.listName, session.getUserID());
        if (success) {
            exchange.sendResponseHeaders(200, -1); // OK
        } else {
            exchange.sendResponseHeaders(400, -1); // Bad Request
        }
    }

    // Inner class to represent the request payload
    private static class CreateListRequest {
        String listName;
        String sessionID;
    }
}