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
            importer.processJSON();
            dealerList.setAll(DealerCatalog.getInstance().getDealers());
            dealerTable.setItems(dealerList);
            FXController.showAlert(Alert.AlertType.INFORMATION, "Import Successful", "JSON file successfully imported!");
        } catch (Exception e) {
            // Log the error message
            LOGGER.severe("Error importing XML: " + e.getMessage());

            // Optionally log the stack trace in a more controlled way
            LOGGER.log(java.util.logging.Level.SEVERE, "Error Details", e);

            // Show the alert with the error message
            FXController.showAlert(Alert.AlertType.ERROR, "Import Failed", "Error importing JSON:\n" + e.getMessage());
        }
    }

    /**
     * Imports dealer and vehicle data from an XML file.
     *
     * @param stage         the JavaFX stage used for file chooser
     * @param dealerList    the observable list of dealers to update
     * @param dealerTable   the TableView to update
     * @param vehicleList   the observable list of vehicles to clear
     * @param vehicleTable  the TableView to clear
     */
    public static void importXML(Stage stage,
                                 ObservableList<Dealer> dealerList,
                                 TableView<Dealer> dealerTable,
                                 ObservableList<Vehicle> vehicleList,
                                 TableView<Vehicle> vehicleTable) {
        try {
            XMLFileImporter xmlImporter = new XMLFileImporter(stage);
            Document doc = xmlImporter.getXmlDocument();

            if (doc != null) {
                xmlImporter.processXML();
                dealerList.setAll(DealerCatalog.getInstance().getDealers());
                dealerTable.setItems(dealerList);
                vehicleList.clear();
                vehicleTable.setItems(vehicleList);
                FXController.showAlert(Alert.AlertType.INFORMATION, "Import Successful", "XML file successfully imported!");
            }
        } catch (Exception e) {
            // Log the error message
            LOGGER.severe("Error importing XML: " + e.getMessage());

            // Optionally log the stack trace in a more controlled way
            LOGGER.log(java.util.logging.Level.SEVERE, "Error Details", e);

            // Show the alert with the error message
            FXController.showAlert(Alert.AlertType.ERROR, "Import Failed", "Error importing XML:\n" + e.getMessage());
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
            FXController.showAlert(Alert.AlertType.WARNING, "No Dealers Selected", "Please select dealer(s) to export.");
            return;
        }

        new JSONFileExporter().exportDealers(stage, selectedDealers);
    }

    /**
     * Exports selected dealers as an XML file using XMLFileExporter.
     *
     * @param stage       the JavaFX stage used for file dialog
     * @param dealerTable the TableView containing selected dealers
     */
    public static void exportXML(Stage stage, TableView<Dealer> dealerTable) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .collect(Collectors.toList());

        if (selectedDealers.isEmpty()) {
            FXController.showAlert(Alert.AlertType.WARNING, "No Dealers Selected", "Please select dealer(s) to export.");
            return;
        }

        new XMLFileExporter().exportDealers(stage, selectedDealers);
    }
}
