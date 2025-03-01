package models;

import java.util.ArrayList;
import java.util.List;

import java.util.Date;

public class Evennement {
    private int id_event;
    private String nom_event;
    private String description;
    private Date date_event;
    private String lieu_event;
    private String  organisateur ;
    private String  statut;
    private String img_event;
    private List<Participation> participations;

    public Evennement() {
        this.participations = new ArrayList<>();
    }

    public Evennement( String nom_event, String description, Date date_event, String  lieu_event, String  organisateur, String  statut ,String img_event) {
        this.nom_event = nom_event;
        this.description = description;
        this.date_event = date_event;
        this.lieu_event = lieu_event;
        this.organisateur = organisateur;
        this.statut = statut;
        this.img_event = img_event;
        this.participations = new ArrayList<>();
    }
    public Evennement(int id_event, String nom_event, String description, Date date_event, String lieu_event, String  organisateur, String  statut ,String img_event) {
        this.id_event = id_event;
        this.nom_event = nom_event;
        this.description = description;
        this.date_event = date_event;
        this.lieu_event = lieu_event;
        this.organisateur = organisateur;
        this.statut = statut;
        this.img_event = img_event;
        this.participations = new ArrayList<>();
    }

    public int getId_event() {
        return id_event;
    }

    public String getNom_event() {
        return nom_event;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate_event() {
        return date_event;
    }

    public String  getLieu_event() {
        return lieu_event;
    }

    public String getOrganisateur() {
        return organisateur;
    }

    public String  getStatut() {
        return statut;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public void setNom_event(String nom_event) {
        this.nom_event = nom_event;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_event() {
        return img_event;
    }

    public void setImg_event(String img_event) {
        this.img_event = img_event;
    }

    public void setDate_event(Date date_event) {
        this.date_event = date_event;
    }

    public void setLieu_event(String lieu_event) {
        this.lieu_event = lieu_event;
    }

    public void setOrganisateur(String  organisateur) {
        this.organisateur = organisateur;
    }

    public void setStatut(String  statut) {
        this.statut = statut;
    }
    public void addFormation(Participation participation) {
        this.participations.add(participation);
    }

    public List<Participation> getFormations() {
        return participations;
    }

    @Override
    public String toString() {
        return "Evennement{" +
                "id_event=" + id_event +
                ", nom_event='" + nom_event + '\'' +
                ", description='" + description + '\'' +
                ", date_event=" + date_event +
                ", lieu_event=" + lieu_event +
                ", organisateur=" + organisateur +
                ", statut=" + statut +
                ", formations=" + participations +
                '}';
    }
}
