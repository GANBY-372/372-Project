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


    /**
     * Displays an informational success alert with the given title and message.
     *
     * @param title   the title of the alert window
     * @param message the message to show (or default if null/empty)
     */
    public static void showSuccess(String title, String message) {
        showAlert(Alert.AlertType.INFORMATION, title, message);
    }


    /**
     * Displays an error alert with the given title and message.
     *
     * @param title   the title of the alert window
     * @param message the message to show (or default if null/empty)
     */
    public static void showError(String title, String message) {
        showAlert(Alert.AlertType.ERROR, title, message);
    }


    /**
     * Displays a warning alert with the given title and message.
     *
     * @param title   the title of the alert window
     * @param message the message to show (or default if null/empty)
     */
    public static void showWarning(String title, String message) {
        showAlert(Alert.AlertType.WARNING, title, message);
    }

    /**
     * Displays a confirmation dialog with custom buttons and returns the user's choice.
     *
     * @param title   the dialog title
     * @param header  the header text (shown above the content)
     * @param content the body content text
     * @param buttons the custom buttons to show (e.g., YES, NO, CANCEL)
     * @return an Optional containing the ButtonType the user clicked
     */
    public static Optional<ButtonType> showConfirmation(String title, String header, String content, ButtonType... buttons) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttons);
        return alert.showAndWait();
    }


    /**
     * Displays an alert of the specified type with a title and message.
     * If not on the JavaFX Application Thread, the alert is run on the UI thread.
     *
     * @param type    the AlertType (e.g., INFORMATION, ERROR)
     * @param title   the title of the alert window
     * @param message the message to display (defaults if null or empty)
     */
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
