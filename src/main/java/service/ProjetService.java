package service;

import models.Equipe;
import models.Projet;
import tools.myDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetService implements Iservise<Projet> {
    private Connection cnx;

    public ProjetService() {
        cnx = myDb.getmyDb().getCnx();
    }

    @Override
    public void ajouter(Projet projet) throws SQLException {
        String sql = "INSERT INTO projet (nom_projet, Deadline, manager, nom_client, id_equipe) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, projet.getNom_projet());
            ps.setDate(2, projet.getDeadline());
            ps.setString(3, projet.getManager());
            ps.setString(4, projet.getNom_client());
            ps.setInt(5, projet.getEquipe().getIdEquipe()); // Utiliser l'ID de l'équipe
            ps.executeUpdate();
            System.out.println("Projet ajouté avec succès.");
        } catch (SQLException ex) { // Renamed 'e' to 'ex' to avoid conflict
            System.err.println("Erreur lors de l'ajout du projet : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM projet WHERE id_projet = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Projet supprimé avec succès.");
        } catch (SQLException ex) { // Renamed 'e' to 'ex' to avoid conflict
            System.err.println("Erreur lors de la suppression du projet : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public void modifier(Projet projet) throws SQLException {
        String req = "UPDATE projet SET nom_projet = ?, Deadline = ?, manager = ?, nom_client = ?, id_equipe = ? WHERE id_projet = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, projet.getNom_projet());
            ps.setDate(2, projet.getDeadline());
            ps.setString(3, projet.getManager());
            ps.setString(4, projet.getNom_client());
            ps.setInt(5, projet.getEquipe().getIdEquipe()); // Utiliser l'ID de l'équipe
            ps.setInt(6, projet.getId_projet());
            ps.executeUpdate();
            System.out.println("Projet modifié avec succès.");
        } catch (SQLException ex) { // Renamed 'e' to 'ex' to avoid conflict
            System.err.println("Erreur lors de la modification du projet : " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    public List<Projet> getAll(Projet projetParam) throws SQLException { // Renamed 'e' to 'projetParam'
        String sql = "SELECT p.*, e.nomEquipe, e.nbrEmployee, e.nomTeqlead " +
                "FROM projet p " +
                "JOIN equipe e ON p.id_equipe = e.idEquipe";
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
        } catch (SQLException ex) { // Renamed 'e' to 'ex' to avoid conflict
            System.err.println("Erreur lors de la récupération des projets : " + ex.getMessage());
            throw ex;
        }

        return projets;
    }

    // Méthode pour rechercher des projets par nom ou manager
    public List<Projet> searchProjects(String searchText) throws SQLException {
        String sql = "SELECT p.*, e.nomEquipe, e.nbrEmployee, e.nomTeqlead " +
                "FROM projet p " +
                "JOIN equipe e ON p.id_equipe = e.idEquipe " +
                "WHERE p.nom_projet LIKE ? OR p.manager LIKE ?";
        List<Projet> projets = new ArrayList<>();

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, "%" + searchText + "%");
            ps.setString(2, "%" + searchText + "%");

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
        } catch (SQLException ex) { // Renamed 'e' to 'ex' to avoid conflict
            System.err.println("Erreur lors de la recherche des projets : " + ex.getMessage());
            throw ex;
        }

        return projets;
    }
}