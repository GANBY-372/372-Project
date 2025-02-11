package edu.metrostate.ics372.ganby;

public class UserDriver {
    public static void main(String[] args) {
        printWelcomeBanner();
        printMainMenu();
    }

    // Method to print the welcome banner
    public static void printWelcomeBanner() {
        System.out.println("\n\n****************************************************");
        System.out.println("*                                                  *");
        System.out.println("*  Welcome to the Dealership Inventory Manager!    *");
        System.out.println("*                                                  *");
        System.out.println("****************************************************");
    }

    // Method to print the main menu
    public static void printMainMenu() {


        System.out.println("\nMain Menu");

        System.out.println("----------------------------------------------------");
        System.out.println("Enter the number of the option you would like to select:");
        System.out.println("\t1. Add a vehicle");
        System.out.println("\t2. Add a dealer");
        System.out.println("\t3. List all vehicles");
        System.out.println("\t4. List all dealers");
        System.out.println("\t5. Export to JSON");
        System.out.println("\t6. Import from JSON");
        System.out.println("\t7. Exit");
        System.out.println("----------------------------------------------------\n");
    }
}
