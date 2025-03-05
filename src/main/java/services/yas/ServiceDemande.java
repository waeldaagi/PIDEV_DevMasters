package services.yas;

import models.Demande;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDemande implements IServices<Demande> {
    private final Connection cnx;

    public ServiceDemande() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void ajouter(Demande d) throws SQLException {
        if (d.getId_user() <= 0 || d.getId_offre() <= 0 || d.getType_contrat().trim().isEmpty() || d.getCv().trim().isEmpty() || d.getLettre_motivation().trim().isEmpty()) {
            return;
        }

        String sql = "INSERT INTO demande (id_user, id_offre, type_contrat, cv, lettre_motivation) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, d.getId_user());
            ste.setInt(2, d.getId_offre());
            ste.setString(3, d.getType_contrat());
            ste.setString(4, d.getCv());
            ste.setString(5, d.getLettre_motivation());
            ste.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        if (id <= 0) return;

        String sql = "DELETE FROM demande WHERE id_demande=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, id);
            ste.executeUpdate();
        }
    }

    @Override
    public void modifier(int id, String type_contrat) throws SQLException {
        if (id <= 0 || type_contrat.trim().isEmpty()) return;

        String sql = "UPDATE demande SET type_contrat=? WHERE id_demande=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setString(1, type_contrat);
            ste.setInt(2, id);
            ste.executeUpdate();
        }
    }

    @Override
    public List<Demande> recuperer() throws SQLException {
        String sql = "SELECT * FROM demande";
        List<Demande> demandes = new ArrayList<>();

        try (Statement ste = cnx.createStatement(); ResultSet rs = ste.executeQuery(sql)) {
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
        }
        return demandes;
    }

    public List<Demande> recupererDemandeParOffre(int idOffre) throws SQLException {
        if (idOffre <= 0) return new ArrayList<>();

        String sql = "SELECT * FROM demande WHERE id_offre = ?";
        List<Demande> demandes = new ArrayList<>();

        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, idOffre);
            try (ResultSet rs = ste.executeQuery()) {
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
            }
        }
        return demandes;
    }

    public boolean checkDemande(int idOffre, int idUser) throws SQLException {
        if (idOffre <= 0 || idUser <= 0) return false;

        String query = "SELECT COUNT(*) FROM demande WHERE id_offre = ? AND id_user = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, idOffre);
            stmt.setInt(2, idUser);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }
}
