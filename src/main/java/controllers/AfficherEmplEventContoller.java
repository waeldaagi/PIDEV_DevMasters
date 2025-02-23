package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Evennement;
import services.ServiceEvennement;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class AfficherEmplEventContoller {

    @FXML
    private ListView<String> events_empl;
    @FXML
    private TextField searchField;

    private final ServiceEvennement serviceEvennement = new ServiceEvennement(); // Instance du service
    private final Map<String, Integer> eventMap = new HashMap<>(); // Associer affichage ↔ ID réel


    @FXML
    public void initialize() {
        refreshList(); // Charger la liste dès l’affichage de la page

        // Listener pour détecter un clic sur un événement
        events_empl.setOnMouseClicked(event -> {
            String selectedEvent = events_empl.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                System.out.println("Événement sélectionné : " + selectedEvent);

                Integer idEvent = eventMap.get(selectedEvent);
                if (idEvent != null) {
                    ouvrirAjoutParticipation(idEvent);
                } else {
                    System.err.println("Erreur : ID introuvable pour l'événement sélectionné.");
                }
            }
        });
    }

    // Mettre à jour la ListView avec les événements sans afficher l'ID
    public void refreshList() {
        try {
            List<Evennement> evennements = serviceEvennement.recuperer();
            events_empl.getItems().clear();
            eventMap.clear(); // Réinitialiser la map

            for (Evennement ev : evennements) {
                String eventText = "Nom de l'événement : " + ev.getNom_event() + "\n" +
                        "Description : " + ev.getDescription() + "\n" +
                        "Date de l'événement : " + ev.getDate_event() + "\n" +
                        "Lieu de l'événement : " + ev.getLieu_event() + "\n" +
                        "Organisateur : " + ev.getOrganisateur() + "\n" +
                        "Statut : " + ev.getStatut();

                events_empl.getItems().add(eventText);
                eventMap.put(eventText, ev.getId_event()); // Associer affichage ↔ ID réel
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }

    // Méthode pour ouvrir la fenêtre d'ajout de participation
    private void ouvrirAjoutParticipation(int idEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjoutParticipation.fxml"));
            Parent root = loader.load();

            // Passer l'ID à AjoutParticipationController
            AjoutParticipationController controller = loader.getController();
            controller.setIdEvent(idEvent);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chercher_event(ActionEvent actionEvent) {
        String query = searchField.getText().toLowerCase(); // Récupérer le texte et le convertir en minuscules
        events_empl.getItems().clear(); // Effacer la liste actuelle

        try {
            List<Evennement> evennements = serviceEvennement.recuperer(); // Récupérer tous les événements

            for (Evennement ev : evennements) {
                // Vérifier si le nom de l'événement ou d'autres détails correspondent à la requête
                if (ev.getNom_event().toLowerCase().contains(query) ||
                        ev.getDescription().toLowerCase().contains(query) ||
                        ev.getLieu_event().toLowerCase().contains(query)) {

                    String eventText = "Nom de l'événement : " + ev.getNom_event() + "\n" +
                            "Description : " + ev.getDescription() + "\n" +
                            "Date de l'événement : " + ev.getDate_event() + "\n" +
                            "Lieu de l'événement : " + ev.getLieu_event() + "\n" +
                            "Organisateur : " + ev.getOrganisateur() + "\n" +
                            "Statut : " + ev.getStatut();

                    events_empl.getItems().add(eventText);
                    eventMap.put(eventText, ev.getId_event()); // Associer affichage ↔ ID réel
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }

    }
}
