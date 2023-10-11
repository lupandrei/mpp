package com.example.utils;

import java.net.Socket;

public abstract class AbstractConcurrentServer extends AbstractServer {
    public AbstractConcurrentServer(int port) {
        super(port);
        System.out.println("Using concurrent server...");
    }

    protected void processRequest(Socket client) {
        Thread worker = createWorker(client);
        worker.start();
    }

    protected abstract Thread createWorker(Socket client) ;
}
