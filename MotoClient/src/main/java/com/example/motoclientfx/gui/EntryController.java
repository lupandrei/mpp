package com.example.motoclientfx.gui;

import com.example.model.Entry;
import com.example.model.Participant;
import com.example.model.Race;
import com.example.service.MotoException;
import com.example.service.MotoService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;


import java.util.List;

public class EntryController {

    private MotoService server;
    @FXML
    Button buttonAdd;

    @FXML
    TextField textFieldFirstName;
    @FXML
    TextField textFieldLastName;

    @FXML
    TextField textFieldTeamName;

    @FXML
    ComboBox<Integer> combo;

    public void setService(MotoService srv){
        this.server=srv;
        try{
            List<Integer> capacities = srv.getAllEngineCapacities();
            for(int capacity:capacities){
                combo.getItems().add(capacity);
            }
        }
        catch(MotoException e){
            System.err.println(e.getMessage());
        }
    }

    public void handleButtonAdd(ActionEvent actionEvent) {
        String firstName =  textFieldFirstName.getText();
        String lastName = textFieldLastName.getText();
        String teamName = textFieldTeamName.getText();
        if(!firstName.isEmpty() && !lastName.isEmpty() &&!teamName.isEmpty() && combo.getSelectionModel().getSelectedItem()!=null){
                int engineCapacity = combo.getValue();
                try{
                    Race race =server.findRaceByEngineCapacity(engineCapacity);
                    Participant participant = new Participant(0,firstName,lastName,teamName);
                    server.addParticipant(participant);
                    Participant participant2 = server.getLastParticipant();
                    Entry entry = new Entry(race.getID(),participant.getID());
                    server.addEntry(entry);
                }
                catch(MotoException ex){
                    System.err.println(ex.getMessage());
                }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Invalid input");
            alert.showAndWait();
        }
    }
}
