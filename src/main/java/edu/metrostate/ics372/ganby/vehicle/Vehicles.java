/*

package edu.metrostate.ics372.ganby.vehicle;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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
        if (vehicle== null) {
            throw new IllegalArgumentException("Cannot add null vehicle to collection");
        } else if (vehicles.contains(vehicle)) {
            System.out.println("Vehicle Id #" + vehicle.getId() + " is already in the collection");
            return;
        } else if (!DealerCatalog.getInstance().getDealers().findDealerById(vehicle.getDealer().getId()).
                getVehicleAcquisitionStatus()) {
            System.out.println("Dealer Id #" + vehicle.getDealer().getId() + " is not accepting vehicles at the moment.");
        } else {
            this.vehicles.add(vehicle);

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
            if (jsonObject != null) {
                jsonArray.add((JSONObject) vehicle.toJSON()); }
        }
        return jsonArray;
    }
}


 */