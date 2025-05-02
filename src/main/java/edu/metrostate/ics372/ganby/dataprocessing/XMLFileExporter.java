package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

/**
 * Utility class for exporting dealers and their vehicles to an XML file.
 * Supports both manual save via file dialog and silent autosave to a fixed path.
 */
public class XMLFileExporter {

    /**
     * Prompts the user with a save dialog and exports selected dealers to XML.
     *
     * @param stage           the current JavaFX window (used for dialog)
     * @param selectedDealers the list of dealers to export
     * @return true if export was successful; false if user canceled or error occurred
     */
    public boolean exportDealers(Stage stage, List<Dealer> selectedDealers) {
        File file = FileSelector.chooseXmlSaveFile(stage);
        if (file == null) {
            return false; // ❌ User cancelled export
        }

        try {
            exportToFile(file, selectedDealers, true);
            return true;
        } catch (Exception e) {
            showErrorAlert(e.getMessage());
            return false;
        }
    }

    /**
     * Exports dealer data directly to the specified XML file without showing a file chooser.
     * This is used for background autosave or system-driven exports.
     *
     * @param file       the file to write the XML data to
     * @param dealers    list of dealers to export
     * @param showAlert  if true, displays a success/failure alert to the user
     */
    public static void exportToFile(File file, List<Dealer> dealers, boolean showAlert) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Create root element <Dealers>
            Element dealersElement = doc.createElement("Dealers");
            doc.appendChild(dealersElement);

            // Append each dealer as an XML node
            for (Dealer dealer : dealers) {
                appendDealerToDocument(doc, dealersElement, dealer);
            }

            // Configure XML output formatting
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // Write the XML to file
            transformer.transform(new DOMSource(doc), new StreamResult(file));

            if (showAlert) {
                showSuccessAlert(file.getAbsolutePath());
            }

        } catch (Exception e) {
            if (showAlert) {
                showErrorAlert(e.getMessage());
            } else {
                System.err.println("❌ Autosave export failed: " + e.getMessage());
            }
        }
    }

    /**
     * Appends a single dealer and all of their vehicles to the XML document.
     *
     * @param doc    the XML document being built
     * @param parent the parent XML element (usually <Dealers>)
     * @param dealer the dealer to serialize
     */
    private static void appendDealerToDocument(Document doc, Element parent, Dealer dealer) {
        Element dealerElement = doc.createElement("Dealer");
        dealerElement.setAttribute("id", dealer.getId());
        parent.appendChild(dealerElement);

        // Dealer Name
        Element nameElement = doc.createElement("Name");
        nameElement.appendChild(doc.createTextNode(dealer.getName()));
        dealerElement.appendChild(nameElement);

        // Acquisition Status
        Element acquisitionStatus = doc.createElement("Acquisition_Status");
        acquisitionStatus.appendChild(doc.createTextNode(String.valueOf(dealer.isVehicleAcquisitionEnabled())));
        dealerElement.appendChild(acquisitionStatus);

        // Append all vehicles for this dealer
        for (Vehicle vehicle : dealer.vehicleCatalog.values()) {
            Element vehicleElement = doc.createElement("Vehicle");
            vehicleElement.setAttribute("type", vehicle.getType());
            vehicleElement.setAttribute("id", vehicle.getVehicleId());

            Element price = doc.createElement("Price");
            price.setAttribute("unit", "dollars");
            price.appendChild(doc.createTextNode(String.valueOf(vehicle.getPrice())));

            Element make = doc.createElement("Make");
            make.appendChild(doc.createTextNode(vehicle.getManufacturer()));

            Element model = doc.createElement("Model");
            model.appendChild(doc.createTextNode(vehicle.getModel()));

            Element rentStatus = doc.createElement("is_rented_out");
            rentStatus.appendChild(doc.createTextNode(String.valueOf(vehicle.getIsRentedOut())));

            vehicleElement.appendChild(price);
            vehicleElement.appendChild(make);
            vehicleElement.appendChild(model);
            vehicleElement.appendChild(rentStatus);
            dealerElement.appendChild(vehicleElement);
        }
    }

    /**
     * Displays a success alert dialog to inform the user of successful export.
     *
     * @param filePath the path where the XML file was saved
     */
    private static void showSuccessAlert(String filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText(null);
        alert.setContentText("Dealers exported to:\n" + filePath);
        alert.showAndWait();
    }

    /**
     * Displays an error alert dialog if the export fails.
     *
     * @param message the error message to show
     */
    private static void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Export Failed");
        alert.setHeaderText("Error exporting dealers");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
