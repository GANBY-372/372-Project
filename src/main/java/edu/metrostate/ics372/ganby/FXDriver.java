package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.FXAPP.FXController;
import edu.metrostate.ics372.ganby.dataprocessing.PersistenceManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXDriver extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("FXAPP-View.fxml"));
        Parent root = loader.load();

        FXController controller = loader.getController();
//        PersistenceManager.loadAutosave(controller);  // Load saved data

        primaryStage.setTitle("Vehicle Tracking System");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() {
//        PersistenceManager.saveAutosave();  // Save on exit
    }

    public static void main(String[] args) {
        launch();

    }
}
