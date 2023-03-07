package test.com.supernova;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import com.supernova.server.SerialServer;

public class SerialServerTest {
    @Test
    public void serverTest() throws IOException {
        SerialServer server = new SerialServer(80);
        Thread serverThread = new Thread(server);
        serverThread.start();

        // Connect to the server and make a request
        URL url = new URL("http://localhost/smallfile.txt");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        // Check the response
        assertEquals(200, connection.getResponseCode());
        assertTrue(connection.getHeaderField("Content-Type").startsWith("text/plain"));
        InputStream inputStream = connection.getInputStream();
        byte[] responseBytes = inputStream.readAllBytes();
        String responseString = new String(responseBytes, StandardCharsets.UTF_8);
        System.out.println(responseString);

        serverThread.interrupt();
    }
}
