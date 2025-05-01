package edu.metrostate.ics372.ganby.dealer;

import edu.metrostate.ics372.ganby.vehicle.Pickup;
import edu.metrostate.ics372.ganby.vehicle.Sedan;
import edu.metrostate.ics372.ganby.vehicle.SportsCar;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DealerCatalogTest {

    private DealerCatalog catalog;
    private LocalDateTime now;
    private Dealer d001, d002;
    private Vehicle v001, v002, v003, v004;

    @BeforeEach
    void setUp() {
        catalog = DealerCatalog.getInstance();
        catalog.clear();
        now = LocalDateTime.now();

        d001 = new Dealer("001", "Toyota of St. Paul");
        d002 = new Dealer("002", "Honda of St. Paul");

        catalog.addDealer(d001);
        catalog.addDealer(d002);

        v001 = new Sedan("V001", "Camry", "Toyota", 15000.00, "001", now, false);
        v002 = new Pickup("V002", "F-150", "Ford", 25000.00, "001", now, false);
        v003 = new SportsCar("V003", "Corvette", "Chevrolet", 50000.00, "001", now, false);
        v004 = new Sedan("V004", "Accord", "Honda", 20000.00, "002", now, false);

        catalog.addVehicle(v001);
        catalog.addVehicle(v002);
        catalog.addVehicle(v003);
        catalog.addVehicle(v004);
    }

    @AfterEach
    void tearDown() {
        catalog.clear();
    }

    @Test
    void getInstance() {
        assertNotNull(DealerCatalog.getInstance());
    }

    @Test
    void getDealerWithId() {
        assertEquals(d001, catalog.getDealerWithId("001"));
        assertEquals(d002, catalog.getDealerWithId("002"));
        assertNull(catalog.getDealerWithId("999"));
    }

    @Test
    void getDealerWithName() {
        assertEquals(d001, catalog.getDealerWithName("Toyota of St. Paul"));
        assertEquals(d002, catalog.getDealerWithName("honda of st. paul"));
        assertNull(catalog.getDealerWithName("Not Here"));
        assertNull(catalog.getDealerWithName(null));
    }

    @Test
    void getDealersWhoAreBuying() {
        catalog.disableDealerAcquisition("001");
        ObservableList<Dealer> buyers = catalog.getDealersWhoAreBuying();
        assertEquals(1, buyers.size());
        assertTrue(buyers.contains(d002));
    }

    @Test
    void addVehicle() {
        Vehicle v005 = new Sedan("V005", "Civic", "Honda", 18000.00, "002", now, false);
        catalog.addVehicle(v005);
        assertNotNull(catalog.getDealerWithId("002").findVehicleById("V005"));
    }

    @Test
    void addVehicleFromAutosave_shouldIgnoreAcquisitionStatus() {
        catalog.disableDealerAcquisition("002");
        Vehicle v006 = new Sedan("V006", "Insight", "Honda", 17000.00, "002", now, false);
        catalog.addVehicleFromAutosave(v006);
        assertNotNull(catalog.getDealerWithId("002").findVehicleById("V006"));
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
    void getTypes_shouldReturnAllVehicleTypes() {
        ArrayList<String> types = catalog.getTypes();
        assertTrue(types.contains("Sedan"));
        assertTrue(types.contains("Pickup"));
        assertTrue(types.contains("SportsCar"));
        assertEquals(3, types.size());
    }

    @Test
    void enableDisableDealerAcquisition() {
        catalog.disableDealerAcquisition("001");
        assertFalse(catalog.getDealerWithId("001").isVehicleAcquisitionEnabled());

        catalog.enableDealerAcquisition("001");
        assertTrue(catalog.getDealerWithId("001").isVehicleAcquisitionEnabled());
    }

    @Test
    void updateDealer_shouldChangeName() {
        Dealer updated = new Dealer("001", "New Toyota Name");
        catalog.updateDealer(updated);
        assertEquals("New Toyota Name", catalog.getDealerWithId("001").getName());
    }

    @Test
    void transferInventory_shouldMoveVehiclesToNewDealer() {
        ArrayList<Vehicle> vehiclesToMove = new ArrayList<>(List.of(v001, v002));
        Dealer newDealer = new Dealer("009", "Subaru of St. Paul");
        catalog.addDealer(newDealer);

        catalog.transferInventory(vehiclesToMove, "009");

        assertNotNull(catalog.getDealerWithId("009").findVehicleById("V001"));
        assertNotNull(catalog.getDealerWithId("009").findVehicleById("V002"));
        assertEquals("009", v001.getDealerId());
    }

    @Test
    void addDealer_shouldPreventDuplicates() {
        Dealer duplicate = new Dealer("001", "Some Name");
        catalog.addDealer(duplicate);
        assertEquals(2, catalog.amountOfAllDealers());
        assertEquals("Toyota of St. Paul", catalog.getDealerWithId("001").getName());
    }

    @Test
    void addVehicle_shouldIgnoreDuplicatesAndRespectAcquisitionStatus() {
        catalog.clear();
        Dealer freshDealer = new Dealer("001", "Fresh Dealer");
        catalog.addDealer(freshDealer);

        Vehicle v1 = new Sedan("DUPLICATE", "Camry", "Toyota", 20000.0, "001", now, false);
        Vehicle v2 = new Sedan("NEW", "Avalon", "Toyota", 25000.0, "001", now, false);

        catalog.addVehicle(v1);
        catalog.addVehicle(v1); // duplicate

        freshDealer.disableVehicleAcquisition("001");
        catalog.addVehicle(v2); // should be ignored

        assertEquals(1, catalog.getDealerWithId("001").vehicleCatalog.size());
    }
}
