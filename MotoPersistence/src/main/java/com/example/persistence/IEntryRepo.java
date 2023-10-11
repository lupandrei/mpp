package com.example.persistence;

import com.example.model.Entry;
import com.example.model.ParticipantDTO;
import com.example.model.RaceDTO;

import java.util.List;
import java.util.Set;

public interface IEntryRepo extends Repository<Entry, Set<Integer>>{
    List<RaceDTO> getRaceEntriesByEngineCapacity();

    List<ParticipantDTO> getParticipantNameAndEngineCapacity(String teamName);
}
