package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.Evennement;
import services.ServiceEvennement;

import java.sql.SQLException;
import java.sql.Date; // Pour les dates SQL
import java.time.LocalDate;
import java.time.ZoneId;

public class UpdateEvennementController {

    @FXML
    private TextField description_r;

    @FXML
    private TextField nom_e;

    @FXML
    private TextField statut_e2; // Organisateur

    @FXML
    private TextField statut_e3; // Lieu

    @FXML
    private DatePicker datePicker_event;

    private final ServiceEvennement serviceEvennement = new ServiceEvennement();
    private Integer idEvent; // ID de l'événement à modifier

    // ✅ Méthode pour définir l'ID de l'événement et charger les détails
    public void setIdEvent(Integer idEvent) {
        this.idEvent = idEvent;
        loadEventDetails();
    }

    // ✅ Charger les détails de l'événement pour modification
    private void loadEventDetails() {
        if (idEvent != null) {
            try {
                Evennement ev = serviceEvennement.getById(idEvent);
                if (ev != null) {
                    nom_e.setText(ev.getNom_event());
                    nom_e.setEditable(false); // Désactiver l'édition du nom
                    description_r.setText(ev.getDescription());
                    statut_e2.setText(ev.getOrganisateur());
                    statut_e3.setText(ev.getLieu_event());

                    // Ne pas charger la date dans le DatePicker
                    // datePicker_event.setValue(null); // Optionnel : si vous voulez qu'il soit vide

                } else {
                    showAlert(Alert.AlertType.ERROR, "Erreur", "Événement non trouvé !");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Erreur lors de la récupération des détails : " + e.getMessage());
            }
        }
    }



    // ✅ Action pour mettre à jour l'événement
    @FXML
    public void update_evennement(ActionEvent event) {
        try {
            String description = description_r.getText().trim();
            String organisateur = statut_e2.getText().trim();
            String lieu = statut_e3.getText().trim();
            LocalDate date = datePicker_event.getValue();

            // ✅ Vérification des champs
            if (description.isEmpty() || organisateur.isEmpty() || lieu.isEmpty() || date == null) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
                return;
            }

            // ✅ Vérification des longueurs
            if (description.length() < 3 || organisateur.length() < 3 || lieu.length() < 3) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les champs doivent contenir au moins 3 caractères !");
                return;
            }

            // ✅ Mise à jour dans la base de données
            serviceEvennement.modifierEvent(idEvent, description, Date.valueOf(date), lieu, organisateur);
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Événement mis à jour avec succès !");

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur SQL", "Problème lors de la mise à jour : " + e.getMessage());
        }
    }

    // ✅ Fonction pour afficher une alerte
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
