package edu.metrostate.ics372.ganby.DataProcessing;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.*;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.time.LocalDateTime;

public class XMLFileImporter {

    private Document xmlDocument;

    /**
     * Constructor: prompts user with JavaFX FileChooser dialog
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

    public Document getXmlDocument() {
        return xmlDocument;
    }

    public void processXML(Document xmlDocument) throws IllegalAccessException {
        NodeList dealerList = xmlDocument.getElementsByTagName("Dealer");
        for (int i = 0; i < dealerList.getLength(); i++) {
            Node dealerNode = dealerList.item(i);
            if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                Element dealerElement = (Element) dealerNode;
                String dealerId = dealerElement.getAttribute("id");
                String dealerName = dealerElement.getElementsByTagName("Name").item(0).getTextContent();
                Dealer dealer = new Dealer(dealerId, dealerName);

                NodeList vehicleList = dealerElement.getElementsByTagName("Vehicle");
                for (int j = 0; j < vehicleList.getLength(); j++) {
                    Node vehicleNode = vehicleList.item(j);
                    if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element vehicleElement = (Element) vehicleNode;
                        Vehicle vehicle = createVehicle(vehicleElement, dealerId);
                        if (vehicle != null) {
                            DealerCatalog.getInstance().addVehicle(vehicle);
                        }
                    }
                }
            }
        }
    }

    private Vehicle createVehicle(Element vehicleElement, String dealerId) throws IllegalAccessException {
        if (vehicleElement == null) {
            throw new IllegalArgumentException("XML vehicle element is null.");
        }

        String manufacturer = vehicleElement.getElementsByTagName("Make").item(0).getTextContent();
        String model = vehicleElement.getElementsByTagName("Model").item(0).getTextContent();
        String id = vehicleElement.getAttribute("id");
        double price = Double.parseDouble(vehicleElement.getElementsByTagName("Price").item(0).getTextContent());

        String type = vehicleElement.getAttribute("type");
        if (type == null || type.isBlank()) {
            System.out.println("Skipping vehicle due to missing or empty vehicle type.");
            return null;
        }

        LocalDateTime acquisitionDate = LocalDateTime.now(); // Placeholder for now

        return switch (type.trim().replaceAll("\\s+", "").toUpperCase()) {
            case "SUV" -> new SUV(id, model, manufacturer, price, dealerId, acquisitionDate);
            case "SEDAN" -> new Sedan(id, model, manufacturer, price, dealerId, acquisitionDate);
            case "PICKUP" -> new Pickup(id, model, manufacturer, price, dealerId, acquisitionDate);
            case "SPORTSCAR" -> new SportsCar(id, model, manufacturer, price, dealerId, acquisitionDate);
            default -> {
                System.out.println("Unknown vehicle type: " + type);
                yield null;
            }
        };
    }
}
