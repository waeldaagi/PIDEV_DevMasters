package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import models.Participation;
import services.ServiceParticipation;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AjoutParticipationController {

    @FXML
    private TextField contact;

    @FXML
    private TextField depart_participant;

    @FXML
    private TextField experience_event;

    @FXML
    private TextField remarque;

    @FXML
    private TextField role_p;

    private int idEvent; // Stocker l'ID de l'événement sélectionné

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
        System.out.println("ID de l'événement reçu : " + idEvent);
    }

    @FXML
    void participer_event(ActionEvent event) {
        if (role_p == null || depart_participant == null || contact == null || experience_event == null || remarque == null) {
            System.err.println("Erreur : Un des champs est null !");
            showAlert("Erreur", "Un des champs est non initialisé.");
            return;
        }

        String role = role_p.getText().trim();
        String departement = depart_participant.getText().trim();
        String numTel = contact.getText().trim();
        String experience = experience_event.getText().trim();
        String remarqueText = remarque.getText().trim();

        // Vérification des champs vides
        if (role.isEmpty() || departement.isEmpty() || numTel.isEmpty()) {
            showAlert("Erreur de saisie", "Tous les champs doivent être remplis !");
            return;
        }

        // Vérification du numéro de téléphone (exactement 8 chiffres)
        if (!numTel.matches("\\d{8}")) {
            showAlert("Erreur de saisie", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return;
        }

        // ID de l'utilisateur connecté (à adapter)
        int idUser = 1;
        LocalDate dateParticipation = LocalDate.now();
        Date dateParticipationUtil = Date.from(dateParticipation.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Création de l'objet Participation avec la date convertie
        Participation participation = new Participation(0, idEvent, idUser, dateParticipationUtil, role, departement, numTel, experience, remarqueText);

        // Enregistrement dans la BD
        ServiceParticipation service = new ServiceParticipation();
        try {
            service.ajouter(participation);
            showAlert("Succès", "Participation ajoutée avec succès !");
            System.out.println("Participation ajoutée avec succès !");

            // Redirection vers AfficherParticipation.fxml après ajout réussi
           // goToAfficherEvent(event);
            fermerFenetre(event);

        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de la participation : " + e.getMessage());
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de la participation.");
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//    // Méthode pour aller vers AfficherParticipation.fxml après participation
//    private void goToAfficherEvent(ActionEvent event) {
//        try {
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherEmplEvent.fxml"));
//            Parent root = loader.load();
//
//            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            stage.setScene(new Scene(root));
//            stage.show();
//        } catch (IOException e) {
//            System.err.println("Erreur lors du chargement de AfficherParticipation.fxml : " + e.getMessage());
//            showAlert("Erreur", "Impossible d'afficher la liste des participations.");
//        }
//    }
    private void fermerFenetre(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
