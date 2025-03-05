package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OffreRecrutement {
    private int id_offre;
    private Date date_pub;
    private Date date_limite;
    private int salaire;
    private String poste;
    private List<Demande> demandes;


    public OffreRecrutement() {this.demandes = new ArrayList<>();}

    public OffreRecrutement(int id_offre, Date date_pub, Date date_limite, int salaire, String poste) {
        this.id_offre = id_offre;
        this.date_pub = date_pub;
        this.date_limite = date_limite;
        this.salaire = salaire;
        this.poste = poste;
        this.demandes = new ArrayList<>();
    }

    public OffreRecrutement(Date date_pub, Date date_limite, int salaire, String poste) {
        this.date_pub = date_pub;
        this.date_limite = date_limite;
        this.salaire = salaire;
        this.poste = poste;
        this.demandes = new ArrayList<>();
    }


    public List<Demande> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<Demande> demandes) {
        this.demandes = demandes;
    }

    public int getId_offre() {
        return id_offre;
    }

    public void setId_offre(int id_offre) {
        this.id_offre = id_offre;
    }

    public Date getDate_pub() {
        return date_pub;
    }

    public void setDate_pub(Date date_pub) {
        this.date_pub = date_pub;
    }

    public Date getDate_limite() {
        return date_limite;
    }

    public void setDate_limite(Date date_limite) {
        this.date_limite = date_limite;
    }

    public int getSalaire() {
        return salaire;
    }

    public void setSalaire(int salaire) {
        this.salaire = salaire;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    @Override
    public String toString() {
        return "OffreRecrutement{" +
                "id_offre=" + id_offre +
                ", date_pub=" + date_pub +
                ", date_limite=" + date_limite +
                ", salaire=" + salaire +
                ", poste='" + poste + '\'' +
                ", demandes=" + demandes +
                '}';
    }
}
