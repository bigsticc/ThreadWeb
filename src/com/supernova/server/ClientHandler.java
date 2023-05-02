package com.supernova.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class ClientHandler extends Thread implements Handler {
    // Client to be handled
    SelectionKey key;
    
    // "Sync-set" of keys currently being handled, used by the server to prevent multiple threads from being dispatched
    Set<SelectionKey> handled;
    
    // Constructor to initialize a ClientHandler
    public ClientHandler(SelectionKey key, Set<SelectionKey> handled) {
        this.key = key;
        this.handled = handled;
    }

    // Main method to handle a client
    @Override
    public void run() {
        try(SocketChannel client = (SocketChannel)key.channel()) {
            // Initializing buffers
            ByteBuffer in = ByteBuffer.allocate(1024);
            ByteBuffer out = ByteBuffer.allocate(30000);
            
            // Reading the request, and processing a response
            client.read(in);
            in.flip();
            process(in, out);
            client.write(out);

            // Removing the client from the sync-set
            handled.remove(key);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
