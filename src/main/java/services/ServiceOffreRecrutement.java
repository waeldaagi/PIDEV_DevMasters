package services;

import models.OffreRecrutement;
import models.Recrutement;
import tools.myDataBase;

import java.sql.Date;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffreRecrutement implements IServices<OffreRecrutement>{
    private Connection cnx;

    public ServiceOffreRecrutement() {
        this.cnx = myDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(OffreRecrutement o) throws SQLException {
        String sql = "INSERT INTO offre_recrutement (date_pub, date_limite, salaire, poste) VALUES (?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setDate(1, new java.sql.Date(o.getDate_pub().getTime()));
        ste.setDate(2, new java.sql.Date(o.getDate_limite().getTime()));
        ste.setInt(3, o.getSalaire());
        ste.setString(4, o.getPoste());
        ste.executeUpdate();
        System.out.println("Offre de recrutement ajoutée");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM offre_recrutement WHERE id_offre=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("Offre Recrutement supprimé");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public void modifier(int id, String poste) throws SQLException {
        String sql = "UPDATE offre_recrutement SET poste=? WHERE id_offre=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, poste);
        ste.setInt(2, id);
        ste.executeUpdate();
        System.out.println("Offre de recrutement modifiée");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public List<OffreRecrutement> recuperer() throws SQLException {
        String sql = "SELECT * FROM offre_recrutement";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<OffreRecrutement> offres = new ArrayList<>();

        while (rs.next()) {
            OffreRecrutement o = new OffreRecrutement();
            o.setId_offre(rs.getInt("id_offre"));
            o.setDate_pub(rs.getDate("date_pub"));
            o.setDate_limite(rs.getDate("date_limite"));
            o.setSalaire(rs.getInt("salaire"));
            o.setPoste(rs.getString("poste"));
            offres.add(o);
            myDataBase.getInstance().getConnection().commit();
        }
        return offres;
    }







}
