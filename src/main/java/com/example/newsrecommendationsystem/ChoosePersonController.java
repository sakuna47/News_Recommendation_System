package com.example.newsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ChoosePersonController {

    @FXML
    private Button ChooseSystemAdmin;

    // Method for handling Reader button click
    @FXML
    public void handleReaderClick(ActionEvent event1) {
        try {
            // Load the UserLogin.fxml file
            Parent loginView = FXMLLoader.load(getClass().getResource("UserLogin.fxml"));

            // Set the scene with the loaded FXML
            Scene loginScene = new Scene(loginView);

            // Get the current stage and set the new scene
            Stage currentStage = (Stage) ((Node) event1.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("User Login");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading UserLogin.fxml");
        }
    }

    // Method for handling System Admin button click
    @FXML
    public void handleSystemAdminClick() {
        try {
            // Load the AdminLogin.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminLogin.fxml"));
            AnchorPane adminLoginRoot = loader.load();

            // Get the current stage and set a new scene for Admin Login
            Stage stage = (Stage) ChooseSystemAdmin.getScene().getWindow();
            Scene scene = new Scene(adminLoginRoot);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
