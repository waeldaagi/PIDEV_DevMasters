package controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.*;
import models.User;
import services.AdminService;

import java.sql.Date;

public class ModifyRHController {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextField addressField;
    @FXML private TextField salaryField;
    @FXML private DatePicker birthDatePicker;
    @FXML private Button btnSaveChanges;

    private final AdminService adminService = new AdminService();
    private User currentUser;
    private listRHController parentController;

    public void setEmployee(User user) {
        this.currentUser = user;
        usernameField.setText(user.getUsername());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNumber());
        addressField.setText(user.getAddress());
        salaryField.setText(String.valueOf(user.getSalary()));

        if (user.getBirthDate() != null) {
            birthDatePicker.setValue(new java.sql.Date(user.getBirthDate().getTime()).toLocalDate());
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

        adminService.updateEmployee(currentUser);
        parentController.loadRHUsers();

        Stage stage = (Stage) btnSaveChanges.getScene().getWindow();
        stage.close();
    }

    public void setParentController(listRHController parentController) {
        this.parentController = parentController;
    }
}
