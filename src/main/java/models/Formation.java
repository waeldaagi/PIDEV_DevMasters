package models;

import java.util.Date;

public class Formation {
    private int id_formation;
    private String titre_formation;
    private String formateur;
    private Date date_formation;
    private String descrip_formation;
    private int id_event;

    public Formation() {}

    public Formation( String titre_formation, String formateur, Date date_formation, String descrip_formation,int id_event) {

        this.titre_formation = titre_formation;
        this.formateur = formateur;
        this.date_formation = date_formation;
        this.id_event = id_event;

    }

    public Formation(int id_formation, String titre_formation, String formateur, Date date_formation, String descrip_formation , int id_event) {
        this.id_formation = id_formation;
        this.titre_formation = titre_formation;
        this.formateur = formateur;
        this.date_formation = date_formation;
        this.descrip_formation = descrip_formation;
        this.id_event = id_event;
    }

    public int getId_formation() {
        return id_formation;
    }

    public void setId_formation(int id_formation) {
        this.id_formation = id_formation;
    }

    public String getTitre_formation() {
        return titre_formation;
    }

    public void setTitre_formation(String titre_formation) {
        this.titre_formation = titre_formation;
    }

    public String getFormateur() {
        return formateur;
    }

    public void setFormateur(String formateur) {
        this.formateur = formateur;
    }

    public Date getDate_formation() {
        return date_formation;
    }

    public void setDate_formation(Date date_formation) {
        this.date_formation = date_formation;
    }

    public String getDescrip_formation() {
        return descrip_formation;
    }

    public void setDescrip_formation(String descrip_formation) {
        this.descrip_formation = descrip_formation;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id_formation=" + id_formation +
                ", titre_formation='" + titre_formation + '\'' +
                ", formateur='" + formateur + '\'' +
                ", date_formation=" + date_formation +
                ", descrip_formation='" + descrip_formation + '\'' +
                ", id_event=" + id_event +
                '}';
    }
}
