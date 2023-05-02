package com.supernova.server;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import com.supernova.http.HttpRequest;

import com.supernova.MimeType;
import com.supernova.http.HttpParser;
import com.supernova.http.HttpResponse;
import com.supernova.http.HttpStatus;

public interface Handler {
    // Method to process a client's request
    default void process(ByteBuffer in, ByteBuffer out) {
        // Parse the request from bytes and create a response template
        HttpRequest req = HttpParser.parseRequest(in);
        HttpResponse res = new HttpResponse();
        
        // Get the path from the request
        URI path = req.getUri();

        // Create a string for the content of the file, open the specified file, and return an error if the file does not exist
        String content = "";
        Path file = Path.of("res/" + path.getRawPath());
        if(Files.notExists(file)) {
            String message = "<html><h1>" + HttpStatus.NOT_FOUND.getReasonPhrase() + "</h1></html>";
            res.setVersion("HTTP/1.1");
            res.setStatus(HttpStatus.NOT_FOUND);
            res.header("Content-Type", "text/html");
            res.header("Content-Length", Integer.toString(message.length()));
            res.setBody(message);
            return;
        }

        // Read the file, and convert it to Base64 if its a binary file
        try {
            if(MimeType.valueOf(getFileExtension(file)) == MimeType.jar) {
                content = Base64.getEncoder().encodeToString(Files.readAllBytes(file));
            } else {
                content = Files.readString(file);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        // Fill in components for the response
        res.setVersion("HTTP/1.1");
        res.setStatus(HttpStatus.OK);
        res.header("Content-Type", MimeType.valueOf(getFileExtension(file)).getMime());
        res.header("Content-Length", Integer.toString(content.length()));
        res.setBody(content);

        // Write the response to the ByteBuffer
        HttpParser.serializeResponse(out, res);
    }

    // Helper to get the extension of a file
    default String getFileExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return "";
        }
    }
}
