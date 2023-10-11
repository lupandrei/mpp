package com.example.protobuffprotocol;

import com.example.model.*;
import com.example.rpcprotocol.TransportServiceRpcProxy;
import com.example.service.MotoException;
import com.example.service.MotoObserver;
import com.example.service.MotoService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProtoProxy implements MotoService {

    private String host;
    private int port;

    private MotoObserver client;

    private InputStream input;
    private OutputStream output;
    private Socket connection;

    private BlockingQueue<MotoProtobufs.MotoResponse> qresponses;
    private volatile boolean finished;
    public ProtoProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<MotoProtobufs.MotoResponse>();
    }
    @Override
    public Admin loginAdmin(Admin admin, MotoObserver obs) throws MotoException {
        initializeConnection();
        System.out.println("Login request ...");
        sendRequest(ProtoUtils.createLoginRequest(admin));
        MotoProtobufs.MotoResponse response=readResponse();
        if (response.getType()==MotoProtobufs.MotoResponse.Type.Ok){
            this.client=obs;
            return admin;
        }
        if (response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText=ProtoUtils.getError(response);
            closeConnection();
            throw new MotoException(errorText);
        }
        return null;
    }
    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(MotoProtobufs.MotoRequest request)throws MotoException{
        try {
            System.out.println("Sending request ..."+request);
            //request.writeTo(output);
            request.writeDelimitedTo(output);
            output.flush();
            System.out.println("Request sent.");
        } catch (IOException e) {
            throw new MotoException("Error sending object "+e);
        }

    }

    private MotoProtobufs.MotoResponse readResponse() throws MotoException{
        MotoProtobufs.MotoResponse response=null;
        try{
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }
    private void initializeConnection() throws MotoException{
        try {
            connection=new Socket(host,port);
            output=connection.getOutputStream();
            //output.flush();
            input=connection.getInputStream();     //new ObjectInputStream(connection.getInputStream());
            finished=false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    @Override
    public List<RaceDTO> getRaceEntriesByEngineCapacity() throws MotoException {
        sendRequest(ProtoUtils.createGetRaceEntriesByEngineCapacityRequest());
        MotoProtobufs.MotoResponse response =readResponse();
        if(response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText =ProtoUtils.getError(response);
            throw new MotoException(errorText);
        }
        List<RaceDTO> races = ProtoUtils.getRaceEntriesByEngineCapacity(response);
        return races;
    }

    @Override
    public List<ParticipantDTO> getParticipantNameAndEngineCapacity(String teamName) throws MotoException {
        sendRequest(ProtoUtils.createGetParticipantNameAndEngineCapacityRequest(teamName));
        MotoProtobufs.MotoResponse response=readResponse();
        if(response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText =ProtoUtils.getError(response);
            throw new MotoException(errorText);
        }
        List<ParticipantDTO> participants = ProtoUtils.getParticipantNameAndEngineCapacity(response);
        return participants;
    }

    @Override
    public List<String> getAllTeamNames() throws MotoException {
        sendRequest(ProtoUtils.createGetAllTeamNamesRequest());
        MotoProtobufs.MotoResponse response=readResponse();
        if(response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText =ProtoUtils.getError(response);
            throw new MotoException(errorText);
        }
        List<String> teamnames = ProtoUtils.getTeamNames(response);
        return teamnames;
    }

    @Override
    public List<Integer> getAllEngineCapacities() throws MotoException {
        sendRequest(ProtoUtils.createGetAllEngineCapacitesRequest());
        MotoProtobufs.MotoResponse response=readResponse();
        if(response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText =ProtoUtils.getError(response);
            throw new MotoException(errorText);
        }
        List<Integer> capacities = ProtoUtils.getEngineCapacities(response);
        return capacities;
    }

    @Override
    public Race findRaceByEngineCapacity(int engineCapacity) throws MotoException {
        sendRequest(ProtoUtils.createFindRaceByEngineCapacityRequest(engineCapacity));
        MotoProtobufs.MotoResponse response = readResponse();
        if(response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText = ProtoUtils.getError(response);
            throw new MotoException(errorText);
        }
        Race race = ProtoUtils.findRaceByEngineCapacity(response);
        return race;
    }

    @Override
    public Participant getLastParticipant() throws MotoException {
        sendRequest(ProtoUtils.createGetLastParticipantRequest());
        MotoProtobufs.MotoResponse response = readResponse();
        if(response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText = ProtoUtils.getError(response);
            throw new MotoException(errorText);
        }
        Participant participant = ProtoUtils.getLastParticipant(response);
        return participant;
    }

    @Override
    public void addParticipant(Participant participant) throws MotoException {
        sendRequest(ProtoUtils.createAddParticipantRequeset(participant));
        MotoProtobufs.MotoResponse response = readResponse();
        if(response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText = ProtoUtils.getError(response);
            throw new MotoException(errorText);
        }
    }

    @Override
    public void addEntry(Entry entry) throws MotoException {
        sendRequest(ProtoUtils.createAddEntryRequeset(entry));
        MotoProtobufs.MotoResponse response = readResponse();
        if(response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText = ProtoUtils.getError(response);
            throw new MotoException(errorText);
        }
    }

    @Override
    public void logout(Admin Admin, MotoObserver client) throws MotoException {
        sendRequest(ProtoUtils.createLogoutRequest(Admin));
        MotoProtobufs.MotoResponse response = readResponse();
        if(response.getType()==MotoProtobufs.MotoResponse.Type.Error){
            String errorText = ProtoUtils.getError(response);
            throw new MotoException(errorText);
        }
    }
    private class ReaderThread implements Runnable{
        public void run() {
            while(!finished){
                try {
                    MotoProtobufs.MotoResponse response=MotoProtobufs.MotoResponse.parseDelimitedFrom(input);
                    System.out.println("response received "+response);

                    if (isUpdateResponse(response.getType())){
                        handleUpdate(response);
                    }else{
                        try {
                            qresponses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error "+e);
                }
            }
        }


    }
    private void handleUpdate(MotoProtobufs.MotoResponse response) {
        try{
            client.entryAdded();
        }
        catch (MotoException exception){
            System.out.println(exception.getMessage());
        }

    }
    private boolean isUpdateResponse(MotoProtobufs.MotoResponse.Type type){
        if(type.equals(MotoProtobufs.MotoResponse.Type.Unknown)){
            return true;
        }
        return false;
    }
}

