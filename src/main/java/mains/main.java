package mains;
import services.ServiceFormation;
import services.ServiceEvennement;
import models.Formation;
import models.Evennement;
import java.sql.SQLException;
import java.util.Date;

import java.sql.SQLException;

public class main {
    public static void main(String[] args) {

        // Gestion des Evennement
        Evennement event = new Evennement( "Conférence Tech", "Conférence sur les nouvelles technologies", new Date(), "Paris", "John Doe", "Prévu");
        ServiceEvennement serviceEvent = new ServiceEvennement();

        try {
            // Ajouter un événement
            serviceEvent.ajouter(event);

            // Modifier un événement avec un ID spécifique
            serviceEvent.modifier(2, "Hackathon IA", "Compétition sur l'intelligence artificielle", "Terminé");

            // Afficher la liste des événements
            System.out.println(serviceEvent.recuperer());

            // Supprimer un événement avec un ID spécifique
            //serviceEvent.supprimer(4);

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // Gestion des formations
        Formation formation = new Formation(0, "Formation Java", "Alice Smith", new Date(), "Apprentissage avancé de Java", 1 );
        ServiceFormation serviceFormation = new ServiceFormation();

        try {
            // Ajouter une formation
            serviceFormation.ajouter(formation);

            // Modifier une formation avec un ID spécifique
            serviceFormation.modifier(8, "Formation Spring Boot", "Bob Johnson", "Développement d'API avec Spring Boot");

            // Afficher la liste des formations
            System.out.println(serviceFormation.recuperer());

            // Supprimer une formation avec un ID spécifique
            serviceFormation.supprimer(12);

        } catch (SQLException e) {
            System.err.println("Erreur Formation : " + e.getMessage());
        }

    }
}
