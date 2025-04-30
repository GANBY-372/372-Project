package edu.metrostate.ics372.ganby.vehicle;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PickupTest {

    @Test
    void testConstructorWithRentStatus() {
        LocalDateTime now = LocalDateTime.now();
        Pickup pickup = new Pickup("P001", "Tacoma", "Toyota", 28000.00, "D001", now, true);
        assertEquals("P001", pickup.getVehicleId());
        assertEquals("Tacoma", pickup.getModel());
        assertEquals("Toyota", pickup.getManufacturer());
        assertEquals(28000.00, pickup.getPrice());
        assertEquals("D001", pickup.getDealerId());
        assertEquals(now, pickup.getAcquisitionDate());
        assertTrue(pickup.getIsRentedOut());
    }

    @Test
    void testConstructorWithoutRentStatus() {
        LocalDateTime now = LocalDateTime.now();
        Pickup pickup = new Pickup("P002", "Colorado", "Chevy", 26000.00, "D002", now);
        assertEquals("P002", pickup.getVehicleId());
        assertFalse(pickup.getIsRentedOut());
    }

    @Test
    void getTypeReturnsPickup() {
        Pickup pickup = new Pickup("P003", "F-150", "Ford", 30000.00, "D003", LocalDateTime.now(), false);
        assertEquals("Pickup", pickup.getType());
    }

    @Test
    void testToStringIncludesPickupPrefix() {
        Pickup pickup = new Pickup("P004", "Ranger", "Ford", 27000.00, "D004", LocalDateTime.now(), false);
        assertTrue(pickup.toString().startsWith("Pickup Pickup [id:"));
    }

    @Test
    void testEqualsSameId() {
        LocalDateTime now = LocalDateTime.now();
        Pickup a = new Pickup("P005", "Ridgeline", "Honda", 32000.00, "D005", now, false);
        Pickup b = new Pickup("P005", "DifferentModel", "Other", 12345.00, "XXX", now, true);
        assertEquals(a, b);
    }

    @Test
    void testEqualsDifferentId() {
        Pickup a = new Pickup("P006", "Ram", "Dodge", 35000.00, "D006", LocalDateTime.now(), false);
        Pickup b = new Pickup("P007", "Ram", "Dodge", 35000.00, "D006", LocalDateTime.now(), false);
        assertNotEquals(a, b);
    }

    @Test
    void testEqualsNullAndDifferentType() {
        Pickup pickup = new Pickup("P008", "Frontier", "Nissan", 24000.00, "D008", LocalDateTime.now(), false);
        assertNotEquals(pickup, null);
        assertNotEquals(pickup, "not a vehicle");
    }
}

