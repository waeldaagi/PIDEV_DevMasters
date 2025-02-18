package services;
import java.sql.SQLException;
import java.util.List;
public interface IServices<T> {
    void ajouter(T t) throws SQLException;
    void supprimer(int id) throws SQLException;
    void modifier(int id, String field1, String field2 , String field3) throws SQLException;
    List<T> recuperer() throws SQLException;

}
