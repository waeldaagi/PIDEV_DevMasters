package services;
import models.Evennement;
import tools.myDataBase;

import java.sql.Date;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvennement implements IServices<Evennement>{
private Connection cnx;

    public ServiceEvennement() {
        cnx = myDataBase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Evennement e) throws SQLException {
        String sql = "INSERT INTO evennement (nom_event, description, date_event, lieu_event, organisateur, statut) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, e.getNom_event());
        ste.setString(2, e.getDescription());
        ste.setDate(3, new java.sql.Date(e.getDate_event().getTime()));
        ste.setString(4, e.getLieu_event());
        ste.setString(5, e.getOrganisateur());
        ste.setString(6, e.getStatut());
        ste.executeUpdate();
        System.out.println("Événement ajouté");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM evennement WHERE id_event=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("Événement supprimé");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public void modifier(int id, String nom_event, String description, String statut) throws SQLException {
        String sql = "UPDATE evennement SET nom_event=?, description=?, statut=? WHERE id_event=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, nom_event);
        ste.setString(2, description);
        ste.setString(3, statut);
        ste.setInt(4, id);
        ste.executeUpdate();
        System.out.println("Événement modifié");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public List<Evennement> recuperer() throws SQLException {
        String sql = "SELECT * FROM evennement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<Evennement> evenements = new ArrayList<>();

        while (rs.next()) {
            Evennement e = new Evennement();
            e.setId_event(rs.getInt("id_event"));
            e.setNom_event(rs.getString("nom_event"));
            e.setDescription(rs.getString("description"));
            e.setDate_event(rs.getDate("date_event"));
            e.setLieu_event(rs.getString("lieu_event"));
            e.setOrganisateur(rs.getString("organisateur"));
            e.setStatut(rs.getString("statut"));
            evenements.add(e);
        }
        return evenements;
    }




}
