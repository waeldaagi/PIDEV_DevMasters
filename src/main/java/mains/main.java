package mains;
import services.ServiceDemande;
import services.ServiceOffreRecrutement;
import models.Demande;
import models.OffreRecrutement;
import java.sql.SQLException;
import java.util.Date;

public class main {
    public static void main(String[] args) {

        OffreRecrutement r = new OffreRecrutement( new Date(), new Date(), 14500, "CDI");
        ServiceOffreRecrutement sr = new ServiceOffreRecrutement();

        // Creating Candidat object
        Demande d = new Demande( 1, 2, "CDI", "path/cv", "dev java");
        ServiceDemande sc = new ServiceDemande();

        try {
            sr.ajouter(r);
            sr.modifier(2, "Ingénieur Logiciel");
            System.out.println(sr.recuperer());
            sr.supprimer(1);

            // CRUD operations for demande
            sc.ajouter(d);
            sc.modifier(1, "CDA");  // Example modification (changing prenom)
            System.out.println(sc.recuperer());
            sc.supprimer(1);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }




    }
}
