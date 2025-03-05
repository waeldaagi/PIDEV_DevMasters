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

public class ListCandidateController {

    @FXML private ListView<String> listView;
    @FXML private Button btnAddCandidate;
    @FXML private Button btnModifyCandidate;
    @FXML private Button btnDeleteCandidate;
    @FXML private Button btnPromoteCandidate;
    @FXML private Button btnBanCandidate;

    private final AdminService adminService = new AdminService();
    private List<User> candidates;

    @FXML
    private void initialize() {
        loadCandidates();
        btnAddCandidate.setOnAction(event -> openAddCandidateForm());
        btnModifyCandidate.setOnAction(event -> openModifyCandidateForm());
        btnDeleteCandidate.setOnAction(event -> deleteSelectedCandidate());
        btnPromoteCandidate.setOnAction(event -> promoteSelectedCandidate());
        btnBanCandidate.setOnAction(event -> toggleBanStatus());
    }

    public void loadCandidates() {
        candidates = adminService.getAllCandidates();
        listView.getItems().clear();

        for (User candidate : candidates) {
            listView.getItems().add(formatCandidateDetails(candidate));
        }

        listView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() >= 0) {
                User selectedUser = candidates.get(newVal.intValue());
                btnBanCandidate.setText(selectedUser.isActive() ? "🚫 Ban" : "✅ Unban");
            }
        });
    }

    private void openAddCandidateForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/addCandidate.fxml"));
            Parent root = loader.load();
            AddCandidateController addCandidateController = loader.getController();
            addCandidateController.setParentController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Candidate");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openModifyCandidateForm() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = candidates.get(selectedIndex);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/modifyCandidate.fxml"));
                Parent root = loader.load();

                ModifyCandidateController modifyCandidateController = loader.getController();
                modifyCandidateController.setCandidate(selectedUser);
                modifyCandidateController.setParentController(this);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modify Candidate");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a candidate to modify.");
        }
    }

    private void deleteSelectedCandidate() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = candidates.get(selectedIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Candidate");
            alert.setContentText("Are you sure you want to delete " + selectedUser.getUsername() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.deleteCandidate(selectedUser.getId());
                loadCandidates();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a candidate to delete.");
        }
    }

    private void promoteSelectedCandidate() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = candidates.get(selectedIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Promotion");
            alert.setHeaderText("Promote Candidate");
            alert.setContentText("Are you sure you want to promote " + selectedUser.getUsername() + " to employee?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.promoteCandidateToEmployee(selectedUser.getId());
                loadCandidates();
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a candidate to promote.");
        }
    }

    private void toggleBanStatus() {
        int selectedIndex = listView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            User selectedUser = candidates.get(selectedIndex);
            boolean isCurrentlyActive = selectedUser.isActive();

            String action = isCurrentlyActive ? "ban" : "unban";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Action");
            alert.setHeaderText("Change Candidate Status");
            alert.setContentText("Are you sure you want to " + action + " " + selectedUser.getUsername() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                adminService.setUserActiveStatus(selectedUser.getId(), !isCurrentlyActive);


                selectedUser.setActive(!isCurrentlyActive);
                listView.getItems().set(selectedIndex, formatCandidateDetails(selectedUser));


                btnBanCandidate.setText(selectedUser.isActive() ? "🚫 Ban" : "✅ Unban");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a candidate to ban/unban.");
        }
    }

    private String formatCandidateDetails(User candidate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthDate = (candidate.getBirthDate() != null) ? dateFormat.format(candidate.getBirthDate()) : "N/A";
        String activeStatus = candidate.isActive() ? "✅ Active" : "❌ Inactive";

        return String.format(
                "👤 %s (%s)\n📧 %s | 📞 %s\n🏠 %s | 🎂 %s\n💰 Salary: %.2f | %s",
                candidate.getUsername(), candidate.getEmail(),
                candidate.getEmail(), candidate.getPhoneNumber(),
                candidate.getAddress(), birthDate,
                candidate.getSalary(), activeStatus
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
