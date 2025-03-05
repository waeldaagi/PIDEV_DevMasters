package controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import models.User;
import models.UserRole;
import services.SignUpService;

import java.time.LocalDate;

public class SignUpController {

    @FXML
    private TextField usernameField, emailField, phoneNumberField, addressField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private DatePicker birthDateField;

    @FXML
    private Button signupButton;

    private final SignUpService signUpService = new SignUpService();

    @FXML
    private void initialize() {
    }

    @FXML
    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String address = addressField.getText().trim();
        UserRole role = UserRole.CANDIDATE;
        java.sql.Date birthDate = java.sql.Date.valueOf(birthDateField.getValue());
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || phoneNumber.isEmpty() || address.isEmpty()) {
            showAlert("⚠️ Warning", "All fields must be filled!", Alert.AlertType.WARNING);
            return;
        }
        if (signUpService.isEmailAlreadyUsed(email)) {
            showAlert("⚠️ Error", "Email is already associated with an account!", Alert.AlertType.ERROR);
            return;
        }
        if (!phoneNumber.matches("\\d{8}")) {
            showAlert("⚠️ Error", "Phone number must be exactly 8 digits!", Alert.AlertType.ERROR);
            return;
        }
        if (birthDate.after(java.sql.Date.valueOf(LocalDate.now()))) {
            showAlert("⚠️ Error", "Birth date must be earlier than today's date!", Alert.AlertType.ERROR);
            return;
        }
        User user = new User(0, username, email, role, true, phoneNumber, address, birthDate, 0.0, password);

        if (signUpService.registerUser(user)) {
            showAlert("✅ Success", "Account created successfully!", Alert.AlertType.INFORMATION);
            redirectToSignIn();
        } else {
            showAlert("⚠️ Error", "There was an issue creating your account. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void redirectToSignIn() {
        try {
            Stage stage = (Stage) signupButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Sign In");
            stage.show();
        } catch (Exception e) {
            showAlert("⚠️ Error", "Failed to load the SignIn page.", Alert.AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
