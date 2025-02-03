package pro.pantrypilot.endpoints.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Index implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response;
        try {
            response = new String(Files.readAllBytes(Paths.get("src/main/resources/static/index.html")));
        } catch (IOException e) {
            e.printStackTrace();
            response = "Error: Unable to load index.html";
        }

        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
