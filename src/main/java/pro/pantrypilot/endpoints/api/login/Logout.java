package pro.pantrypilot.endpoints.api.login;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.db.classes.session.SessionsDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Logout implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(Logout.class);

    // POJO to represent the logout request payload
    private static class LogoutRequest {
        String sessionID;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Only accept POST requests
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            logger.debug("Invalid request method: {}", exchange.getRequestMethod());
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        // Read the JSON payload from the request body
        String requestBody;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            requestBody = reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            logger.error("Error reading request body", e);
            exchange.sendResponseHeaders(400, -1);
            return;
        }

        // Initialize Gson and attempt to parse the JSON payload into a LogoutRequest object
        Gson gson = new Gson();
        LogoutRequest logoutRequest;
        try {
            logoutRequest = gson.fromJson(requestBody, LogoutRequest.class);
            if (logoutRequest == null || logoutRequest.sessionID == null || logoutRequest.sessionID.isEmpty()) {
                logger.debug("Invalid logout request, missing sessionID");
                sendResponse(exchange, 400, "{\"message\": \"Missing sessionID\"}");
                return;
            }
        } catch (Exception e) {
            logger.error("Error parsing JSON", e);
            sendResponse(exchange, 400, "{\"message\": \"Invalid JSON format\"}");
            return;
        }

        // Delete the session from the database
        boolean success = SessionsDatabase.deleteSession(logoutRequest.sessionID);

        if (!success) {
            logger.debug("Failed to delete session: {}", logoutRequest.sessionID);
            sendResponse(exchange, 400, "{\"message\": \"Invalid session or session already expired\"}");
            return;
        }

        // Set expired cookies to clear them on the client side
        exchange.getResponseHeaders().add("Set-Cookie", 
            "sessionID=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure; HttpOnly; SameSite=Strict");
        exchange.getResponseHeaders().add("Set-Cookie", 
            "username=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure; SameSite=Strict");

        // Return success response
        sendResponse(exchange, 200, "{\"message\": \"Logout successful\"}");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes(StandardCharsets.UTF_8));
        }
    }
}