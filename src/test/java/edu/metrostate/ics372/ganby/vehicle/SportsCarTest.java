package edu.metrostate.ics372.ganby.vehicle;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SportsCarTest {

    private SportsCar sportsCar;

    @BeforeEach
    void setUp() {
        sportsCar = new SportsCar("123", "Model S", "Tesla", 100000, "dealer123", LocalDateTime.now(), true);
    }

    @AfterEach
    void tearDown() {
        sportsCar = null;
    }

    @Test
    void getType() {
        assertEquals("SportsCar", sportsCar.getType());
    }

    @Test
    void defaultRentStatusIsFalseEvenIfTruePassedToConstructor() {
        assertFalse(sportsCar.getIsRentedOut(), "Constructor should always set rented out to false");
    }

    @Test
    void setRentedOutWorksLikeVehicle() {
        sportsCar.setRentedOut(true);
        assertTrue(sportsCar.getIsRentedOut(), "setRentedOut(true) should succeed");

        sportsCar.setRentedOut(false);
        assertFalse(sportsCar.getIsRentedOut(), "setRentedOut(false) should succeed");
    }

    @Test
    void testEquals() {
        SportsCar duplicate = new SportsCar("123", "Model S", "Tesla", 99999, "otherDealer", LocalDateTime.now());
        assertEquals(sportsCar, duplicate);

        assertNotEquals(null, sportsCar);
        assertNotEquals("not a car", sportsCar);
    }

    @Test
    void testToString() {
        String str = sportsCar.toString();
        assertTrue(str.startsWith("SportsCar SportsCar [id: 123"), "toString() should start with 'SportsCar'");
    }
}
