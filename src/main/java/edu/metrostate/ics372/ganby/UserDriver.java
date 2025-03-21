package edu.metrostate.ics372.ganby;

import edu.metrostate.ics372.ganby.catalog.DealerCatalog;
import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.json.JSONFileExporter;
import edu.metrostate.ics372.ganby.json.JSONFileImporter;
import edu.metrostate.ics372.ganby.xml.XMLFileImporter;
import org.w3c.dom.Document;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.awt.FileDialog;
import java.awt.Frame;

public class UserDriver {

    public static void main(String[] args) throws FileNotFoundException, IllegalAccessException {
        runProgram();   // This is the master method that runs the program. It uses the other three methods when appropriate.
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
        System.out.println("Dealers: " + DealerCatalog.getInstance().amountOfAllDealers() +
                "\t\tVehicles: " + DealerCatalog.getInstance().amountOfAllVehicles());

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
            System.out.println("1. Import JSON or XML vehicle inventory file.");
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
                case 1: // Importing a JSON or XML file
                    System.out.println("Please choose a file type to import: ");
                    System.out.println("1. JSON");
                    System.out.println("2. XML");
                    System.out.println("or type the file format directly (json or xml)");

                    String userInputFile = scanner.nextLine().toLowerCase(); // Get user input as a string and convert to lowercase

                    // If the user selects JSON or 'json', call the JSONFileImporter
                    if (userInputFile.equals("1") || userInputFile.equals("json")) {
                        JSONFileImporter jsonFileImporter = new JSONFileImporter();
                        jsonFileImporter.processJSON();  // This will open the file dialog and process the selected JSON file
                    }
                    // If the user selects XML or 'xml', call the XMLFileImporter
                    else if (userInputFile.equals("2") || userInputFile.equals("xml")) {
                        try {
                            // Create XMLFileImporter instance to select and parse XML
                            XMLFileImporter xmlFileImporter = new XMLFileImporter();

                            // Get the Document object from XMLFileImporter
                            Document xmlDocument = xmlFileImporter.getXmlDocument();

                            // Pass the Document object to processXML()
                            if (xmlDocument != null) {
                                xmlFileImporter.processXML(xmlDocument);
                            }

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("Invalid choice. Please select either 1, 2, json, or xml.");
                    }
                    break;

                case 2: // Enabling or Disabling Dealer Acquisition
                    System.out.print("Enter ID of dealer: (or Q to quit) ");
                    if (scanner.hasNextInt()) {
                        String idToEnableOrDisable = scanner.nextLine();

                        label:
                        while (true) {
                            System.out.print("Enable (E) or Disable (D) acquisition (or Q to quit): ");
                            String option = scanner.next().trim().toUpperCase();

                            switch (option) {
                                case "E":
                                    DealerCatalog.getInstance().enableDealerAcquisition(idToEnableOrDisable);
                                    break label;
                                case "D":
                                    DealerCatalog.getInstance().disableDealerAcquisition(idToEnableOrDisable);
                                    break label;
                                case "Q":
                                    System.out.println("Operation canceled.");
                                    break label;
                                default:
                                    System.out.println("Invalid option. Please enter E to enable, D to disable, " +
                                            "or Q to quit.");
                                    break;
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

                case 3: // Exporting Dealer Inventory
                    while (true) {
                        System.out.print("Enter ID of dealer (or Q to quit): ");
                        String id = scanner.nextLine().trim();

                        if (id.equalsIgnoreCase("Q")) {
                            System.out.println("Operation canceled.");
                            break;
                        }

                        Dealer dealer = DealerCatalog.getInstance().getDealerWithId(id);
                        if (dealer == null) {
                            System.out.println("Dealer with ID " + id + " not found. Please enter a valid ID.");
                            continue;
                        }

                        System.out.println("Selected dealer: " + dealer.getDealerId());

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
                            System.out.println("Inventory exported successfully.");
                        } else {
                            System.out.println("Export operation canceled.");
                        }
                        break;
                    }
                    break;

                case 4: // Shows list of current vehicles for each dealer
                    DealerCatalog.getInstance().printAllVehiclesByDealerId();
                    break;

                case 5: // End program
                    System.out.println("Ending Program.");
                    printCurrentInventoryStatus();  // Lets user take a last look at inventory
                    printExitMessage();    // Prints final exit message
                    break;

                default:
                    System.out.println("Invalid option. Please enter a number between 1 and 5.");
            }
        }
        scanner.close(); // Close scanner when exiting loop
    }
}
