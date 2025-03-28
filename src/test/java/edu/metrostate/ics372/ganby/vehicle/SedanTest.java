package edu.metrostate.ics372.ganby.vehicle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

public class SedanTest {

    @Test
    void getType() {
        Sedan sedan = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", LocalDateTime.now(), false);
        assertEquals("Sedan", sedan.getType(), "getType() should return 'Sedan'");
    }

    @Test
    void testEquals() {
        LocalDateTime now = LocalDateTime.now();
        Sedan sedan1 = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", LocalDateTime.now(), false);
        Sedan sedan2 = new Sedan("V001", "Accord", "Honda", 20000.00, "002", LocalDateTime.now(), false);
        Sedan sedan3 = new Sedan("V002", "Corolla", "Toyota", 18000.00, "001",  LocalDateTime.now(), false);

        assertEquals(sedan1, sedan2, "Sedans with the same ID should be equal");
        assertNotEquals(sedan1, sedan3, "Sedans with different IDs should not be equal");
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        Sedan sedan = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", LocalDateTime.now(), false);
        String expectedString = "Sedan " + sedan.toString().substring(6); // Ensuring consistent output

        assertEquals(expectedString, sedan.toString(), "toString() should return correct formatted string");
    }
}
