
package edu.metrostate.ics372.ganby;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a Label to display "Hello, JavaFX!"
        Label helloLabel = new Label("Hello, JavaFX!");

        // Create a layout pane to hold the label
        StackPane root = new StackPane();
        root.getChildren().add(helloLabel);

        // Create a Scene with the layout pane
        Scene scene = new Scene(root, 300, 200); // Width: 300, Height: 200

        // Set the title of the Stage (window)
        primaryStage.setTitle("Hello JavaFX Application");

        // Set the Scene for the Stage
        primaryStage.setScene(scene);

        // Show the Stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}