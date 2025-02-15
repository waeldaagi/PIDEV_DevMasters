package models;

public class Projet {
    private  int id_projet;
    private String nom_projet;
    private String duree;
    private  String manager;
    private String nom_client;
    private int idEquipe;
    public Projet() {}
    public Projet( String nom_projet, String duree, String manager, int idEquipe) {
        this.nom_projet = nom_projet;
        this.duree = duree;
        this.manager = manager;
        this.idEquipe = idEquipe;
    }

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
    public String getDuree() {
        return duree;
    }
    public void setDuree(String duree) {
        this.duree = duree;
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
    public int getIdEquipe() {
        return idEquipe;
    }
    public void setIdEquipe(int idEquipe) {
        this.idEquipe = idEquipe;
    }

    @Override
    public String toString() {
        return "Projet{" +
                "id_projet=" + id_projet +
                ", nom_projet='" + nom_projet + '\'' +
                ", duree='" + duree + '\'' +
                ", manager='" + manager + '\'' +
                ", nom_client='" + nom_client + '\'' +
                ", idEquipe=" + idEquipe +
                '}';
    }
}
