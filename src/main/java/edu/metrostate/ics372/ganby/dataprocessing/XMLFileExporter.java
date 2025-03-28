package edu.metrostate.ics372.ganby.dataprocessing;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.ZoneId;

public class XMLFileExporter {

    /**
     * Export dealer data to XML file with the correct format.
     * @param dealer Dealer
     * @param filePath String
     */
    public void exportToFile(Dealer dealer, String filePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            // Root <Dealers> element
            Element dealersElement = doc.createElement("Dealers");
            doc.appendChild(dealersElement);

            // Dealer element
            Element dealerElement = doc.createElement("Dealer");
            dealerElement.setAttribute("id", dealer.getId());
            dealersElement.appendChild(dealerElement);

            // Dealer name
            Element nameElement = doc.createElement("Name");
            nameElement.appendChild(doc.createTextNode(dealer.getName()));
            dealerElement.appendChild(nameElement);

            // Loop through all vehicles
            for (Vehicle vehicle : dealer.getVehicleCatalog().values()) {
                Element vehicleElement = doc.createElement("Vehicle");
                vehicleElement.setAttribute("type", vehicle.getType());
                vehicleElement.setAttribute("id", vehicle.getVehicleId());

                // Price element with currency unit
                Element priceElement = doc.createElement("Price");
                priceElement.setAttribute("unit", "dollars");  // Adjust based on currency data
                priceElement.appendChild(doc.createTextNode(String.valueOf(vehicle.getPrice())));

                // Make and Model elements
                Element makeElement = doc.createElement("Make");
                makeElement.appendChild(doc.createTextNode(vehicle.getManufacturer()));

                Element modelElement = doc.createElement("Model");
                modelElement.appendChild(doc.createTextNode(vehicle.getModel()));

                // Append to vehicle
                vehicleElement.appendChild(priceElement);
                vehicleElement.appendChild(makeElement);
                vehicleElement.appendChild(modelElement);

                // Append vehicle to dealer
                dealerElement.appendChild(vehicleElement);
            }

            // Write XML to file with pretty formatting
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);

            System.out.println("Successfully exported to " + filePath);

        } catch (Exception e) {
            System.out.println("An error occurred while exporting the dealer data: " + e.getMessage());
        }
    }
}