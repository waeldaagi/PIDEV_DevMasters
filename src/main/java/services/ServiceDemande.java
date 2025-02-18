package services;
import models.Demande;
import tools.myDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDemande implements IServices<Demande>{
    private Connection cnx;

    public ServiceDemande() {
        cnx = myDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Demande d) throws SQLException {
        String sql = "INSERT INTO demande (id_user, id_offre, type_contrat, cv, lettre_motivation) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, d.getId_user());
        ste.setInt(2, d.getId_offre());
        ste.setString(3, d.getType_contrat());
        ste.setString(4, d.getCv());
        ste.setString(5, d.getLettre_motivation());
        ste.executeUpdate();
        System.out.println("Demande ajoutée");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM demande WHERE id_demande=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("Demande supprimée");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public void modifier(int id, String type_contrat) throws SQLException {
        String sql = "UPDATE demande SET type_contrat=? WHERE id_demande=?";
        PreparedStatement ste = cnx.prepareStatement(sql);

        // Set the new value for type_contrat
        ste.setString(1, type_contrat);
        ste.setInt(2, id);  // Set the ID of the record to update

        ste.executeUpdate();
        System.out.println("Demande modifiée");
        myDataBase.getInstance().getConnection().commit();
    }

    @Override
    public List<Demande> recuperer() throws SQLException {
        String sql = "SELECT * FROM demande";
        Statement ste = cnx.createStatement();
        ResultSet rs = ste.executeQuery(sql);
        List<Demande> demandes = new ArrayList<>();

        while (rs.next()) {
            Demande d = new Demande();
            d.setId_demande(rs.getInt("id_demande"));
            d.setId_user(rs.getInt("id_user"));
            d.setId_offre(rs.getInt("id_offre"));
            d.setType_contrat(rs.getString("type_contrat"));
            d.setCv(rs.getString("cv"));
            d.setLettre_motivation(rs.getString("lettre_motivation"));
            demandes.add(d);
            myDataBase.getInstance().getConnection().commit();
        }
        return demandes;
    }
}
