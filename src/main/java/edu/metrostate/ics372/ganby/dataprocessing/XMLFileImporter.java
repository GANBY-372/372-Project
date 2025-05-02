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
 * Supports both interactive file selection (via dialog) and direct file path loading.
 */
public class XMLFileImporter {

    private Document xmlDocument;

    /**
     * Constructor: Opens a file chooser dialog and loads the selected XML file.
     *
     * @param primaryStage the JavaFX Stage used to anchor the file chooser dialog
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
     * Constructor: Loads and parses an XML file from the specified path.
     *
     * @param filePath the absolute file path to the XML file
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
     * Loads and parses the XML content from a file.
     *
     * @param file the XML file to load
     * @throws Exception if XML parsing fails
     */
    private void loadDocumentFromFile(File file) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        this.xmlDocument = dBuilder.parse(file);
        this.xmlDocument.getDocumentElement().normalize();
    }

    /**
     * Processes the loaded XML file by parsing all dealers and their vehicles.
     *
     * @param ignoreAcquisitionCheck if true, allows adding vehicles even if acquisition is disabled (used for autosave)
     */
    public void processXML(boolean ignoreAcquisitionCheck) {
        if (xmlDocument == null) {
            System.err.println("❌ No XML document loaded to process.");
            return;
        }

        NodeList dealerList = xmlDocument.getElementsByTagName("Dealer");

        for (int i = 0; i < dealerList.getLength(); i++) {
            Node dealerNode = dealerList.item(i);

            if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                Element dealerElement = (Element) dealerNode;

                // Build dealer from XML and add to catalog
                Dealer dealer = DealerBuilder.buildDealerFromXML(dealerElement);
                DealerCatalog.getInstance().addDealer(dealer);

                // Parse all <Vehicle> elements under this <Dealer>
                NodeList vehicleList = dealerElement.getElementsByTagName("Vehicle");

                for (int j = 0; j < vehicleList.getLength(); j++) {
                    Node vehicleNode = vehicleList.item(j);

                    if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element vehicleElement = (Element) vehicleNode;

                        // Create vehicle from XML element
                        Vehicle vehicle = VehicleBuilder.buildVehicleFromXML(
                                vehicleElement, dealer.getId(), dealer.getName()
                        );

                        if (vehicle != null) {
                            // Decide how to add vehicle based on acquisition check flag
                            if (ignoreAcquisitionCheck) {
                                DealerCatalog.getInstance().addVehicleFromAutosave(vehicle);
                            } else {
                                DealerCatalog.getInstance().addVehicle(vehicle);
                            }
                        }
                    }
                }
            }
        }

        System.out.println("✅ XML import finished successfully!");
    }

    /**
     * Convenience method for processing XML normally (not during autosave).
     * Automatically sets ignoreAcquisitionCheck to false.
     */
    public void processXML() {
        processXML(false);
    }

    /**
     * Gets the parsed XML document (for testing or debugging).
     *
     * @return the loaded org.w3c.dom.Document
     */
    public Document getXmlDocument() {
        return xmlDocument;
    }
}
