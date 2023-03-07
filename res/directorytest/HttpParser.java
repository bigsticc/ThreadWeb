package res.directorytest;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpParser {
    public static HttpRequest parseRequest(ByteBuffer buffer) {
        Charset charset = Charset.forName("UTF-8");
        String requestString = charset.decode(buffer).toString();
    
        String[] parts = requestString.split("\\r?\\n\\r?\\n", 2);
        String[] lines = parts[0].split("\\r?\\n");
        String headers = parts.length > 1 ? parts[1] : "";
    
        String[] requestLineParts = lines[0].split(" ");
        String method = requestLineParts[0];
        String uri = requestLineParts[1];
        String httpVersion = requestLineParts[2];
    
        Map<String, String> headersMap = new HashMap<>();
        String[] headerLines = headers.split("\\r?\\n");
        for (String headerLine : headerLines) {
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                headersMap.put(headerParts[0], headerParts[1]);
            }
        }
    
        String body = parts.length > 1 ? parts[1] : "";
    
        HttpRequest request = new HttpRequest();
        request.setMethod(method);
        request.setUri(URI.create(uri));
        request.setVersion(httpVersion);
        request.setHeaders(headersMap);
        request.setBody(body);
    
        return request;
    }

    public static void serializeRequest(ByteBuffer buffer, HttpRequest request) {
        String requestLine = request.getMethod() + " " + request.getUri().getPath() + " " + request.getVersion();
        
        String headers = "";
        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            headers += header.getKey() + ": " + header.getValue() + "\r\n";
        }
        
        String body = request.getBody();
        String requestString = requestLine + "\r\n" + headers + "\r\n" + body;
        
        buffer.put(requestString.getBytes());
        buffer.flip();
    }
    
    public static void serializeResponse(ByteBuffer buffer, HttpResponse response) {
        String statusLine = response.getVersion() + " " + response.getStatus().statusCode + " " + response.getStatus().reasonPhrase;
        
        String headers = "";
        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            headers += header.getKey() + ": " + header.getValue() + "\r\n";
        }
        
        String body = response.getBody();
        String responseString = statusLine + "\r\n" + headers + "\r\n" + body;
        
        buffer.put(responseString.getBytes());
        buffer.flip();
    }
}
