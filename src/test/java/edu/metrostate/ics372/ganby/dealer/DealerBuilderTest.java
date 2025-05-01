package edu.metrostate.ics372.ganby.dealer;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.*;

class DealerBuilderTest {

    @Test
    void buildDealerFromValidJSON() {
        JSONObject json = new JSONObject();
        json.put("dealership_id", "123");
        json.put("dealer_name", "Test Dealer");

        Dealer dealer = DealerBuilder.buildDealerFromJSON(json);
        assertNotNull(dealer);
        assertEquals("123", dealer.getId());
        assertEquals("Test Dealer", dealer.getName());
    }

    @Test
    void buildDealerFromInvalidJSON() {
        JSONObject json = new JSONObject(); // Missing keys
        Dealer dealer = DealerBuilder.buildDealerFromJSON(json);
        assertNull(dealer); // Should catch exception and return null
    }

    @Test
    void buildDealerFromXML_withAcquisitionStatus() throws Exception {
        String xml =
                "<Dealer id=\"456\">" +
                        "<Name>XML Dealer</Name>" +
                        "<Acquisition_Status>true</Acquisition_Status>" +
                        "</Dealer>";

        Element element = parseXmlToElement(xml, "Dealer");
        Dealer dealer = DealerBuilder.buildDealerFromXML(element);

        assertNotNull(dealer);
        assertEquals("456", dealer.getId());
        assertEquals("XML Dealer", dealer.getName());
        assertTrue(dealer.isVehicleAcquisitionEnabled());
    }

    @Test
    void buildDealerFromXML_withoutAcquisitionStatus() throws Exception {
        String xml =
                "<Dealer id=\"789\">" +
                        "<Name>Fallback Dealer</Name>" +
                        "</Dealer>";

        Element element = parseXmlToElement(xml, "Dealer");
        Dealer dealer = DealerBuilder.buildDealerFromXML(element);

        assertNotNull(dealer);
        assertEquals("789", dealer.getId());
        assertEquals("Fallback Dealer", dealer.getName());
        assertTrue(dealer.isVehicleAcquisitionEnabled()); // Defaults to true
    }

    @Test
    void buildDealerFromXML_nullElementThrows() {
        assertThrows(IllegalArgumentException.class, () -> DealerBuilder.buildDealerFromXML(null));
    }

    // === Utility method ===
    private Element parseXmlToElement(String xml, String tagName) throws Exception {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new java.io.ByteArrayInputStream(xml.getBytes()));
        doc.getDocumentElement().normalize();
        return (Element) doc.getElementsByTagName(tagName).item(0);
    }
}
