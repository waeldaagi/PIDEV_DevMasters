package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.User;
import services.AdminService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

public class listEmployeController {

    @FXML private ListView<String> listView;
    @FXML private Button btnAddEmploye;
    @FXML private Button btnModifyEmploye;
    @FXML private Button btnDeleteEmploye;
    @FXML private Button btnPromoteEmploye;
    @FXML private Button btnDemoteEmploye;
    @FXML private Button btnBanEmployee;

    private final AdminService adminService = new AdminService();
    private List<User> employees;

    @FXML
    private void initialize() {
        loadEmployees();
        btnAddEmploye.setOnAction(event -> openAddEmployeeForm());
        btnModifyEmploye.setOnAction(event -> openModifyEmployeeForm());
        btnDeleteEmploye.setOnAction(event -> deleteSelectedEmployee());
        btnPromoteEmploye.setOnAction(event -> promoteSelectedEmployee());
        btnDemoteEmploye.setOnAction(event -> demoteSelectedEmployee());
        btnBanEmployee.setOnAction(event -> toggleBanStatus());
    }

    public void loadEmployees() {
        employees = adminService.getAllEmployees();
        listView.getItems().clear();

        for (User employee : employees) {
            listView.getItems().add(formatEmployeeDetails(employee));
        }

        listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() >= 0) {
                User selectedUser = employees.get(newVal.intValue());
                btnBanEmployee.setText(selectedUser.isActive() ? "🚫 Ban" : "✅ Unban");
            }
        });
    }

    private void openAddEmployeeForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/addEmploye.fxml"));
            Parent root = loader.load();

            AddEmployeController addEmployeController = loader.getController();
            addEmployeController.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Employee");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openModifyEmployeeForm() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = employees.get(selectedIndex);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/modifyEmploye.fxml"));
                Parent root = loader.load();

                ModifyEmployeController modifyEmployeController = loader.getController();
                modifyEmployeController.setEmployee(selectedUser);
                modifyEmployeController.setParentController(this);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modify Employee");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select an employee to modify.");
        }
    }

    private void deleteSelectedEmployee() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = employees.get(selectedIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Employee");
            alert.setContentText("Are you sure you want to delete " + selectedUser.getUsername() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.deleteEmployee(selectedUser.getId());
                loadEmployees();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select an employee to delete.");
        }
    }

    private void promoteSelectedEmployee() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = employees.get(selectedIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Promotion");
            alert.setHeaderText("Promote Employee");
            alert.setContentText("Are you sure you want to promote " + selectedUser.getUsername() + " to RH?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.promoteEmployee(selectedUser.getId(), selectedUser.getSalary());
                loadEmployees();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select an employee to promote.");
        }
    }

    private void demoteSelectedEmployee() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = employees.get(selectedIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Demotion");
            alert.setHeaderText("Demote Employee");
            alert.setContentText("Are you sure you want to demote " + selectedUser.getUsername() + " to Candidate?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.demoteToCandidate(selectedUser.getId(), selectedUser.getSalary());
                loadEmployees();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select an employee to demote.");
        }
    }

    private void toggleBanStatus() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = employees.get(selectedIndex);
            boolean isCurrentlyActive = selectedUser.isActive();

            String action = isCurrentlyActive ? "ban" : "unban";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Action");
            alert.setHeaderText("Change Employee Status");
            alert.setContentText("Are you sure you want to " + action + " " + selectedUser.getUsername() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.setUserActiveStatus(selectedUser.getId(), !isCurrentlyActive);


                selectedUser.setActive(!isCurrentlyActive);
                listView.getItems().set(selectedIndex, formatEmployeeDetails(selectedUser));


                btnBanEmployee.setText(selectedUser.isActive() ? "🚫 Ban" : "✅ Unban");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select an employee to ban/unban.");
        }
    }

    private String formatEmployeeDetails(User employee) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthDate = (employee.getBirthDate() != null) ? dateFormat.format(employee.getBirthDate()) : "N/A";
        String activeStatus = employee.isActive() ? "✅ Active" : "❌ Inactive";

        return String.format(
                "👤 %s (%s)\n📧 %s | 📞 %s\n🏠 %s | 🎂 %s\n💰 Salary: %.2f | %s",
                employee.getUsername(), employee.getEmail(),
                employee.getEmail(), employee.getPhoneNumber(),
                employee.getAddress(), birthDate,
                employee.getSalary(), activeStatus
        );
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
