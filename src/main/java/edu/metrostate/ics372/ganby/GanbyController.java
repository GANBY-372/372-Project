package edu.metrostate.ics372.ganby;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GanbyController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick () {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}