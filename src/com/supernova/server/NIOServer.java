package com.supernova.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOServer implements Server {
    // Hub for communication
    Selector selector = Selector.open();
    // Interface to the network
    ServerSocketChannel socket;
    // Thread pool for executing handlers
    ExecutorService executor;

    // Sync-set for tracking which clients were handled
    Set<SelectionKey> handled = new HashSet<>();

    public NIOServer(int port) throws IOException {
        // Open a socket channel, bind it to the given port number, and register it on the selector
        socket = ServerSocketChannel.open();
        socket.socket().bind(new InetSocketAddress(port));
        socket.configureBlocking(false);
        socket.register(selector, socket.validOps());

        // Create the executor
        executor = Executors.newFixedThreadPool(10);
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                // Select all channels that are ready to perform IO operations
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
            
                // For each selected key:
                while(keys.hasNext()) {
                    SelectionKey key = keys.next();
                    // If the key can be accepted into the server's system, run a routine which accepts a client into the server
                    if(key.isAcceptable()) handleAccept();
                    // If the key can be read from, and isn't already being dispatched, dispatch a ClientHandler to handle it's request
                    else if(key.isReadable() && !handled.contains(key)) {
                        ClientHandler handle = new ClientHandler(key, handled);
                        handled.add(key);
                        executor.execute(handle);
                    }
                    // Remove the key from the selected keys, as it was already handled
                    keys.remove();
                }
            }
            socket.close();
            selector.close();
            executor.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Routine for accepting a client into the server's I/O system
    private void handleAccept() throws IOException {
        // Accept a client from the network
        SocketChannel client = socket.accept();
        // Ensure that the client does not stop execution when called on
        client.configureBlocking(false);
        // Register the client on the selector, flagging it as readable
        client.register(selector, SelectionKey.OP_READ);
    }
}
