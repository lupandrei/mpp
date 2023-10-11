package com.example;

import com.example.persistence.repository.jdbc.*;
import com.example.server.MotoServiceImpl;
import com.example.service.MotoService;
import com.example.utils.AbstractServer;
import com.example.utils.ServerException;
import com.example.utils.TransportRpcConcurrentServer;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {
    private static final int defaultPort = 55555;
    public static void main(String[] args){
        Properties serverProps = new Properties();
        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set...");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find server.properties: " + e);
            return;
        }
        MotoService serviceImpl = new MotoServiceImpl(
                new AdminDBRepo(serverProps),
                new EntryDBRepo(serverProps),
                //new RaceDBRepo(serverProps),
                new RaceHBNRepo(),
                new ParticipantDBRepo(serverProps)
        );
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong port number: " + e);
            System.out.println("Using default port: " + defaultPort);
        }
        AbstractServer server = new TransportRpcConcurrentServer(serverPort, serviceImpl);
        try {
            server.start();
        } catch (com.example.utils.ServerException e)
        {
            System.err.println("Error starting the server: " + e);
        } finally {
            try {
                server.stop();
            } catch(ServerException e) {
                System.err.println("Error stopping server: " + e);
            }
        }

    }
}
