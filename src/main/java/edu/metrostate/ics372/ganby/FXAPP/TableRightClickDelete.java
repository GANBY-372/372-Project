package edu.metrostate.ics372.ganby.FXAPP;

import javafx.collections.ObservableList;
import javafx.scene.control.*;

public class TableRightClickDelete {

    /**
     * Enable right-click delete with a confirmation wizard and deletion conditions.
     *
     * @param tableView  The TableView instance to enable the right-click menu for
     * @param data       The ObservableList backing the TableView
     * @param validator  A functional interface for validation (e.g., checking if dealer has vehicles)
     */
    public static <T> void enableRightClickDelete(
            TableView<T> tableView,
            ObservableList<T> data,
            Validator<T> validator
    ) {
        // Create a ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        // Create the "Delete" menu item
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(event -> {
            T selectedItem = tableView.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                // Run the Delete Wizard
                showDeleteWizard(selectedItem, data, validator);
            }
        });

        // Add the menu item to the context menu
        contextMenu.getItems().add(deleteItem);

        // Set the context menu for the table rows
        tableView.setRowFactory(tv -> {
            TableRow<T> row = new TableRow<>();
            row.setOnContextMenuRequested(event -> {
                if (!row.isEmpty()) {
                    contextMenu.show(row, event.getScreenX(), event.getScreenY());
                }
            });
            return row;
        });
    }

    /**
     * Shows a delete wizard to confirm and validate deletion.
     *
     * @param selectedItem The item to delete
     * @param data         The ObservableList backing the TableView
     * @param validator    A validation mechanism to ensure the item can be deleted
     */
    private static <T> void showDeleteWizard(T selectedItem, ObservableList<T> data, Validator<T> validator) {
        // Create a confirmation alert
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Delete Confirmation");
        confirmationAlert.setContentText("Are you sure you want to delete the selected item?");

        // Wait for user confirmation
        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Validate before deletion
                if (validator.isValid(selectedItem)) {
                    data.remove(selectedItem); // Remove the item if validation passes
                } else {
                    // Show an error dialog if validation fails
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Cannot Delete");
                    errorAlert.setHeaderText("Deletion Blocked");
                    errorAlert.setContentText("This item cannot be deleted because it is associated with other records (such as vehicles).");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    /**
     * A simple functional interface for validating whether an item can be deleted.
     */
    @FunctionalInterface
    public interface Validator<T> {
        boolean isValid(T item);
    }
}