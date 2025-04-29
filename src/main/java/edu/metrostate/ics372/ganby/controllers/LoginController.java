package edu.metrostate.ics372.ganby.controllers;

import edu.metrostate.ics372.ganby.FXDriver;
import edu.metrostate.ics372.ganby.user.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Username and password cannot be empty.");
            return;
        }

        try {
            if (UserManager.validateUser(username, password)) {
                FXDriver.launchMainApp((Stage) usernameField.getScene().getWindow());
            } else {
                showAlert("Invalid username or password.");
            }
        } catch (Exception e) {
            showAlert("Login failed: " + e.getMessage());
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
