package services;

import models.Formation;
import tools.myDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements IServices<Formation>{
    private Connection cnx;

    public ServiceFormation() {
        cnx = myDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Formation f) throws SQLException {
        String sql = "INSERT INTO formation (titre_formation, formateur, date_formation, descrip_formation, id_event) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, f.getTitre_formation());
        ste.setString(2, f.getFormateur());
        ste.setDate(3, new java.sql.Date(f.getDate_formation().getTime()));
        ste.setString(4, f.getDescrip_formation());
        ste.setInt(5, f.getId_event());
        ste.executeUpdate();
        System.out.println("Formation ajoutée");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM formation WHERE id_formation=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("Formation supprimée");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public void modifier(int id, String titre, String formateur, String descrip_formation) throws SQLException {
        String sql = "UPDATE formation SET titre_formation=?, formateur=?, descrip_formation=? WHERE id_formation=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, titre);
        ste.setString(2, formateur);
        ste.setString(3, descrip_formation);
        ste.setInt(4, id);
        ste.executeUpdate();
        System.out.println("Formation modifiée");
        myDataBase.getInstance().getConnection().commit();
    }


    @Override
    public List<Formation> recuperer() throws SQLException {
        String sql = "SELECT * FROM formation";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<Formation> formations = new ArrayList<>();

        while (rs.next()) {
            Formation f = new Formation();
            f.setId_formation(rs.getInt("id_formation"));
            f.setTitre_formation(rs.getString("titre_formation"));
            f.setFormateur(rs.getString("formateur"));
            f.setDate_formation(rs.getDate("date_formation"));
            f.setDescrip_formation(rs.getString("descrip_formation"));
            f.setId_event(rs.getInt("id_event"));
            formations.add(f);
        }
        return formations;
    }

}
