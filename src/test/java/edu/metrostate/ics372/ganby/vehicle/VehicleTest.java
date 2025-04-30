package edu.metrostate.ics372.ganby.vehicle;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    private Vehicle sedan;
    private Vehicle pickup;

    @BeforeEach
    void setUp() {
        sedan = new Sedan("V001", "Camry", "Toyota", 15000.0, "D001", LocalDateTime.now(), false);
        pickup = new Pickup("V002", "F-150", "Ford", 25000.0, "D002", LocalDateTime.now(), true);
    }

    @Test
    void testJavaFXProperties() {
        assertEquals("V001", sedan.idProperty().get());
        assertEquals("Camry", sedan.modelProperty().get());
        assertEquals("Toyota", sedan.manufacturerProperty().get());
        assertEquals(15000.0, sedan.priceProperty().get());
        assertEquals("D001", sedan.dealerIdProperty().get());
        assertNotNull(sedan.acquisitionDateProperty().get());
        assertFalse(sedan.isRentedOutProperty().get());
        assertFalse(sedan.selectedProperty().get());
    }

    @Test
    void testStandardGetters() {
        assertEquals("V001", sedan.getVehicleId());
        assertEquals("Camry", sedan.getModel());
        assertEquals("Toyota", sedan.getManufacturer());
        assertEquals(15000.0, sedan.getPrice());
        assertEquals("D001", sedan.getDealerId());
        assertNotNull(sedan.getAcquisitionDate());
        assertFalse(sedan.getIsRentedOut());
        assertFalse(sedan.isSelected());
    }

    @Test
    void testSetSelected() {
        sedan.setSelected(true);
        assertTrue(sedan.isSelected());
        sedan.setSelected(false);
        assertFalse(sedan.isSelected());
    }

    @Test
    void testSetDealerId() {
        sedan.setDealerId("NEW_ID");
        assertEquals("NEW_ID", sedan.getDealerId());
    }

    @Test
    void testSetDealerValid() {
        Dealer d = new Dealer("123", "Dealer");
        sedan.setDealer(d);
        assertEquals("123", sedan.getDealerId());
    }

    @Test
    void testSetDealer_NullThrowsNPE() {
        assertThrows(NullPointerException.class, () -> sedan.setDealer(null));
    }

    @Test
    void testSetAcquisitionDateValid() {
        LocalDateTime newDate = LocalDateTime.now().minusDays(3);
        sedan.setAcquisitionDate(newDate);
        assertEquals(newDate, sedan.getAcquisitionDate());
    }

    @Test
    void testSetAcquisitionDate_NullThrowsNPE() {
        assertThrows(NullPointerException.class, () -> sedan.setAcquisitionDate(null));
    }

    @Test
    void testSetPriceValid() {
        sedan.setPrice(19999.99);
        assertEquals(19999.99, sedan.getPrice());
    }

    @Test
    void testSetPriceNegativeThrows() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> sedan.setPrice(-1.0));
        assertEquals("Price cannot be negative", e.getMessage());
    }

    @Test
    void testSetRentedOut() {
        sedan.setRentedOut(true);
        assertTrue(sedan.getIsRentedOut());
        sedan.setRentedOut(false);
        assertFalse(sedan.getIsRentedOut());
    }

    @Test
    void testEqualsAndHashCode() {
        Vehicle sameId = new Sedan("V001", "Other", "Other", 9999.0, "D999", LocalDateTime.now(), false);
        assertEquals(sedan, sameId);
        assertEquals(sedan.hashCode(), sameId.hashCode());

        assertNotEquals(sedan, pickup);
        assertNotEquals(sedan, null);
        assertNotEquals(sedan, "Not a Vehicle");
    }

    @Test
    void testToString() {
        String str = sedan.toString();
        assertTrue(str.contains("id: V001"));
        assertTrue(str.contains("model: Camry"));
        assertTrue(str.contains("manufacturer: Toyota"));
        assertTrue(str.contains("price: 15000.0"));
        assertTrue(str.contains("dealerId: D001"));
    }
}
