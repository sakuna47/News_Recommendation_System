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
            System.out.println("All fields must be filled!");
            return;
        }

        // Check if the password and confirm password match
        if (!password.equals(confirmPassword)) {
            System.out.println("Passwords do not match!");
            return;
        }

        // Validate email format
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format!");
            return;
        }

        try {
            // Check if the username already exists
            MongoCollection<Document> usersCollection = Database.getDatabase().getCollection("Users");
            Document existingUser = usersCollection.find(new Document("name", username)).first();
            if (existingUser != null) {
                System.out.println("Username already taken!");
                return;
            }

            // Check if the email already exists
            Document existingEmail = usersCollection.find(new Document("email", email)).first();
            if (existingEmail != null) {
                System.out.println("Email is already associated with an account!");
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

            System.out.println("User registered successfully!");

            // Simulate email verification (you should integrate actual email service here)
            sendVerificationEmail(email);

            System.out.println("Verification email sent to: " + email);

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
            System.err.println("Error loading UserLogin.fxml.");
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
}
