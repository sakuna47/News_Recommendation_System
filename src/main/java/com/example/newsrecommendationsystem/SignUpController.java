package com.example.newsrecommendationsystem;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
 // Import BCrypt for password hashing

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField; // Add a confirm password field

    /**
     * Handles the sign-up button click. This method collects the user's input,
     * validates it, hashes the password, and saves it to MongoDB.
     */
    @FXML
    private void handleSignUpClick(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            System.out.println("All fields must be filled!");
            return;
        }

        // Check if the password and confirm password match
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        try {
            // Hash the password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Get the "users" collection
            MongoCollection<Document> usersCollection = Database.getDatabase().getCollection("Users");

            // Create a new user document
            Document user = new Document()
                    .append("name", username)
                    .append("email", email)
                    .append("password", hashedPassword); // Store hashed password

            // Insert the document into the collection
            usersCollection.insertOne(user);

            System.out.println("User registered successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while saving user to MongoDB.");
        }
    }

    /**
     * Handles the back button click. This method navigates back to the login screen.
     */
    @FXML
    private void handleBackClick(ActionEvent event) {
        try {
            // Load the UserLogin.fxml file
            Parent loginView = FXMLLoader.load(getClass().getResource("UserLogin.fxml"));

            // Set the scene with the loaded FXML
            Scene loginScene = new Scene(loginView);

            // Get the current stage and set the new scene
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("User Login");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading UserLogin.fxml.");
        }
    }
}
