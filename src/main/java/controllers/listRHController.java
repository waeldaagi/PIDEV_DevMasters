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

public class listRHController {

    @FXML private ListView<String> listView;
    @FXML private Button btnModifyRH;
    @FXML private Button btnDeleteRH;
    @FXML private Button btnDemoteRH;
    @FXML private Button btnBanRH;

    private final AdminService adminService = new AdminService();
    private List<User> rhList;

    @FXML
    private void initialize() {
        loadRHUsers();
        btnModifyRH.setOnAction(event -> openModifyRHForm());
        btnDeleteRH.setOnAction(event -> deleteSelectedRH());
        btnDemoteRH.setOnAction(event -> demoteSelectedRH());
        btnBanRH.setOnAction(event -> toggleBanStatus());
    }

    public void loadRHUsers() {
        rhList = adminService.getAllRH();
        listView.getItems().clear();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        for (User rh : rhList) {
            String birthDate = (rh.getBirthDate() != null) ? dateFormat.format(rh.getBirthDate()) : "N/A";
            String activeStatus = rh.isActive() ? "✅ Active" : "❌ Inactive";

            String rhDetails = String.format(
                    "👤 %s (%s)\n📧 %s | 📞 %s\n🏠 %s | 🎂 %s\n💰 Salary: %.2f | %s",
                    rh.getUsername(), rh.getEmail(),
                    rh.getEmail(), rh.getPhoneNumber(),
                    rh.getAddress(), birthDate,
                    rh.getSalary(), activeStatus
            );
            listView.getItems().add(rhDetails);
        }

        listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() >= 0) {
                User selectedUser = rhList.get(newVal.intValue());
                btnBanRH.setText(selectedUser.isActive() ? "🚫 Ban" : "✅ Unban");
            }
        });
    }


    private void openModifyRHForm() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = rhList.get(selectedIndex);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/modifyRH.fxml"));
                Parent root = loader.load();

                ModifyRHController modifyRHController = loader.getController();
                modifyRHController.setEmployee(selectedUser);
                modifyRHController.setParentController(this);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modify RH");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select an RH to modify.");
        }
    }

    private void deleteSelectedRH() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = rhList.get(selectedIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete RH");
            alert.setContentText("Are you sure you want to delete " + selectedUser.getUsername() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.deleteEmployee(selectedUser.getId()); // Use deleteEmployee for RHs too
                loadRHUsers();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select an RH to delete.");
        }
    }

    private void demoteSelectedRH() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = rhList.get(selectedIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Demotion");
            alert.setHeaderText("Demote RH");
            alert.setContentText("Are you sure you want to demote " + selectedUser.getUsername() + " to Employee?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.demoteRH(selectedUser.getId(), selectedUser.getSalary());
                loadRHUsers();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select an RH to demote.");
        }
    }
    private void toggleBanStatus() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = rhList.get(selectedIndex);
            boolean isCurrentlyActive = selectedUser.isActive();

            String action = isCurrentlyActive ? "ban" : "unban";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Action");
            alert.setHeaderText("Change User Status");
            alert.setContentText("Are you sure you want to " + action + " " + selectedUser.getUsername() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.setUserActiveStatus(selectedUser.getId(), !isCurrentlyActive);

                selectedUser.setActive(!isCurrentlyActive);

                listView.getItems().set(selectedIndex, formatRHDetails(selectedUser));

                btnBanRH.setText(selectedUser.isActive() ? "🚫 Ban" : "✅ Unban");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select an RH to ban/unban.");
        }
    }
    private String formatRHDetails(User rh) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthDate = (rh.getBirthDate() != null) ? dateFormat.format(rh.getBirthDate()) : "N/A";
        String activeStatus = rh.isActive() ? "✅ Active" : "❌ Inactive";

        return String.format(
                "👤 %s (%s)\n📧 %s | 📞 %s\n🏠 %s | 🎂 %s\n💰 Salary: %.2f | %s",
                rh.getUsername(), rh.getEmail(),
                rh.getEmail(), rh.getPhoneNumber(),
                rh.getAddress(), birthDate,
                rh.getSalary(), activeStatus
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
