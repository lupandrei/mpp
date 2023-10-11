package com.example.persistence;

import com.example.model.Race;

import java.util.List;

public interface IRaceRepo extends Repository<Race,Integer>{

    Race findRaceByEngineCapacity(int engineCapacity);

    List<Integer> getAllEngineCapacities();

    Race getLastRaceAdded();

    void deleteRace(Race race) throws RepositoryException;

}
