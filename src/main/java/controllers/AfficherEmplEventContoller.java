package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Evennement;
import services.ServiceEvennement;
import services.ServiceParticipation;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;




public class AfficherEmplEventContoller {

    @FXML
    private ListView<HBox> events_empl; // Changer le type de ListView pour accepter des HBox
    @FXML
    private TextField searchField;

    private final ServiceParticipation serviceParticipant = new ServiceParticipation();
    private final ServiceEvennement serviceEvennement = new ServiceEvennement(); // Instance du service
    private final Map<HBox, Integer> eventMap = new HashMap<>(); // Associer affichage ↔ ID réel

    @FXML
    public void initialize() {
        refreshList(); // Charger la liste dès l’affichage de la page

        // Listener pour détecter un clic sur un événement
        events_empl.setOnMouseClicked(event -> {
            HBox selectedHBox = events_empl.getSelectionModel().getSelectedItem();
            if (selectedHBox != null) {
                Evennement selectedEvent = (Evennement) selectedHBox.getUserData(); // Récupérer l'événement sélectionné
                Integer idEvent = eventMap.get(selectedHBox);
                if (idEvent != null) {
                    ouvrirAjoutParticipation(idEvent);
                } else {
                    System.err.println("Erreur : ID introuvable pour l'événement sélectionné.");
                }
            }
        });

        // Ajouter un listener sur le champ de recherche pour filtrer les événements
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterEvent(newValue.toLowerCase());
        });
    }

    // Mettre à jour la ListView avec les événements, images et détails
    public void refreshList() {
        try {
            List<Evennement> evennements = serviceEvennement.recuperer();
            events_empl.getItems().clear();
            eventMap.clear(); // Réinitialiser la map

            for (Evennement ev : evennements) {
                // Créer les labels avec les informations de l'événement
                Label nomLabel = new Label("Nom : " + ev.getNom_event());
                Label descLabel = new Label("Description : " + ev.getDescription());
                Label dateLabel = new Label("Date : " + ev.getDate_event().toString());
                Label lieuLabel = new Label("Lieu : " + ev.getLieu_event());
                Label organisateurLabel = new Label("Organisateur : " + ev.getOrganisateur());
                Label statutLabel = new Label("Statut : " + ev.getStatut());

                // Créer un ImageView pour afficher l'image de l'événement
                ImageView imageView = new ImageView();
                if (ev.getImg_event() != null && !ev.getImg_event().isEmpty()) {
                    Image image = new Image("file:" + ev.getImg_event(), 200, 200, true, true); // Charger l'image
                    imageView.setImage(image);
                }

                // Créer un VBox pour organiser les labels verticalement
                VBox eventDetails = new VBox(nomLabel, descLabel, dateLabel, lieuLabel, organisateurLabel, statutLabel);
                eventDetails.setSpacing(5); // Espacement entre les labels

                // Créer une HBox pour contenir l'image et les détails
                HBox eventHBox = new HBox(imageView, eventDetails);
                eventHBox.setSpacing(10);
                eventHBox.setAlignment(Pos.CENTER_LEFT); // Aligner le contenu à gauche
                eventHBox.setUserData(ev); // Stocker l'objet Evennement dans l'HBox

                // Ajouter l'élément à la ListView
                events_empl.getItems().add(eventHBox);
                eventMap.put(eventHBox, ev.getId_event()); // Associer affichage ↔ ID réel
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }

    // Méthode pour ouvrir la fenêtre d'ajout de participation
    private void ouvrirAjoutParticipation(int idEvent) {
        int idUser = 12; // Remplace ça par load réel de l'utilisateur connecté
        try {
            if (serviceParticipant.checkParticipation(idEvent, idUser)) {
                showAlert("Information", "Vous avez déjà participé à cet événement.", Alert.AlertType.INFORMATION);
                return; // Arrête l'exécution
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutParticipation.fxml"));
            Parent root = loader.load();

            // Passer l'ID à AjoutParticipationController
            AjoutParticipationController controller = loader.getController();
            controller.setIdEvent(idEvent);
            controller.setUserId(idUser); // Passer aussi l'ID utilisateur

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour filtrer les événements par recherche
    private void filterEvent(String query) {
        events_empl.getItems().clear(); // Vider la liste avant de filtrer
        eventMap.clear(); // Réinitialiser la map pour éviter des erreurs

        try {
            List<Evennement> evennements = serviceEvennement.recuperer();

            for (Evennement ev : evennements) {
                if (ev.getNom_event().toLowerCase().contains(query) ||
                        ev.getDescription().toLowerCase().contains(query) ||
                        ev.getLieu_event().toLowerCase().contains(query)) {

                    // Créer les labels avec les informations de l'événement
                    Label nomLabel = new Label("Nom : " + ev.getNom_event());
                    Label descLabel = new Label("Description : " + ev.getDescription());
                    Label dateLabel = new Label("Date : " + ev.getDate_event().toString());
                    Label lieuLabel = new Label("Lieu : " + ev.getLieu_event());
                    Label organisateurLabel = new Label("Organisateur : " + ev.getOrganisateur());
                    Label statutLabel = new Label("Statut : " + ev.getStatut());

                    // Créer un ImageView pour afficher l'image de l'événement
                    ImageView imageView = new ImageView();
                    if (ev.getImg_event() != null && !ev.getImg_event().isEmpty()) {
                        Image image = new Image("file:" + ev.getImg_event(), 200, 200, true, true); // Charger l'image
                        imageView.setImage(image);
                    }

                    // Créer un VBox pour organiser les labels verticalement
                    VBox eventDetails = new VBox(nomLabel, descLabel, dateLabel, lieuLabel, organisateurLabel, statutLabel);
                    eventDetails.setSpacing(5); // Espacement entre les labels

                    // Créer une HBox pour contenir l'image et les détails
                    HBox eventHBox = new HBox(imageView, eventDetails);
                    eventHBox.setSpacing(10);
                    eventHBox.setAlignment(Pos.CENTER_LEFT); // Aligner le contenu à gauche
                    eventHBox.setUserData(ev); // Stocker l'objet Evennement dans l'HBox

                    // Ajouter l'élément à la ListView
                    events_empl.getItems().add(eventHBox);
                    eventMap.put(eventHBox, ev.getId_event()); // Associer affichage ↔ ID réel
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }
}

