package com.example.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Entry implements Identifiable<Set<Integer>>, Serializable {

    int idRace;
    int idParticipant;

    public Entry(int idRace, int idParticipant) {
        this.idRace = idRace;
        this.idParticipant = idParticipant;
    }

    @Override
    public Set<Integer> getID() {
        HashSet<Integer> ids = new HashSet<>();
        ids.add(idRace);
        ids.add(idParticipant);
        return ids;
    }

    @Override
    public void setID(Set<Integer> integers) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entry entry = (Entry) o;
        return idRace == entry.idRace && idParticipant == entry.idParticipant;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idRace, idParticipant);
    }
    public int getIdRace() {
        return idRace;
    }

    public void setIdRace(int idRace) {
        this.idRace = idRace;
    }

    public int getIdParticipant() {
        return idParticipant;
    }

    public void setIdParticipant(int idParticipant) {
        this.idParticipant = idParticipant;
    }
}
