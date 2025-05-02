package edu.metrostate.ics372.ganby.controllers;

import edu.metrostate.ics372.ganby.FXDriver;
import edu.metrostate.ics372.ganby.user.UserManager;
import edu.metrostate.ics372.ganby.FXAPP.AlertHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controller class for handling the login functionality in the application.
 * It verifies user credentials and launches the main application on successful login.
 */
public class LoginController {

    /** Text field for the user's username input. */
    @FXML
    private TextField usernameField;

    /** Password field for the user's password input. */
    @FXML
    private PasswordField passwordField;

    /**
     * Handles the login button click event.
     * Validates input and authenticates the user using {@link UserManager}.
     * If authentication succeeds, launches the main app; otherwise, shows an error.
     *
     * @param event the {@link ActionEvent} triggered by the login button
     */
    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Validate that both fields are filled
        if (username.isEmpty() || password.isEmpty()) {
            AlertHelper.showWarning("Login Warning", "Username and password cannot be empty.");
            return;
        }

        try {
            // Validate credentials using UserManager
            if (UserManager.validateUser(username, password)) {
                // Launch main application UI on successful login
                FXDriver.launchMainApp((Stage) usernameField.getScene().getWindow());
            } else {
                AlertHelper.showWarning("Login Warning", "Invalid username or password.");
            }
        } catch (Exception e) {
            // Handle unexpected errors during login process
            AlertHelper.showError("Login Error", "Login failed: " + e.getMessage());
        }
    }
}
