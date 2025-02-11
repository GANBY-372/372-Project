/**
 * UserDriver.java
 * @author Gil
 * This class runs the user interface for the GANBY Dealership Inventory Manager.
 * It will display a welcome banner, and the main menu.
 * User is able to enter numbers to navigate through the menu.
 * Main menu options are listed in order respective of the requirements.
 */

package edu.metrostate.ics372.ganby;

public class UserDriver {
    public static void main(String[] args) {
        printWelcomeBanner();
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
        System.out.println("\nMain Menu");
        System.out.println("----------------------------------------------------");
        System.out.println("Enter the number of the option you would like to select:");
        System.out.println("\t1. Read JSON vehicle file. (This will bring up file explorer)");
        System.out.println("\t2. Import JSON vehicle file. (This will bring up file explorer)");
        System.out.println("\t3. Add incoming vehicle");
        System.out.println("\t4. Enable Vehicle Acquisition");
        System.out.println("\t5. Disable Vehicle Acquisition");
        System.out.println("\t6. Export all vehicles from a dealer to a JSON file");
        System.out.println("\t7. View dealer inventory");
        System.out.println("\t8. Exit");
        System.out.println("----------------------------------------------------");
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
                System.out.println("Read JSON vehicle file.");
                // readJSONVehicleFile();
                break;
            case 2:
                System.out.println("Import JSON vehicle file.");
                // importJSONVehicleFile();
                break;
            case 3:
                System.out.println("Add incoming vehicle.");
                // addIncomingVehicle();
                break;
            case 4:
                System.out.println("Enable Vehicle Acquisition.");
                // enableVehicleAcquisition();
                break;
            case 5:
                System.out.println("Disable Vehicle Acquisition.");
                // disableVehicleAcquisition();
                break;
            case 6:
                System.out.println("Export all vehicles from a dealer to a JSON file.");
                // exportAllVehiclesToJSON();
                break;
            case 7:
                System.out.println("View dealer inventory.");
                // viewDealerInventory();
                break;
            case 8:
                System.out.println("Exit.");
                break;
        }
    }
}
