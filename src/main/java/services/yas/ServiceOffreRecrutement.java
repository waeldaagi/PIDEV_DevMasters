package services.yas;

import models.OffreRecrutement;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceOffreRecrutement implements IServices<OffreRecrutement> {
    private final Connection cnx;

    public ServiceOffreRecrutement() {
        this.cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void ajouter(OffreRecrutement o) throws SQLException {
        String sql = "INSERT INTO offre_recrutement (date_pub, date_limite, salaire, poste) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setDate(1, new Date(o.getDate_pub().getTime()));
            ste.setDate(2, new Date(o.getDate_limite().getTime()));
            ste.setInt(3, o.getSalaire());
            ste.setString(4, o.getPoste());
            ste.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        if (id <= 0) return;

        String sql = "DELETE FROM offre_recrutement WHERE id_offre=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setInt(1, id);
            ste.executeUpdate();
        }
    }

    @Override
    public void modifier(int id, String poste) throws SQLException {
        if (id <= 0 || poste.trim().isEmpty()) return;

        String sql = "UPDATE offre_recrutement SET poste=? WHERE id_offre=?";
        try (PreparedStatement ste = cnx.prepareStatement(sql)) {
            ste.setString(1, poste);
            ste.setInt(2, id);
            ste.executeUpdate();
        }
    }

    @Override
    public List<OffreRecrutement> recuperer() throws SQLException {
        String sql = "SELECT * FROM offre_recrutement";
        List<OffreRecrutement> offres = new ArrayList<>();

        try (Statement ste = cnx.createStatement(); ResultSet rs = ste.executeQuery(sql)) {
            while (rs.next()) {
                OffreRecrutement o = new OffreRecrutement();
                o.setId_offre(rs.getInt("id_offre"));
                o.setDate_pub(rs.getDate("date_pub"));
                o.setDate_limite(rs.getDate("date_limite"));
                o.setSalaire(rs.getInt("salaire"));
                o.setPoste(rs.getString("poste"));
                offres.add(o);
            }
        }
        return offres;
    }

    public OffreRecrutement getById(int id) throws SQLException {
        if (id <= 0) return null;

        String sql = "SELECT * FROM offre_recrutement WHERE id_offre = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    OffreRecrutement offre = new OffreRecrutement();
                    offre.setId_offre(rs.getInt("id_offre"));
                    offre.setDate_pub(rs.getDate("date_pub"));
                    offre.setDate_limite(rs.getDate("date_limite"));
                    offre.setSalaire(rs.getInt("salaire"));
                    offre.setPoste(rs.getString("poste"));
                    return offre;
                }
            }
        }
        return null;
    }
}
