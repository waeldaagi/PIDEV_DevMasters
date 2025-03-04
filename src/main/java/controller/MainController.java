package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainController {
    
    @FXML
    private Button btnEquipes;
    
    @FXML
    private Button btnProjets;

    @FXML
    private void handleEquipesButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherEquipe.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des Équipes");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProjetsButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/afficherProjets.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Gestion des Projets");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 