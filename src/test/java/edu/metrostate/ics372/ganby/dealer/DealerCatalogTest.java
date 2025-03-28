package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.vehicle.Pickup;
import edu.metrostate.ics372.ganby.vehicle.Sedan;
import edu.metrostate.ics372.ganby.vehicle.SportsCar;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.time.LocalDateTime;
import javafx.collections.ObservableList;
import java.util.ArrayList;

public class DealerCatalogTest {
    private DealerCatalog catalog;
    private LocalDateTime now;
    private Vehicle v001, v002, v003, v004;
    private Dealer d001, d002;

    @BeforeEach
    void setUp() {
        catalog = DealerCatalog.getInstance();
        catalog.clear(); // Ensure a clean slate before each test
        now = LocalDateTime.now();

        d001 = new Dealer("001", "Toyota of St. Paul");
        d002 = new Dealer("002", "Honda of St. Paul");

        catalog.addDealer(d001);
        catalog.addDealer(d002);

        v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", "name1", now, false);
        v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "001","name2", now, false);
        v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "001", "name3", now, false);
        v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "002","name4", now, false);

        catalog.addVehicle(v001);
        catalog.addVehicle(v002);
        catalog.addVehicle(v003);
        catalog.addVehicle(v004);
    }

    @AfterEach
    void tearDown() {
        catalog.clear(); // Cleanup after each test to maintain test isolation
    }

    @Test
    void getInstance() {
        assertNotNull(DealerCatalog.getInstance());
    }

    @Test
    void getDealerWithId() {
        assertEquals(d001, catalog.getDealerWithId("001"));
        assertEquals(d002, catalog.getDealerWithId("002"));
        assertNotEquals(d001, catalog.getDealerWithId("003"));
    }

    @Test
    void addVehicle() {
        Vehicle v005 = new Sedan("V005", "Civic", "Honda", 18000.00, "002", "name1", now, false);
        catalog.addVehicle(v005);
        assertNotNull(catalog.getDealerWithId("002").findVehicleById("V005"));
    }

    @Test
    void modifyVehiclePrice() {
        catalog.modifyVehiclePrice("V001", 20000.00);
        assertEquals(20000.00, catalog.getDealerWithId("001").findVehicleById("V001").getPrice());
    }

    @Test
    void amountOfAllDealers() {
        assertEquals(2, catalog.amountOfAllDealers());
    }

    @Test
    void amountOfAllVehicles() {
        assertEquals(4, catalog.amountOfAllVehicles());
    }

    @Test
    void getAllVehicles() {
        ArrayList<Vehicle> vehicles = catalog.getAllVehicles();
        assertEquals(4, vehicles.size());
    }

    @Test
    void getVehiclesByType() {
        ArrayList<Vehicle> sedans = catalog.getVehiclesByType("Sedan");
        assertEquals(2, sedans.size());
    }

    @Test
    void enableDisableDealerAcquisition() {
        catalog.disableDealerAcquisition("001");
        assertFalse(catalog.getDealerWithId("001").getIsVehicleAcquisitionEnabled());

        catalog.enableDealerAcquisition("001");
        assertTrue(catalog.getDealerWithId("001").getIsVehicleAcquisitionEnabled());
    }
}
