package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import models.Evennement;

import java.util.HashMap;
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
import java.util.Map;

public class AfficherParticipantController {

    @FXML
    private TextField id_part_delete;

    @FXML
    private ListView<String> list_participant;

    private final ServiceParticipation serviceParticipation = new ServiceParticipation();
    private final Map<String, Integer> eventMap = new HashMap<>();
    private String selectedParticipant; // Variable membre pour stocker le participant sélectionné

    public void initialize()
    {
        refreshList(); // Charger la liste dès l’affichage de la page

        // Listener pour détecter un clic sur un participant
        list_participant.setOnMouseClicked(event -> {
            selectedParticipant = list_participant.getSelectionModel().getSelectedItem(); // Met à jour le participant sélectionné
            if (selectedParticipant != null) {
                // Affiche le participant sélectionné dans la console
                System.out.println("Participant sélectionné : " + selectedParticipant);
            }
        });
    }
    @FXML
    void delete_participant(ActionEvent event) {
        if (selectedParticipant == null) {
            System.err.println("Erreur : Aucun participant sélectionné.");
            return;
        }

        // Extraire l'ID en fonction de la façon dont vous affichez les participants
        Integer idParticipation = eventMap.get(selectedParticipant); // Assurez-vous que l'ID est correctement mappé

        if (idParticipation != null) {
            try {
                serviceParticipation.supprimer(idParticipation); // Supprimer la participation
                refreshList();
                System.out.println("Participant supprimé avec succès !");
            } catch (SQLException e) {
                System.err.println("Erreur SQL lors de la suppression de la participation : " + e.getMessage());
            }
        } else {
            System.err.println("Erreur : ID introuvable pour le participant sélectionné.");
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
        list_participant.getItems().clear();  // Vider la liste précédente
        eventMap.clear(); // Réinitialiser la map pour le nouvel affichage
        for (Participation part : participations) {
            String participantText =
                    "Date de participation : " + part.getDate_participation() + "\n" +
                            "Rôle du participant : " + part.getRole_participant() + "\n" +
                            "Département : " + part.getDepart_participant() + "\n" +
                            "Contact : " + part.getContact() + "\n" +
                            "Expérience de l'événement : " + part.getExperience_event() + "\n" +
                            "Remarque : " + part.getRemarque();

            list_participant.getItems().add(participantText);
            eventMap.put(participantText, part.getId_participation()); // Associer affichage ↔ ID réel
        }
    }

    // Rafraîchir la liste des événements
    public void refreshList() {
        try {
            List<Participation> participations = serviceParticipation.recuperer(); // Récupérer les événements depuis la BD
            list_participant.getItems().clear(); // Vider la liste avant de la remplir à nouveau
            for (Participation ev : participations) {
                list_participant.getItems().add(
                        "Date de participation : " + ev.getDate_participation() + "\n" +
                                "Rôle du participant : " + ev.getRole_participant() + "\n" +
                                "Département : " + ev.getDepart_participant() + "\n" +
                                "Contact : " + ev.getContact() + "\n" +
                                "Expérience de l'événement : " + ev.getExperience_event() + "\n" +
                                "Remarque : " + ev.getRemarque()
                );
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des événements : " + e.getMessage());
        }




    }
    public void refreshListByEventId(int idEvent) {
        try {
            List<Participation> participations = serviceParticipation.recupererParEvenement(idEvent); // Récupérer les participations par événement
            setListeParticipations(participations); // Met à jour la liste des participations
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des participations : " + e.getMessage());
        }
    }

}
