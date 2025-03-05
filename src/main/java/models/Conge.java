package models;

import java.util.Date;

public class Conge {
    private int idConge;
    private int idUser;
    private TypeConge typeConge;
    private Date dateDebut;
    private Date dateFin;
    private Date dateDemande;
    private StatutConge statut;
    private String image;

    // Constructor with parameters
    public Conge(int idConge, int idUser, TypeConge typeConge, Date dateDebut, Date dateFin, Date dateDemande, StatutConge statut, String image) {
        this.idConge = idConge;
        this.idUser = idUser;
        this.typeConge = typeConge;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.dateDemande = dateDemande;
        this.statut = statut;
        this.image = image;
    }

    // Default constructor
    public Conge() {}

    // Getters and Setters
    public int getIdConge() {
        return idConge;
    }

    public void setIdConge(int idConge) {
        this.idConge = idConge;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public TypeConge getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(TypeConge typeConge) {
        this.typeConge = typeConge;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(Date dateDemande) {
        this.dateDemande = dateDemande;
    }

    public StatutConge getStatut() {
        return statut;
    }

    public void setStatut(StatutConge statut) {
        this.statut = statut;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Conge{" +
                "idConge=" + idConge +
                ", idUser=" + idUser +
                ", typeConge=" + typeConge +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", dateDemande=" + dateDemande +
                ", statut=" + statut +
                ", image='" + image + '\'' +
                '}';
    }
}
