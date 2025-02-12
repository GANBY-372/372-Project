package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.catalog.VehicleCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.json.JSONFileExporter;
import edu.metrostate.ics372.ganby.json.JSONFileImporter;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.swing.JFileChooser;
import java.io.File;

public class UserDriver {

    public static void main(String[] args) throws FileNotFoundException, IllegalAccessException {

        printWelcomeBanner();

        /* Commenting this out to let the user input the inventory JSON manually by choosing option 1 in the menu
        System.out.println("\nPlease load a JSON file to begin. Press enter to continue to file explorer.");
        // System.console().readLine();
        JSONFileImporter jsonFileImporter = new JSONFileImporter();
        jsonFileImporter.processJSON();
         */

        printOptions();
        //acceptMainMenuOption();
        printCurrentInventoryStatus();
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
    public static void printOptions() throws FileNotFoundException, IllegalAccessException {
        Scanner scanner = new Scanner(System.in); // Use a single scanner instance
        int userInput = -1;

        while (userInput != 5) { // Continue until user chooses to exit
            printCurrentInventoryStatus();
            System.out.println("\nMenu Options:");
            System.out.println("1. Import JSON vehicle inventory file.");
            System.out.println("2. Enable or disable vehicle acquisition.");
            System.out.println("3. Export dealer inventory to JSON file.");
            System.out.println("4. List all vehicles in the catalog.");
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
                case 1:
                    JSONFileImporter fileImporter = new JSONFileImporter();
                    fileImporter.processJSON();
                    break;

                case 2:
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

                case 3:
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

                        // Use JSONFileExporter to export the dealer's inventory to a file
                        JSONFileExporter fileExporter = new JSONFileExporter();
                        // Open a file chooser dialog for user to pick where to save the file
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setDialogTitle("Specify a file to save");
                        fileChooser.setSelectedFile(new File("dealer_inventory_" + id + ".json"));
                        int userSelection = fileChooser.showSaveDialog(null);

                        if (userSelection == JFileChooser.APPROVE_OPTION) {
                            // Get the selected file path
                            File fileToSave = fileChooser.getSelectedFile();
                            // Pass the file path to exportToFile method
                            fileExporter.exportToFile(dealer, fileToSave.getAbsolutePath());
                        } else {
                            System.out.println("Save operation cancelled.");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a valid dealer ID.");
                        scanner.next(); // Clear invalid input
                    }
                    break;


                case 4:
                    DealerCatalog.getInstance().printAllVehiclesById();
                    break;

                case 5:
                    System.out.println("Ending Program.");
                    printExitMessage();
                    break;

                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 5.");
            }
        }
        scanner.close(); // Close scanner when exiting loop
    }
}
