package com.example.newsrecommendationsystem.Controllers.User;

import com.example.newsrecommendationsystem.Database.Database;
import com.example.newsrecommendationsystem.Service.SessionManager;
import com.mongodb.client.MongoCollection;
import javafx.fxml.FXMLLoader;
import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class UserLoginController {

    @FXML
    private TextField L_usernameField;

    @FXML
    private TextField L_emailField;

    @FXML
    private PasswordField L_password;

    public String curentUser;

    // Handle login button click
    @FXML
    public void handleLoginClick(ActionEvent event) {
        String email = L_emailField.getText().trim();
        String password = L_password.getText().trim();

        // Check if email, username, and password fields are empty
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Username, email, and password are required.");
            return;
        }

        // Get the "users" collection from MongoDB
        MongoCollection<Document> usersCollection = Database.getDatabase().getCollection("Users");

        // Build a query using either username or email
        Document query = new Document();
        if (!email.isEmpty()) {
            query.append("email", email);
        }

        // Debugging: Log the username/email being searched
        System.out.println("Attempting to find user with email: " + email);

        // Query the database for the user with the provided username/email
        Document userDocument = usersCollection.find(query).first();

        // If the user is not found in the database
        if (userDocument == null) {
            System.out.println("User not found!"); // Debugging: Log user not found
            showAlert("Error", "User not found!");
            return;
        }

        // Get the stored hashed password from the database
        String storedHashedPassword = userDocument.getString("password");

        // Verify the entered password with the stored hashed password
        if (BCrypt.checkpw(password, storedHashedPassword)) {
            // If passwords match, login is successful, proceed to the UserView screen
            navigateToUserView(event);
        } else {
            // If password is incorrect
            showAlert("Error", "Invalid password!");
        }
    }

    // Navigate to the UserView screen after successful login
    private void navigateToUserView(ActionEvent event) {
        try {
            // Load the UserView.fxml file
            Parent userView = FXMLLoader.load(getClass().getResource("/com/example/newsrecommendationsystem/User/UserView.fxml"));
            Scene userViewScene = new Scene(userView);

            // Get the current stage and set the new scene
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(userViewScene);
            currentStage.setTitle("User View");
            currentStage.show();
            SessionManager.getInstance().setUsername(L_emailField.getText());

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load the user view.");
        }
    }

    // Show alert messages for errors or success
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Handle the Sign Up button click
    @FXML
    public void handleSignUpClick(ActionEvent event) {
        try {
            // Load the SignUp.fxml file
            Parent signUpView = FXMLLoader.load(getClass().getResource("/com/example/newsrecommendationsystem/User/SignUp.fxml"));
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

    // Handle the Back button click to return to the previous screen
    @FXML
    public void handleBackClick(ActionEvent event) {
        try {
            // Load the ChoosePerson.fxml file
            Parent choosePersonView = FXMLLoader.load(getClass().getResource("/com/example/newsrecommendationsystem/Choose_Person.fxml"));
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
