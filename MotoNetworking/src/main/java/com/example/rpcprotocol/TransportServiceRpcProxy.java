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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TransportServiceRpcProxy implements MotoService {
    private final String host;
    private final int port;
    private MotoObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;
    private BlockingQueue<Response> responses;
    private volatile boolean finished;

    public TransportServiceRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<>();
    }

    @Override
    public Admin loginAdmin(Admin admin, MotoObserver obs) throws MotoException {
        initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN).data(admin).build();
        sendRequest(req);
        Response res = readResponse();
        if (res.type() == ResponseType.OK) {
            this.client = obs;
        }
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            closeConnection();
            throw new MotoException(err);
        }
        return (Admin) res.data();
    }

    @Override
    public List<RaceDTO> getRaceEntriesByEngineCapacity() throws MotoException {
        Request req = new Request.Builder().type(RequestType.GET_RACE_ENTRIES_BY_ENGINE_CAPACITY).build();
        sendRequest(req);
        Response res = readResponse();
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            throw new MotoException(err);
        }
        return (List<RaceDTO>) res.data();
    }

    @Override
    public List<ParticipantDTO> getParticipantNameAndEngineCapacity(String teamName) throws MotoException{
        Request req = new Request.Builder().type(RequestType.GET_PARTICIPANT_NAME_AND_ENGINE_CAPACITY).data(teamName).build();
        sendRequest(req);
        Response res = readResponse();
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            throw new MotoException(err);
        }
        return (List<ParticipantDTO>) res.data();
    }

    @Override
    public List<String> getAllTeamNames() throws MotoException {
        Request req = new Request.Builder().type(RequestType.GET_ALL_TEAM_NAMES).build();
        sendRequest(req);
        Response res = readResponse();
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            throw new MotoException(err);
        }
        return (List<String>) res.data();
    }

    @Override
    public List<Integer> getAllEngineCapacities() throws MotoException{
        Request req = new Request.Builder().type(RequestType.GET_ALL_ENGINE_CAPACITIES).build();
        sendRequest(req);
        Response res = readResponse();
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            throw new MotoException(err);
        }
        return (List<Integer>) res.data();
    }

    @Override
    public Race findRaceByEngineCapacity(int engineCapacity) throws MotoException{
        Request req = new Request.Builder().type(RequestType.FIND_RACE_BY_ENGINE_CAPACITY).data(engineCapacity).build();
        sendRequest(req);
        Response res = readResponse();
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            throw new MotoException(err);
        }
        return (Race) res.data();
    }

    @Override
    public Participant getLastParticipant() throws MotoException{
        Request req = new Request.Builder().type(RequestType.GET_LAST_PARTICIPANT).build();
        sendRequest(req);
        Response res = readResponse();
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            throw new MotoException(err);
        }
        return (Participant) res.data();
    }

    @Override
    public void addParticipant(Participant participant) throws MotoException{
        Request req = new Request.Builder().type(RequestType.ADD_PARTICIPANT).data(participant).build();
        sendRequest(req);
        Response res = readResponse();
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            throw new MotoException(err);
        }
    }

    @Override
    public void addEntry(Entry entry) throws MotoException{
        Request req = new Request.Builder().type(RequestType.ADD_ENTRY).data(entry).build();
        sendRequest(req);
        Response res = readResponse();
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            throw new MotoException(err);
        }
    }

    @Override
    public void logout(Admin admin, MotoObserver client) throws MotoException {
        Request req = new Request.Builder().type(RequestType.LOGOUT).data(admin).build();
        sendRequest(req);
        Response res = readResponse();
        closeConnection();
        if (res.type() == ResponseType.ERROR) {
            String err = res.data().toString();
            throw new MotoException(err);
        }
    }

    private void initializeConnection() {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread thread = new Thread(new ReaderThread());
        thread.start();
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws MotoException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new MotoException("Proxy: Error sending object: " + e);
        }
    }

    private Response readResponse() {
        Response response = null;
        try{
            response = responses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.ADD_ENTRY || response.type() == ResponseType.UPDATE;
    }

    private void handleUpdate(Response response) {
        if (response.type() == ResponseType.ADD_ENTRY) {
            try {
                client.entryAdded();
            } catch (MotoException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while(!finished){
                try {
                    Object response = input.readObject();
                    System.out.println("Proxy: Response received: " + response);
                    if (isUpdate((Response) response)) {
                        handleUpdate((Response) response);
                    } else {
                        try {
                            responses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Proxy: Reading error: " + e);
                }
            }
        }
    }
}
