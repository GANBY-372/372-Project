

package edu.metrostate.ics372.GANBY.JSON;

import edu.metrostate.ics372.GANBY.Dealer.*;
import edu.metrostate.ics372.GANBY.Catalogs.*;
import edu.metrostate.ics372.GANBY.Vehicle.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneOffset;

public class JSONFileExporter {

    private final File file;

    //Constructor, we are initializing the file path
    public JSONFileExporter() {
        file = new File("./data.json");
        //exportToFile();
    }

    //Method for exporting data for a specific dealer (By getting dealerId)
    public void exportToFile(Integer dealerId) {
        Dealer dealer = DealerCatalog.getInstance().getDealerCollection().get(dealerId);
        if (dealer == null){
        System.out.println("Dealer with id " + dealerId + " not found");
        return;
        }

        //Array to store vehicle data
        JSONArray jsonArray = new JSONArray();

        //Loop through the array to store all the vehicle data
        for (Vehicle vehicle : dealer.getVehicles().values()){
            JSONObject vehicleObject = new JSONObject();
            vehicleObject.put("dealership_id", dealer.getId());
            vehicleObject.put("vehicle_type", vehicle.getType());
            vehicleObject.put("vehicle_manufactorer", vehicle.getManufacturer());
            vehicleObject.put("vehicle_model", vehicle.getModel());
            vehicleObject.put("vehicle_id", vehicle.getVehicleId());
            vehicleObject.put("price", vehicle.getPrice());
            vehicleObject.put("acquisition_date", vehicle.getAcquisitionDate().toEpochSecond(ZoneOffset.UTC));

            //Add the object to our array
            jsonArray.add(vehicleObject);

        }



        //write the JSON array to the file
        writeToFile(jsonArray);
    }


        public void exportAllDealersToFile(){
        JSONArray allDealersArray = new JSONArray(); //store dealers

          for (Dealer dealer : DealerCatalog.getInstance().getDealerCollection().values()) {
                JSONArray dealerVehiclesArray = new JSONArray(); //Vehicles associated with dealers

              //Loop through the array to sotre all the vehicle data
              for (Vehicle vehicle : dealer.getVehicles().values()) {
                  JSONObject vehicleObject = new JSONObject();
                  vehicleObject.put("dealership_id", dealer.getId());
                  vehicleObject.put("vehicle_type", vehicle.getType());
                  vehicleObject.put("vehicle_manufactorer", vehicle.getManufacturer());
                  vehicleObject.put("vehicle_model", vehicle.getModel());
                  vehicleObject.put("vehicle_id", vehicle.getDealerId());
                  vehicleObject.put("price", vehicle.getPrice());
                  vehicleObject.put("acquisition_date", vehicle.getAcquisitionDate());

                  //Add the object to our array
                 allDealersArray.add(vehicleObject);


                  // Create dealer JSON object
                  JSONObject dealerObject = new JSONObject();
                  dealerObject.put("dealer_id", dealer.getId());
                  dealerObject.put("vehicles", dealerVehiclesArray);

                  allDealersArray.add(vehicleObject);

              }


          }

          writeToFile(allDealersArray);


        }


        //helper method to write the JSON array to the file
    private void writeToFile(JSONArray jsonArray){
        try {
            //We check to see if the directory exists before we write the file
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) { //if parentDir is null or the directory does NOT exist

                parentDir.mkdirs(); //We make the directory (for now we can prob implement this in a better way but for starters)
            }

            //Create the file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile(); //we create a file
            }

            //Write the JSON array to the file
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write(jsonArray.toJSONString());
                fileWriter.flush(); //basically confirms that it is pushed out
                System.out.println("Date exported successfully to: " + file.getAbsolutePath());

            }
        }catch (IOException e){

            e.printStackTrace();
        }

        }


    }







