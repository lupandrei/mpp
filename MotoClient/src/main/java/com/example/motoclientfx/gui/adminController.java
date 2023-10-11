package com.example.motoclientfx.gui;

//import com.google.common.collect.Table;
import com.example.dto.RaceDTO;
import com.example.model.Admin;
import com.example.motoclientfx.StartRPCClientFX;
import com.example.service.MotoException;
import com.example.service.MotoObserver;
import com.example.service.MotoService;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class adminController implements MotoObserver {
    private MotoService server;

    private Admin admin;
    private ObservableList<RaceDTO> model = FXCollections.observableArrayList();

    @FXML
    TableView<RaceDTO> tableView;

    @FXML
    TableColumn<RaceDTO,String> tableColumnName;

    @FXML
    TableColumn<RaceDTO,Integer> tableColumnEngineCapacity;

    @FXML
    TableColumn<RaceDTO,Integer> tableColumnParticipants;

    @FXML
    Button buttonSignUp;

    @FXML
    Button buttonLogOut;
    
    @FXML
    Button buttonSearch;

    public void setServer(MotoService srv){
        this.server=srv;

    }
    public void setAdmin(Admin admin){
        this.admin=admin;
        initModel();
    }

    @FXML
    private void initModel() {
        try{
            model.setAll((List<RaceDTO>) (List<?>)server.getRaceEntriesByEngineCapacity());
        }
        catch(MotoException ex){
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    public void initialize(){
        tableColumnName.setCellValueFactory(new PropertyValueFactory<RaceDTO,String>("raceName"));
        tableColumnEngineCapacity.setCellValueFactory(new PropertyValueFactory<RaceDTO,Integer>("engineCapacity"));
        tableColumnParticipants.setCellValueFactory(new PropertyValueFactory<RaceDTO,Integer>("participants"));
        tableView.setItems(model);
    }

    public void handleButtonSearch(ActionEvent actionEvent) {
        Stage currentStage = (Stage) tableView.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/searchview.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 400, 400);
            stage.setTitle("Search");
            stage.setScene(scene);

            SearchController searchController =fxmlLoader.getController();
            searchController.setSrv(server);

            stage.show();
        }
        catch(IOException ex){

        }
    }

    public void handleButtonSignUp(ActionEvent actionEvent) {
        Stage currentStage = (Stage) tableView.getScene().getWindow();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("views/entryview.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 329, 400);
            stage.setTitle("Enroll");
            stage.setScene(scene);

            EntryController entryController =fxmlLoader.getController();
            entryController.setService(server);
            stage.show();
        }
        catch(IOException ex){

        }
    }

    public void handleButtonLogOut() {
        try{
            server.logout(admin,this);
            Stage currentStage = (Stage) tableView.getScene().getWindow();
            currentStage.close();
        }
        catch(MotoException ex){
            System.err.println(ex.getMessage());
        }

    }


    @Override
    public void entryAdded() throws MotoException {
        Platform.runLater(this::initModel);
    }
}
