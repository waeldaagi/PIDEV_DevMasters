//package models;
//
//import java.util.Date;
//
//public class Candidat {
//    private int id_candidat;
//    private String nom,prenom,mail,type_contrat;
//    private Date date_naissance;
//    private int id_rec; // Foreign key to Recrutement
//    public Candidat() {}
//
//    public Candidat(int id_candidat, String nom, String prenom, String mail, String type_contrat, Date date_naissance,int id_rec) {
//        this.id_candidat = id_candidat;
//        this.nom = nom;
//        this.prenom = prenom;
//        this.mail = mail;
//        this.type_contrat = type_contrat;
//        this.date_naissance = date_naissance;
//        this.id_rec = id_rec;
//    }
//
//    public Candidat( String nom, String prenom, String mail, String type_contrat, Date date_naissance,int id_rec) {
//        this.nom = nom;
//        this.prenom = prenom;
//        this.mail = mail;
//        this.type_contrat = type_contrat;
//        this.date_naissance = date_naissance;
//        this.id_rec = id_rec;
//    }
//
//    public int getId_rec() {
//        return id_rec;
//    }
//
//    public void setId_rec(int id_rec) {
//        this.id_rec = id_rec;
//    }
//
//    public int getId_candidat() {
//        return id_candidat;
//    }
//
//    public void setId_candidat(int id_candidat) {
//        this.id_candidat = id_candidat;
//    }
//
//    public String getNom() {
//        return nom;
//    }
//
//    public void setNom(String nom) {
//        this.nom = nom;
//    }
//
//    public String getPrenom() {
//        return prenom;
//    }
//
//    public void setPrenom(String prenom) {
//        this.prenom = prenom;
//    }
//
//    public String getMail() {
//        return mail;
//    }
//
//    public void setMail(String mail) {
//        this.mail = mail;
//    }
//
//    public String getType_contrat() {
//        return type_contrat;
//    }
//
//    public void setType_contrat(String type_contrat) {
//        this.type_contrat = type_contrat;
//    }
//
//    public Date getDate_naissance() {
//        return date_naissance;
//    }
//
//    public void setDate_naissance(Date date_naissance) {
//        this.date_naissance = date_naissance;
//    }
//
//    @Override
//    public String toString() {
//        return "Candidat{" +
//                "id_candidat=" + id_candidat +
//                ", nom='" + nom + '\'' +
//                ", prenom='" + prenom + '\'' +
//                ", mail='" + mail + '\'' +
//                ", type_contrat='" + type_contrat + '\'' +
//                ", date_naissance=" + date_naissance +
//                ", id_rec=" + id_rec +
//                '}';
//    }
//}
