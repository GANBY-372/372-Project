package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.vehicle.Pickup;
import edu.metrostate.ics372.ganby.vehicle.Sedan;
import edu.metrostate.ics372.ganby.vehicle.SportsCar;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import java.time.LocalDateTime;

class DealerTest {
    private Dealer dealer;
    private Vehicle v001, v002, v003, v004;

    @BeforeEach
    void setUp() {
        dealer = new Dealer("123", "Test Dealer");
        v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", LocalDateTime.now(), false);
        v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "001", LocalDateTime.now(), false);
        v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "001", LocalDateTime.now(), false);
        v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "002", LocalDateTime.now(), false);
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
        assertTrue(dealer.getIsVehicleAcquisitionEnabled());
    }

    @Test
    void getVehicleCatalog() {
        assertNotNull(dealer.getVehicleCatalog());
        assertTrue(dealer.getVehicleCatalog().isEmpty());
    }

    @Test
    void enableVehicleAcquisition() {
        dealer.enableVehicleAcquisition("123");
        assertTrue(dealer.getIsVehicleAcquisitionEnabled());
    }

    @Test
    void disableVehicleAcquisition() {
        dealer.disableVehicleAcquisition("123");
        assertFalse(dealer.getIsVehicleAcquisitionEnabled());
    }

    @Test
    void findVehicleById() {
        dealer.addVehicle(v001);
        assertEquals(v001, dealer.findVehicleById("V001"));
    }

    @Test
    void addVehicle() {
        dealer.addVehicle(v002);
        assertEquals(v002, dealer.getVehicleCatalog().get("V002"));
    }

    @Test
    void testEquals() {
        Dealer sameDealer = new Dealer("123", "Another Name");
        Dealer differentDealer = new Dealer("456", "Test Dealer");

        assertEquals(dealer, sameDealer);
        assertNotEquals(dealer, differentDealer);
    }

    @Test
    void testHashCode() {
        Dealer sameDealer = new Dealer("123", "Another Name");
        assertEquals(dealer.hashCode(), sameDealer.hashCode());
    }
}
