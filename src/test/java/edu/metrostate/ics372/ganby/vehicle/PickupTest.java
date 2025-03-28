package edu.metrostate.ics372.ganby.vehicle;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;


class PickupTest {

    @Test
    void getType() {
        Pickup pickup = new Pickup("V002", "F-150", "Ford", 25000.00, "001", "name1",LocalDateTime.now(), false);
        assertEquals("Pickup", pickup.getType(), "The type should be 'Pickup'");
    }

    @Test
    void testEquals() {
        Pickup pickup1 = new Pickup("V002", "F-150", "Ford", 25000.00, "001", "name2", LocalDateTime.now(), false);
        Pickup pickup2 = new Pickup("V002", "F-150", "Ford", 25000.00, "001","name3", LocalDateTime.now(),false);
        Pickup pickup3 = new Pickup("V003", "Ram 1500", "Dodge", 30000.00, "002","name4", LocalDateTime.now(),false);

        assertEquals(pickup1, pickup2, "Vehicles with the same ID should be equal");
        assertNotEquals(pickup1, pickup3, "Vehicles with different IDs should not be equal");
    }

    @Test
    void testToString() {
        LocalDateTime now = LocalDateTime.now();
        Pickup pickup = new Pickup("V002", "F-150", "Ford", 25000.00, "001", "name5", now, false);
        String expected = "Pickup Pickup [id:V002 model:F-150 manufacturer:Ford price:25000.0 dealer id:001 acquisitionDate:" + now + "]";
        assertEquals(expected, pickup.toString(), "The toString output should match the expected format");
    }
}
