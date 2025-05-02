package edu.metrostate.ics372.ganby.controllers;

import edu.metrostate.ics372.ganby.FXAPP.AlertHelper;
import edu.metrostate.ics372.ganby.FXDriver;
import edu.metrostate.ics372.ganby.user.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateUserController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleCreateUser(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            AlertHelper.showWarning("Sign-in","Username and password cannot be empty.");
            return;
        }

        try {
            UserManager.saveUser(username, password);
            FXDriver.launchMainApp((Stage) usernameField.getScene().getWindow());
        } catch (Exception e) {
            AlertHelper.showError("Sign-in Error","Failed to save user: " + e.getMessage());
        }
    }

}

