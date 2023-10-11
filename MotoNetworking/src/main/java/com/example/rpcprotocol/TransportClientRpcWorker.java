package com.example.rpcprotocol;

import com.example.model.*;
import com.example.service.MotoException;
import com.example.service.MotoObserver;
import com.example.service.MotoService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class TransportClientRpcWorker implements Runnable, MotoObserver {
    private MotoService server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private static final Response okResponse = new Response.Builder().type(ResponseType.OK).build();
    public TransportClientRpcWorker(MotoService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        while(connected) {
            try {
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if (response != null) {
                    sendResponse(response);
                }
            } catch (IOException | ClassNotFoundException e) {
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
            System.out.println("Worker error: " + e);
        }
    }

    private Response handleRequest(Request request) {
        Response response = null;
        if (request.type() == RequestType.LOGIN) {
            System.out.println("Login request... " + request.type());
            Admin admin = (Admin) request.data();
            try {
                server.loginAdmin(admin, this);
                return okResponse;
            } catch (MotoException e) {
                connected = false;
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type() == RequestType.LOGOUT) {
            System.out.println("Logout request... " + request.type());
            Admin admin = (Admin) request.data();
            try {
                server.logout(admin, this);
                connected = false;
                return okResponse;
            } catch (MotoException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.GET_RACE_ENTRIES_BY_ENGINE_CAPACITY){
            System.out.println("get race entries...");
            List<RaceDTO> races =null;
            try{
                races=server.getRaceEntriesByEngineCapacity();
                return new Response.Builder().type(ResponseType.GET_ALL_ENGINE_CAPACITIES).data(races).build();
            }
            catch(MotoException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.GET_PARTICIPANT_NAME_AND_ENGINE_CAPACITY){
            System.out.println("get participant and engine capacity...");
            List<ParticipantDTO> participants =null;
            try{
                participants=server.getParticipantNameAndEngineCapacity(String.valueOf(request.data()));
                return new Response.Builder().type(ResponseType.GET_PARTICIPANT_NAME_AND_ENGINE_CAPACITY).data(participants).build();
            }
            catch(MotoException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.GET_ALL_TEAM_NAMES){
            System.out.println("get race entries...");
            List<String> teams =null;
            try{
                teams=server.getAllTeamNames();
                return new Response.Builder().type(ResponseType.GET_ALL_TEAM_NAMES).data(teams).build();
            }
            catch(MotoException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.GET_ALL_ENGINE_CAPACITIES){
            System.out.println("get race entries...");
            List<Integer> capacities =null;
            try{
                capacities=server.getAllEngineCapacities();
                return new Response.Builder().type(ResponseType.GET_ALL_ENGINE_CAPACITIES).data(capacities).build();
            }
            catch(MotoException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.FIND_RACE_BY_ENGINE_CAPACITY){
            System.out.println("get race entries...");
            Race race =null;
            try{
                race=server.findRaceByEngineCapacity((int)request.data());
                return new Response.Builder().type(ResponseType.FIND_RACE_BY_ENGINE_CAPACITY).data(race).build();
            }
            catch(MotoException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.GET_LAST_PARTICIPANT){
            System.out.println("get race entries...");
            Participant particiapnt =null;
            try{
                particiapnt=server.getLastParticipant();
                return new Response.Builder().type(ResponseType.GET_LAST_PARTICIPANT).data(particiapnt).build();
            }
            catch(MotoException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.ADD_PARTICIPANT){
            System.out.println("get race entries...");
            Participant participant =(Participant)request.data();
            try{
                server.addParticipant(participant);
                return okResponse;
            }
            catch(MotoException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.ADD_ENTRY){
            System.out.println("get race entries...");
            Entry entry = (Entry) request.data();
            try{
                server.addEntry(entry);
                return okResponse;
            }
            catch(MotoException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    @Override
    public void entryAdded() throws MotoException {
        // MessageDTO mdto= DTOUtils.getDTO(message);
        Response resp = new Response.Builder().type(ResponseType.ADD_ENTRY).build();
        System.out.println("Worker: entry added...");
        try {
            sendResponse(resp);
        } catch (IOException e) {
            throw new MotoException("Worker error: " + e);
        }
    }

    private void sendResponse(Response resp) throws IOException{
        System.out.println("Sending response " + resp);
        output.writeObject(resp);
        output.flush();
    }
}
