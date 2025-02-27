package services;
import models.Demande;
import tools.myDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDemande implements IServices<Demande> {
    private Connection cnx;

    public ServiceDemande() {
        cnx = myDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Demande d) throws SQLException {
        if (d.getId_user() <= 0 || d.getId_offre() <= 0 || d.getType_contrat().trim().isEmpty() || d.getCv().trim().isEmpty() || d.getLettre_motivation().trim().isEmpty()) {
            System.out.println("Erreur : Tous les champs doivent être remplis correctement.");
            return;
        }

        String sql = "INSERT INTO demande (id_user, id_offre, type_contrat, cv, lettre_motivation) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, d.getId_user());
        ste.setInt(2, d.getId_offre());
        ste.setString(3, d.getType_contrat());
        ste.setString(4, d.getCv());
        ste.setString(5, d.getLettre_motivation());
        ste.executeUpdate();
        System.out.println("Demande ajoutée");
        cnx.commit();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        if (id <= 0) {
            System.out.println("Erreur : ID invalide.");
            return;
        }

        String sql = "DELETE FROM demande WHERE id_demande=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, id);
        ste.executeUpdate();
        System.out.println("Demande supprimée");
        cnx.commit();
    }

    @Override
    public void modifier(int id, String type_contrat) throws SQLException {
        if (id <= 0 || type_contrat.trim().isEmpty()) {
            System.out.println("Erreur : ID ou type de contrat invalide.");
            return;
        }

        String sql = "UPDATE demande SET type_contrat=? WHERE id_demande=?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setString(1, type_contrat);
        ste.setInt(2, id);
        ste.executeUpdate();
        System.out.println("Demande modifiée");
        cnx.commit();
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
        }
        return demandes;
    }
    public List<Demande> recupererDemandeParOffre(int idOffre) throws SQLException {
        String sql = "SELECT * FROM demande WHERE id_offre = ?";
        PreparedStatement ste = cnx.prepareStatement(sql);
        ste.setInt(1, idOffre);
        ResultSet rs = ste.executeQuery();
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
        }
        return demandes;
    }
    public boolean checkDemande(int idOffre, int idUser) throws SQLException {
        String query = "SELECT COUNT(*) FROM demande WHERE id_offre = ? AND id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, idOffre);
            stmt.setInt(2, idUser);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retourne true si une demande existe
            }
        }
        return false;
    }



}
