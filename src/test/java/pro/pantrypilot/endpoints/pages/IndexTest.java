package pro.pantrypilot.endpoints.pages;

import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

class IndexTest {

    @Test
    void handle() throws IOException {
        // Load configuration and get the server port.
        int port = Integer.parseInt(System.getenv("SERVER_PORT"));

        // Create and start an HTTP server on the configured port.
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new Index());
        server.start();

        try {
            // Make a web request to localhost:port/
            URL url = new URL("http://localhost:" + port + "/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            // Ensure that the HTTP response code is 200 OK.
            int responseCode = connection.getResponseCode();
            assertEquals(200, responseCode, "Expected HTTP response code 200");

            // Read the response from the connection.
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            String responseBody = response.toString();

            // Assert that the response body is not null or empty.
            assertNotNull(responseBody, "Response body should not be null");
            assertFalse(responseBody.isEmpty(), "Response body should not be empty");

            // Assert that the response starts with "<!DOCTYPE html>"
            // (trim() is used in case there is any leading whitespace)
            assertTrue(responseBody.trim().startsWith("<!DOCTYPE html>"),
                    "Response should start with \"<!DOCTYPE html>\"");

            // Asset that the response ends with </html>
            // (trim() is used in case there is any trailing whitespace)
            assertTrue(responseBody.trim().endsWith("</html>"),
                    "Response should end with \"</html>\"");

        } finally {
            // Stop the server regardless of test outcome.
            server.stop(0);
        }
    }
}
