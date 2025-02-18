package models;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Recrutement {
    private int id_rec,salaire;
    private String post,etat;
    private Date date_publication,date_limite;
    private List<Candidat> candidats; // One-to-Many Relationship

    public Recrutement() {
        this.candidats = new ArrayList<>();
    }

    public Recrutement(int id_rec, int salaire, String post, String etat, Date date_publication, Date date_limite) {
        this.id_rec = id_rec;
        this.salaire = salaire;
        this.post = post;
        this.etat = etat;
        this.date_publication = date_publication;
        this.date_limite = date_limite;
        this.candidats = new ArrayList<>();
    }

    public Recrutement(int salaire, String post, String etat, Date date_publication, Date date_limite) {
        this.salaire = salaire;
        this.post = post;
        this.etat = etat;
        this.date_publication = date_publication;
        this.date_limite = date_limite;
        this.candidats = new ArrayList<>();
    }

    public void addCandidat(Candidat candidat) {
        candidats.add(candidat);
    }

    public List<Candidat> getCandidats() {
        return candidats;
    }

    public int getId_rec() {
        return id_rec;
    }

    public void setId_rec(int id_rec) {
        this.id_rec = id_rec;
    }

    public int getSalaire() {
        return salaire;
    }

    public void setSalaire(int salaire) {
        this.salaire = salaire;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public Date getDate_publication() {
        return date_publication;
    }

    public void setDate_publication(Date date_publication) {
        this.date_publication = date_publication;
    }

    public Date getDate_limite() {
        return date_limite;
    }

    public void setDate_limite(Date date_limite) {
        this.date_limite = date_limite;
    }

    @Override
    public String toString() {
        return "Recrutement{" +
                "id_rec=" + id_rec +
                ", salaire=" + salaire +
                ", post='" + post + '\'' +
                ", etat='" + etat + '\'' +
                ", date_publication=" + date_publication +
                ", date_limite=" + date_limite +
                ", candidats=" + candidats +
                '}';
    }


}
