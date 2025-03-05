package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.SessionManager;

import java.io.IOException;
import java.net.URL;

import static utils.SessionManager.logout;

public class SidebarcandidatController {

    @FXML private Button afficherc;
    @FXML private ImageView profileImage;
    @FXML private Button profileButton;

    @FXML
    private Button btnLogout;

    @FXML
    private void initialize() {
        URL imageUrl = getClass().getResource("/images/profile_icon.png");
        if (imageUrl != null) {
            profileImage.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            System.err.println("⚠️ Profile image not found: /images/profile_icon.png");
        }

        afficherc.setOnAction(event -> loadView("/candidate/AfficherCandidat.fxml"));
        btnLogout.setOnAction(event -> logout());
        profileButton.setOnAction(event -> openProfileView());


    }
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }
            Parent root = loader.load();
            Stage stage = (Stage) afficherc.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error loading FXML file: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void openProfileView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin/profileadmin.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found: /admin/profileadmin.fxml");
            }
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(" Profile");
            stage.show();
        } catch (IOException e) {
            System.err.println("❌ Error loading profileadmin.fxml");
            e.printStackTrace();
        }
    }

    private void logout() {
        System.out.println("🚪 User logged out.");
        SessionManager.logout();
        loadView("/signin.fxml");
    }
}
