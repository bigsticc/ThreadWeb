package com.supernova.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class ClientHandler extends Thread implements Handler {
    SelectionKey key;
    Set<SelectionKey> handled;
    public ClientHandler(SelectionKey key, Set<SelectionKey> handled) {
        this.key = key;
        this.handled = handled;
    }

    @Override
    public void run() {
        try(SocketChannel client = (SocketChannel)key.channel()) {
            // Initializing buffers
            ByteBuffer in = ByteBuffer.allocate(1024);
            ByteBuffer out = ByteBuffer.allocate(30000);
            
            // reading the request, and processing a response
            client.read(in);
            in.flip();
            process(in, out);
            client.write(out);

            // removing the client from the sync-set
            handled.remove(key);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
