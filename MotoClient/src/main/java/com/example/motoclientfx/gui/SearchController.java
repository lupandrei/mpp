package com.example.motoclientfx.gui;

import com.example.model.ParticipantDTO;
import com.example.service.MotoException;
import com.example.service.MotoService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;


import java.util.List;

public class SearchController  {
    private MotoService server;

    private ObservableList<ParticipantDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<ParticipantDTO> tableview;

    @FXML
    TableColumn<ParticipantDTO,String> tableColumnFirstName;

    @FXML
    TableColumn<ParticipantDTO,String> tableColumnLastName;

    @FXML
    TableColumn<ParticipantDTO,Integer> tableColumnEngineCapacity;

    @FXML
    ComboBox<String> combo;

    public void setSrv(MotoService srv){
        this.server=srv;
        try{
            List<String> comboValues = srv.getAllTeamNames();
            combo.getItems().add("all");
            for(String team:comboValues)
            {
                combo.getItems().add(team);
            }
        }
        catch(MotoException ex){
            System.err.println(ex.getMessage());
        }
        try{
            initModel(srv.getParticipantNameAndEngineCapacity("all"));
        }
        catch(MotoException ex){
            System.err.println(ex.getMessage());
        }

    }

    @FXML
    public void initialize(){
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<ParticipantDTO,String>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<ParticipantDTO,String>("lastName"));
        tableColumnEngineCapacity.setCellValueFactory(new PropertyValueFactory<ParticipantDTO,Integer>("engineCapacity"));
        tableview.setItems(model);
    }
    private void initModel(List<ParticipantDTO> all) {
        model.setAll(all);
    }


    public void changeCombo(ActionEvent actionEvent) {

        try{
            String team = combo.getValue();
            System.out.println(team);
            initModel(server.getParticipantNameAndEngineCapacity(team));
        }
        catch(MotoException ex){
            System.err.println(ex.getMessage());
        }
    }
}
