package com.example.newsrecommendationsystem;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Choose_Person.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Start background task for multiple users
        String username = "user1";  // Simulating user login
        startBackgroundTask(username);
    }

    private void startBackgroundTask(String username) {
        Task<Void> backgroundTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Simulate a time-consuming operation
                System.out.println("Fetching recommendations for user: " + username);
                Thread.sleep(2000); // Simulate delay for article fetching
                return null;
            }

            @Override
            protected void succeeded() {
                // Task completed successfully, log to console
                System.out.println("Background task completed for " + username + "!");
            }

            @Override
            protected void failed() {
                // Task failed, log to console
                System.out.println("Background task failed for " + username + "!");
            }
        };

        Thread taskThread = new Thread(backgroundTask);
        taskThread.setDaemon(true); // Ensures the thread terminates when the app closes
        taskThread.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
