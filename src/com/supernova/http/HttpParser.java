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
        // Convert the raw bytes into a string of text
        Charset charset = Charset.forName("UTF-8");
        String requestString = charset.decode(buffer).toString();
    
        // Split the head and body of the request
        String[] parts = requestString.split("\r\n\r\n");

        // Get the head from the parts and split it into lines, and get the body if there is one
        String[] head = parts[0].split(LINE_SEPARATOR);
        String body = (parts.length > 1) ? parts[1] : null;

        // Split the first line of the head by spaces, and extracting the HTTP Method, Path, and Version from it
        String[] requestLine = head[0].split(" ");
        String method = requestLine[0];
        String uri = requestLine[1];
        String version = requestLine[2];
    
        // Parse the head into a table, which can be used for fast lookups 
        Map<String, String> headers = new HashMap<>();
        String[] headerLines = Arrays.copyOfRange(head, 1, head.length);
        for (String headerLine : headerLines) {
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }
    
        // Create an HttpRequest object using the components
        HttpRequest request = new HttpRequest(method, URI.create(uri), version, headers, body);
    
        return request;
    }

    public static HttpResponse parseResponse(ByteBuffer buffer) {
        // Convert the raw bytes into a string of text
        Charset charset = Charset.forName("UTF-8");
        String requestString = charset.decode(buffer).toString();

        // Split the head and body of the response
        String[] parts = requestString.split("\r\n\r\n");
    
        // Get the head from the parts and split it into lines, and get the body if there is one
        String[] head = parts[0].split(LINE_SEPARATOR);
        String body = (parts.length > 1) ? parts[1] : null;

        // Split the first line of the head by spaces, extract the Version, and look up the Status Code 
        String[] requestLine = head[0].split(" ");
        String version = requestLine[0];
        HttpStatus status = HttpStatus.getStatus(Integer.parseInt(requestLine[1]));

        // Parse the head into a table, which can be used for fast lookups 
        Map<String, String> headers = new HashMap<>();
        String[] headerLines = Arrays.copyOfRange(head, 1, head.length);
        for (String headerLine : headerLines) {
            String[] headerParts = headerLine.split(": ", 2);
            if (headerParts.length == 2) {
                headers.put(headerParts[0], headerParts[1]);
            }
        }

        // Create an HttpRequest object using the components
        HttpResponse response = new HttpResponse(version, status, headers, body);

        return response;
    }

    public static void serializeRequest(ByteBuffer buffer, HttpRequest request) {
        // Substitute the Method, Path, and Version into a space separated string
        String requestLine = request.getMethod() + " " + request.getUri().getPath() + " " + request.getVersion();
        
        // For each header in the header table, add a colon between the key and value, add a newline to the end, and add it to a string of text
        String headers = "";
        for (Map.Entry<String, String> header : request.getHeaders().entrySet()) {
            headers += header.getKey() + ": " + header.getValue() + "\r\n";
        }
        
        // Get the body from the request, and substitute the request line, header string, and body into a newline separated string
        String body = request.getBody();
        String requestString = requestLine + "\r\n" + headers + "\r\n" + body;
        
        // Encode the string into bytes, and place it in the provided ByteBuffer
        buffer.put(requestString.getBytes());
        buffer.flip();
    }
    
    public static void serializeResponse(ByteBuffer buffer, HttpResponse response) {
        // Substitute the Version, Status Code, and Status Reason Phrase into a space separated string
        String statusLine = response.getVersion() + " " + response.getStatus().statusCode + " " + response.getStatus().reasonPhrase;
        
        // For each header into the header table, add a colon between the key and value, add a newline to the end, and add it to a string of text
        String headers = "";
        for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
            headers += header.getKey() + ": " + header.getValue() + "\r\n";
        }
        
        // Get the body from the request, and substitute the status line, header string, and body into a newline separated string
        String body = response.getBody();
        String responseString = statusLine + "\r\n" + headers + "\r\n" + body;
        
        // Encode the string into bytes, and place it in the provided ByteBuffer
        buffer.put(responseString.getBytes());
        buffer.flip();
    }
}
