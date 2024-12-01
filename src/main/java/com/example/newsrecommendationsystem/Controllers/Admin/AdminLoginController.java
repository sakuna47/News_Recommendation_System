package com.example.newsrecommendationsystem.Controllers.Admin;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class AdminLoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;

    // Handle login logic when the user clicks the Login button
    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Updated credentials: username = "admin", password = "1234"
        if ("admin".equals(username) && "1234".equals(password)) {
            // Show success message and load the Admin View
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Login Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully logged in as Admin!");
            alert.showAndWait();

            loadAdminView();
        } else {
            // Show error message for incorrect credentials
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password. Please try again.");
            alert.showAndWait();
        }
    }

    // Load the Admin View FXML file after successful login
    private void loadAdminView() {
        try {
            // Admin View FXML is "AdminView.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/Admin/AdminView.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Get the current window
            Scene scene = new Scene(loader.load());
            stage.setScene(scene); // Switch to the Admin View scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading the Admin View.");
            alert.showAndWait();
        }
    }

    // Handle the Back button click to navigate to the "Choose Person" menu
    @FXML
    public void handleBackClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/Choose_Person.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow(); // Get the current window
            Scene scene = new Scene(loader.load());
            stage.setScene(scene); // Switch to the Choose Person menu scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading the Choose Person menu.");
            alert.showAndWait();
        }
    }
}
