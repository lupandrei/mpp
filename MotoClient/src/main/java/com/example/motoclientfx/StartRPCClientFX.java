package com.example.motoclientfx;

import com.example.motoclientfx.gui.LoginController;
import com.example.motoclientfx.gui.adminController;
import com.example.persistence.IAdminRepo;
import com.example.rpcprotocol.TransportServiceRpcProxy;
import com.example.service.MotoService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartRPCClientFX extends Application {
    private Stage primaryStage;
    private static final int defaultChatPort = 55555;
    private static final String defaultServer = "localhost";

    public StartRPCClientFX() {
    }

    public void start(Stage primaryStage) throws Exception {
        System.out.println("Starting client...");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRPCClientFX.class.getResourceAsStream("/motoclient.properties"));
            System.out.println("Client properties set...");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties: " + e);
            return;
        }

        String serverIP = clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong port number: " + e);
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP: " + serverIP);
        System.out.println("Using server port: " + serverPort);

        MotoService server = new TransportServiceRpcProxy(serverIP, serverPort);
        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("views/loginview.fxml"));
        Parent root = loader.load();
        LoginController loginController = loader.getController();
        loginController.setServer(server);

        FXMLLoader mainLoader = new FXMLLoader(this.getClass().getClassLoader().getResource("views/adminView.fxml"));
        Parent mainRoot = mainLoader.load();
        adminController mainController = mainLoader.getController();
        mainController.setServer(server);
        loginController.setMainController(mainController);
        loginController.setMainParent(mainRoot);

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}
