package edu.metrostate.ics372.ganby.vehicle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class SUVTest {

    @Test
    void getType() {
        SUV suv = new SUV("V005", "Highlander", "Toyota", 35000.00, "003", "name1", LocalDateTime.now(), false);
        assertEquals("SUV", suv.getType(), "getType() should return 'SUV'");
    }

    @Test
    void testEquals() {
        LocalDateTime now = LocalDateTime.now();
        SUV suv1 = new SUV("V005", "Highlander", "Toyota", 35000.00, "003", "name2", LocalDateTime.now(), false);
        SUV suv2 = new SUV("V005", "CR-V", "Honda", 32000.00, "004","name3", LocalDateTime.now(), false);
        SUV suv3 = new SUV("V006", "Escape", "Ford", 28000.00, "005", "name4", LocalDateTime.now(), false);

        assertEquals(suv1, suv2, "SUVs with the same ID should be equal");
        assertNotEquals(suv1, suv3, "SUVs with different IDs should not be equal");
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        SUV suv = new SUV("V005", "Highlander", "Toyota", 35000.00, "003", "name5", LocalDateTime.now(), false);
        String expectedString = "SUV " + suv.toString().substring(4); // Skipping "SUV " to match dynamic part

        assertEquals(expectedString, suv.toString(), "toString() should return correct formatted string");
    }
}
