package mains;
import services.ServiceCandidat;
import services.ServiceRecrutement;
import models.Candidat;
import models.Recrutement;
import java.sql.SQLException;
import java.util.Date;

public class main {
    public static void main(String[] args) {
        Recrutement r = new Recrutement( 5000, "Développeur", "Ouvert", new Date(), new Date());
        ServiceRecrutement sr = new ServiceRecrutement();

        // Creating Candidat object
        Candidat c = new Candidat("Doe", "John", "john.doe@example.com", "CDI", new Date(), 12);
        ServiceCandidat sc = new ServiceCandidat();

        try {
            sr.ajouter(r);
            sr.modifier(2, "Ingénieur Logiciel", "accepté");
            System.out.println(sr.recuperer());
            sr.supprimer(7);

            // CRUD operations for Candidat
            sc.ajouter(c);
            sc.modifier(1, "prenom", "Jonathan");  // Example modification (changing prenom)
            System.out.println(sc.recuperer());
            sc.supprimer(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }




    }
}
