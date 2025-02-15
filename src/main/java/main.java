import models.Equipe;
import servise.EquipeServise;
import tools.myDb;

import java.sql.SQLException;

public class main {
    public static void main(String[] args) {
        EquipeServise equipeServise = new EquipeServise();
        Equipe e = new Equipe("don",55,"sabri");
        try {
            equipeServise.ajouter(e);
            //equipeServise.supprimer(2);
            System.out.println(equipeServise.getAll(new Equipe()));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }
}
