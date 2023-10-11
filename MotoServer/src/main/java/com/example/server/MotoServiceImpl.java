package com.example.server;

/*import com.example.model.Admin;
import com.example.model.Entry;
import com.example.model.Participant;
import com.example.model.Race;*/
import com.example.model.*;
import com.example.persistence.IAdminRepo;
import com.example.persistence.IEntryRepo;
import com.example.persistence.IParticipantRepo;
import com.example.persistence.IRaceRepo;
import com.example.persistence.repository.jdbc.AdminDBRepo;
import com.example.persistence.repository.jdbc.EntryDBRepo;
import com.example.persistence.repository.jdbc.ParticipantDBRepo;
import com.example.persistence.repository.jdbc.RaceDBRepo;
import com.example.service.MotoException;
import com.example.service.MotoObserver;
import com.example.service.MotoService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MotoServiceImpl implements MotoService {
    /*private AdminDBRepo adminDBRepo;
    //List<Observer> obs;
    private EntryDBRepo entryDBRepo;
    private RaceDBRepo raceDBRepo;
    private ParticipantDBRepo participantDBRepo;*/

    private IAdminRepo adminDBRepo;

    private IEntryRepo entryDBRepo;
    private IRaceRepo raceDBRepo;
    private IParticipantRepo participantDBRepo;
    private Map<String, MotoObserver> loggedAdmins;

    public MotoServiceImpl(IAdminRepo adminDBRepo, IEntryRepo entryDBRepo, IRaceRepo raceDBRepo, IParticipantRepo participantDBRepo) {
        this.adminDBRepo = adminDBRepo;
        this.entryDBRepo = entryDBRepo;
        this.raceDBRepo = raceDBRepo;
        this.participantDBRepo = participantDBRepo;
        this.loggedAdmins = new ConcurrentHashMap<>();
    }

    private final int defaultThreadsNo = 5;

    /*public MotoServiceImpl(AdminDBRepo adminDBRepo, EntryDBRepo entryDBRepo, RaceDBRepo raceDBRepo, ParticipantDBRepo participantDBRepo) {
        this.adminDBRepo = adminDBRepo;
        this.entryDBRepo = entryDBRepo;
        this.raceDBRepo = raceDBRepo;
        this.participantDBRepo = participantDBRepo;
        //this.obs=new ArrayList<>();
        loggedAdmins = new ConcurrentHashMap<>();
    }*/
    @Override
    public synchronized  Admin loginAdmin(Admin admin2, MotoObserver client) throws MotoException {
        Admin admin= adminDBRepo.findByID(admin2.getID());
        if(admin==null){
            throw new MotoException("Invalid credentials");
        }
        if(admin.getPassword().equals(admin2.getPassword()))
        {
            loggedAdmins.put(admin.getID(), client);
            return admin;
        }

        throw new MotoException("Incorrect password");
    }
    @Override
    public synchronized List<RaceDTO> getRaceEntriesByEngineCapacity(){
        return entryDBRepo.getRaceEntriesByEngineCapacity();
    }
    @Override
    public synchronized List<ParticipantDTO> getParticipantNameAndEngineCapacity(String teamName){
        return entryDBRepo.getParticipantNameAndEngineCapacity(teamName);
    }
    @Override
    public synchronized List<String> getAllTeamNames(){
        return participantDBRepo.getAllTeamNames();
    }

    @Override
    public synchronized List<Integer> getAllEngineCapacities(){
        return raceDBRepo.getAllEngineCapacities();
    }

    @Override
    public synchronized Race findRaceByEngineCapacity(int engineCapacity){
        return raceDBRepo.findRaceByEngineCapacity(engineCapacity);
    }

    @Override
    public synchronized Participant getLastParticipant(){
        return participantDBRepo.getLastParticipant();
    }

    @Override
    public synchronized void addParticipant(Participant participant){
        participantDBRepo.add(participant);
    }
    public  synchronized void addEntry(Entry entry){

        entryDBRepo.add(entry);
        notifyEntryAdded();
        //notifyObservers();
    }

    @Override
    public  synchronized void logout(Admin admin, MotoObserver client) {
        MotoObserver localClient = loggedAdmins.remove(admin.getID());
    }
    private void notifyEntryAdded() {
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for (MotoObserver client : loggedAdmins.values()) {
            executor.execute(() -> {
                try {
                    client.entryAdded();
                } catch (MotoException e) {
                    System.err.println("Error notifying logged in employes!");
                }
            });
        }
    }

}
