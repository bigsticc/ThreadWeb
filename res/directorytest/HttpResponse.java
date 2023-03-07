package res.directorytest;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    String version;
    HttpStatus status;
    Map<String, String> headers = new HashMap<>();
    String body;

    public HttpResponse() {}
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

    public HttpResponse header(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public String toString() {
        StringBuilder response = new StringBuilder();
        response.append(version).append(" ").append(status.getStatus()).append(" ").append(status.getReasonPhrase()).append("\r\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            response.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        response.append("\r\n");
        response.append(body);
        return response.toString();
    }
}
