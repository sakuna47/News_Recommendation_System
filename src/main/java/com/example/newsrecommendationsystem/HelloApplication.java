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

        // Example of using concurrency for a background task
        Task<Void> backgroundTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Simulate a time-consuming operation (e.g., recommending articles)
                Thread.sleep(2000); // Simulating a 2-second operation
                return null;
            }

            @Override
            protected void succeeded() {
                // Task completed successfully, no alert, just a log (optional)
                System.out.println("Background task completed successfully!");
            }

            @Override
            protected void failed() {
                // Task failed, no alert, just a log (optional)
                System.out.println("Background task failed!");
            }
        };

        // Run the background task asynchronously on a new thread
        Thread taskThread = new Thread(backgroundTask);
        taskThread.setDaemon(true); // Ensures the thread is terminated when the application exits
        taskThread.start();
    }

    public static void main(String[] args) {
        launch();
    }
}
