package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import models.Participation;
import services.QRCodeGenerator;
import services.ServiceParticipation;

import java.util.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class AjoutParticipationController {

    @FXML
    private TextField contact;

    @FXML
    private ChoiceBox<String> depart_participant;

    @FXML
    private ChoiceBox<String> experience_event;

    @FXML
    private TextField remarque;

    @FXML
    private ChoiceBox<String> role_p;
    private int userId;

    private int idEvent; // Stocker l'ID de l'événement sélectionné

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
        System.out.println("ID de l'événement reçu : " + idEvent);
    }

    @FXML
    public void initialize() {
        // Initialiser les choix des ChoiceBox
        depart_participant.getItems().addAll("IT", "Finance", "Marketing", "RH"); // Ajoutez d'autres départements
        experience_event.getItems().addAll("Oui", "Non");
        role_p.getItems().addAll("Jury", "Participant");
    }

    @FXML
    void participer_event(ActionEvent event) {
        if (role_p.getValue() == null || depart_participant.getValue() == null || contact.getText().trim().isEmpty() || experience_event.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        String role = role_p.getValue();
        String departement = depart_participant.getValue();
        String numTel = contact.getText().trim();
        String experience = experience_event.getValue();
        String remarqueText = remarque.getText().trim();

        if (!numTel.matches("\\d{8}")) {
            showAlert("Erreur de saisie", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return;
        }

        int idUser = 1; // Remplace par la vraie ID utilisateur
        LocalDate dateParticipation = LocalDate.now();
        Date dateParticipationUtil = Date.from(dateParticipation.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Participation participation = new Participation(0, idEvent, idUser, dateParticipationUtil, role, departement, numTel, experience, remarqueText);

        ServiceParticipation service = new ServiceParticipation();
        try {
            service.ajouter(participation);

            // Fetch User & Event Details
            String username = service.getUserNameById(idUser);
            String eventDetails = service.getEventDetailsById(idEvent);

            // Prepare QR Code Data
            // Include dynamic event URL with event ID as a parameter
            String qrData = "https://sahar-khiari.github.io/PIDEV_DevMasters/event_participation.html?event_id=" + idEvent + "&username=" + username + "&role=" + role;

            String filePath = "C:/Users/PC/Desktop/QRCode_" + username + ".png";

            // Generate QR Code
            QRCodeGenerator.generateQRCode(qrData, filePath);

            showAlert("Succès", "Participation ajoutée avec succès ! QR Code généré !");
            System.out.println("Participation ajoutée avec succès !");
            fermerFenetre(event);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de la participation : " + e.getMessage());
            showAlert("Erreur", "Une erreur est survenue lors de l'ajout de la participation.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void fermerFenetre(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    public void setUserId(int idUser) {
        this.userId = idUser; // Assure-toi que 'userId' est bien défini comme un attribut dans la classe
    }

}
