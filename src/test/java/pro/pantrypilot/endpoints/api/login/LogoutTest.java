package pro.pantrypilot.endpoints.api.login;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pro.pantrypilot.db.classes.session.SessionsDatabase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutTest {

    private Logout logoutHandler;

    @Mock
    private HttpExchange exchange;

    @Mock
    private Headers headers;

    @BeforeEach
    void setUp() {
        logoutHandler = new Logout();
    }

    @Test
    void testInvalidRequestMethod() throws IOException {
        when(exchange.getRequestMethod()).thenReturn("GET");
        logoutHandler.handle(exchange);
        verify(exchange, times(1)).sendResponseHeaders(405, -1);
    }

    @Test
    void testValidLogoutRequest() throws IOException {
        String sessionID = "test-session-id";
        String requestBody = "{\"sessionID\": \"" + sessionID + "\"}";
        ByteArrayInputStream requestInputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();

        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(requestInputStream);
        when(exchange.getResponseBody()).thenReturn(responseOutputStream);
        when(exchange.getResponseHeaders()).thenReturn(headers);

        try (MockedStatic<SessionsDatabase> mockedStaticDB = Mockito.mockStatic(SessionsDatabase.class)) {
            mockedStaticDB.when(() -> SessionsDatabase.deleteSession(sessionID)).thenReturn(true);
            logoutHandler.handle(exchange);
        }

        verify(headers, times(1)).set(eq("Content-Type"), eq("application/json; charset=UTF-8"));
        verify(exchange, times(1)).sendResponseHeaders(200, 0);
        String response = responseOutputStream.toString();
        assert(response.contains("Logout successful"));
    }

    @Test
    void testInvalidSessionID() throws IOException {
        String sessionID = "invalid-session-id";
        String requestBody = "{\"sessionID\": \"" + sessionID + "\"}";
        ByteArrayInputStream requestInputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();

        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(requestInputStream);
        when(exchange.getResponseBody()).thenReturn(responseOutputStream);
        when(exchange.getResponseHeaders()).thenReturn(headers);

        try (MockedStatic<SessionsDatabase> mockedStaticDB = Mockito.mockStatic(SessionsDatabase.class)) {
            mockedStaticDB.when(() -> SessionsDatabase.deleteSession(sessionID)).thenReturn(false);
            logoutHandler.handle(exchange);
        }

        verify(headers, times(1)).set(eq("Content-Type"), eq("application/json; charset=UTF-8"));
        verify(exchange, times(1)).sendResponseHeaders(400, 0);
        String response = responseOutputStream.toString();
        assert(response.contains("Invalid session or session already expired"));
    }

    @Test
    void testMissingSessionID() throws IOException {
        String requestBody = "{}";
        ByteArrayInputStream requestInputStream = new ByteArrayInputStream(requestBody.getBytes(StandardCharsets.UTF_8));
        ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();

        when(exchange.getRequestMethod()).thenReturn("POST");
        when(exchange.getRequestBody()).thenReturn(requestInputStream);
        when(exchange.getResponseBody()).thenReturn(responseOutputStream);
        when(exchange.getResponseHeaders()).thenReturn(headers);

        logoutHandler.handle(exchange);

        verify(headers, times(1)).set(eq("Content-Type"), eq("application/json; charset=UTF-8"));
        verify(exchange, times(1)).sendResponseHeaders(400, 0);
        String response = responseOutputStream.toString();
        assert(response.contains("Missing sessionID"));
    }
}