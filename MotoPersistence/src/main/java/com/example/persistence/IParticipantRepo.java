package com.example.persistence;

import com.example.model.Participant;
import java.util.List;

public interface IParticipantRepo extends Repository<Participant,Integer>{

    List<Participant> findParticipantsByTeamName(String teamName);

    List<String> getAllTeamNames();

    Participant getLastParticipant();

}
