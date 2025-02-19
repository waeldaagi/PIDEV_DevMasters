package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Projet;
import service.ProjetService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Afficher_Projet_controller {

    private final ProjetService ps = new ProjetService();

    @FXML
    private VBox projetsContainer; // Renommé pour correspondre aux projets

    public void initialize() throws SQLException {
        // Récupérer les données des projets
        List<Projet> projets = ps.getAll(new Projet());

        for (Projet projet : projets) {
            HBox projetBox = new HBox(10);
            Label idLabel = new Label("ID : " + projet.getId_projet());
            Label nomLabel = new Label("Nom : " + projet.getNom_projet());
            Label dureeLabel = new Label("Durée : " + projet.getDuree());
            Label managerLabel = new Label("Manager : " + projet.getManager());
            Label clientLabel = new Label("Client : " + projet.getNom_client());
            Label equipeIdLabel = new Label("Équipe ID : " + projet.getIdEquipe());

            projetBox.getChildren().addAll(idLabel, nomLabel, dureeLabel, managerLabel, clientLabel, equipeIdLabel);
            projetsContainer.getChildren().add(projetBox);
        }
    }

    @FXML
    public void retourVersAjouter(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/ajouterProjet.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void effacerprojet(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/supprimerProjet.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void modifierprojet(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/modifierProjet.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}