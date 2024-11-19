package com.example.newsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserLoginController {

    @FXML
    public void handleSignUpClick(ActionEvent event) {
        try {
            // Load the SignUp.fxml file
            Parent signUpView = FXMLLoader.load(getClass().getResource("SignUp.fxml"));

            // Set the scene with the loaded FXML
            Scene signUpScene = new Scene(signUpView);

            // Get the current stage and set the new scene
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(signUpScene);
            currentStage.setTitle("Sign Up");
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading SignUp.fxml");
        }
    }

    @FXML
    public void handleBackClick(ActionEvent event) {
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
    }
}
