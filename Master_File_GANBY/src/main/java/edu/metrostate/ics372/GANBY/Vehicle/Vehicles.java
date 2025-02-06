/*

package edu.metrostate.ics372.GANBY.Vehicle;

import edu.metrostate.ics372.GANBY.Dealer.*;

import edu.metrostate.ics372.GANBY.Vehicle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.*;

public class Vehicles {

    private final Set<Vehicle> vehicles;

    public Vehicles () {
        this.vehicles = new HashSet<>();
    }


    public int size() {
        return vehicles.size();
    }

    public Set<Vehicle> getVehicles () {
        return vehicles;
    }

    public Iterator<Vehicle> iterator() { return vehicles.iterator(); }

    public void addVehicle (Vehicle vehicle) throws IllegalAccessException {
        if (vehicle== null)
            throw new IllegalArgumentException("Cannot add null vehicleEntity to collection");
        if (vehicles.contains(vehicle)) {
            System.out.println(vehicle.getId() + " is already in the collection");
            return;
        } else {
            System.out.println("adding " + vehicle.toString() + " to the catalog");
            this.vehicles.add(vehicle);
            System.out.println(this.vehicles.size());
        }
    }

    public void removeVehicle (String id) {
        Vehicle vehicle = findVehicleById(id);
        if (vehicle != null) {
            vehicles.remove(vehicle);
        }
    }

    public Vehicle findVehicleById (String id) {
        for (Vehicle vehicle : vehicles) {
            if (id.equalsIgnoreCase(vehicle.getId())) return vehicle;
        }
        return null;
    }

    public Set<Vehicle> filterByDealer (Dealer dealer) {
        Set<Vehicle> matches = new HashSet<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getDealer().equals(dealer)) { matches.add(vehicle); }
        }
        return matches;
    }

    public Vehicle findDealerVehicleById(Dealer dealer, String vehicleId) {
        if (dealer == null)
            throw new IllegalArgumentException("The dealer cannot be null");
        for (Vehicle vehicle : filterByDealer(dealer)) {
            if (vehicle.getId().equalsIgnoreCase(vehicleId)) return vehicle;
        }
        return null;
    }

    public JSONArray toJSONArray () {
        JSONArray jsonArray = new JSONArray();
        for (Vehicle vehicle : vehicles) {
            JSONObject jsonObject = vehicle.toJSON();
            if (jsonObject != null) { jsonArray.add((JSONObject) vehicle.toJSON()); }
        }
        return jsonArray;
    }
}


 */
