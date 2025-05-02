package edu.metrostate.ics372.ganby.FXAPP;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * Utility class for displaying alerts across the application.
 * Provides consistent formatting and usage for error and success messages.
 */
public class AlertHelper {

    public static void showSuccess(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }

    public static void showError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }

    public static void showWarning(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message);
    }

    /**
     * Shows a confirmation dialog with custom button options.
     *
     * @param title   the dialog title
     * @param header  the header text
     * @param content the message content
     * @param buttons the buttons to display
     * @return the button the user clicked
     */
    public static Optional<ButtonType> showConfirmation(String title, String header, String content, ButtonType... buttons) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttons);
        return alert.showAndWait();
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        // Ensure we are on JavaFX thread
        if (Platform.isFxApplicationThread()) {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText((message == null || message.trim().isEmpty())
                    ? "Operation completed successfully."
                    : message);
            alert.showAndWait();  // ❗ called directly
        } else {
            Platform.runLater(() -> {
                Alert alert = new Alert(type);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText((message == null || message.trim().isEmpty())
                        ? "Operation completed successfully."
                        : message);
                alert.showAndWait();  // still inside runLater, but only fallback
            });
        }
    }
}
