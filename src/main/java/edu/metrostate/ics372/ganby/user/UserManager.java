package edu.metrostate.ics372.ganby.user;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for managing a simple user login system.
 * Stores a single username and password in a local text file (user.txt).
 * Provides methods to check for user existence, save new credentials, and validate login attempts.
 */
public class UserManager {
    private static final String USER_FILE = "user.txt";  // File where user credentials are stored

    /**
     * Checks whether the user file exists.
     *
     * @return true if the user.txt file exists, false otherwise
     */
    public static boolean userExists() {
        return Files.exists(Paths.get(USER_FILE));
    }

    /**
     * Saves a new username and password to the user file.
     * Overwrites any existing content.
     *
     * @param username the username to save
     * @param password the password to save
     * @throws IOException if there is an error writing to the file
     */
    public static void saveUser(String username, String password) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            writer.write(username + "," + password);  // Store credentials in CSV format
        }
    }

    /**
     * Validates the provided username and password against the saved credentials.
     *
     * @param username the entered username
     * @param password the entered password
     * @return true if credentials match, false otherwise
     * @throws IOException if there is an error reading the user file
     */
    public static boolean validateUser(String username, String password) throws IOException {
        // If the file doesn't exist, there are no users to validate
        if (!userExists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line = reader.readLine();  // Read the first (and only) line
            if (line != null) {
                String[] parts = line.split(",");  // Split into [username, password]
                if (parts.length == 2) {
                    return parts[0].equals(username) && parts[1].equals(password);
                }
            }
        }
        return false;
    }
}
