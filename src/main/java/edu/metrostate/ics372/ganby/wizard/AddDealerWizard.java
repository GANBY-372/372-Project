package edu.metrostate.ics372.ganby.wizard;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddDealerWizard {

    public void startWizard() {
        // Create a new Stage for the Wizard
        Stage wizardStage = new Stage();
        wizardStage.initModality(Modality.APPLICATION_MODAL); // Makes it modal
        wizardStage.setTitle("Add New Dealer Wizard");

        // Step 1: Dealer Information
        Label nameLabel = new Label("Dealer Name:");
        TextField nameField = new TextField();
        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();

        // Buttons for navigation
        Button nextButton = new Button("Next");
        Button cancelButton = new Button("Cancel");

        // GridPane for layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(locationLabel, 0, 1);
        grid.add(locationField, 1, 1);

        // Button actions
        nextButton.setOnAction(event -> {
            // Simulate form submission for now
            Alert successAlert = new Alert(AlertType.INFORMATION);
            successAlert.setTitle("Save Successful");
            successAlert.setHeaderText(null);
            successAlert.setContentText("New dealer added successfully!");
            successAlert.showAndWait();
            wizardStage.close();
        });

        cancelButton.setOnAction(event -> wizardStage.close()); // Close wizard

        // Add buttons to an HBox
        HBox buttonBox = new HBox(10, nextButton, cancelButton);

        // Layout: Combine grid and button box
        VBox wizardLayout = new VBox(10, grid, buttonBox);
        wizardLayout.setPadding(new Insets(10));

        // Set up the wizard scene
        Scene wizardScene = new Scene(wizardLayout, 400, 200);
        wizardStage.setScene(wizardScene);

        // Display the Wizard
        wizardStage.showAndWait();
    }
}