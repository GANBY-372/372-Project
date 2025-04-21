import edu.metrostate.ics372.ganby.dealer.Dealer;
import edu.metrostate.ics372.ganby.dealer.DealerCatalog;
import edu.metrostate.ics372.ganby.vehicle.Vehicle;
import javafx.application.Application;
import javafx.stage.Stage;
import org.w3c.dom.Document;

import java.util.Scanner;

/**
 * Interactive test driver for validating JSON and XML importers
 * using both file paths and GUI-based file selection.
 */
public class FilesTestDriver extends Application {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void start(Stage primaryStage) {
        System.out.println("FilesTestDriver Started\n");

        String input;
        do {
            printMenu();
            System.out.print("Enter your choice: ");
            input = scanner.nextLine().trim().toLowerCase();

            switch (input) {
                case "1" -> runJsonPathImport();
                case "2" -> runXmlPathImport();
                case "3" -> printCatalogContents();
                case "4" -> runAllTests(primaryStage);
                case "q", "exit" -> System.out.println("Exiting FilesTestDriver...");
                default -> System.out.println("Invalid option. Please try again.");
            }

        } while (!input.equals("q") && !input.equals("exit"));
    }

    private void printMenu() {
        System.out.println("""
            -------------------- MENU --------------------
            [1] Run JSON import via file path
            [2] Run XML import via file path
            [3] Show current DealerCatalog contents
            [4] Run all tests
            [q] Quit
            -----------------------------------------------
        """);
    }

    private void runAllTests(Stage stage) {
        runJsonPathImport();
        runXmlPathImport();
        printCatalogContents();
    }

    private void runJsonPathImport() {
        try {
            System.out.println("\n[1] JSON import via file path:");
            JSONFileImporter importer = new JSONFileImporter("src/main/resources/Imports/inventory.json");
            importer.processJSON();
        } catch (Exception e) {
            System.err.println("JSON (path) import failed: " + e.getMessage());
        }
    }

    private void runXmlPathImport() {
        try {
            System.out.println("\n[3] XML import via file path:");
            XMLFileImporter importer = new XMLFileImporter("src/main/resources/Imports/Dealer.xml");
            Document doc = importer.getXmlDocument();
            if (doc != null) {
                importer.processXML(doc);
            }
        } catch (Exception e) {
            System.err.println("XML (path) import failed: " + e.getMessage());
        }
    }

    private void printCatalogContents() {
        System.out.println("\n Current DealerCatalog:");
        for (Dealer dealer : DealerCatalog.getInstance().getDealers()) {
            System.out.println("Dealer: " + dealer.getId() + " - " + dealer.getName());
            for (Vehicle vehicle : dealer.getVehicleCatalog().values()) {
                System.out.printf("  ↳ Vehicle: %s (%s %s), $%.2f\n",
                        vehicle.getVehicleId(),
                        vehicle.getManufacturer(),
                        vehicle.getModel(),
                        vehicle.getPrice());
                        vehicle.getAcquisitionDate();
                        vehicle.getIsRentedOut();
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
