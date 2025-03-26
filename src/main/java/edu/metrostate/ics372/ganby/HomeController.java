package edu.metrostate.ics372.ganby;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HomeController {
    public Button vehicleViewButton;
    public Button dealerViewButton;
    public ButtonBar fileMenuButtonBar;
    public ButtonBar entityViewButtonBar;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick () {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void openDealerView(ActionEvent event) {
        try {
            // Load the DealerView FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/metrostate/ics372/ganby/dealer/DealerView.fxml"));
            Parent dealerView = loader.load();

            // Create a new stage for the DealerView
            Stage dealerViewStage = new Stage();
            dealerViewStage.setTitle("Dealer View");
            dealerViewStage.setScene(new Scene(dealerView));
            dealerViewStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Print error in case of exception
        }
    }
}