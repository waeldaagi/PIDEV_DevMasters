package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuPrincipalController {

    @FXML
    public void versEquipe(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/afficherEquipe.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Operation sur Projets");
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    public void versProjet(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ajouterProjet.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Récupérer le Stage existant
        stage.setScene(scene);
        stage.setTitle("Operation sur Equipes");

        stage.setMaximized(true); // Agrandir la fenêtre après avoir défini la scène

        stage.show();
    }
}