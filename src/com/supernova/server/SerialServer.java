package com.supernova.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class SerialServer implements Server, Handler {
    // Hub for communication
    Selector selector = Selector.open();
    // Interface to the network
    ServerSocketChannel socket = ServerSocketChannel.open();

    public SerialServer(int port) throws IOException {
        // Bind the socket channel to the given network port, and register it on the selector
        socket.socket().bind(new InetSocketAddress(port));
        socket.configureBlocking(false);
        socket.register(selector, socket.validOps());
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                // Select all channels that are ready to perform I/O operations
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                
                // For each selected key:
                while(keys.hasNext()) {
                    SelectionKey key = keys.next();
                    // If the key is waiting to be accepted, accept it into the server's I/O systems
                    if(key.isAcceptable()) handleAccept();
                    // If the key is readable, then perform a read operation.
                    else if(key.isReadable()) handleRead(key);
                    // Remove the key from the selected keys because it was already handled
                    keys.remove();
                }
            }
            selector.close();
            socket.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept() throws IOException {
        // Accept a client from the network
        SocketChannel client = socket.accept();
        // Ensure that the client does not block execution when it is called on
        client.configureBlocking(false);
        // Register the client on the server, flagging it as readable
        client.register(selector, SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) {
        try(SocketChannel client = (SocketChannel)key.channel()) {
            // Initializing buffers
            ByteBuffer in = ByteBuffer.allocate(1024);
            ByteBuffer out = ByteBuffer.allocate(30000);

            // Reading the request
            client.read(in);
            in.flip();
            
            // Calling the process routine with the buffers, and writing out the result
            process(in, out);
            client.write(out);
            
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
