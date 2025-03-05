//package services;
//
//import models.Recrutement;
//import tools.myDataBase;
//
//import java.sql.Date;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ServiceRecrutement implements IServices<Recrutement> {
//    private Connection cnx;
//
//    public ServiceRecrutement() {
//        cnx = myDataBase.getInstance().getConnection();
//    }
//
//    @Override
//    public void ajouter(Recrutement r) throws SQLException {
//        String sql = "INSERT INTO recrutement (date_publication, date_limite, salaire, poste, etat) VALUES (?, ?, ?, ?, ?)";
//        PreparedStatement ste = cnx.prepareStatement(sql);
//        ste.setDate(1, new java.sql.Date(r.getDate_publication().getTime()));
//        ste.setDate(2, new java.sql.Date(r.getDate_limite().getTime()));
//        ste.setInt(3, r.getSalaire());
//        ste.setString(4, r.getPost());
//        ste.setString(5, r.getEtat());
//        ste.executeUpdate();
//        System.out.println("Recrutement ajouté");
//        myDataBase.getInstance().getConnection().commit();
//    }
//
//    @Override
//    public void supprimer(int id) throws SQLException {
//        String sql = "DELETE FROM recrutement WHERE id_rec=?";
//        PreparedStatement ste = cnx.prepareStatement(sql);
//        ste.setInt(1, id);
//        ste.executeUpdate();
//        System.out.println("Recrutement supprimé");
//        myDataBase.getInstance().getConnection().commit();
//    }
//
//    @Override
//    public void modifier(int id, String poste) throws SQLException {
//        String sql = "UPDATE recrutement SET poste=? WHERE id_rec=?";
//        PreparedStatement ste = cnx.prepareStatement(sql);
//        ste.setString(1, poste);
//        ste.setInt(2, id);
//        ste.executeUpdate();
//        System.out.println("Recrutement modifié");
//        myDataBase.getInstance().getConnection().commit();
//    }
//
//    @Override
//    public List<Recrutement> recuperer() throws SQLException {
//        String sql = "SELECT * FROM recrutement";
//        Statement ste = cnx.createStatement();
//        ResultSet rs = ste.executeQuery(sql);
//        List<Recrutement> recrutements = new ArrayList<>();
//
//        while (rs.next()) {
//            Recrutement r = new Recrutement();
//            r.setId_rec(rs.getInt("id_rec"));
//            r.setDate_publication(rs.getDate("date_publication"));
//            r.setDate_limite(rs.getDate("date_limite"));
//            r.setSalaire(rs.getInt("salaire"));
//            r.setPost(rs.getString("poste"));
//            r.setEtat(rs.getString("etat"));
//            recrutements.add(r);
//            myDataBase.getInstance().getConnection().commit();
//        }
//        return recrutements;
//    }
//
//}
