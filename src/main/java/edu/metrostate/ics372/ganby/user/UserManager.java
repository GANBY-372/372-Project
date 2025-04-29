package edu.metrostate.ics372.ganby.user;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UserManager {
    private static final String USER_FILE = "user.txt";

    public static boolean userExists() {
        return Files.exists(Paths.get(USER_FILE));
    }

    public static void saveUser(String username, String password) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_FILE))) {
            writer.write(username + "," + password);
        }
    }

    public static boolean validateUser(String username, String password) throws IOException {
        if (!userExists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(USER_FILE))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    return parts[0].equals(username) && parts[1].equals(password);
                }
            }
        }
        return false;
    }
}