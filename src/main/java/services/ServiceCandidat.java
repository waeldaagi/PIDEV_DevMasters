//package services;
//// menich nestamel fih
//
//import models.Candidat;
//import tools.myDataBase;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ServiceCandidat implements IServices<Candidat>{
//    private Connection cnx;
//    public ServiceCandidat() {
//        cnx = myDataBase.getInstance().getConnection();
//    }
//
//    @Override
//    public void ajouter(Candidat c) throws SQLException {
//        String sql = "INSERT INTO candidat (nom, prenom, date_naissance, mail, type_contrat, id_rec) VALUES (?, ?, ?, ?, ?, ?)";
//        PreparedStatement ste = cnx.prepareStatement(sql);
//        ste.setString(1, c.getNom());
//        ste.setString(2, c.getPrenom());
//        ste.setDate(3, new java.sql.Date(c.getDate_naissance().getTime()));
//        ste.setString(4, c.getMail());
//        ste.setString(5, c.getType_contrat());
//        ste.setInt(6, c.getId_rec());
//        ste.executeUpdate();
//        System.out.println("Candidat ajouté");
//        myDataBase.getInstance().getConnection().commit();
//    }
//
//    @Override
//    public void supprimer(int id) throws SQLException {
//        String sql = "DELETE FROM candidat WHERE id_candidat=?";
//        PreparedStatement ste = cnx.prepareStatement(sql);
//        ste.setInt(1, id);
//        ste.executeUpdate();
//        System.out.println("Candidat supprimé");
//        myDataBase.getInstance().getConnection().commit();
//    }
//
//    @Override
//    public void modifier(int id, String nom) throws SQLException {
//        String sql = "UPDATE candidat SET nom=? WHERE id_candidat=?";
//        PreparedStatement ste = cnx.prepareStatement(sql);
//        ste.setString(1, nom);
//        ste.setInt(2, id);
//        ste.executeUpdate();
//        System.out.println("Candidat modifié");
//        myDataBase.getInstance().getConnection().commit();
//    }
//
//    @Override
//    public List<Candidat> recuperer() throws SQLException {
//        String sql = "SELECT * FROM candidat";
//        Statement ste = cnx.createStatement();
//        ResultSet rs = ste.executeQuery(sql);
//        List<Candidat> candidats = new ArrayList<>();
//
//        while (rs.next()) {
//            Candidat c = new Candidat();
//            c.setId_candidat(rs.getInt("id_candidat"));
//            c.setNom(rs.getString("nom"));
//            c.setPrenom(rs.getString("prenom"));
//            c.setDate_naissance(rs.getDate("date_naissance"));
//            c.setMail(rs.getString("mail"));
//            c.setType_contrat(rs.getString("type_contrat"));
//            c.setId_rec(rs.getInt("id_rec"));
//            candidats.add(c);
//            myDataBase.getInstance().getConnection().commit();
//        }
//        return candidats;
//    }
//
//}
