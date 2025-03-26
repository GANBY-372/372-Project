package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.vehicle.Pickup;
import edu.metrostate.ics372.ganby.vehicle.Sedan;
import edu.metrostate.ics372.ganby.vehicle.SportsCar;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class DealerCatalogTest {
    private DealerCatalog catalog;
    private LocalDateTime now;
    private Vehicle v001, v002, v003, v004;
    private Dealer d001, d002;

    @BeforeEach
    void setUp() {
        catalog = DealerCatalog.getInstance();
        now = LocalDateTime.now();

        v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", now, false);
        v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "001", now, false);
        v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "001", now, false);
        v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "002", now, false);

        d001 = new Dealer("001", "Toyota of St. Paul");
        d002 = new Dealer("002", "Honda of St. Paul");

        catalog.addVehicle(v001);
        catalog.addVehicle(v002);
        catalog.addVehicle(v003);
        catalog.addVehicle(v004);
    }

    @Test
    void getInstance() {
        assertNotNull(DealerCatalog.getInstance());
    }

    @Test
    void getDealerWithId() {
        assertEquals(d001, catalog.getDealerWithId("001"));
        assertEquals(d002, catalog.getDealerWithId("002"));
    }

    @Test
    void addVehicle() {
        Vehicle v005 = new Sedan("V005", "Civic", "Honda", 18000.00, "002", now, false);
        catalog.addVehicle(v005);
        assertEquals(v005, catalog.getDealerWithId("002").findVehicleById("V005"));
    }

    @Test
    void modifyVehiclePrice() {
        catalog.modifyVehiclePrice("V001", 20000.00);
        assertEquals(20000.00, v001.getPrice());
    }

    @Test
    void printAllDealers() {
        assertEquals(2, catalog.amountOfAllDealers());
    }

    // TODO: fix this test
    @Test
    void printAllVehiclesByDealerId() {
//        assertEquals(3, catalog.getDealerWithId("001").getInventorySize());
//        assertEquals(1, catalog.getDealerWithId("002").getInventorySize());
    }

    @Test
    void amountOfAllVehicles() {
        assertEquals(4, catalog.amountOfAllVehicles());
    }
}
