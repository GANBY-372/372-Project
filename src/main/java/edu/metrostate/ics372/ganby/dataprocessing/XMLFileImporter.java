package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerBuilder;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import edu.metrostate.ics372.ganby.vehicle.VehicleBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Importing dealer and vehicle data from an XML file.
 */
public class XMLFileImporter {

    private Document xmlDocument;

    /**
     * Constructor: Opens a file chooser dialog and loads the selected XML file.
     *
     * @param primaryStage the JavaFX stage to anchor the file chooser
     */
    public XMLFileImporter(Stage primaryStage) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select an XML File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("XML Files", "*.xml")
            );

            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                this.xmlDocument = dBuilder.parse(selectedFile);
                this.xmlDocument.getDocumentElement().normalize();

                System.out.println("Successfully parsed XML file: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("File selection cancelled.");
            }

        } catch (Exception e) {
            throw new RuntimeException("Error loading XML file: " + e.getMessage(), e);
        }
    }

    /**
     * Returns the loaded XML document.
     *
     * @return the parsed Document instance
     */
    public Document getXmlDocument() {
        return xmlDocument;
    }

    /**
     * Processes the loaded XML document and adds data to the DealerCatalog.
     *
     * @param xmlDocument the parsed XML document
     */
    public void processXML(Document xmlDocument) {
        NodeList dealerList = xmlDocument.getElementsByTagName("Dealer");
        for (int i = 0; i < dealerList.getLength(); i++) {
            Node dealerNode = dealerList.item(i);
            if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                Element dealerElement = (Element) dealerNode;

                // Use DealerBuilder to create Dealer instance
                Dealer dealer = DealerBuilder.buildDealerFromXML(dealerElement);

                // Add the dealer to the catalog
                if (dealer != null) {
                    DealerCatalog.getInstance().addDealer(dealer);
                }

                // Process vehicles
                NodeList vehicleList = dealerElement.getElementsByTagName("Vehicle");
                for (int j = 0; j < vehicleList.getLength(); j++) {
                    Node vehicleNode = vehicleList.item(j);
                    if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element vehicleElement = (Element) vehicleNode;

                        Vehicle vehicle = VehicleBuilder.buildVehicleFromXML(vehicleElement, dealer.getId(), dealer.getName());
                        if (vehicle != null) {
                            DealerCatalog.getInstance().addVehicle(vehicle);
                        }
                    }
                }
            }
        }
    }
}
