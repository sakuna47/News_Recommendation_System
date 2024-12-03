package com.example.newsrecommendationsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HelloApplication extends Application {

    private ExecutorService executorService;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("Choose_Person.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        // Initialize the executor service to handle tasks concurrently
        executorService = Executors.newFixedThreadPool(10);  // can increase the number here to handle more concurrent tasks

        // Simulate multiple users logging in
        List<String> usernames = Arrays.asList("user1", "user2", "user3", "user4", "user5");
        startBackgroundTasks(usernames);
    }

    private void startBackgroundTasks(List<String> usernames) {
        for (String username : usernames) {
            executorService.submit(() -> {
                try {
                    System.out.println("Fetching recommendations for user: " + username);
                    Thread.sleep(2000); // Simulate delay (can be replaced by actual processing logic)
                    System.out.println("Recommendations fetched for " + username);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Task interrupted for " + username);
                }
            });
        }
    }

    @Override
    public void stop() {
        // Shut down the executor service when the application stops
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
