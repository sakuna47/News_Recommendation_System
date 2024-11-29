package com.example.newsrecommendationsystem.Controllers.User;

import com.example.newsrecommendationsystem.Database.Database;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

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
            showAlert("Error", "All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }

        // Check if the password and confirm password match
        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match!", Alert.AlertType.ERROR);
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            showAlert("Error", "Invalid email format!", Alert.AlertType.ERROR);
            return;
        }

        try {
            MongoCollection<Document> usersCollection = Database.getDatabase().getCollection("Users");

            // Check if the username already exists
            Document existingUser = usersCollection.find(new Document("name", username)).first();
            if (existingUser != null) {
                showAlert("Error", "Username already taken!", Alert.AlertType.ERROR);
                return;
            }

            // Check if the email already exists
            Document existingEmail = usersCollection.find(new Document("email", email)).first();
            if (existingEmail != null) {
                showAlert("Error", "Email is already associated with an account!", Alert.AlertType.ERROR);
                return;
            }

            // Hash the password using BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            // Create a new user document
            Document user = new Document()
                    .append("name", username)
                    .append("email", email)
                    .append("password", hashedPassword)
                    .append("emailVerified", false); // Flag for email verification

            // Insert the document into the collection
            usersCollection.insertOne(user);

            // Simulate email verification
            sendVerificationEmail(email);

            // Success message
            showAlert("Success",
                    "User registered successfully!\n\n" +
                            "Verification email sent to: " + email + ". Please check your inbox.",
                    Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Error while saving user to MongoDB.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Handles the back button click. This method navigates back to the login screen.
     */
    @FXML
    private void handleBackClick(ActionEvent event) {
        try {
            // Load the UserLogin.fxml file
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/example/newsrecommendationsystem/User/UserLogin.fxml"));

            // Set the scene with the loaded FXML
            Scene loginScene = new Scene(loginView);

            // Get the current stage and set the new scene
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(loginScene);
            currentStage.setTitle("User Login");
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Error loading UserLogin.fxml.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Validates the email format using a regular expression.
     * @param email The email to validate.
     * @return true if the email format is valid, false otherwise.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Simulates sending a verification email to the user.
     * In a real scenario, you would integrate an email service like SMTP or a third-party service.
     * @param email The user's email.
     */
    private void sendVerificationEmail(String email) {
        // Simulate the email sending process
        System.out.println("Verification email sent to " + email + ". Please check your inbox.");

        // You can implement actual email service here
        // For example, use JavaMail API or a third-party service like SendGrid, Mailgun, etc.
    }

    /**
     * Utility method to show an alert dialog.
     *
     * @param title  The title of the alert.
     * @param message The content of the alert.
     * @param alertType The type of the alert (e.g., ERROR, INFORMATION).
     */
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
