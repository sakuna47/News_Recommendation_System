package com.example.newsrecommendationsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserViewController {

    @FXML
    private Button Business;

    @FXML
    private Button LNews;

    @FXML
    private Button WNews;

    @FXML
    private Button back2login;

    @FXML
    private Button entertainment;

    @FXML
    private Button health;

    @FXML
    private Button politics;

    @FXML
    private Button recommendation;

    @FXML
    private Button sport;

    @FXML
    private Button weather;

    @FXML
    private Button deleteAccount;

    @FXML
    void onback2logincick(ActionEvent event) {
        try {
            // Load the login page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserLogin.fxml"));
            AnchorPane loginPage = loader.load();

            // Set the scene with the login page
            Stage stage = (Stage) back2login.getScene().getWindow();
            stage.setScene(new Scene(loginPage));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
