package com.supernova.http;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    // String containing the method used to access, which can be GET, POST, PUT, or HEAD
    String method;
    
    // Desired resource's location on the server
    URI uri;
    
    // Version of HTTP being used (HTTP/1.1 in this case)
    String version;

    // Table of attributes used to give specifics about the Request
    Map<String, String> headers = new HashMap<>();
    
    // Optional text content to be delivered to the server
    String body;
    
    // Empty constructor for convenience in creating new objects
    public HttpRequest() {}
    // Constructor for creating requests and filling in all components
    public HttpRequest(String method, URI uri, String version, Map<String, String> headers, String body) {
        this.method = method;
        this.uri = uri;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public void setUri(URI uri) {
        this.uri = uri;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void header(String name, String value) {
        headers.put(name, value);
    }

    // Debug method for printing the components of a request
    @Override
    public String toString() {
        return String.format("HttpRequest [method=%s, uri=%s, version=%s, headers=%s, body=%s]",
            method, uri, version, headers.toString(), body
        );
    }
}
