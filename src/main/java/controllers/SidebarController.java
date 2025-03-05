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

public class SidebarController {

    @FXML private Button btnHome;
    @FXML private Button btnListRH;
    @FXML private Button btnListEmploye;
    @FXML private Button btnListCandidates;
    @FXML private Button btnChatAssistant;
    @FXML private Button btnLogout;
    @FXML private Button profileButton;
    @FXML private ImageView profileImage;
    @FXML private Button btnEquipes;
    @FXML private Button btnProjets;
    @FXML private Button gererevent;

    @FXML
    private void initialize() {
        URL imageUrl = getClass().getResource("/images/profile_icon.png");
        if (imageUrl != null) {
            profileImage.setImage(new Image(imageUrl.toExternalForm()));
        } else {
            System.err.println("⚠️ Profile image not found: /images/profile_icon.png");
        }

        btnHome.setOnAction(event -> loadView("/admin/dashboardadmin.fxml"));
        btnListRH.setOnAction(event -> loadView("/admin/listrh.fxml"));
        btnListEmploye.setOnAction(event -> loadView("/admin/listemploye.fxml"));
        btnListCandidates.setOnAction(event -> loadView("/admin/listcandidate.fxml"));
        btnLogout.setOnAction(event -> logout());
        btnChatAssistant.setOnAction(event -> loadView("/admin/chatassistant.fxml"));
        profileButton.setOnAction(event -> openProfileView());
        btnEquipes.setOnAction(event -> loadView("/equipe/afficherEquipe.fxml"));
        btnProjets.setOnAction(event -> loadView("/projet/afficherProjets.fxml"));
        gererevent.setOnAction(event -> loadView("/admin/sahar/AfficherEvennement.fxml"));


    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            if (loader.getLocation() == null) {
                throw new IOException("FXML file not found: " + fxmlPath);
            }
            Parent root = loader.load();
            Stage stage = (Stage) btnHome.getScene().getWindow();
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
            stage.setTitle("Admin Profile");
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
