package servise;

import models.Projet;
import tools.myDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetServise implements Iservise<Projet> {
    private Connection cnx;

    public ProjetServise() {
        cnx = myDb.getmyDb().getCnx();
    }

    @Override
    public void ajouter(Projet e) throws SQLException {
        String sql = "INSERT INTO projet (nom_projet,dure,manager,nom_client,id_equipe) VALUES (?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, e.getNom_projet());
        ps.setString(2, e.getDuree());
        ps.setString(3, e.getManager());
        ps.setString(4, e.getNom_client());
        ps.setInt(5, e.getIdEquipe());
        ps.executeUpdate();
        System.out.println("projet ajoutée");
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM projet WHERE id_projet = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("peojet suprimée");
    }

    @Override
    public void modifier(Projet e, String nom) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public List<Projet> getAll(Projet e) throws SQLException {
        String sql = "SELECT * FROM projet ";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ArrayList<Projet> projets = new ArrayList<Projet>();
        while (rs.next()) {
            Projet p = new Projet();
            p.setId_projet(rs.getInt("id_projet"));
            p.setNom_projet(rs.getString("nom_projet"));
            p.setDuree(rs.getString("dure"));
            p.setManager(rs.getString("manager"));
            p.setNom_client(rs.getString("nom_client"));
            p.setIdEquipe(rs.getInt("id_equipe"));
            projets.add(p);
//            projets.add(p);
        }
        return projets;
    }
}

