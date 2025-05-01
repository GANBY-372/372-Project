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
     * Method to process XML documents and create the dealers and vehicles
     * This method will be used in either of two ways:
     *      1.  Loading the autosave file upon booting
     *      2.  Loading any file imported during the runtime of the program
     * Generally speaking, if a dealer's acquisition status is false, it should not accept any vehicles,
     * but this doesn't apply to loading the autosave file because some dealers might have their status as false but
     * still have vehicles.
     * Therefore the persistence manager, when loading the autosave file, will call this method with true as the parameter,
     * and the method will check whether the boolean passed is true or not. If true, it will call addVehicleFromAutosave()
     * method in DealerCatalog, which adds vehicles to dealers without checking their acquisition statuses. However if the
     * boolean is false then the normal addVehicle() method will be used which checks acquisition status.
     *
     * However if we don't want to require to pass in a true or false, so there is another method with the same name as
     * this that has no parameters. If it has no parameters, we can assume that we are not loading from autosave file and
     * we can then call this method and pass false as the value.
     * @param ignoreAcquisitionCheck boolean to know whether to take into account acquisition status or not
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
     * Processes any loaded XML document other than the autosave file
     */
    public void processXML() {
        //If processXML() method is called, that means we are not reading the autosave file
        //So we will call the processXML(boolean ignoreAcquisitionCheck) method with false
        processXML(false);
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
