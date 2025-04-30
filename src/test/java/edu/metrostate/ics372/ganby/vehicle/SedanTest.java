package edu.metrostate.ics372.ganby.vehicle;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SedanTest {

    @Test
    void testConstructorAndProperties() {
        LocalDateTime date = LocalDateTime.now();
        Sedan sedan = new Sedan("S001", "Camry", "Toyota", 22000.00, "D001", date);

        assertEquals("S001", sedan.getVehicleId());
        assertEquals("Camry", sedan.getModel());
        assertEquals("Toyota", sedan.getManufacturer());
        assertEquals(22000.00, sedan.getPrice());
        assertEquals("D001", sedan.getDealerId());
        assertEquals(date, sedan.getAcquisitionDate());
        assertFalse(sedan.getIsRentedOut(), "Default rented out should be false");
    }

    @Test
    void testTypeReturnsSedan() {
        Sedan sedan = new Sedan("S002", "Accord", "Honda", 23000.00, "D002", LocalDateTime.now());
        assertEquals("Sedan", sedan.getType());
    }

    @Test
    void testEquals_SameId() {
        LocalDateTime now = LocalDateTime.now();
        Sedan a = new Sedan("S003", "ModelA", "MakerA", 21000.00, "D003", now);
        Sedan b = new Sedan("S003", "ModelB", "MakerB", 29000.00, "D004", now);
        assertEquals(a, b);
    }

    @Test
    void testEquals_DifferentId() {
        Sedan a = new Sedan("S004", "Fusion", "Ford", 20000.00, "D004", LocalDateTime.now());
        Sedan b = new Sedan("S005", "Fusion", "Ford", 20000.00, "D004", LocalDateTime.now());
        assertNotEquals(a, b);
    }

    @Test
    void testEquals_NullAndDifferentType() {
        Sedan sedan = new Sedan("S006", "Altima", "Nissan", 21000.00, "D005", LocalDateTime.now());
        assertNotEquals(sedan, null);
        assertNotEquals(sedan, "Not a sedan");
    }

    @Test
    void testToStringIncludesType() {
        Sedan sedan = new Sedan("S007", "Impreza", "Subaru", 25000.00, "D006", LocalDateTime.now());
        assertTrue(sedan.toString().startsWith("Sedan Sedan [id:"));
    }
}
