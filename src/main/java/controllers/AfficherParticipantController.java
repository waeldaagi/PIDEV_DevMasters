package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import models.Evennement;

import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import models.Participation;
import services.ServiceParticipation;

import java.io.IOException;
import java.sql.SQLException;
public class AfficherParticipantController {

    @FXML
    private TextField id_part_delete;

    @FXML
    private ListView<String> list_participant;

    private final ServiceParticipation serviceParticipation = new ServiceParticipation();
    public void initialize()
    {
        refreshList();
    }
    @FXML
    void delete_participant(ActionEvent event) {
        try {
            int id = Integer.parseInt(id_part_delete.getText());
            serviceParticipation.supprimer(id); // Suppression de l’événement
            refreshList(); // Actualiser la liste après suppression
        } catch (NumberFormatException e) {
            System.err.println("Erreur : ID invalide.");
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors de la suppression de le participation : " + e.getMessage());
        }

    }

    @FXML
    void go_update_paticipant(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UpdateParticipation.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // Method to update the ListView with events
    public void setListeParticipations(List<Participation> participations) {
        list_participant.getItems().clear();  // Clear the previous list
        for (Participation ev : participations) {
            list_participant.getItems().add(
                            ev.getId_participation() + " | " +
                            ev.getId_event() + " | " +
                            ev.getDate_participation() + " | " +
                            ev.getRole_participant() + " | " +
                            ev.getDepart_participant() + " | " +
                            ev.getContact() + " | " +
                            ev.getExperience_event()+ " | " +
                            ev.getRemarque()
            );

        }
    }
    // Rafraîchir la liste des événements
    public void refreshList() {
        try {
            List<Participation> participations = serviceParticipation.recuperer(); // Récupérer les événements depuis la BD
            list_participant.getItems().clear(); // Vider la liste avant de la remplir à nouveau
            for (Participation ev : participations) {
                list_participant.getItems().add(
                        ev.getId_participation() + " | " +
                                ev.getId_event() + " | " +
                                ev.getDate_participation() + " | " +
                                ev.getRole_participant() + " | " +
                                ev.getDepart_participant() + " | " +
                                ev.getContact() + " | " +
                                ev.getExperience_event()+ " | " +
                                ev.getRemarque()
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }
    }
}
