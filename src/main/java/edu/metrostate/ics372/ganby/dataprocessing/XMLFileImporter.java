package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerBuilder;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import edu.metrostate.ics372.ganby.vehicle.VehicleBuilder;
import javafx.stage.Stage;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Handles importing dealer and vehicle data from an XML file.
 * Supports file selection or direct file path loading.
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
            File selectedFile = FileSelector.chooseXmlOpenFile(primaryStage);
            if (selectedFile != null) {
                loadDocumentFromFile(selectedFile);
                System.out.println("✅ Successfully parsed XML file: " + selectedFile.getAbsolutePath());
            } else {
                System.out.println("⚠️ File selection cancelled.");
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ Error loading XML file: " + e.getMessage(), e);
        }
    }

    /**
     * Constructor: Loads an XML file from a given file path.
     *
     * @param filePath path to the XML file
     */
    public XMLFileImporter(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                loadDocumentFromFile(file);
                System.out.println("✅ Successfully parsed XML file: " + filePath);
            } else {
                System.out.println("❌ File not found: " + filePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("❌ Error loading XML file: " + e.getMessage(), e);
        }
    }

    /**
     * Loads the XML content from a file into xmlDocument.
     *
     * @param file the XML file to parse
     * @throws Exception if parsing fails
     */
    private void loadDocumentFromFile(File file) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.xmlDocument = dBuilder.parse(file);
        this.xmlDocument.getDocumentElement().normalize();
    }

    /**
     * Processes the loaded XML document and adds data to the DealerCatalog.
     */
    public void processXML() {
        if (xmlDocument == null) {
            System.err.println("❌ No XML document loaded to process.");
            return;
        }

        NodeList dealerList = xmlDocument.getElementsByTagName("Dealer");
        for (int i = 0; i < dealerList.getLength(); i++) {
            Node dealerNode = dealerList.item(i);
            if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                Element dealerElement = (Element) dealerNode;

                Dealer dealer = DealerBuilder.buildDealerFromXML(dealerElement);
                DealerCatalog.getInstance().addDealer(dealer);

                NodeList vehicleList = dealerElement.getElementsByTagName("Vehicle");
                for (int j = 0; j < vehicleList.getLength(); j++) {
                    Node vehicleNode = vehicleList.item(j);
                    if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element vehicleElement = (Element) vehicleNode;

                        Vehicle vehicle = VehicleBuilder.buildVehicleFromXML(
                                vehicleElement, dealer.getId(), dealer.getName()
                        );

                        if (vehicle != null) {
                            DealerCatalog.getInstance().addVehicleFromFile(vehicle);
                        }
                    }
                }
            }
        }

        System.out.println("✅ XML import finished successfully!");
    }

    /**
     * Returns the loaded XML document.
     *
     * @return the parsed Document instance
     */
    public Document getXmlDocument() {
        return xmlDocument;
    }
}
