package com.example.model;

import java.io.Serializable;

public class RaceDTO implements Serializable {
    private String raceName;
    private int engineCapacity;
    private int participants;

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public RaceDTO(String raceName, int engineCapacity, int participants) {
        this.raceName = raceName;
        this.engineCapacity = engineCapacity;
        this.participants = participants;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }
}
