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
    public static void exportData(Stage stage) {
        new DataExporter().promptAndExportCatalog(stage);
    }

    private static void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
