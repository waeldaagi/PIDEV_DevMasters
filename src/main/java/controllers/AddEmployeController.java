package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import services.AdminService;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Date;
import java.util.regex.Pattern;

public class AddEmployeController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField salaryField;
    @FXML private DatePicker birthDatePicker;
    @FXML private PasswordField passwordField;
    @FXML private Button btnSave;

    private final AdminService adminService = new AdminService();
    private listEmployeController parentController;

    public void setParentController(listEmployeController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void saveEmployee() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressField.getText().trim();
        String salaryStr = salaryField.getText().trim();
        Date birthDate = birthDatePicker.getValue() != null ? Date.valueOf(birthDatePicker.getValue()) : null;
        String password = passwordField.getText().trim();


        if (!validateInputs(username, email, phone, address, salaryStr, birthDate, password)) {
            return;
        }

        double salary = Double.parseDouble(salaryStr);

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User(0, username, email, models.UserRole.EMPLOYE, false, phone, address, birthDate, salary, hashedPassword);

        if (adminService.addEmployee(newUser)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Employee added successfully!");

            if (parentController != null) {
                parentController.loadEmployees();
            }

            closeWindow();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Email or Username already exists.");
        }
    }

    private boolean validateInputs(String username, String email, String phone, String address, String salaryStr, Date birthDate, String password) {
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || salaryStr.isEmpty() || birthDate == null || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required!");
            return false;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format!");
            return false;
        }

        if (!isValidPhoneNumber(phone)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Phone number must be 8 digits!");
            return false;
        }

        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Password must be at least 6 characters, contain 1 uppercase letter and 1 number.");
            return false;
        }

        try {
            Double.parseDouble(salaryStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Salary must be a valid number!");
            return false;
        }

        return true;
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{8}");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d).{6,}$");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void closeWindow() {
        Stage stage = (Stage) btnSave.getScene().getWindow();
        stage.close();
    }
}
