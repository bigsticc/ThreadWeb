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
    Selector selector = Selector.open();
    ServerSocketChannel socket;
    ExecutorService executor;

    Set<SelectionKey> handled = new HashSet<>();

    public NIOServer(int port) throws IOException {
        socket = ServerSocketChannel.open();
        socket.socket().bind(new InetSocketAddress(port));
        socket.configureBlocking(false);
        socket.register(selector, socket.validOps());

        executor = Executors.newFixedThreadPool(10);
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
                    else if(key.isReadable() && !handled.contains(key)) {
                        ClientHandler handle = new ClientHandler(key, handled);
                        handled.add(key);
                        executor.execute(handle);
                    }
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

    private void handleAccept() throws IOException {
        SocketChannel client = socket.accept();
        client.configureBlocking(false);

        client.register(selector, SelectionKey.OP_READ);
    }
}
