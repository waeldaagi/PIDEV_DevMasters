package test;

import models.Equipe;
import models.Projet;
import servise.EquipeServise;
import servise.ProjetServise;

import java.sql.SQLException;

public class main {
    public static void main(String[] args) {
        ProjetServise projetServise = new ProjetServise();
        EquipeServise equipeServise = new EquipeServise();
        Equipe e = new Equipe("don",55,"sabri");
        Projet p = new Projet("java","30 jour","motaz",4);
        try {
        //    equipeServise.ajouter(e);
            //equipeServise.supprimer(2);
          //  System.out.println(equipeServise.getAll(new Equipe()));
           // projetServise.ajouter(p);
            //projetServise.supprimer(3);
            System.out.println(projetServise.getAll(new Projet()));
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }

    }
}
