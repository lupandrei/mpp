package com.example.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="race")
public class Race implements Identifiable<Integer>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int engineCapacity;


    private String raceName;

    public Race(){};

    public Race(int id, int engineCapacity, String raceName) {
        this.id = id;
        this.engineCapacity = engineCapacity;
        this.raceName = raceName;
    }

    public int getEngineCapacity() {
        return engineCapacity;
    }

    public void setEngineCapacity(int engineCapacity) {
        this.engineCapacity = engineCapacity;
    }
    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public Race(int engineCapacity, String raceName) {
        this.engineCapacity = engineCapacity;
        this.raceName = raceName;
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
