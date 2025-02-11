package edu.metrostate.ics372.ganby;

public class UserDriver {
    public static void main(String[] args) {
        printWelcomeBanner();
        printMainMenu();
    }

    // Method to print the welcome banner
    public static void printWelcomeBanner() {
        System.out.println("\n\n*********************************************************");
        System.out.println("*                                                  *");
        System.out.println("*  Welcome to the GANBY Dealership Inventory Manager!    *");
        System.out.println("*                                                  *");
        System.out.println("*********************************************************");
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
        System.out.println("----------------------------------------------------\n");
    }


}
