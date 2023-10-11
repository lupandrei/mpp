package com.example.utils;

import com.example.rpcprotocol.TransportClientRpcWorker;
import com.example.service.MotoService;

import java.net.Socket;

public class TransportRpcConcurrentServer extends AbstractConcurrentServer {
    private MotoService motoServer;

    public TransportRpcConcurrentServer(int port, MotoService motoServer) {
        super(port);
        this.motoServer = motoServer;
        System.out.println("Using concurrent RPC server...");
    }

    @Override
    protected Thread createWorker(Socket client) {
        TransportClientRpcWorker worker = new TransportClientRpcWorker(motoServer, client);
        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping service...");
    }
}