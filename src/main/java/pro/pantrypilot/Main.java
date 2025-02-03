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

//        static webpages
        server.createContext("/", new pro.pantrypilot.endpoints.pages.Index());
        server.createContext("/login", new pro.pantrypilot.endpoints.pages.login.Login());
        server.createContext("/signup", new pro.pantrypilot.endpoints.pages.signup.SignUp());

//        API endpoints
        server.createContext("/api/createUser", new pro.pantrypilot.endpoints.api.signup.CreateUser());
        server.createContext("/api/login", new pro.pantrypilot.endpoints.api.login.Login());

        server.start();
    }

}
