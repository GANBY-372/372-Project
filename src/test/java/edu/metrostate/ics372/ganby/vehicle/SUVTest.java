package edu.metrostate.ics372.ganby.vehicle;

import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SUVTest {

    private SUV suv;

    @BeforeEach
    void setUp() {
        suv = new SUV("S001", "Highlander", "Toyota", 35000.00, "D001", LocalDateTime.now(), true);
    }

    @AfterEach
    void tearDown() {
        suv = null;
    }

    @Test
    void testTypeReturnsSUV() {
        assertEquals("SUV", suv.getType());
    }

    @Test
    void testConstructorWithIsRentedOutTrue() {
        assertTrue(suv.getIsRentedOut());
    }

    @Test
    void testConstructorDefaultsToNotRented() {
        SUV defaultRented = new SUV("S002", "Pilot", "Honda", 34000.00, "D002", LocalDateTime.now());
        assertFalse(defaultRented.getIsRentedOut());
    }

    @Test
    void testSetRentedOut() {
        suv.setRentedOut(false);
        assertFalse(suv.getIsRentedOut());

        suv.setRentedOut(true);
        assertTrue(suv.getIsRentedOut());
    }

    @Test
    void testAllInheritedProperties() {
        SUV testSUV = new SUV("S003", "Santa Fe", "Hyundai", 30000.0, "D003", LocalDateTime.now(), false);
        assertEquals("S003", testSUV.getVehicleId());
        assertEquals("Santa Fe", testSUV.getModel());
        assertEquals("Hyundai", testSUV.getManufacturer());
        assertEquals(30000.0, testSUV.getPrice());
        assertEquals("D003", testSUV.getDealerId());
        assertNotNull(testSUV.getAcquisitionDate());
    }

    @Test
    void testEqualsSameId() {
        SUV another = new SUV("S001", "Different Model", "Different Make", 99999.0, "XXX", LocalDateTime.now(), false);
        assertEquals(suv, another);
    }

    @Test
    void testEqualsDifferentId() {
        SUV different = new SUV("S999", "CX-5", "Mazda", 32000.0, "D005", LocalDateTime.now(), false);
        assertNotEquals(suv, different);
    }

    @Test
    void testEqualsNullAndDifferentType() {
        assertNotEquals(suv, null);
        assertNotEquals(suv, "not a vehicle");
    }

    @Test
    void testToStringIncludesSUVPrefix() {
        assertTrue(suv.toString().startsWith("SUV SUV [id:"));
    }
}
