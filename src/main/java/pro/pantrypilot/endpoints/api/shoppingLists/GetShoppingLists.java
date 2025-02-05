package pro.pantrypilot.endpoints.api.shoppingLists;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.db.classes.session.Session;
import pro.pantrypilot.db.classes.session.SessionsDatabase;
import pro.pantrypilot.db.classes.shoppingList.ShoppingList;
import pro.pantrypilot.db.classes.shoppingList.ShoppingListsDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class GetShoppingLists implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetShoppingLists.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            logger.debug("Invalid request method: {}", exchange.getRequestMethod());
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        // Read the sessionID from the request body
        String requestBody;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            requestBody = reader.lines().collect(Collectors.joining("\n"));
        }

        String sessionID = new Gson().fromJson(requestBody, SessionRequest.class).sessionID;

        // Validate sessionID and get userID
        Session session = SessionsDatabase.getSession(sessionID);
        if (session == null) {
            logger.debug("Invalid sessionID: {}", sessionID);
            exchange.sendResponseHeaders(401, -1); // Unauthorized
            return;
        }

        String userID = session.getUserID();

        // Fetch shopping lists for the user
        List<ShoppingList> shoppingLists = ShoppingListsDatabase.getShoppingListsByUser(userID);

        // Convert shopping lists to JSON
        String jsonResponse = new Gson().toJson(shoppingLists);

        // Send response
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }

    // Inner class to represent the request payload
    private static class SessionRequest {
        String sessionID;
    }
}