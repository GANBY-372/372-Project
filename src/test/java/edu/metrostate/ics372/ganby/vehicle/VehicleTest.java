package edu.metrostate.ics372.ganby.vehicle;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
class VehicleTest {

    @BeforeEach
    void setUp() {
        Vehicle v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", LocalDateTime.now(), false);
        Vehicle v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "001", LocalDateTime.now(), false);
        Vehicle v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "001", LocalDateTime.now(), false);
        Vehicle v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "002", LocalDateTime.now(), false);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getType() {
    }

    @Test
    void setRentedOut() {
    }

    @Test
    void getIsRentedOut() {
    }

    @Test
    void getVehicleId() {
    }

    @Test
    void getModel() {
    }

    @Test
    void getManufacturer() {
    }

    @Test
    void getPrice() {
    }

    @Test
    void getDealerId() {
    }

    @Test
    void getAcquisitionDate() {
    }

    @Test
    void setPrice() {
    }

    @Test
    void setDealer() {
    }

    @Test
    void setAcquisitionDate() {
    }

    @Test
    void testEquals() {
    }

    @Test
    void testToString() {
    }
}