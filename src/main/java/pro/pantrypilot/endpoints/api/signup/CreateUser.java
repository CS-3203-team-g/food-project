package pro.pantrypilot.endpoints.api.signup;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import pro.pantrypilot.db.classes.user.User;
import pro.pantrypilot.db.classes.user.UsersDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class CreateUser implements HttpHandler {

    // POJO to represent the sign-up request payload
    private static class SignupRequest {
        String username;
        String email;
        String password;

        // Optionally, add getters/setters if needed.
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

        // Initialize Gson and attempt to parse the JSON payload into a SignupRequest object
        Gson gson = new Gson();
        SignupRequest signupRequest;
        try {
            signupRequest = gson.fromJson(requestBody, SignupRequest.class);
        } catch (JsonSyntaxException e) {
            // If parsing fails, return a 400 Bad Request response with an error message.
            String errorResponse = "{\"message\": \"Invalid JSON format\"}";
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            byte[] errorBytes = errorResponse.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(400, errorBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errorBytes);
            }
            return;
        }

        User newUser = new User(signupRequest.username, signupRequest.email, signupRequest.password);
        boolean success = UsersDatabase.createUser(newUser);

        if (!success) {
            // If user creation fails, return a 500 Internal Server Error response.
            exchange.sendResponseHeaders(500, -1);
            return;
        }

        // Here you would implement your logic to create the user in your system.
        // For this example, we'll assume user creation is successful.
        String jsonResponse = "{\"message\": \"User created successfully\"}";
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        byte[] responseBytes = jsonResponse.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, responseBytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
