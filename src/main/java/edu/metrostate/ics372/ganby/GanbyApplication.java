package edu.metrostate.ics372.ganby;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GanbyApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("dealer-view.fxml"));

            // Log to console in case the resource is not found
            if (fxmlLoader == null) {
                throw new RuntimeException("FXML file not found in the resources folder!");
            }

            // Load FXML and set up scene
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Ganby Application!");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Failed to load FXML file: " + e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}