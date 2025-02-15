package servise;

import models.Equipe;

import java.sql.SQLException;
import java.util.List;

public interface Iservise<T> {
    void ajouter(T e) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(T e , String nom);
    public List<T> getAll(T e) throws SQLException;
}
