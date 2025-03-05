package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import utils.SessionManager;

import java.io.IOException;

public class CandidateController {

    @FXML
    private Button logoutButton;

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        System.out.println("User logged out and session cleared.");

        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/signin.fxml"));
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Sign In");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
