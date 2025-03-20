package edu.metrostate.ics372.ganby.xml;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.Dealers;
import edu.metrostate.ics372.ganby.vehicle.*;
import lombok.Getter;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;

public class XMLFileImporter {

    // XML file reader tools
    private Reader reader;

    // Getters
    @Getter
    private Dealers dealers;
    @Getter
    private Vehicles vehicles;
    private Dealer dealer;
    private Vehicle vehicle;

    // Constructor initializes collections
    public XMLFileImporter() {
        this.dealers = new Dealers();
        this.vehicles = new Vehicles();
    }

    // Method to allow the user to choose a file and determine its type
    public void importFile() throws Exception {
        try {
            // File chooser dialog
            Frame frame = new Frame();
            FileDialog fileDialog = new FileDialog(frame, "Select a File", FileDialog.LOAD);
            fileDialog.setVisible(true);

            String directory = fileDialog.getDirectory();
            String filename = fileDialog.getFile();

            if (filename != null) {
                File selectedFile = new File(directory, filename);
                String fileExtension = getFileExtension(selectedFile);

                if ("xml".equalsIgnoreCase(fileExtension)) {
                    importXML(selectedFile);
                } else {
                    System.out.println("Unsupported file type: " + fileExtension);
                }
            } else {
                System.out.println("File selection cancelled.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error importing file: " + e.getMessage(), e);
        }
    }

    // Helper method to get the file extension
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        return (lastIndex == -1) ? "" : name.substring(lastIndex + 1);
    }

    // Method to import XML files
    private void importXML(File file) throws Exception {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            NodeList dealerNodes = document.getElementsByTagName("Dealer");

            for (int i = 0; i < dealerNodes.getLength(); i++) {
                Node dealerNode = dealerNodes.item(i);
                if (dealerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element dealerElement = (Element) dealerNode;
                    processDealerFromXML(dealerElement);
                }
            }

            System.out.println("Successfully parsed XML file: " + file.getAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML file: " + e.getMessage(), e);
        }
    }

    // Method to process a dealer and its vehicles from an XML element
    private void processDealerFromXML(Element dealerElement) throws IllegalAccessException {
        String dealerId = dealerElement.getAttribute("id");
        if (dealerId == null || dealerId.isBlank()) {
            System.out.println("Skipping dealer due to missing or invalid ID.");
            return;
        }

        Dealer dealer = new Dealer(Integer.parseInt(dealerId));
        dealers.addDealer(dealer);
        DealerCatalog.getInstance().getDealers().addDealer(dealer);

        NodeList vehicleNodes = dealerElement.getElementsByTagName("Vehicle");
        for (int i = 0; i < vehicleNodes.getLength(); i++) {
            Node vehicleNode = vehicleNodes.item(i);
            if (vehicleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element vehicleElement = (Element) vehicleNode;
                processVehicleFromXML(vehicleElement, dealer);
            }
        }
    }

    // Method to process a vehicle from an XML element
    private void processVehicleFromXML(Element vehicleElement, Dealer dealer) throws IllegalAccessException {
        String vehicleId = vehicleElement.getAttribute("id");
        String type = vehicleElement.getAttribute("type");
        if (vehicleId == null || vehicleId.isBlank() || type == null || type.isBlank()) {
            System.out.println("Skipping vehicle due to missing or invalid attributes.");
            return;
        }

        String manufacturer = getElementText(vehicleElement, "Make");
        String model = getElementText(vehicleElement, "Model");
        String priceText = getElementText(vehicleElement, "Price");
        double price = priceText != null ? Double.parseDouble(priceText) : 0.0;

        VehicleCategory category = VehicleCategory.fromString(type);
        if (category == null) {
            System.out.println("Skipping vehicle due to invalid category: " + type);
            return;
        }

        LocalDateTime acquisitionDate = LocalDateTime.now(); // Default to current time if not provided

        Vehicle vehicle;
        switch (category) {
            case SUV:
                vehicle = new SUV.Builder().dealer(dealer)
                        .id(vehicleId)
                        .manufacturer(manufacturer)
                        .model(model)
                        .price(price)
                        .acquisitionDate(acquisitionDate)
                        .build();
                break;
            case PICKUP:
                vehicle = new Pickup.Builder().dealer(dealer)
                        .id(vehicleId)
                        .manufacturer(manufacturer)
                        .model(model)
                        .price(price)
                        .acquisitionDate(acquisitionDate)
                        .build();
                break;
            case SEDAN:
                vehicle = new Sedan.Builder().dealer(dealer)
                        .id(vehicleId)
                        .manufacturer(manufacturer)
                        .model(model)
                        .price(price)
                        .acquisitionDate(acquisitionDate)
                        .build();
                break;
            case SPORTS_CAR:
                vehicle = new SportsCar.Builder().dealer(dealer)
                        .id(vehicleId)
                        .manufacturer(manufacturer)
                        .model(model)
                        .price(price)
                        .acquisitionDate(acquisitionDate)
                        .build();
                break;
            default:
                System.out.println("Unknown category: " + category);
                return;
        }

        vehicles.addVehicle(vehicle);
        VehicleCatalog.getInstance().getVehicles().addVehicle(vehicle);
    }

    // Helper method to get text content of an element
    private String getElementText(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return null;
    }
}
