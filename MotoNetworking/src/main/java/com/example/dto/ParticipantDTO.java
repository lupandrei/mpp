package com.example.dto;

import java.io.Serializable;

public class ParticipantDTO implements Serializable {
    private String firstName;
    private String lastName;
    private int engineCapacity;

    public ParticipantDTO(String firstName, String lastName, int engineCapacity) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.engineCapacity = engineCapacity;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }
}
