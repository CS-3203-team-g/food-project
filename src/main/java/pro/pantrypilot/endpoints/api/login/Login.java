package pro.pantrypilot.endpoints.api.login;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pro.pantrypilot.db.classes.session.Session;
import pro.pantrypilot.db.classes.session.SessionsDatabase;
import pro.pantrypilot.db.classes.user.User;
import pro.pantrypilot.db.classes.user.UsersDatabase;
import pro.pantrypilot.helpers.PasswordHasher;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Login implements HttpHandler {

    // POJO to represent the login request payload
    private static class LoginRequest {
        String username;
        String password;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Only accept POST requests
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
            return;
        }

        // Read the JSON payload from the request body
        String requestBody;
        try (InputStream is = exchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            requestBody = reader.lines().collect(Collectors.joining("\n"));
        }

        // Initialize Gson and attempt to parse the JSON payload into a LoginRequest object
        Gson gson = new Gson();
        LoginRequest loginRequest;
        try {
            loginRequest = gson.fromJson(requestBody, LoginRequest.class);
        } catch (JsonSyntaxException e) {
            sendResponse(exchange, 400, "{\"message\": \"Invalid JSON format\"}");
            return;
        }

        // Retrieve the user from the database
        User user = UsersDatabase.getUser(loginRequest.username);
        if (!user.isActive()) {
            sendResponse(exchange, 401, "{\"message\": \"Invalid username or password\"}");
            return;
        }

        // Verify the password
        if (!PasswordHasher.verifyPassword(loginRequest.password, user.getSalt(), user.getPasswordHash())) {
            sendResponse(exchange, 401, "{\"message\": \"Invalid username or password\"}");
            return;
        }

        Session session = new Session(user.getUserID(), exchange.getRemoteAddress().getAddress().getHostAddress());
        session = SessionsDatabase.createSession(session);

//        System.out.println("S ID:" + session.getSessionID());

        if(session == null || session.getSessionID() == null) {
            sendResponse(exchange, 500, "{\"message\": \"Internal server error, null session ID\"}");
            return;
        }

        // Login successful
        sendResponse(exchange, 200, "{\"message\": \"Login successful\",\"sessionID\": \"" + session.getSessionID() + "\"}");
    }


    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
