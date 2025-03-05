package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ModifyPasswordController {

    private static final Logger logger = Logger.getLogger(ModifyPasswordController.class.getName());

    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button updateButton;
    @FXML
    private Label statusLabel;

    private String userEmail;
    private Stage stage;

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleUpdatePassword() {
        String newPassword = newPasswordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            statusLabel.setText("Both fields are required.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            statusLabel.setText("Passwords do not match.");
            return;
        }

        if (!isValidPassword(newPassword)) {
            statusLabel.setText("Password must be at least 8 characters long, contain an uppercase letter, and a number.");
            return;
        }

        UserService userService = new UserService();
        User user = userService.getUserByEmail(userEmail);
        if (user != null) {
            user.setPassword(newPassword);
            boolean updated = userService.updateUserPassword(user);
            if (updated) {
                statusLabel.getStyleClass().add("success");
                statusLabel.setText("Password updated successfully!");
                logger.info("Password updated successfully for user: " + userEmail);
                showSuccessAlertAndClose();
            } else {
                statusLabel.setText("Failed to update password. Please try again.");
                logger.severe("Failed to update password for user: " + userEmail);
            }
        } else {
            statusLabel.setText("User not found. Please try again.");
            logger.severe("User not found for email: " + userEmail);
        }
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        if (!Pattern.compile("[A-Z]").matcher(password).find()) return false;
        if (!Pattern.compile("[0-9]").matcher(password).find()) return false;
        return true;
    }

    private void showSuccessAlertAndClose() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Your password has been updated successfully!");
        alert.showAndWait();

        if (stage != null) {
            stage.close();
        }
    }
}