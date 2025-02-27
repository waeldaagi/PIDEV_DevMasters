package models;

public class Demande {
    private int id_demande;
    private int id_user;
    private int id_offre;
    private String type_contrat;
    private String cv;
    private String lettre_motivation;

    public Demande() {}

    public Demande(int id_demande, int id_user, int id_offre, String type_contrat, String cv, String lettre_motivation) {
        this.id_demande = id_demande;
        this.id_user = id_user;
        this.id_offre = id_offre;
        this.type_contrat = type_contrat;
        this.cv = cv;
        this.lettre_motivation = lettre_motivation;
    }

    public Demande(int id_user, int id_offre, String type_contrat, String cv, String lettre_motivation) {
        this.id_user = id_user;
        this.id_offre = id_offre;
        this.type_contrat = type_contrat;
        this.cv = cv;
        this.lettre_motivation = lettre_motivation;
    }

    public int getId_demande() {
        return id_demande;
    }

    public void setId_demande(int id_demande) {
        this.id_demande = id_demande;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_offre() {
        return id_offre;
    }

    public void setId_offre(int id_offre) {
        this.id_offre = id_offre;
    }

    public String getType_contrat() {
        return type_contrat;
    }

    public void setType_contrat(String type_contrat) {
        this.type_contrat = type_contrat;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getLettre_motivation() {
        return lettre_motivation;
    }

    public void setLettre_motivation(String lettre_motivation) {
        this.lettre_motivation = lettre_motivation;
    }

    @Override
    public String toString() {
        return "Demande{" +
                "id_demande=" + id_demande +
                ", id_user=" + id_user +
                ", id_offre=" + id_offre +
                ", type_contrat='" + type_contrat + '\'' +
                ", cv='" + cv + '\'' +
                ", lettre_motivation='" + lettre_motivation + '\'' +
                '}';
    }



}
