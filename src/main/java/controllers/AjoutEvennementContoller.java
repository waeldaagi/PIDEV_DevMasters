package controllers;

import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;

import models.Evennement;
import services.ServiceEvennement;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

public class AjoutEvennementContoller {

    @FXML
    private DatePicker date_event;

    @FXML
    private TextField description;

    @FXML
    private TextField lieu_event;

    @FXML
    private TextField nom_event;

    @FXML
    private TextField organisateur;

    @FXML
    private TextField statut;

    @FXML
    void ajouter_evennement(ActionEvent event) {
        String nom = nom_event.getText().trim();
        String desc = description.getText().trim();
        String lieu = lieu_event.getText().trim();
        String org = organisateur.getText().trim();
        String stat = statut.getText().trim();
        LocalDate date = date_event.getValue(); // Récupération de la date sélectionnée

        // 1. Contrôle de saisie
        if (nom.isEmpty() || desc.isEmpty() || date == null ||
                lieu.isEmpty() || org.isEmpty() || stat.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        if (nom.length() < 3 || desc.length() < 3) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le nom et la description doivent contenir au moins 3 caractères !");
            return;
        }

        if (date.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "La date de l'événement ne peut pas être dans le passé !");
            return;
        }

        if (!stat.matches("(?i)Confirme|Annule|En attente")) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Le statut doit être 'Confirmé', 'Annulé' ou 'En attente' !");
            return;
        }

        // Convert DatePicker value (LocalDate) to java.sql.Date
        java.sql.Date sqlDate = Date.valueOf(date);

        // 2. Création et ajout de l'événement
        ServiceEvennement serviceEvennement = new ServiceEvennement();
        Evennement ev = new Evennement(nom, desc, sqlDate, lieu, org, stat);

        try {
            serviceEvennement.ajouter(ev);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement ajouté avec succès !");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Problème lors de l'ajout : " + e.getMessage());
            return;
        }

        // 3. Changement de scène pour afficher la liste des événements
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEvennement.fxml"));
            Parent root = loader.load();

            AfficherEvennementController ac = loader.getController();
            ac.setListeEvenements(serviceEvennement.recuperer()); // Mise à jour de la liste

            Stage stage = (Stage) nom_event.getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur chargement FXML: " + e.getMessage());
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur récupération événements: " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
