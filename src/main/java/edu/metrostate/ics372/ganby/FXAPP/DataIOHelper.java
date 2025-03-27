package edu.metrostate.ics372.ganby.FXAPP;

import edu.metrostate.ics372.ganby.dataprocessing.DataExporter;
import edu.metrostate.ics372.ganby.dataprocessing.JSONFileImporter;
import edu.metrostate.ics372.ganby.dataprocessing.XMLFileImporter;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for importing and exporting dealer/vehicle data from/to JSON and XML formats.
 */
public class DataIOHelper {

    /**
     * Imports data from a JSON file and populates the dealer table.
     *
     * @param stage          the parent stage used for file dialog
     * @param dealerList     the dealer observable list to update
     * @param dealerTable    the dealer TableView to refresh
     */
    public static void importJSON(Stage stage, ObservableList<Dealer> dealerList, TableView<Dealer> dealerTable) {
        try {
            JSONFileImporter importer = new JSONFileImporter(stage);
            importer.processJSON();
            dealerList.setAll(DealerCatalog.getInstance().getDealers());
            dealerTable.setItems(dealerList);
            showAlert(Alert.AlertType.INFORMATION, "Import Successful", "JSON file successfully imported!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Import Failed", "Error importing JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Imports data from an XML file and populates the dealer and vehicle tables.
     *
     * @param stage             the parent stage used for file dialog
     * @param dealerList        the dealer observable list to update
     * @param dealerTable       the dealer TableView to refresh
     * @param vehicleList       the vehicle observable list to clear
     * @param vehicleTable      the vehicle TableView to clear
     */
    public static void importXML(Stage stage, ObservableList<Dealer> dealerList, TableView<Dealer> dealerTable,
                                 ObservableList<Vehicle> vehicleList, TableView<Vehicle> vehicleTable) {
        try {
            XMLFileImporter xmlImporter = new XMLFileImporter(stage);
            Document doc = xmlImporter.getXmlDocument();

            if (doc != null) {
                xmlImporter.processXML(doc);
                dealerList.setAll(DealerCatalog.getInstance().getDealers());
                dealerTable.setItems(dealerList);
                vehicleList.clear();
                vehicleTable.setItems(vehicleList);
                showAlert(Alert.AlertType.INFORMATION, "Import Successful", "XML file successfully imported!");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Import Failed", "Error importing XML:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Prompts the user to export the current dealer catalog to a file.
     *
     * @param stage the stage used for file selection
     */
    public static void exportData(Stage stage, TableView<Dealer> dealerTable) {
        List<Dealer> allDealers = dealerTable.getItems();
        new DataExporter().promptAndExportDealers(stage, allDealers);
    }


    /**
     * Exports only the dealers that are selected (checkbox is checked).
     *
     * @param stage the stage used for file chooser dialog
     * @param dealerTable the dealer TableView to check for selected dealers
     */
    public static void exportSelectedDealers(Stage stage, TableView<Dealer> dealerTable) {
        List<Dealer> selectedDealers = dealerTable.getItems().stream()
                .filter(Dealer::isSelected)
                .collect(Collectors.toList());

        if (selectedDealers.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No Dealers Selected", "Please select dealer(s) to export.");
            return;
        }

        try {
            new DataExporter().promptAndExportDealers(stage, selectedDealers);
            showAlert(Alert.AlertType.INFORMATION, "Export Successful", "Selected dealers exported successfully.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Export Failed", "Failed to export dealers:\n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
