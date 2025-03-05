package controllers.yas;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.OffreRecrutement;
import org.controlsfx.control.Notifications;
import services.yas.GoogleCalendarService;
import services.yas.ServiceOffreRecrutement;


import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;





public class AjoutOffreRecrutementController {

    @FXML
    private DatePicker date_limite;

    @FXML
    private DatePicker date_pub;



    @FXML
    private ChoiceBox<String> poste; // Updated from TextField to ChoiceBox

    @FXML
    private TextField salaire;

    @FXML
    private AnchorPane AnchorPane;

    private final ServiceOffreRecrutement serviceOffre = new ServiceOffreRecrutement();

    // Initialize method to set the current date for date_pub
    public void initialize() {
        URL imageUrl = getClass().getResource("/images/backgound.jpg");
        if (imageUrl != null) {
            Image backgroundImage = new Image(imageUrl.toExternalForm());
            BackgroundImage bgImage = new BackgroundImage(
                    backgroundImage,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, true)
            );
            AnchorPane.setBackground(new Background(bgImage));
        } else {
            System.err.println("Erreur : Image de fond introuvable !");
        }
        // Set the current system date to the date_pub DatePicker
        date_pub.setValue(java.time.LocalDate.now());
        // Populate the ChoiceBox with example job positions

        poste.getItems().addAll(Arrays.asList("Développeur", "Analyste", "Designer", "Chef de projet"));

    }

    @FXML
    void ajouter_offre(ActionEvent event) {
        // Ensure all fields are filled
        if (date_pub.getValue() == null || date_limite.getValue() == null || poste.getValue() == null || salaire.getText().trim().isEmpty()) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // Validate salaire value
        int salaireValue;
        try {
            salaireValue = Integer.parseInt(salaire.getText().trim());
            if (salaireValue <= 0) {
                showAlert("Erreur", "Le salaire doit être un nombre positif !");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Veuillez saisir un salaire valide !");
            return;
        }

        // Create the OffreRecrutement object
        OffreRecrutement offre = new OffreRecrutement(
                Date.valueOf(date_pub.getValue()),
                Date.valueOf(date_limite.getValue()),
                salaireValue,
                poste.getValue().trim() // ChoiceBox value
        );

        try {
            // Add the offer to the database
            serviceOffre.ajouter(offre);

            // **🔔 Show a Toast Notification**
            Notifications.create()
                    .title("Offre ajoutée")
                    .text("Votre offre de recrutement a été ajoutée avec succès.")
                    .position(Pos.BOTTOM_RIGHT)
                    .showConfirm();

            // Integrate with Google Calendar API
            try {
                GoogleCalendarService.addEventToCalendar(offre); // Add the event to Google Calendar
            } catch (Exception e) {
                showAlert("Erreur", "Une erreur est survenue lors de l'ajout de l'événement à Google Calendar : " + e.getMessage());
            }

            // Redirect to the offer list
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rh/AfficherOffreRecrutement.fxml"));
            Parent root = loader.load();
            AfficherOffreRecrutementController ac = loader.getController();
            ac.setListeOffreRecrutement(serviceOffre.recuperer());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Offres");
            stage.show();

        } catch (SQLException e) {
            showAlert("Erreur SQL", "Une erreur SQL est survenue : " + e.getMessage());
        } catch (IOException e) {
            showAlert("Erreur", "Une erreur est survenue lors du chargement de la page : " + e.getMessage());
        }
          // Commit the transaction after insertion

    }

    private void showAlert(String title, String message) {
        Alert alert;
        if (title.equals("Erreur")) {
            alert = new Alert(AlertType.ERROR);
        } else {
            alert = new Alert(AlertType.INFORMATION);
        }

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void baack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/rh/AfficherOffreRecrutement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
