package service;

import models.Equipe;
import tools.myDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeServise implements Iservise<Equipe> {
    private Connection cnx;

    public EquipeServise() {
        cnx = myDb.getmyDb().getCnx();
    }

    @Override
    public void ajouter(Equipe e) throws SQLException {
        String sql = "INSERT INTO equipe (nom_equipe, nbr_employee, nom_teqlead) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, e.getNomEquipe());
            ps.setInt(2, e.getNbrEmployee());
            ps.setString(3, e.getNomTeqlead());
            ps.executeUpdate();
            System.out.println("Équipe ajoutée avec succès !");
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM equipe WHERE id_equipe = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Équipe supprimée avec succès !");
        }
    }

    @Override
    public void modifier(Equipe e) throws SQLException {
        String sql = "UPDATE equipe SET nom_equipe = ?, nbr_employee = ?, nom_teqlead = ? WHERE id_equipe = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, e.getNomEquipe());
            ps.setInt(2, e.getNbrEmployee());
            ps.setString(3, e.getNomTeqlead());
            ps.setInt(4, e.getIdEquipe());
            ps.executeUpdate();
            System.out.println("Équipe modifiée avec succès !");
        }
    }

    @Override
    public List<Equipe> getAll(Equipe e) throws SQLException {
        String sql = "SELECT * FROM equipe";
        List<Equipe> equipes = new ArrayList<>();
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Equipe equipe = new Equipe();
                equipe.setIdEquipe(rs.getInt("id_equipe"));
                equipe.setNomEquipe(rs.getString("nom_equipe"));
                equipe.setNbrEmployee(rs.getInt("nbr_employee"));
                equipe.setNomTeqlead(rs.getString("nom_teqlead"));
                equipes.add(equipe);
            }
        }
        return equipes;
    }

    // Additional method for searching teams by name or techlead
    public List<Equipe> searchTeams(String searchText) throws SQLException {
        String sql = "SELECT * FROM equipe WHERE nom_equipe LIKE ? OR nom_teqlead LIKE ?";
        List<Equipe> equipes = new ArrayList<>();
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, "%" + searchText + "%");
            ps.setString(2, "%" + searchText + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Equipe equipe = new Equipe();
                    equipe.setIdEquipe(rs.getInt("id_equipe"));
                    equipe.setNomEquipe(rs.getString("nom_equipe"));
                    equipe.setNbrEmployee(rs.getInt("nbr_employee"));
                    equipe.setNomTeqlead(rs.getString("nom_teqlead"));
                    equipes.add(equipe);
                }
            }
        }
        return equipes;
    }
}