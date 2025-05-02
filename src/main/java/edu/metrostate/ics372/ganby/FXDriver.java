package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.dataprocessing.PersistenceManager;
import edu.metrostate.ics372.ganby.user.UserManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main JavaFX entry point for the Vehicle Tracking System application.
 * Handles login screen vs. user creation logic, and initializes the GUI.
 */
public class FXDriver extends Application {

    /**
     * Entry point when the JavaFX application starts.
     * Displays login or user creation screen depending on saved user state.
     *
     * @param primaryStage the main application stage (window)
     * @throws Exception if FXML loading fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader;
        Parent root;

        // If user data exists, show login screen
        if (UserManager.userExists()) {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("LoginView.fxml"));
            root = loader.load();
            primaryStage.setTitle("User Login");
        } else {
            // Otherwise prompt to create user
            loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateUserView.fxml"));
            root = loader.load();
            primaryStage.setTitle("Create User");
        }

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Launches the main vehicle management UI after user logs in.
     * Loads autosaved data into the controller if available.
     *
     * @param stage the JavaFX stage passed from login controller
     * @throws Exception if FXML loading or controller retrieval fails
     */
    public static void launchMainApp(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(FXDriver.class.getClassLoader().getResource("FXAPP-View.fxml"));
        Parent root = loader.load();

        // Retrieve FXController and load autosaved data
        edu.metrostate.ics372.ganby.FXAPP.FXController controller = loader.getController();
        if (controller != null) {
            PersistenceManager.loadAutosave(controller);
        }

        stage.setTitle("Vehicle Tracking System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Called when the application is about to close.
     * Saves application state (autosave).
     */
    @Override
    public void stop() {
        PersistenceManager.saveAutosave();
    }

    /**
     * Main method to launch the JavaFX application.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        launch();  // Launch JavaFX
    }
}
