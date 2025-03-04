package models;

import java.sql.Date;

public class Projet {
    private int id_projet;
    private String nom_projet;
    private Date Deadline;
    private String manager;
    private String nom_client;
    private Equipe equipe; // Remplace "idEquipe" par un objet "Equipe"

    // Constructeur par défaut
    public Projet() {}

    // Constructeur avec paramètres
    public Projet(String nom_projet, Date Deadline, String manager, String nom_client, Equipe equipe) {
        this.nom_projet = nom_projet;
        this.Deadline = Deadline;
        this.manager = manager;
        this.nom_client = nom_client;
        this.equipe = equipe;
    }

    // Getters et setters
    public int getId_projet() {
        return id_projet;
    }

    public void setId_projet(int id_projet) {
        this.id_projet = id_projet;
    }

    public String getNom_projet() {
        return nom_projet;
    }

    public void setNom_projet(String nom_projet) {
        this.nom_projet = nom_projet;
    }

    public Date getDeadline() {
        return Deadline;
    }

    public void setDeadline(Date Deadline) {
        this.Deadline = Deadline;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getNom_client() {
        return nom_client;
    }

    public void setNom_client(String nom_client) {
        this.nom_client = nom_client;
    }

    public Equipe getEquipe() {
        return equipe;
    }

    public void setEquipe(Equipe equipe) {
        this.equipe = equipe;
    }

    @Override
    public String toString() {
        return "Projet{" +
                "id_projet=" + id_projet +
                ", nom_projet='" + nom_projet + '\'' +
                ", Deadline=" + Deadline +
                ", manager='" + manager + '\'' +
                ", nom_client='" + nom_client + '\'' +
                ", equipe=" + equipe +
                '}';
    }
}