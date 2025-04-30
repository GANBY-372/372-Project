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

            String dealerName = json.containsKey("dealer_name") && json.get("dealer_name") != null
                    ? json.get("dealer_name").toString()
                    : null;

            Boolean acquisitionStatus = json.containsKey("acquisition_status") && json.get("acquisition_status") != null
                    ? Boolean.parseBoolean(json.get("acquisition_status").toString())
                    : null;

            return new Dealer(dealerId, dealerName, acquisitionStatus);
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

        String dealerName = null;
        Node nameNode = dealerElement.getElementsByTagName("Name").item(0);
        if (nameNode != null && nameNode.getTextContent() != null) {
            dealerName = nameNode.getTextContent().trim();
        }

        Boolean acquisitionStatus = null;
        Node acquisitionNode = dealerElement.getElementsByTagName("Acquisition_Status").item(0);
        if (acquisitionNode != null && acquisitionNode.getTextContent() != null) {
            String acquisitionText = acquisitionNode.getTextContent().trim();
            acquisitionStatus = Boolean.parseBoolean(acquisitionText);
        }

        return new Dealer(dealerId, dealerName, acquisitionStatus);
    }
}
