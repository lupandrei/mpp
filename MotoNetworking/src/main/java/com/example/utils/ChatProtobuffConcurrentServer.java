package com.example.utils;

import com.example.protobuffprotocol.ProtoWorker;
import com.example.service.MotoService;

import java.net.Socket;

public class ChatProtobuffConcurrentServer extends AbstractConcurrentServer {
    private MotoService chatServer;
    public ChatProtobuffConcurrentServer(int port, MotoService chatServer) {
        super(port);
        this.chatServer = chatServer;
        System.out.println("Chat- ChatProtobuffConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ProtoWorker worker=new ProtoWorker(chatServer,client);
        Thread tw=new Thread(worker);
        return tw;
    }
}