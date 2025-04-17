package edu.metrostate.ics372.ganby.dataprocessing;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class FileSelector {

    public static File chooseJsonOpenFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        return fileChooser.showOpenDialog(stage);
    }

    public static File chooseJsonSaveFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save JSON File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        fileChooser.setInitialFileName("selected_dealers.json");
        return fileChooser.showSaveDialog(stage);
    }

    public static File chooseXmlOpenFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        return fileChooser.showOpenDialog(stage);
    }

    public static File chooseXmlSaveFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        fileChooser.setInitialFileName("selected_dealers.xml");
        return fileChooser.showSaveDialog(stage);
    }
}
