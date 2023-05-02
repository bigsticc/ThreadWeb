package com.supernova.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    // Version of the HTTP Protocol being used (always HTTP/1.1 in this case)
    String version;
    
    // Numeric code representing the outcome of the request
    HttpStatus status;
    
    // Table of attributes giving specifics about the Response
    Map<String, String> headers = new HashMap<>();
    
    // Optional text content to be delivered back to the client
    String body;

    // Empty constructor for convenience
    public HttpResponse() {}
    // Constructor for filling in components of the Response
    protected HttpResponse(String version, HttpStatus status, Map<String, String> headers, String body) {
        this.version = version;
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    public String getVersion() {
        return version;
    }
    public HttpStatus getStatus() {
        return status;
    }
    public Map<String, String> getHeaders() {
        return headers;
    }
    public String getBody() {
        return body;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public void setStatus(HttpStatus status) {
        this.status = status;
    }
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public void header(String key, String value) {
        headers.put(key, value);
    }
    
    // Debug methods for showing the components of the Response
    @Override
    public String toString() {
        return String.format("HttpResponse [version=%s, status=%d:%s, headers=%s, body=%s]",
            version, status.getStatus(), status.getReasonPhrase(), headers, body
        );
    }
    


}
