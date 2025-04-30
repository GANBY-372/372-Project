package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.vehicle.Pickup;
import edu.metrostate.ics372.ganby.vehicle.Sedan;
import edu.metrostate.ics372.ganby.vehicle.SportsCar;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DealerTest {
    private Dealer dealer;
    private Vehicle v001, v002, v003, v004;

    @BeforeEach
    void setUp() {
        dealer = new Dealer("123", "Test Dealer");
        v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "123", LocalDateTime.now(), false);
        v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "123", LocalDateTime.now(), false);
        v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "123", LocalDateTime.now(), true);
        //v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "123", LocalDateTime.now(), false);
    }

    // === Constructors and basic properties ===

    @Test
    void constructorWithIdOnly() {
        Dealer d = new Dealer("999");
        assertEquals("999", d.getId());
        assertTrue(d.getName().contains("999"));
    }

    @Test
    void constructorWithIdNameAndStatus() {
        Dealer d = new Dealer("789", "Special Dealer", false);
        assertEquals("789", d.getId());
        assertEquals("Special Dealer", d.getName());
        assertFalse(d.isVehicleAcquisitionEnabled());
    }

    @Test
    void getDealerId() {
        assertEquals("123", dealer.getId());
    }

    @Test
    void getDealerName() {
        assertEquals("Test Dealer", dealer.getName());
    }

    @Test
    void getIsVehicleAcquisitionEnabled() {
        assertTrue(dealer.isVehicleAcquisitionEnabled());
    }

    @Test
    void enableVehicleAcquisition() {
        dealer.disableVehicleAcquisition("123");
        dealer.enableVehicleAcquisition("123");
        assertTrue(dealer.isVehicleAcquisitionEnabled());
    }

    @Test
    void disableVehicleAcquisition() {
        dealer.disableVehicleAcquisition("123");
        assertFalse(dealer.isVehicleAcquisitionEnabled());
    }

    @Test
    void setNameAndToString() {
        dealer.setName("Updated Name");
        assertEquals("Updated Name", dealer.getName());
        assertEquals("Updated Name", dealer.toString());
    }

    // === Vehicle Management ===

    @Test
    void addVehicle() {
        dealer.addVehicle(v001);
        assertEquals(v001, dealer.vehicleCatalog.get("V001"));
    }

    @Test
    void addVehicles() {
        ArrayList<Vehicle> vehicles = new ArrayList<>(List.of(v001, v002));
        dealer.addVehicles(vehicles);
        assertEquals(2, dealer.vehicleCatalog.size());
        assertTrue(dealer.vehicleCatalog.containsKey("V001"));
    }

    @Test
    void findVehicleById() {
        dealer.addVehicle(v001);
        assertEquals(v001, dealer.findVehicleById("V001"));
        assertNull(dealer.findVehicleById("XYZ"));
    }

    @Test
    void getVehiclesByType() {
        dealer.addVehicle(v001); // Sedan
        dealer.addVehicle(v002); // Pickup
        dealer.addVehicle(v003); // SportsCar
        HashMap<String, Vehicle> sedans = dealer.getVehiclesByType("Sedan");
        assertEquals(1, sedans.size());
        assertTrue(sedans.containsKey("V001"));
    }

    @Test
    void rentedOutVehicles() {
        Vehicle rentedSedan = new Sedan("V005", "Civic", "Honda", 18000.00, "123", LocalDateTime.now(), true);
        dealer.addVehicle(v001); // not rented
        dealer.addVehicle(rentedSedan); // rented
        List<Vehicle> rented = dealer.getRentedOutVehicles();
        assertEquals(1, rented.size());
        assertEquals(rentedSedan, rented.get(0));
    }

    // === Selection State ===

    @Test
    void selectionState() {
        dealer.setSelected(true);
        assertTrue(dealer.isSelected());
        dealer.setSelected(false);
        assertFalse(dealer.isSelected());
    }

    // === Transfer logic ===

    @Test
    void transferVehicleSuccess() {
        dealer.addVehicle(v001);
        Dealer target = new Dealer("456", "Receiver");
        boolean result = dealer.transferVehicle(v001, target);
        assertTrue(result);
        assertFalse(dealer.vehicleCatalog.containsKey("V001"));
        assertEquals(v001, target.vehicleCatalog.get("V001"));
    }

    @Test
    void transferVehicleFailure() {
        Dealer target = new Dealer("789", "No-op");
        assertFalse(dealer.transferVehicle(null, target));
        assertFalse(dealer.transferVehicle(v001, null));
        assertFalse(dealer.transferVehicle(v001, target)); // not in source
    }

    @Test
    void transferVehicleSet() {
        dealer.addVehicles(new ArrayList<>(List.of(v001, v002)));
        Dealer target = new Dealer("789", "Receiver");
        Set<Vehicle> set = new HashSet<>(List.of(v001, v002));
        assertTrue(dealer.transferVehicleSet(set, target));
        assertTrue(target.vehicleCatalog.containsKey("V001"));
        assertTrue(target.vehicleCatalog.containsKey("V002"));
    }

    @Test
    void transferVehicleSetWithNulls() {
        assertFalse(dealer.transferVehicleSet(null, null));
        assertFalse(dealer.transferVehicleSet(Set.of(v001), null));
        assertFalse(dealer.transferVehicleSet(null, dealer));
    }

    // === Equals and Hashing ===

    @Test
    void testEquals() {
        Dealer sameDealer = new Dealer("123", "Another Name");
        Dealer differentDealer = new Dealer("456", "Test Dealer");

        assertEquals(dealer, sameDealer);
        assertNotEquals(dealer, differentDealer);
        assertNotEquals(dealer, null);
        assertNotEquals(dealer, "string");
    }

    @Test
    void testHashCode() {
        Dealer sameDealer = new Dealer("123", "Another Name");
        assertEquals(dealer.hashCode(), sameDealer.hashCode());
    }
}
