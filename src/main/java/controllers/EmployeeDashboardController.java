package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Parent;
import utils.SessionManager;

public class EmployeeDashboardController {

    @FXML private Button btnLogout;

    @FXML
    private void handleLogout() {
        SessionManager.logout();

        showAlert(Alert.AlertType.INFORMATION, "Logout Successful", "You have been logged out.");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnLogout.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while logging out.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
