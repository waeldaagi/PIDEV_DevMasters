package controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import models.User;
import services.AdminService;

import java.sql.Date;

public class ModifyCandidateController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField salaryField;
    @FXML private DatePicker birthDatePicker;
    @FXML private Button btnSaveChanges;

    private final AdminService adminService = new AdminService();
    private User currentUser;
    private ListCandidateController parentController;

    public void setCandidate(User candidate) {
        this.currentUser = candidate;
        usernameField.setText(candidate.getUsername());
        emailField.setText(candidate.getEmail());
        phoneField.setText(candidate.getPhoneNumber());
        addressField.setText(candidate.getAddress());
        salaryField.setText(String.valueOf(candidate.getSalary()));

        if (candidate.getBirthDate() != null) {
            birthDatePicker.setValue(new java.sql.Date(candidate.getBirthDate().getTime()).toLocalDate());
        }
    }

    @FXML
    private void saveChanges() {
        currentUser.setUsername(usernameField.getText());
        currentUser.setEmail(emailField.getText());
        currentUser.setPhoneNumber(phoneField.getText());
        currentUser.setAddress(addressField.getText());
        currentUser.setSalary(Double.parseDouble(salaryField.getText()));

        if (birthDatePicker.getValue() != null) {
            currentUser.setBirthDate(Date.valueOf(birthDatePicker.getValue()));
        }

        boolean isUpdated = adminService.updateCandidate(currentUser);
        if (isUpdated) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Candidate details updated successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update candidate details.");
        }

        parentController.loadCandidates();

        Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setParentController(ListCandidateController parentController) {
        this.parentController = parentController;
    }
}
