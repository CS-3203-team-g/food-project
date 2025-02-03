package pro.pantrypilot;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {

        HttpServer server = HttpServer.create(new InetSocketAddress(25565), 0);

        server.createContext("/", new pro.pantrypilot.endpoints.pages.Index());

        server.start();
    }

}
