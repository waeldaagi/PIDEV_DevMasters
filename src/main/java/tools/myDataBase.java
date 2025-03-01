package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class myDataBase {
    public final String URL="jdbc:mysql://localhost:3306/pidev";
    public final String USER="root";
    public final String pwd ="";
    private Connection connection;
    private static myDataBase myDataBase;
    private myDataBase(){
        try {
            connection= DriverManager.getConnection(URL,USER,pwd);
            System.out.println("Connection etablie");
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }


    public static myDataBase getInstance() {
        if(myDataBase ==null)
            myDataBase=new myDataBase();
        return myDataBase;
    }

    public  Connection getConnection() {
        return connection;
    }
}
