package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.json.JSONFileExporter;
import edu.metrostate.ics372.ganby.json.JSONFileImporter;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import java.awt.FileDialog;
import java.awt.Frame;

public class UserDriver {

    public static void main(String[] args) throws FileNotFoundException, IllegalAccessException {
        runProgram();   //This is the master method that runs the program. It uses the other three methods when
                        //appropriate.
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

    public static void printCurrentInventoryStatus() {
        System.out.println("\nCurrent Dealer Catalog as of " + java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ":");
        System.out.println("-------------------------------------------------------------------");

        // Dealer and Vehicle count
        System.out.println("Dealers: " + DealerCatalog.getInstance().getDealers().size() + "\t\tVehicles: " + VehicleCatalog.getInstance().getVehicles().size());

        // List of dealers
        DealerCatalog.getInstance().printAllDealers();

        System.out.println("\n-------------------------------------------------------------------");

    }

    // Method to print the main menu
    public static void runProgram() throws FileNotFoundException, IllegalAccessException {

        printWelcomeBanner();

        Scanner scanner = new Scanner(System.in); // Use a single scanner instance for the user input
        int userInput = -1;

        while (userInput != 5) { // Continue until user chooses to exit
            printCurrentInventoryStatus();
            System.out.println("\nMenu Options:");
            System.out.println("1. Import JSON vehicle inventory file.");
            System.out.println("2. Enable or disable vehicle acquisition.");
            System.out.println("3. Export dealer inventory to JSON file.");
            System.out.println("4. Show list of current vehicles for each dealer.");
            System.out.println("5. End Program.");
            System.out.println("-------------------------------------------------------------------");

            System.out.print("Enter option number:\n");
            if (!scanner.hasNextInt()) { // Handle non-integer input
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.next(); // Clear invalid input
                continue;
            }
            userInput = scanner.nextInt();
            scanner.nextLine(); // Consume newline to avoid InputMismatchException

            switch (userInput) {
                case 1: //Importing a JSON file. Works for inventory.json or any json file that contains vehicle(s)
                    JSONFileImporter fileImporter = new JSONFileImporter();
                    fileImporter.processJSON();
                    break;

                case 2: //Enabling or Disabling Dealer Acquisition
                    System.out.print("Enter ID of dealer: (or Q to quit) ");
                    if (scanner.hasNextInt()) {
                        int idToEnableOrDisable = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        while (true) {
                            System.out.print("Enable (E) or Disable (D) acquisition (or Q to quit): ");
                            String option = scanner.next().trim().toUpperCase();

                            if (option.equals("E")) {
                                DealerCatalog.getInstance().enableDealerAcquisition(idToEnableOrDisable);
                                break;
                            } else if (option.equals("D")) {
                                DealerCatalog.getInstance().disableDealerAcquisition(idToEnableOrDisable);
                                break;
                            } else if (option.equals("Q")) {
                                System.out.println("Operation canceled.");
                                break;
                            } else {
                                System.out.println("Invalid option. Please enter E to enable, D to disable, " +
                                        "or Q to quit.");
                            }
                        }
                    } else if (scanner.hasNext("Q") || scanner.hasNext("q")) {
                        scanner.next(); // Consume "Q"
                        System.out.println("Operation canceled.");
                    } else {
                        System.out.println("Invalid input. Please enter a valid dealer ID or Q to quit.");
                        scanner.next(); // Clear invalid input
                    }
                    break;



                case 3: //Exporting Dealer Inventory
                    // Ask user for dealer ID
                    System.out.print("Enter ID of dealer: ");
                    if (scanner.hasNextInt()) {
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Find the dealer by ID
                        Dealer dealer = DealerCatalog.getInstance().getDealers().findDealerById(id);
                        if (dealer == null) {
                            System.out.println("Dealer with ID " + id + " not found.");
                            break;
                        }

                        // Now, ask user where to save the file
                        System.out.println("Selected dealer: " + dealer.getId());

                        // Use FileDialog for file selection
                        FileDialog fileDialog = new FileDialog((Frame) null, "Save File", FileDialog.SAVE);
                        fileDialog.setFile("dealer_inventory_" + id + ".json"); // Default filename
                        fileDialog.setVisible(true);

                        // Get selected file
                        String directory = fileDialog.getDirectory();
                        String filename = fileDialog.getFile();

                        if (directory != null && filename != null) {
                            String filePath = directory + filename; // Construct full path
                            JSONFileExporter fileExporter = new JSONFileExporter();
                            fileExporter.exportToFile(dealer, filePath);
                        } else {
                            System.out.println("Export operation cancelled.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a valid dealer ID.");
                        scanner.next(); // Clear invalid input
                    }
                    break;



                case 4: //Shows list of current vehicles for each dealer
                    DealerCatalog.getInstance().printAllVehiclesById();
                    break;

                case 5: //End program.
                    System.out.println("Ending Program.");
                    printCurrentInventoryStatus();  //Lets user take a last look as inventory
                    printExitMessage();    //Prints final exit message
                    break;

                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 5.");
            }
        }
        scanner.close(); // Close scanner when exiting loop
    }
}
