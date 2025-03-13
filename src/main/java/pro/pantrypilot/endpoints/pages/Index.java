package pro.pantrypilot.endpoints.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pro.pantrypilot.db.classes.session.Session;
import pro.pantrypilot.db.classes.session.SessionsDatabase;
import pro.pantrypilot.endpoints.api.signup.CreateUser;
import pro.pantrypilot.helpers.FileHelper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Index implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(Index.class);

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        logger.debug("Handling updated request for index.html");

        // Extract sessionID from cookies
        String sessionID = null;
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");

        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                cookie = cookie.trim();
                if (cookie.startsWith("sessionID=")) {
                    sessionID = cookie.substring("sessionID=".length());
                    break;
                }
            }
        }

        boolean isAdmin = false;

        Session session = SessionsDatabase.getSession(sessionID);
        if (session != null && session.isValid() && session.getUser() != null && session.getUser().isAdmin()) {
            isAdmin = true;

        }

        byte[] responseBytes;
        try {
            // Read the file as bytes and then convert to String with UTF-8 encoding
            byte[] fileBytes = FileHelper.readFile("static/index.html");
            String response = new String(fileBytes, StandardCharsets.UTF_8);
            // add admin to dropdown if the user is an admin
            if(isAdmin){
                response = response.replace("//ADMIN_DROPDOWN_REPLACE", "'<li><a class=\"dropdown-item\" href=\"/admin\">Admin</a></li>' +");
            }
            responseBytes = response.getBytes(StandardCharsets.UTF_8);
        } catch (IOException e) {
            logger.error("Error reading index.html", e);
            responseBytes = "Error: Unable to load index.html".getBytes(StandardCharsets.UTF_8);
        }

        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, responseBytes.length);
        logger.debug("Response sent with {} bytes", responseBytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(responseBytes);
        }
    }
}
