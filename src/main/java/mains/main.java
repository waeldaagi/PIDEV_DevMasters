package mains;
import services.ServiceParticipation;
import services.ServiceEvennement;
import models.Participation;
import models.Evennement;
import java.sql.SQLException;
import java.util.Date;

import java.sql.SQLException;

public class main {
    public static void main(String[] args) {

        // Gestion des Evennement
       // Evennement event = new Evennement( "Conférence Tech", "Conférence sur les nouvelles technologies", new Date(), "Paris", "John Doe", "Prévu");
        ServiceEvennement serviceEvent = new ServiceEvennement();

        try {
            // Ajouter un événement
           // serviceEvent.ajouter(event);

            // Modifier un événement avec un ID spécifique
           // serviceEvent.modifier(2, "Hackathon IA", "Compétition sur l'intelligence artificielle", "Terminé");

            // Afficher la liste des événements
            System.out.println(serviceEvent.recuperer());

            // Supprimer un événement avec un ID spécifique
            //serviceEvent.supprimer(1);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // Gestion des participations
        Participation participation = new Participation(5, 1, new Date(), "Jury", "Informatique", "123456789", "Très bonne expérience", "Aucun");
        ServiceParticipation serviceParticipation = new ServiceParticipation();

        try {
            // Ajouter une participation
            serviceParticipation.ajouter(participation);

            // Modifier une participation avec un ID spécifique
            //serviceParticipation.modifier(8, "Organisateur", "Marketing", "987654321");

            // Afficher la liste des participations
            System.out.println(serviceParticipation.recuperer());

            // Supprimer une participation avec un ID spécifique
            //serviceParticipation.supprimer(12);

        } catch (SQLException e) {
            System.err.println("Erreur Participation : " + e.getMessage());
        }

    }
}
