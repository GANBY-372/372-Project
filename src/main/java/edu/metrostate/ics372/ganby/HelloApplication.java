package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.dataprocessing.DataExporter;
import edu.metrostate.ics372.ganby.dataprocessing.JSONFileImporter;
import edu.metrostate.ics372.ganby.FXAPP.FXController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class HelloApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("dealer-view-old.fxml"));

        Parent root = fxmlLoader.load();
        System.out.println("FXML file loaded successfully");

        FXController controller = fxmlLoader.getController();

        String autosavePath = "src/main/resources/Auto_Save/dealer_catalog_autosave.json";
        File autosaveFile = new File(autosavePath);
        if (autosaveFile.exists()) {
            try {
                JSONFileImporter importer = new JSONFileImporter(autosavePath);
                importer.processJSON(); // ✅ Import here

                controller.loadData();  // ✅ Refresh UI with imported data

                System.out.println("Successfully loaded JSON from: " + autosavePath);
                System.out.println("Autosave data imported on boot.");
            } catch (Exception e) {
                System.err.println("Error loading autosave data: " + e.getMessage());
            }
        }

        Scene scene = new Scene(root);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        DataExporter exporter = new DataExporter();

        String filePath = "src/main/resources/Auto_Save/dealer_catalog_autosave.json";
        exporter.saveStateToFile(filePath);

        System.out.println("Dealer catalog autosaved to " + filePath + " on application exit.");
    }
    public static void main(String[] args) {
        launch();
    }
}