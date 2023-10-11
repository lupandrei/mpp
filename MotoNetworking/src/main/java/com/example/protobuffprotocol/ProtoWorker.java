package com.example.protobuffprotocol;

import com.example.model.Admin;
import com.example.model.RaceDTO;
import com.example.service.MotoException;
import com.example.service.MotoObserver;
import com.example.service.MotoService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.List;

public class ProtoWorker implements Runnable, MotoObserver {
    private MotoService server;
    private Socket connection;

    private InputStream input;
    private OutputStream output;
    private volatile boolean connected;
    public ProtoWorker(MotoService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=connection.getOutputStream() ;//new ObjectOutputStream(connection.getOutputStream());
            input=connection.getInputStream(); //new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {

        while(connected){
            try {
                // Object request=input.readObject();
                System.out.println("Waiting requests ...");
                MotoProtobufs.MotoRequest request=MotoProtobufs.MotoRequest.parseDelimitedFrom(input);
                System.out.println("Request received: "+request);
                MotoProtobufs.MotoResponse response=handleRequest(request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private MotoProtobufs.MotoResponse handleRequest(MotoProtobufs.MotoRequest request) {
        MotoProtobufs.MotoResponse response=null;
        switch (request.getType()) {
            case Login: {
                System.out.println("Login request ...");
                Admin admin = ProtoUtils.getUser(request);
                try {
                    server.loginAdmin(admin, this);
                    return ProtoUtils.createOkResponse();
                } catch (MotoException e) {
                    connected = false;
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case Logout:{
                System.out.println("Logout request");
                Admin admin=ProtoUtils.getUser(request);
                try {
                    server.logout(admin, this);
                    connected=false;
                    return ProtoUtils.createOkResponse();

                } catch (MotoException e) {
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
            case GetRaceEntriesByEngineCapacity:
            {
                System.out.println("Get race Entries Request");
                try{
                    List<RaceDTO> races = server.getRaceEntriesByEngineCapacity();
                    return ProtoUtils.createGetRaceEntriesByEngineCapacityResponse(races);
                }
                catch(MotoException e){
                    return ProtoUtils.createErrorResponse(e.getMessage());
                }
            }
        }
        return response;
    }

    @Override
    public void entryAdded() throws MotoException {

    }

    private void sendResponse(MotoProtobufs.MotoResponse response) throws IOException{
        System.out.println("sending response "+response);
        response.writeDelimitedTo(output);
        //output.writeObject(response);
        output.flush();
    }
}
