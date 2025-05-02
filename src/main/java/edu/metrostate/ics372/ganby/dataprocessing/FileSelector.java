package edu.metrostate.ics372.ganby.dataprocessing;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

/**
 * Utility class to centralize file selection dialogs for importing and exporting JSON and XML files.
 * Provides methods to open and save files with specific filters and titles for consistency.
 */
public class FileSelector {

    /**
     * Opens a FileChooser to select a JSON file to open.
     *
     * @param stage the parent stage for the dialog
     * @return the selected JSON file, or null if canceled
     */
    public static File chooseJsonOpenFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select JSON File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        return fileChooser.showOpenDialog(stage);
    }

    /**
     * Opens a FileChooser to choose a location to save a JSON file.
     * Sets a default filename for convenience.
     *
     * @param stage the parent stage for the dialog
     * @return the file selected for saving, or null if canceled
     */
    public static File chooseJsonSaveFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save JSON File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JSON Files", "*.json")
        );
        fileChooser.setInitialFileName("selected_dealers.json");
        return fileChooser.showSaveDialog(stage);
    }

    /**
     * Opens a FileChooser to select an XML file to open.
     *
     * @param stage the parent stage for the dialog
     * @return the selected XML file, or null if canceled
     */
    public static File chooseXmlOpenFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select XML File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        return fileChooser.showOpenDialog(stage);
    }

    /**
     * Opens a FileChooser to choose a location to save an XML file.
     * Sets a default filename for convenience.
     *
     * @param stage the parent stage for the dialog
     * @return the file selected for saving, or null if canceled
     */
    public static File chooseXmlSaveFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("XML Files", "*.xml")
        );
        fileChooser.setInitialFileName("selected_dealers.xml");
        return fileChooser.showSaveDialog(stage);
    }
}
