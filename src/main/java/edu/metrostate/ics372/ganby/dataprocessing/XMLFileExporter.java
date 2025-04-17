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
 * Export dealer and vehicle data into an XML file.
 */
public class XMLFileExporter {

    /**
     * Prompts the user to choose a file location and exports the provided dealers
     * and their vehicle inventories to a single XML file.
     *
     * @param stage           the parent JavaFX window, used for file chooser dialog
     * @param selectedDealers the list of dealers whose data will be exported
     */
    public void exportDealers(Stage stage, List<Dealer> selectedDealers) {
        File file = FileSelector.chooseXmlSaveFile(stage);  // ✅ Replaces manual FileChooser

        if (file != null) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.newDocument();

                // Root <Dealers> element
                Element dealersElement = doc.createElement("Dealers");
                doc.appendChild(dealersElement);

                for (Dealer dealer : selectedDealers) {
                    appendDealerToDocument(doc, dealersElement, dealer);
                }

                // Write the final XML document to the selected file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                transformer.transform(new DOMSource(doc), new StreamResult(file));
                showSuccessAlert(file.getAbsolutePath());

            } catch (Exception e) {
                showErrorAlert(e.getMessage());
            }
        }
    }

    /**
     * Appends a single <Dealer> element and its nested <Vehicle> elements to the XML document.
     *
     * @param doc    the XML document being built
     * @param parent the root <Dealers> element to which this dealer will be appended
     * @param dealer the dealer whose data will be written
     */
    private void appendDealerToDocument(Document doc, Element parent, Dealer dealer) {
        Element dealerElement = doc.createElement("Dealer");
        dealerElement.setAttribute("id", dealer.getId());
        parent.appendChild(dealerElement);

        Element nameElement = doc.createElement("Name");
        nameElement.appendChild(doc.createTextNode(dealer.getName()));
        dealerElement.appendChild(nameElement);

        for (Vehicle vehicle : dealer.getVehicleCatalog().values()) {
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

    private void showSuccessAlert(String filePath) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Export Successful");
        alert.setHeaderText(null);
        alert.setContentText("Dealers exported to:\n" + filePath);
        alert.showAndWait();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Export Failed");
        alert.setHeaderText("Error exporting dealers");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
