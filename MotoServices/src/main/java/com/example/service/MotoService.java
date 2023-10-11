package com.example.service;

import com.example.model.*;

import java.util.List;

public interface MotoService {
    Admin loginAdmin(Admin admin,MotoObserver obs) throws MotoException;


    List<RaceDTO> getRaceEntriesByEngineCapacity() throws MotoException;
    List<ParticipantDTO> getParticipantNameAndEngineCapacity(String teamName) throws MotoException;
    List<String> getAllTeamNames() throws MotoException;
    List<Integer> getAllEngineCapacities() throws MotoException;
    Race findRaceByEngineCapacity(int engineCapacity) throws MotoException;
    Participant getLastParticipant() throws MotoException;
    void addParticipant(Participant participant) throws MotoException;
    void addEntry(Entry entry) throws MotoException;

    void logout(Admin Admin, MotoObserver client) throws MotoException;
}
