package servise;

import models.Equipe;
import tools.myDb;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipeServise implements Iservise<Equipe>{
    private Connection cnx;
    public EquipeServise (){
        cnx = myDb.getmyDb().getCnx();
    }
    @Override
    public void ajouter(Equipe e) throws SQLException {
//        String sql = "INSERT INTO equipe (nom_equipe, nbr_employee, nom_teqlead)" +
//                "VALUES ('"+e.getNomEquipe()+"','"+e.getNbrEmployee()+"','"+e.getNomTeqlead()+"')";
//        Statement st = cnx.createStatement();
//        st.executeUpdate(sql);
//        System.out.println("Equipe ajoutée");
        String sql = "INSERT INTO equipe (nom_equipe, nbr_employee, nom_teqlead) VALUES (?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1, e.getNomEquipe());
        ps.setInt(2, e.getNbrEmployee());
        ps.setString(3,e.getNomTeqlead());
        ps.executeUpdate();
        System.out.println("Equipe ajoutée");

    }

    @Override
    public void supprimer(int id) throws SQLException {
        String sql = "DELETE FROM equipe WHERE id_equipe = ?";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setInt(1,id);
        ps.executeUpdate();
        System.out.println("Equipe suprimée");
    }

    @Override
    public void modifier(Equipe e, int id) throws SQLException {
        String sql = "update equipe set nom_equipe = ?, nbr_employee = ?, nom_teqlead = ? where id_equipe = ? ";
        PreparedStatement ps = cnx.prepareStatement(sql);
        ps.setString(1,e.getNomEquipe());
        ps.setInt(2,e.getNbrEmployee());
        ps.setString(3,e.getNomTeqlead());
        ps.executeUpdate();
        System.out.println("Equipe modifier");
    }

    @Override
    public List<Equipe> getAll(Equipe e) throws SQLException {
        String sql = "SELECT * FROM equipe";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(sql);
        ArrayList<Equipe> equipes = new ArrayList<>();
        while (rs.next()) {
            e = new Equipe();
            e.setIdEquipe(rs.getInt("id_equipe"));
            e.setNomEquipe(rs.getString("nom_equipe"));
            e.setNbrEmployee(rs.getInt("nbr_employee"));
            e.setNomTeqlead(rs.getString("nom_teqlead"));
            equipes.add(e);
        }

        return equipes;
    }
}
