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
    default void process(ByteBuffer in, ByteBuffer out) {
        HttpRequest req = HttpParser.parseRequest(in);
        HttpResponse res = new HttpResponse();
        URI path = req.getUri();

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


        try {
            if(MimeType.valueOf(getFileExtension(file)) == MimeType.jar) {
                content = Base64.getEncoder().encodeToString(Files.readAllBytes(file));
            } else {
                content = Files.readString(file);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        res.setVersion("HTTP/1.1");
        res.setStatus(HttpStatus.OK);
        res.header("Content-Type", MimeType.valueOf(getFileExtension(file)).getMime());
        res.header("Content-Length", Integer.toString(content.length()));
        res.setBody(content);

        HttpParser.serializeResponse(out, res);
    }

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
