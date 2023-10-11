package service;

import exception.RepositoryException;
import exception.ServiceException;

import observer.Observable;
import observer.Observer;
import repository.AdminDBRepo;
import repository.EntryDBRepo;
import repository.ParticipantDBRepo;
import repository.RaceDBRepo;

import java.util.ArrayList;
import java.util.List;

public class Service implements Observable {
    private AdminDBRepo adminDBRepo;
    List<Observer> obs;
    private EntryDBRepo entryDBRepo;
    private RaceDBRepo raceDBRepo;
    private ParticipantDBRepo participantDBRepo;

    public Service(AdminDBRepo adminDBRepo, EntryDBRepo entryDBRepo, RaceDBRepo raceDBRepo, ParticipantDBRepo participantDBRepo) {
        this.adminDBRepo = adminDBRepo;
        this.entryDBRepo = entryDBRepo;
        this.raceDBRepo = raceDBRepo;
        this.participantDBRepo = participantDBRepo;
        this.obs=new ArrayList<>();
    }
    public Admin loginAdmin(String username,String password) throws ServiceException, RepositoryException {
            Admin admin= adminDBRepo.findByID(username);
            if(admin.getPassword().equals(password))
                return admin;
            throw new ServiceException("Incorrect password");
    }
    public List<RaceDTO> getRaceEntriesByEngineCapacity(){
        return entryDBRepo.getRaceEntriesByEngineCapacity();
    }
    public List<ParticipantDTO> getParticipantNameAndEngineCapacity(String teamName){
        return entryDBRepo.getParticipantNameAndEngineCapacity(teamName);
    }
    public List<String> getAllTeamNames(){
        return participantDBRepo.getAllTeamNames();
    }
    public List<Integer> getAllEngineCapacities(){
        return raceDBRepo.getAllEngineCapacities();
    }
    public Race findRaceByEngineCapacity(int engineCapacity){
        return raceDBRepo.findRaceByEngineCapacity(engineCapacity);
    }
    public Participant getLastParticipant(){
        return participantDBRepo.getLastParticipant();
    }

    public void addParticipant(String firstName,String lastName,String teamName){
        Participant participant = new Participant(100,firstName,lastName,teamName);
        participantDBRepo.add(participant);
    }
    public void addEntry(int idRace, int idParticipant){
        Entry entry = new Entry(idRace,idParticipant);
        entryDBRepo.add(entry);
        notifyObservers();
    }

    @Override
    public void addObserver(Observer o) {
        obs.add(o);
    }

    @Override
    public void notifyObservers() {
        for(Observer o:obs){
            o.update();
        }
    }
}
