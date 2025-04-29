package edu.metrostate.ics372.ganby.dealer;

import org.w3c.dom.Element;
import org.json.simple.JSONObject;
import org.w3c.dom.Node;

/**
 * Utility class for building Dealer instances from XML input.
 * This class provides methods to create Dealer objects from XML elements.
 */
public class DealerBuilder {

    /**
     * Creates a Dealer object from the given JSON object.
     *
     * @param json the JSON object containing dealer data
     * @return a Dealer instance or null if the dealer data is invalid
     */
    public static Dealer buildDealerFromJSON(JSONObject json) {
        try {
            String dealerId = json.get("dealership_id").toString();
            String dealerName = json.get("dealer_name").toString();

            // Create a new Dealer instance from the extracted data
            Dealer dealer = new Dealer(dealerId, dealerName);

            return dealer;
        } catch (Exception e) {
            System.err.println("Error creating dealer from JSON: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a Dealer object from the given XML element.
     *
     * @param dealerElement the XML element representing the dealer
     * @return a Dealer instance or null if the dealer data is invalid
     */
    public static Dealer buildDealerFromXML(Element dealerElement) {
        if (dealerElement == null) {
            throw new IllegalArgumentException("Dealer XML element is null.");
        }

        String dealerId = dealerElement.getAttribute("id");
        String dealerName = dealerElement.getElementsByTagName("Name").item(0).getTextContent().trim();

        Node acquisitionNode = dealerElement.getElementsByTagName("Acquisition_Status").item(0);

        if (acquisitionNode != null) {
            String acquisitionText = acquisitionNode.getTextContent().trim();
            boolean acquisitionStatus = Boolean.parseBoolean(acquisitionText);
            return new Dealer(dealerId, dealerName, acquisitionStatus);
        } else {
            return new Dealer(dealerId, dealerName);
        }
    }
}
