package com.example.newsrecommendationsystem;

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

    // Handle login logic when the user clicks on the Login button
    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Example hardcoded credentials for demonstration purposes
        if ("admin".equals(username) && "admin123".equals(password)) {
            // If credentials match, show success message and proceed to the next scene (Admin Dashboard or Main Menu)
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Login Success");
            alert.setHeaderText(null);
            alert.setContentText("Successfully logged in as Admin!");
            alert.showAndWait();

            // Load next screen (Admin Dashboard, for example)
            loadAdminDashboard();
        } else {
            // If credentials do not match, show error message
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password. Please try again.");
            alert.showAndWait();
        }
    }

    // Load the Admin Dashboard screen after successful login
    private void loadAdminDashboard() {
        try {
            // Assuming the Admin Dashboard FXML is called "AdminDashboard.fxml"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/newsrecommendationsystem/AdminDashboard.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow(); // Get current window
            Scene scene = new Scene(loader.load());
            stage.setScene(scene); // Switch to the Admin Dashboard scene
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred while loading the Admin Dashboard.");
            alert.showAndWait();
        }
    }
}
