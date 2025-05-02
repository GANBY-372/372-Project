package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dataprocessing.JSONFileExporter;
import edu.metrostate.ics372.ganby.dataprocessing.JSONFileImporter;
import edu.metrostate.ics372.ganby.dataprocessing.XMLFileExporter;
import edu.metrostate.ics372.ganby.dataprocessing.XMLFileImporter;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import java.util.logging.Logger;


import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for importing and exporting dealer and vehicle data in JSON and XML formats.
 * Provides support for user-interactive prompts and handles updates to JavaFX UI components.
 */
public class DataIOHelper {
    private static final Logger LOGGER = Logger.getLogger(DataIOHelper.class.getName());

    /**
     * Imports dealer and vehicle data from a JSON file.
     *
     * @param stage       the JavaFX stage used for file chooser
     * @param dealerList  the observable list of dealers to update
     * @param dealerTable the TableView to refresh
     */
    public static void importJSON(Stage stage, ObservableList<Dealer> dealerList, TableView<Dealer> dealerTable) {
        try {
            JSONFileImporter importer = new JSONFileImporter(stage);

            // Check if the process was cancelled
            boolean isCancelled = !importer.processJSON();  // Assuming processJSON() returns false if canceled
            if (isCancelled) {
                AlertHelper.showSuccess( "Import Cancelled", "JSON import was canceled.");
                return;
            }

            // If not cancelled, proceed with the import
            dealerList.setAll(DealerCatalog.getInstance().getDealers());
            dealerTable.setItems(dealerList);
            AlertHelper.showSuccess("Import Successful", "JSON file successfully imported!");
        } catch (Exception e) {
            // Log the error message
            LOGGER.severe("Error importing JSON: " + e.getMessage());

            // Optionally log the stack trace in a more controlled way
            LOGGER.log(java.util.logging.Level.SEVERE, "Error Details", e);

            // Show the alert with the error message
            AlertHelper.showError( "Import Failed", "Error importing JSON:\n" + e.getMessage());
        }
    }

    /**
     * Imports dealer and vehicle data from an XML file.
     *
     * @param stage        the JavaFX stage used for file chooser
     * @param dealerList   the observable list of dealers to update
     * @param dealerTable  the TableView to update
     * @param vehicleList  the observable list of vehicles to clear
     * @param vehicleTable the TableView to clear
     */
    public static void importXML(Stage stage,
                                 ObservableList<Dealer> dealerList,
                                 TableView<Dealer> dealerTable,
                                 ObservableList<Vehicle> vehicleList,
                                 TableView<Vehicle> vehicleTable) {
        try {
            XMLFileImporter xmlImporter = new XMLFileImporter(stage);
            Document doc = xmlImporter.getXmlDocument();

            if (doc == null) {
                // User cancelled the file selection, show cancellation message
                AlertHelper.showSuccess("Import Cancelled", "XML import was cancelled.");
                return;
            }

            // If XML file is valid, proceed with import
            xmlImporter.processXML();
            dealerList.setAll(DealerCatalog.getInstance().getDealers());
            dealerTable.setItems(dealerList);
            vehicleList.clear();
            vehicleTable.setItems(vehicleList);
            AlertHelper.showSuccess("Import Successful", "XML file successfully imported!");
        } catch (Exception e) {
            // Log the error message
            LOGGER.severe("Error importing XML: " + e.getMessage());

            // Optionally log the stack trace in a more controlled way
            LOGGER.log(java.util.logging.Level.SEVERE, "Error Details", e);

            // Show the alert with the error message
            AlertHelper.showError( "Import Failed", "Error importing XML:\n" + e.getMessage());
        }
    }


    /**
     * Exports selected dealers as a JSON file using JSONFileExporter.
     *
     * @param stage       the JavaFX stage used for file dialog
     * @param dealerTable the TableView containing selected dealers
     */
    public static void exportJSON(Stage stage, TableView<Dealer> dealerTable) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .collect(Collectors.toList());

        if (selectedDealers.isEmpty()) {
            AlertHelper.showWarning("No Dealers Selected", "Please select dealer(s) to export.");
            return;
        }

        JSONFileExporter jsonExporter = new JSONFileExporter();
        boolean exportSuccessful = jsonExporter.exportDealers(stage, selectedDealers);

        if (!exportSuccessful) {
            AlertHelper.showSuccess( "Export Cancelled", "JSON export was cancelled.");
        }
    }



    /**
     * Exports the data of selected dealers (and their vehicles) to an XML file.
     * This method filters the dealer table for selected dealers, prompts the user
     * to choose a save location using a file dialog, and exports the data to XML format
     * using {@link XMLFileExporter}. If no dealers are selected, a warning is shown.
     * If the export is cancelled by the user, a success alert is shown with a cancellation message.
     *
     * @param stage       the JavaFX stage used for displaying the file chooser dialog
     * @param dealerTable the TableView containing dealer data and selection checkboxes
     */
    public static void exportXML(Stage stage, TableView<Dealer> dealerTable) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .collect(Collectors.toList());

        if (selectedDealers.isEmpty()) {
            AlertHelper.showWarning("No Dealers Selected", "Please select dealer(s) to export.");
            return;
        }

        XMLFileExporter xmlExporter = new XMLFileExporter();
        boolean exportSuccessful = xmlExporter.exportDealers(stage, selectedDealers);

        if (!exportSuccessful) {
            AlertHelper.showSuccess("Export Cancelled", "XML export was cancelled.");
        }
    }
}
