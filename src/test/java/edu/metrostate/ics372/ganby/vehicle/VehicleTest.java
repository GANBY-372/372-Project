package edu.metrostate.ics372.ganby.vehicle;

import edu.metrostate.ics372.ganby.dealer.Dealer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class VehicleTest {

    private Vehicle v001, v002, v003, v004;

    @BeforeEach
    void setUp() {
        v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", LocalDateTime.now(), false);
        v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "001", LocalDateTime.now(), false);
        v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "001", LocalDateTime.now(), false);
        v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "002", LocalDateTime.now(), false);
    }

    @AfterEach
    void tearDown() {
        v001 = v002 = v003 = v004 = null;
    }

    @Test
    void getType() {
        assertEquals("Sedan", v001.getType());
        assertEquals("Pickup", v002.getType());
        assertEquals("SportsCar", v003.getType());
    }

    @Test
    void setRentedOut() {
        v001.setRentedOut(true);
        assertTrue(v001.getIsRentedOut());

        v001.setRentedOut(false);
        assertFalse(v001.getIsRentedOut());
    }

    @Test
    void getIsRentedOut() {
        assertFalse(v001.getIsRentedOut());
        v001.setRentedOut(true);
        assertTrue(v001.getIsRentedOut());
    }

    @Test
    void getVehicleId() {
        assertEquals("V001", v001.getVehicleId());
    }

    @Test
    void getModel() {
        assertEquals("Camry", v001.getModel());
    }

    @Test
    void getManufacturer() {
        assertEquals("Toyota", v001.getManufacturer());
    }

    @Test
    void getPrice() {
        assertEquals(15000.00, v001.getPrice());
    }

    @Test
    void setPrice_ValidValue() {
        v001.setPrice(18000.00);
        assertEquals(18000.00, v001.getPrice());
    }

    @Test
    void setPrice_NegativeValue_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> v001.setPrice(-5000.00));
        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    void getDealerId() {
        assertEquals("001", v001.getDealerId());
    }

    @Test
    void getAcquisitionDate() {
        assertNotNull(v001.getAcquisitionDate());
    }

    @Test
    void setDealer_ValidDealer() {
        Dealer newDealer = new Dealer("003");
        v001.setDealer(newDealer);
        assertEquals("003", v001.getDealerId());
    }

    @Test
    void setDealer_NullDealer_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> v001.setDealer(null));
        assertEquals("Dealer cannot be null", exception.getMessage());
    }

    @Test
    void setAcquisitionDate_ValidDate() {
        LocalDateTime newDate = LocalDateTime.now().minusDays(10);
        v001.setAcquisitionDate(newDate);
        assertEquals(newDate, v001.getAcquisitionDate());
    }

    @Test
    void setAcquisitionDate_NullDate_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> v001.setAcquisitionDate(null));
        assertEquals("New acquisition date cannot be null", exception.getMessage());
    }

    @Test
    void testEquals_SameObject() {
        assertEquals(v001, v001);
    }

    @Test
    void testEquals_DifferentObjectSameId() {
        Vehicle duplicate = new Sedan("V001", "Different Model", "Different Manufacturer", 20000.00, "005", LocalDateTime.now(), false);
        assertEquals(v001, duplicate);
    }

    @Test
    void testEquals_NullObject() {
        assertNotEquals(v001, null);
    }

    @Test
    void testToString() {
        String expected = "Sedan Sedan [id:V001 model:Camry manufacturer:Toyota price:15000.0 dealer id:001 acquisitionDate:" + v001.getAcquisitionDate() + "]";
        assertEquals(expected, v001.toString());
    }
}