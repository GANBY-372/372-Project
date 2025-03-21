/**
 * VehicleCategory.java
 * Enum class for vehicle categories
 */
package vehicle;
import vehicle.Vehicle;
public enum VehicleCategory {
    SUV, SEDAN, PICKUP, SPORTS_CAR;

    @Override
    public String toString() {
        return switch (this) {
            case SUV -> "Suv";
            case SEDAN -> "Sedan";
            case PICKUP -> "Pickup";
            case SPORTS_CAR -> "Sports car";
        };
    }

    public static VehicleCategory fromString (String string) {
        if (string.equalsIgnoreCase(SUV.toString())) return SUV;
        else if (string.equalsIgnoreCase(SEDAN.toString())) return SEDAN;
        else if (string.equalsIgnoreCase(PICKUP.toString())) return PICKUP;
        else if (
                string.equalsIgnoreCase(SPORTS_CAR.toString())
                        || string.equalsIgnoreCase("SPORTS_CAR")
                        || string.equalsIgnoreCase("SPORTSCAR")
        ) return SPORTS_CAR;
        else return null;
    }

    //Returns the type of vehicle: Pickup, Sedan, SportsCar, or SUV as a String
    public static String getVehicleType (Vehicle vehicle) {
        if (vehicle instanceof vehicle.SUV) return "SUV";
        else if (vehicle instanceof vehicle.Sedan) return "Sedan";
        else if (vehicle instanceof vehicle.Pickup) return "Pickup";
        else if (vehicle instanceof vehicle.SportsCar) return "SportsCar";
        else return null;
    }
}