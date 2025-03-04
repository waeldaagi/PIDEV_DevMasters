package service;

import models.Equipe;
import tools.myDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeService {
    private Connection conn;
    private PreparedStatement pst;

    public EquipeService() {
        conn = myDb.getmyDb().getCnx();
    }

    public void ajouter(Equipe e) throws SQLException {
        String req = "INSERT INTO equipe (nomEquipe, nbrEmployee, nomTeqlead) VALUES (?,?,?)";
        try {
            pst = conn.prepareStatement(req);
            pst.setString(1, e.getNomEquipe());
            pst.setInt(2, e.getNbrEmployee());
            pst.setString(3, e.getNomTeqlead());
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de l'ajout de l'équipe : " + ex.getMessage());
        }
    }

    public void modifier(Equipe e) throws SQLException {
        String req = "UPDATE equipe SET nomEquipe=?, nbrEmployee=?, nomTeqlead=? WHERE id_equipe=?";
        try {
            pst = conn.prepareStatement(req);
            pst.setString(1, e.getNomEquipe());
            pst.setInt(2, e.getNbrEmployee());
            pst.setString(3, e.getNomTeqlead());
            pst.setInt(4, e.getIdEquipe());
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de la modification de l'équipe : " + ex.getMessage());
        }
    }

    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM equipe WHERE id_equipe=?";
        try {
            pst = conn.prepareStatement(req);
            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de la suppression de l'équipe : " + ex.getMessage());
        }
    }

    public List<Equipe> getAll(Equipe e) throws SQLException {
        List<Equipe> list = new ArrayList<>();
        String req = "SELECT * FROM equipe";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                list.add(new Equipe(
                    rs.getInt("id_equipe"),
                    rs.getString("nomEquipe"),
                    rs.getInt("nbrEmployee"),
                    rs.getString("nomTeqlead")
                ));
            }
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de la récupération des équipes : " + ex.getMessage());
        }
        return list;
    }

    public List<Equipe> searchTeams(String searchText) throws SQLException {
        List<Equipe> list = new ArrayList<>();
        String req = "SELECT * FROM equipe WHERE nomEquipe LIKE ? OR nomTeqlead LIKE ?";
        try {
            pst = conn.prepareStatement(req);
            pst.setString(1, "%" + searchText + "%");
            pst.setString(2, "%" + searchText + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Equipe(
                    rs.getInt("id_equipe"),
                    rs.getString("nomEquipe"),
                    rs.getInt("nbrEmployee"),
                    rs.getString("nomTeqlead")
                ));
            }
        } catch (SQLException ex) {
            throw new SQLException("Erreur lors de la recherche des équipes : " + ex.getMessage());
        }
        return list;
    }
}