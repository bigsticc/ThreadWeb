package com.supernova.http;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpParser {
    private static final String LINE_SEPARATOR = "\r\n";

    public static HttpRequest parseRequest(ByteBuffer buffer) {
        Charset charset = Charset.forName("UTF-8");
        String requestString = charset.decode(buffer).toString();
    
        String[] parts = requestString.split("\r\n\r\n");

        String[] head = parts[0].split(LINE_SEPARATOR);
        String body = (parts.length > 1) ? parts[1] : null;

        String[] requestLine = head[0].split(" ");
        String method = requestLine[0];
        String uri = requestLine[1];
        String version = requestLine[2];
    
        Map<String, String> headers = new HashMap<>();
        String[] headerLines = Arrays.copyOfRange(head, 1, head.length);
        for (String headerLine : headerLines) {
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }
    
        HttpRequest request = new HttpRequest(method, URI.create(uri), version, headers, body);
    
        return request;
    }

    public static HttpResponse parseResponse(ByteBuffer buffer) {
        Charset charset = Charset.forName("UTF-8");
        String requestString = charset.decode(buffer).toString();

        String[] head = requestString.split("\r\n\r\n")[0].split(LINE_SEPARATOR);
        String body = requestString.split("\r\n\r\n")[1];

        String[] requestLine = head[0].split(" ");
        String version = requestLine[0];
        HttpStatus status = HttpStatus.getStatus(Integer.parseInt(requestLine[1]));

        Map<String, String> headers = new HashMap<>();
        String[] headerLines = Arrays.copyOfRange(head, 1, head.length);
        for (String headerLine : headerLines) {
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        HttpResponse response = new HttpResponse(version, status, headers, body);

        return response;
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
