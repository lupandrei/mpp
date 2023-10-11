package com.example.model;

import java.io.Serializable;

public class Participant implements Identifiable<Integer>, Serializable {
    private int id;
    private String firstName;
    private String lastName;

    private String teamName;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }



    public Participant(int id, String firstName, String lastName, String teamName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamName = teamName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer integer) {
        this.id=integer;
    }
}
