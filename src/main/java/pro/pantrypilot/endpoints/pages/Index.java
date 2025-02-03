package pro.pantrypilot.endpoints.pages;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class Index implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        StringBuilder sb = new StringBuilder();

        try{
//            String content = new String(Files.readAllBytes(Paths.get("/home/jlw1808/crtapi/src/main/java/index.html")));
//            sb.append(content);
            sb.append("Hello World!");
        }
        catch (Exception e){
            e.printStackTrace();
        }

        String response = sb.toString();

        // send file to user
        exchange.sendResponseHeaders(200, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }
}