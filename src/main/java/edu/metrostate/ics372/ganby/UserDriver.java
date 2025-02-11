/**
 * UserDriver.java
 * @author G
 * This class runs the user interface for the GANBY Dealership Inventory Manager.
 * It will display a welcome banner, and the main menu.
 * User is able to enter numbers to navigate through the menu.
 * Main menu options are listed in order respective of the requirements.
 *  ---
 * Need to complete:
 *     -    method to list the vehicles in the catalog
 *     -    method to add a vehicle to the catalog? Or this part of the JSON importer?
 *     -    method to print JSON file as a string?
 *
 */

package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import edu.metrostate.ics372.ganby.json.JSONFileImporter;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

public class UserDriver {

    public static void main(String[] args) throws FileNotFoundException, IllegalAccessException {

        printWelcomeBanner();

        System.out.println("\nPlease load a JSON file to begin. Press enter to continue to file explorer.");
        // System.console().readLine();
        JSONFileImporter jsonFileImporter = new JSONFileImporter();
        jsonFileImporter.processJSON();

        printMainMenu();
        acceptMainMenuOption();
        printExitMessage();
    }

    // Method to print the welcome banner
    public static void printWelcomeBanner() {
        System.out.println("\n*********************************************************");
        System.out.println("*                                                       *");
        System.out.println("*  Welcome to the GANBY Dealership Inventory Manager!   *");
        System.out.println("*                                                       *");
        System.out.println("*********************************************************");
    }

    // Method to print the exit message
    public static void printExitMessage() {
        System.out.println("\n\n******************************************************************");
        System.out.println("*                                                                *");
        System.out.println("*  Thank you for using the GANBY Dealership Inventory Manager!   *");
        System.out.println("*                                                                *");
        System.out.println("******************************************************************");
    }

    // Method to print the main menu
    public static void printMainMenu() {
        System.out.println("\nCurrent Dealer Catalog as of " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ":");
        System.out.println("-------------------------------------------------------------------");

        // Dealer and Vehicle count
        System.out.println("Dealers: " + DealerCatalog.getInstance().getDealers().size() + "\t\tVehicles: " + VehicleCatalog.getInstance().getVehicles().size());

        // List of dealers
        System.out.println("\n" + DealerCatalog.getInstance().getDealers().toString());

        //List of vehicles at each dealer
        //System.out.println("\n" + VehicleCatalog.getInstance().getVehicles().toString());

        System.out.println("\n-------------------------------------------------------------------");

        // Main Menu options
        System.out.println("Menu Options:");
        System.out.println("1. Import JSON vehicle inventory file.");
        System.out.println("2. Enable or disable vehicle acquisition.");
        System.out.println("3. Export dealer inventory to JSON file.");
        System.out.println("4. List vehicles in the catalog.");
        System.out.println("-------------------------------------------------------------------");
        System.out.println("Enter option number: ");
    }

    // Method to accept Main Menu option input from user
    // This method will be used to navigate through the main menu
    // It will accept user input and call the respective method
    // It will only accept a number that has an option
    // any other input will be rejected, and the user will be prompted to reenter a valid number.
    public static void acceptMainMenuOption() {
        int option = 0;
        boolean validOption = false;
        while (!validOption) {
            try {
                option = Integer.parseInt(System.console().readLine());
                if (option > 0 && option < 9) {
                    validOption = true;
                } else {
                    System.out.println("Invalid option. Please enter a number between 1 and 8.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number between 1 and 8.");
            }
        }
        switch (option) {
                case 1:
                    System.out.println("Import JSON vehicle file.");
                    // importJSONVehicleFile();
                    break;
                case 2:
                    break;
                case 3:
                    break;
        }
    }

    // Method to read JSON vehicle file
    public static void readJSONVehicleFile() {
        System.out.println("Reading JSON vehicle file...");


    }
}
