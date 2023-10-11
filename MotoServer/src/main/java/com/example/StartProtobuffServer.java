package com.example;

import com.example.persistence.repository.jdbc.AdminDBRepo;
import com.example.persistence.repository.jdbc.EntryDBRepo;
import com.example.persistence.repository.jdbc.ParticipantDBRepo;
import com.example.persistence.repository.jdbc.RaceDBRepo;
import com.example.server.MotoServiceImpl;
import com.example.service.MotoService;
import com.example.utils.AbstractServer;
import com.example.utils.ChatProtobuffConcurrentServer;
import com.example.utils.ServerException;

import java.io.IOException;
import java.util.Properties;

public class StartProtobuffServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {


        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find chatserver.properties " + e);
            return;
        }
        MotoService serviceImpl = new MotoServiceImpl(
                new AdminDBRepo(serverProps),
                new EntryDBRepo(serverProps),
                new RaceDBRepo(serverProps),
                new ParticipantDBRepo(serverProps)
        );
        int chatServerPort = defaultPort;
        try {
            chatServerPort = Integer.parseInt(serverProps.getProperty("chat.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + chatServerPort);
        AbstractServer server = new ChatProtobuffConcurrentServer(chatServerPort, serviceImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
