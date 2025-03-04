package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import utils.JWTUtils;
import utils.SessionManager;

import java.io.IOException;
import java.util.Date;
import java.util.regex.Pattern;

public class ProfileAdminController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private DatePicker birthDateField;
    @FXML private Button updateButton;

    private User currentUser;
    private final UserService userService = new UserService();

    @FXML
    private void initialize() {
        String accessToken = SessionManager.getAccessToken();
        if (accessToken != null && JWTUtils.isTokenValid(accessToken)) {
            currentUser = JWTUtils.validateAccessToken(accessToken);

            if (currentUser != null) {
                usernameField.setText(currentUser.getUsername());
                emailField.setText(currentUser.getEmail());
                phoneField.setText(currentUser.getPhoneNumber());
                addressField.setText(currentUser.getAddress());
                birthDateField.setValue(currentUser.getBirthDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            }
        }
    }

    @FXML
    private void handleUpdate() {
        String newUsername = usernameField.getText().trim();
        String newEmail = emailField.getText().trim();
        String newPhone = phoneField.getText().trim();
        String newAddress = addressField.getText().trim();
        Date newBirthDate = java.sql.Date.valueOf(birthDateField.getValue());

        // Validate input
        if (!isValidUsername(newUsername) || !isValidEmail(newEmail) || !isValidPhoneNumber(newPhone) || !isValidBirthDate(newBirthDate)) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please check your input.");
            return;
        }

        if (userService.doesUsernameExist(newUsername, currentUser.getId())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Username already exists.");
            return;
        }

        if (userService.doesEmailExist(newEmail, currentUser.getId())) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Email already exists.");
            return;
        }


        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setPhoneNumber(newPhone);
        currentUser.setAddress(newAddress);
        currentUser.setBirthDate(newBirthDate);

        boolean isUpdated = userService.updateUserProfile(currentUser);
        if (!isUpdated) {
            showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update profile in the database.");
            return;
        }

        String updatedAccessToken = JWTUtils.generateAccessToken(currentUser);
        SessionManager.refreshAccessToken(updatedAccessToken);

        showAlert(Alert.AlertType.INFORMATION, "Profile Updated", "Your profile has been updated successfully.");
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) updateButton.getScene().getWindow(); // Get the current stage
        stage.close();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidUsername(String username) {
        return username.length() > 3;
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.length() == 8 && phone.matches("\\d+");
    }

    private boolean isValidBirthDate(Date birthDate) {
        return birthDate.before(new Date());
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
