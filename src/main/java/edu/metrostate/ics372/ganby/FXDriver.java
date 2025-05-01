package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.dataprocessing.PersistenceManager;
import edu.metrostate.ics372.ganby.user.UserManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXDriver extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader;
        Parent root;

        if (UserManager.userExists()) {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("LoginView.fxml"));
            root = loader.load();
            primaryStage.setTitle("User Login");
        } else {
            loader = new FXMLLoader(getClass().getClassLoader().getResource("CreateUserView.fxml"));
            root = loader.load();
            primaryStage.setTitle("Create User");
        }

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void launchMainApp(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(FXDriver.class.getClassLoader().getResource("FXAPP-View.fxml"));
        Parent root = loader.load();

        // ✅ Only here is FXController valid
        edu.metrostate.ics372.ganby.FXAPP.FXController controller = loader.getController();
        if (controller != null) {
            PersistenceManager.loadAutosave(controller);
        }

        stage.setTitle("Vehicle Tracking System");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() {
        PersistenceManager.saveAutosave();  // Save on exit
    }

    public static void main(String[] args) {
        launch();
    }
}
