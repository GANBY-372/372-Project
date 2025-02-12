package edu.metrostate.ics372.ganby.vehicle;

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

    public static String getVehicleType (Vehicle vehicle) {
        if (vehicle instanceof SUV) return "SUV";
        else if (vehicle instanceof Sedan) return "Sedan";
        else if (vehicle instanceof Pickup) return "Pickup";
        else if (vehicle instanceof SportsCar) return "SportsCar";
        else return null;
    }
}