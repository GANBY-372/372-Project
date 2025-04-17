package edu.metrostate.ics372.ganby.vehicle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

class SportsCarTest {

    private SportsCar sportsCar;

    @BeforeEach
    void setUp() {
        // Fresh setup before each test case
        sportsCar = new SportsCar("123", "Model S", "Tesla", 100000, "dealer123", LocalDateTime.now(), false);
    }

    @AfterEach
    void tearDown() {
        // Cleanup after each test case (if necessary)
        sportsCar = null;  // Explicitly nullifying the object to clean up
    }

    @Test
    void getType() {
        // Verify that the type of SportsCar is "SportsCar"
        assertEquals("SportsCar", sportsCar.getType());
    }

    @Test
    void setRentedOut() {
       // Old test that tests that SportsCar will not set rented status and will instead return "SportsCar"
        //But now we changed it so that the GUI takes care of the logic to make sure SportsCars are not rented out
        //So I'm not sure how we can test that now
       // assertEquals("SportsCar", sportsCar.setRentedOut(true));
       // assertEquals("SportsCar", sportsCar.setRentedOut(false));
    }

    @Test
    void testEquals() {
        // Create another SportsCar with the same ID
        SportsCar anotherCar = new SportsCar("123", "Model S", "Tesla", 100000, "dealer123",  LocalDateTime.now(), false);

        // Test that the two SportsCars are equal based on their ID
        assertEquals(sportsCar, anotherCar);

        // Test that a SportsCar is not equal to null
        assertNotEquals(null, sportsCar);

        // Test that a SportsCar is not equal to a different object type
        assertNotEquals(new Object(), sportsCar);
    }

    @Test
    void testToString() {
        // Test that the toString method returns the correct string representation
        assertTrue(sportsCar.toString().startsWith("SportsCar"));
    }
}
