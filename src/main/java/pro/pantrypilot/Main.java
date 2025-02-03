package pro.pantrypilot;

import com.sun.net.httpserver.HttpServer;
import pro.pantrypilot.config.ConfigurationManager;
import pro.pantrypilot.db.DatabaseConnectionManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {

        ConfigurationManager.loadConfig();

        DatabaseConnectionManager.connectToDatabase();
        DatabaseConnectionManager.initializeDatabase();

        HttpServer server = HttpServer.create(new InetSocketAddress(ConfigurationManager.getIntProperty("server.port")), 0);

        server.createContext("/", new pro.pantrypilot.endpoints.pages.Index());

        server.start();
    }

}
