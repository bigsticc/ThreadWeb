package test.com.supernova;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

import com.supernova.http.HttpParser;
import com.supernova.http.HttpRequest;
import com.supernova.http.HttpResponse;
import com.supernova.http.HttpStatus;

public class ParserTest {
    @Test
    void testIO() {
        HttpRequest req = new HttpRequest();
        req.setMethod("GET");
        req.setUri(URI.create("/"));
        req.setVersion("HTTP/1.1");
        req.header("Accept", "application/json");

        ByteBuffer buff = ByteBuffer.allocate(1024);
        HttpParser.serializeRequest(buff, req);
        HttpRequest req2 = HttpParser.parseRequest(buff);

        HttpResponse res = new HttpResponse();
        res.setVersion("HTTP/1.1");
        res.setStatus(HttpStatus.OK);
        res.header("Content-Encoding", "UTF-8");
        res.setBody("Hiiiiiiii :)");

        ByteBuffer buff2 = ByteBuffer.allocate(1024);
        HttpParser.serializeResponse(buff2, res);
        HttpResponse res2 = HttpParser.parseResponse(buff2);

        System.out.println("Request IO (should be equal):");
        System.out.println(req.toString());
        System.out.println(req2.toString());
        assertEquals(req.toString(), req2.toString());
        
        System.out.println();
        
        System.out.println("Response IO (should be equal):");
        System.out.println(res.toString());
        System.out.println(res2.toString());
        assertEquals(res.toString(), res2.toString());

    }
}
