package com.example.newsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UserLoginController {

    @FXML
    private Button Back_Login;

    @FXML
    private PasswordField L_Password;

    @FXML
    private TextField L_User_Name;

    @FXML
    private Button Login;

    @FXML
    private Button Sign;

    @FXML
    private void handleBackClick(ActionEvent event) {
        try {
            // Load the ChoosePerson.fxml file
            Parent choosePersonView = FXMLLoader.load(getClass().getResource("Choose_Person.fxml"));

            // Set the scene with the loaded FXML
            Scene choosePersonScene = new Scene(choosePersonView);

            // Get the current stage and set the new scene
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(choosePersonScene);
            currentStage.setTitle("Choose Person");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading ChoosePerson.fxml");
        }

}}
