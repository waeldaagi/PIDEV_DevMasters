package service;

import models.Equipe;
import models.Projet;
import tools.myDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetService implements Iservise<Projet> {
    private Connection cnx;
    private PreparedStatement pst;

    public ProjetService() {
        cnx = myDb.getmyDb().getCnx();
    }

    @Override
    public void ajouter(Projet projet) throws SQLException {
        String req = "INSERT INTO projet (nom_projet, Deadline, manager, nom_client, id_equipe) VALUES (?,?,?,?,?)";
        try {
            pst = cnx.prepareStatement(req);
            pst.setString(1, projet.getNom_projet());
            pst.setDate(2, projet.getDeadline());
            pst.setString(3, projet.getManager());
            pst.setString(4, projet.getNom_client());
            pst.setInt(5, projet.getEquipe().getIdEquipe());
            pst.executeUpdate();
            System.out.println("Projet ajouté avec succès.");
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de l'ajout du projet : " + ex.getMessage());
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM projet WHERE id_projet=?";
        try {
            pst = cnx.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
            System.out.println("Projet supprimé avec succès.");
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de la suppression du projet : " + ex.getMessage());
        }
    }

    @Override
    public void modifier(Projet projet) throws SQLException {
        String req = "UPDATE projet SET nom_projet=?, Deadline=?, manager=?, nom_client=?, id_equipe=? WHERE id_projet=?";
        try {
            pst = cnx.prepareStatement(req);
            pst.setString(1, projet.getNom_projet());
            pst.setDate(2, projet.getDeadline());
            pst.setString(3, projet.getManager());
            pst.setString(4, projet.getNom_client());
            pst.setInt(5, projet.getEquipe().getIdEquipe());
            pst.setInt(6, projet.getId_projet());
            pst.executeUpdate();
            System.out.println("Projet modifié avec succès.");
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de la modification du projet : " + ex.getMessage());
        }
    }

    @Override
    public List<Projet> getAll(Projet projetParam) throws SQLException {
        String sql = "SELECT p.*, e.id_equipe, e.nomEquipe, e.nbrEmployee, e.nomTeqlead " +
                    "FROM projet p " +
                    "JOIN equipe e ON p.id_equipe = e.id_equipe";
        List<Projet> projets = new ArrayList<>();

        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                // Créer un objet Equipe
                Equipe equipe = new Equipe(
                    rs.getString("nomEquipe"),
                    rs.getInt("nbrEmployee"),
                    rs.getString("nomTeqlead")
                );
                equipe.setIdEquipe(rs.getInt("id_equipe"));

                // Créer un objet Projet
                Projet projet = new Projet(
                    rs.getString("nom_projet"),
                    rs.getDate("Deadline"),
                    rs.getString("manager"),
                    rs.getString("nom_client"),
                    equipe
                );
                projet.setId_projet(rs.getInt("id_projet"));

                projets.add(projet);
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la récupération des projets : " + ex.getMessage());
            throw ex;
        }

        return projets;
    }

    public List<Projet> searchProjects(String searchText) throws SQLException {
        String sql = "SELECT p.*, e.id_equipe, e.nomEquipe, e.nbrEmployee, e.nomTeqlead " +
                    "FROM projet p " +
                    "JOIN equipe e ON p.id_equipe = e.id_equipe " +
                    "WHERE p.nom_projet LIKE ? OR p.manager LIKE ? OR p.nom_client LIKE ? OR p.nom_client LIKE ?";
        List<Projet> projets = new ArrayList<>();

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, "%" + searchText + "%");
            ps.setString(2, "%" + searchText + "%");
            ps.setString(3, "%" + searchText + "%");
            ps.setString(4, "%" + searchText + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Créer un objet Equipe
                    Equipe equipe = new Equipe(
                        rs.getString("nomEquipe"),
                        rs.getInt("nbrEmployee"),
                        rs.getString("nomTeqlead")
                    );
                    equipe.setIdEquipe(rs.getInt("id_equipe"));

                    // Créer un objet Projet
                    Projet projet = new Projet(
                        rs.getString("nom_projet"),
                        rs.getDate("Deadline"),
                        rs.getString("manager"),
                        rs.getString("nom_client"),
                        equipe
                    );
                    projet.setId_projet(rs.getInt("id_projet"));

                    projets.add(projet);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erreur lors de la recherche des projets : " + ex.getMessage());
            throw ex;
        }

        return projets;
    }
}