package com.example.motoclientfx.gui;


import com.example.model.Admin;
import com.example.persistence.RepositoryException;
import com.example.service.MotoException;
import com.example.service.MotoService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    private MotoService server;

    private adminController adminController;

    private Parent mainParent;
    private Admin admin;

    @FXML
    Button login;

    @FXML
    TextField textfield;

    @FXML
    PasswordField passwordField;

    public void setServer(MotoService srv){
        this.server=srv;
    }
    public void setMainController(adminController mainCtrl) {
        this.adminController = mainCtrl;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
    public void setMainParent(Parent mainParent) {
        this.mainParent = mainParent;
    }



    public void handleLoginButton(ActionEvent actionEvent) {
        String username = textfield.getText();
        String password = passwordField.getText();
        admin = new Admin(username,password);
        try{
            server.loginAdmin(admin,adminController);
            connect(admin);
        }
        catch(MotoException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
    }

    private void connect(Admin admin) {
        Stage mainStage = new Stage();
        Scene mainScene = new Scene(mainParent);
        mainStage.setTitle("Routes");
        mainStage.setScene(mainScene);
        mainStage.setOnCloseRequest(closeEvent -> {
            adminController.handleButtonLogOut();
            System.exit(0);
        });
        mainStage.show();
        passwordField.getScene().getWindow().hide();
        adminController.setServer(server);
        adminController.setAdmin(admin);
    }
}
