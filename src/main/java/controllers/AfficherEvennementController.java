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

public class AfficherEvennementController {

    @FXML
    private ListView<String> list_event;
    @FXML
    private TextField id_event_delete;
    private final ServiceEvennement serviceEvennement = new ServiceEvennement(); // Instance du service
    @FXML
    public void initialize() {
        refreshList(); // Charger la liste dès l’affichage de la page

        // Listener pour détecter un clic sur un événement
        list_event.setOnMouseClicked(event -> {
            String selectedEvent = list_event.getSelectionModel().getSelectedItem();
            if (selectedEvent != null) {
                // Affiche l'événement sélectionné dans la console
                System.out.println("Événement sélectionné : " + selectedEvent);

                int idEvent = extractIdFromString(selectedEvent);
                if (idEvent != -1) {
                    ouvrirAjoutParticipation(idEvent);
                }
            }
        });
    }

    @FXML
    void delete_evennement(ActionEvent event) {
        try {
            int id = Integer.parseInt(id_event_delete.getText());
            serviceEvennement.supprimer(id); // Suppression de l’événement
            refreshList(); // Actualiser la liste après suppression
        } catch (NumberFormatException e) {
            System.err.println("Erreur : ID invalide.");
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression de l'événement : " + e.getMessage());
        }
    }

    @FXML
    void go_update_evennement(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateEvennement.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    // Method to update the ListView with events
    public void setListeEvenements(List<Evennement> events) {
        list_event.getItems().clear();  // Clear the previous list
        for (Evennement ev : events) {
            list_event.getItems().add(
                    ev.getId_event() + " | " +
                    ev.getNom_event() + " | " +
                            ev.getDescription() + " | " +
                            ev.getDate_event() + " | " +
                            ev.getLieu_event() + " | " +
                            ev.getOrganisateur() + " | " +
                            ev.getStatut()
            );

        }
    }
    // Rafraîchir la liste des événements
    public void refreshList() {
        try {
            List<Evennement> evennements = serviceEvennement.recuperer(); // Récupérer les événements depuis la BD
            list_event.getItems().clear(); // Vider la liste avant de la remplir à nouveau
            for (Evennement ev : evennements) {
                list_event.getItems().add(
                        ev.getId_event() + " | " +
                        ev.getNom_event() + " | " +
                                ev.getDescription() + " | " +
                                ev.getDate_event() + " | " +
                                ev.getLieu_event() + " | " +
                                ev.getOrganisateur() + " | " +
                                ev.getStatut()
                );
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
}
