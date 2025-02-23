package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import models.Evennement;
import services.ServiceEvennement;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

public class AfficherEvennementController {

    @FXML
    private ListView<String> list_event;
    @FXML
    private TextField id_event_delete;
    private final ServiceEvennement serviceEvennement = new ServiceEvennement(); // Instance du service
    private final Map<String, Integer> eventMap = new HashMap<>(); // Associer affichage ↔ ID réel
    private String selectedEvent; // Member variable to hold the selected event

    @FXML
    public void initialize() {
        refreshList(); // Charger la liste dès l’affichage de la page

        // Listener pour détecter un clic sur un événement
        list_event.setOnMouseClicked(event -> {
            selectedEvent = list_event.getSelectionModel().getSelectedItem(); // Update the selected event
            if (selectedEvent != null) {
                // Affiche l'événement sélectionné dans la console
                System.out.println("Événement sélectionné : " + selectedEvent);
            }
        });
    }

    @FXML
    void delete_evennement(ActionEvent event) {
        String selectedEvent = list_event.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        Integer idEvent = eventMap.get(selectedEvent);
        if (idEvent != null) {
            try {
                serviceEvennement.supprimer(idEvent);
                refreshList();
                System.out.println("Événement supprimé avec succès !");
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la suppression de l'événement : " + e.getMessage());
            }
        } else {
            System.err.println("Erreur : ID introuvable pour l'événement sélectionné.");
        }
    }

    @FXML
    void go_update_evennement(ActionEvent event) {
        if (selectedEvent == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        Integer idEvent = eventMap.get(selectedEvent);
        if (idEvent != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvennement.fxml"));
                Parent root = loader.load();

                // Passer l'ID à UpdateEvennementController
                UpdateEvennementController controller = loader.getController();
                controller.setIdEvent(idEvent); // Make sure this method exists in UpdateEvennementController

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Erreur : ID introuvable pour l'événement sélectionné.");
        }
    }

    // Method to update the ListView with events
    public void setListeEvenements(List<Evennement> events) {
        list_event.getItems().clear();  // Clear the previous list
        for (Evennement ev : events) {
            list_event.getItems().add(
                    //ev.getId_event() + " | " +
                    "Nom de l'événement : " + ev.getNom_event() + "\n" +
                            "Description : " + ev.getDescription() + "\n" +
                            "Date de l'événement : " + ev.getDate_event() + "\n" +
                            "Lieu de l'événement : " + ev.getLieu_event() + "\n" +
                            "Organisateur : " + ev.getOrganisateur() + "\n" +
                            "Statut : " + ev.getStatut()
            );
        }
    }

    // Rafraîchir la liste des événements
    public void refreshList() {
        try {
            List<Evennement> evennements = serviceEvennement.recuperer();
            list_event.getItems().clear();
            eventMap.clear(); // Réinitialiser la map

            for (Evennement ev : evennements) {
                String eventText =
                        "Nom de l'événement : " + ev.getNom_event() + "\n" +
                        "Description : " + ev.getDescription() + "\n" +
                        "Date de l'événement : " + ev.getDate_event() + "\n" +
                        "Lieu de l'événement : " + ev.getLieu_event() + "\n" +
                        "Organisateur : " + ev.getOrganisateur() + "\n" +
                        "Statut : " + ev.getStatut();

                list_event.getItems().add(eventText);
                eventMap.put(eventText, ev.getId_event()); // Associer affichage ↔ ID réel
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }

    // Méthode pour extraire l'ID de l'événement depuis la chaîne affichée dans la ListView
    private int extractIdFromString(String eventString) {
        try {
            String[] parts = eventString.split("\\|");
            if (parts.length > 0) {
                return Integer.parseInt(parts[0].trim());
            } else {
                System.err.println("Erreur : La chaîne ne contient pas d'ID.");
                return -1;
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Erreur lors de l'extraction de l'ID : " + e.getMessage());
            return -1;
        }
    }

    public void voirListP(ActionEvent actionEvent) {
        if (selectedEvent == null) {
            System.err.println("Erreur : Aucun événement sélectionné.");
            return;
        }

        Integer idEvent = eventMap.get(selectedEvent);
        if (idEvent != null) {
            try {
                // Chargez le FXML pour le contrôleur des participants
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherParticipant.fxml"));
                Parent root = loader.load();

                // Passer l'ID à AfficherParticipantController
                AfficherParticipantController controller = loader.getController();
                controller.refreshListByEventId(idEvent); // Méthode pour charger les participants par ID

                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Erreur : ID introuvable pour l'événement sélectionné.");
        }
    }

}
