package com.controller;

import com.example.model.Race;
import com.example.persistence.IEntryRepo;
import com.example.persistence.IRaceRepo;
import com.example.persistence.RepositoryException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("app/races")
public class RaceController {

    @Autowired
    private IRaceRepo raceRepo;

    @RequestMapping( method = RequestMethod.GET)
    public Iterable<Race> getAll(){
        System.out.println("Get all users ...");
        return raceRepo.getAll();
    }
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable int id){
        System.out.println("Find by id "+id);
        Race race= raceRepo.findByID(id);
        if (race==null)
            return new ResponseEntity<String>("Race not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Race>(race, HttpStatus.OK);
    }
    @PostMapping
    public Race add(@RequestBody Race race){
        System.out.println("adding race...");
        raceRepo.add(race);
        List<Race> list = new ArrayList<>();
        raceRepo.getAll().forEach(list::add);
        return list.get(list.size()-1);
    }

    @RequestMapping(value="/{id}",method=RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Race race,@PathVariable Integer id){
        System.out.println("updating race...");
        if(raceRepo.findByID(id)!=null)
        {
            raceRepo.update(race,id);
            return new ResponseEntity<Race>(race, HttpStatus.OK);
        }
        return new ResponseEntity<String>("Race with id " + id +" does not exist",HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        System.out.println("Deleting user ... "+id);
        Race race = raceRepo.findByID(id);
        System.out.println(race);
        if(race!=null) {
            try {
                raceRepo.deleteRace(race);
                return new ResponseEntity<Race>(HttpStatus.OK);

            } catch (RepositoryException ex) {
                return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        else{
            return new ResponseEntity<String>("Race with id " + id +" does not exist",HttpStatus.BAD_REQUEST);
        }
    }

    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }

}
