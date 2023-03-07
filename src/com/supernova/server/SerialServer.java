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
    Selector selector = Selector.open();
    ServerSocketChannel socket = ServerSocketChannel.open();

    public SerialServer(int port) throws IOException {
        socket.socket().bind(new InetSocketAddress(port));
        socket.configureBlocking(false);
        socket.register(selector, socket.validOps());
    }

    @Override
    public void run() {
        try {
            while(!Thread.interrupted()) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while(keys.hasNext()) {
                    SelectionKey key = keys.next();
                    if(key.isAcceptable()) handleAccept();
                    else if(key.isReadable()) handleRead(key);
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
        SocketChannel client = socket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) {
        try(SocketChannel client = (SocketChannel)key.channel()) {
            // Initializing buffers
            ByteBuffer in = ByteBuffer.allocate(1024);
            ByteBuffer out = ByteBuffer.allocate(30000);

            // reading the request, and processing a response
            client.read(in);
            in.flip();
            process(in, out);
            client.write(out);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
