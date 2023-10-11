package com.example.labmpp;

import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import repository.AdminDBRepo;
import repository.EntryDBRepo;
import repository.ParticipantDBRepo;
import repository.RaceDBRepo;
import service.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class MainDB extends Application {
    private Service srv;
    @Override
    public void start(Stage primaryStage) throws IOException{
        Properties props=new Properties();
        try {
            props.load(new FileReader("bd.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }
        AdminDBRepo adminDBRepo = new AdminDBRepo(props);
        ParticipantDBRepo participantDBRepo = new ParticipantDBRepo(props);
        RaceDBRepo raceDBRepo = new RaceDBRepo(props);
        EntryDBRepo entryDBRepo = new EntryDBRepo(props);
        srv = new Service(adminDBRepo,entryDBRepo,raceDBRepo,participantDBRepo);
        initView(primaryStage);
        primaryStage.show();
    }

    private void initView(Stage primaryStage) throws IOException{
        FXMLLoader loader =new FXMLLoader(getClass().getResource("views/loginview.fxml"));
        Scene scene = new Scene(loader.load(),600,400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Log in");

        LoginController loginController = loader.getController();
        loginController.setService(srv);
    }
    public static void main(String[] args) {
        launch(args);
        //admin
        /*AdminDBRepo adminDBRepo = new AdminDBRepo(props);
        Admin admin = new Admin("thomas","parola");
        adminDBRepo.add(admin);
        System.out.println(adminDBRepo.findByID("thomas"));

        //participant
        ParticipantDBRepo participantDBRepo = new ParticipantDBRepo(props);
        Participant participant = new Participant(100,"raul","lini","Honda");
        participantDBRepo.add(participant);
        System.out.println(participantDBRepo.findParticipantsByTeamName("Honda"));

        //race
        RaceDBRepo raceDBRepo = new RaceDBRepo(props);
        Race race = new Race(100,155,"newrace");
        raceDBRepo.add(race);
        System.out.println(raceDBRepo.findRaceByEngineCapacity(155));

        //entry
        EntryDBRepo entryDBRepo = new EntryDBRepo(props);
        Entry entry = new Entry(3,3);
        entryDBRepo.add(entry);
        List<ParticipantDTO> participantDTOS =entryDBRepo.getParticipantNameAndEngineCapacity();
        for(ParticipantDTO participantDTO:participantDTOS){
            System.out.println(participantDTO.getFirstName() +" "+ participantDTO.getLastName() + " "+ participantDTO.getEngineCapacity());
        }
        List<RaceDTO>raceDTOS = entryDBRepo.getRaceEntriesByEngineCapacity();
        for(RaceDTO raceDTO:raceDTOS){
            System.out.println(raceDTO.getRaceName() + " " + raceDTO.getEngineCapacity() +  " " +raceDTO.getParticipants());
        }*/
    }
}
