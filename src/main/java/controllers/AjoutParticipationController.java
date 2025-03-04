package controllers;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.io.IOException;
import java.util.Base64;



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

        int idUser = 13; // Remplace par la vraie ID utilisateur
        LocalDate dateParticipation = LocalDate.now();
        Date dateParticipationUtil = Date.from(dateParticipation.atStartOfDay(ZoneId.systemDefault()).toInstant());

        Participation participation = new Participation(0, idEvent, idUser, dateParticipationUtil, role, departement, numTel, experience, remarqueText);

        ServiceParticipation service = new ServiceParticipation();
        try {
            service.ajouter(participation);

            // 🔹 Fetch User Email
            String userEmail = service.getUserEmailById(idUser); // You need to implement this method
            String username = service.getUserNameById(idUser);
            String eventDetails = service.getEventDetailsById(idEvent);

            // 🔹 Prepare QR Code Data
            String qrData = "https://sahar-khiari.github.io/evnets_participer/event_participation.html?event_id=" + idEvent + "&username=" + username + "&role=" + role;
            String filePath = "C:/Users/PC/Documents/GitHub/PIDEV_DevMasters/QRCode_" + username + ".png";

            // 🔹 Generate QR Code
            QRCodeGenerator.generateQRCode(qrData, filePath);

            // 🔹 Send Email with QR Code
            sendEmailWithQRCode(userEmail, username, filePath);
            //QRCodeGeneratorAPI.generateQRCode(qrData, filePath);


            showAlert("Succès", "Participation ajoutée avec succès ! QR Code envoyé par email.");
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

    private void sendEmailWithQRCode(String recipientEmail, String username, String filePath) {
        Email from = new Email("khiarisahar55@gmail.com"); // Remplace avec ton email
        Email to = new Email(recipientEmail);
        Content content = new Content("text/plain", "scann me");
        Mail mail = new Mail(from, "Pass Event", to, content);

        try {
            // Lire le fichier QR code
            byte[] fileData = readFileAsBytes(filePath);
            String encodedFile = Base64.getEncoder().encodeToString(fileData);
            Attachments attachments = new Attachments();
            attachments.setFilename("QRCode.png");
            attachments.setType("image/png");
            attachments.setDisposition("attachment");
            attachments.setContent(encodedFile);
            mail.addAttachments(attachments);

            // Envoi avec SendGrid
            //SendGrid sg = new SendGrid("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"); // 🔹 Replace with your actual API key
            // SendGrid sg = new SendGrid("  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");
            SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));

            // Clé API dans les variables d'environnement
            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println("Email envoyé, statut : " + response.getStatusCode());
        } catch (IOException e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }

private static byte[] readFileAsBytes(String filePath) throws IOException {
    File file = new File(filePath);
    FileInputStream fileInputStream = new FileInputStream(file);
    byte[] fileData = new byte[(int) file.length()];
    fileInputStream.read(fileData);
    fileInputStream.close();
    return fileData;
}

}
