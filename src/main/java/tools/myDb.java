package tools;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class myDb {
    public final String URL ="jdbc:mysql://localhost:3306/pi";
    public final String USER ="root";
    public final String PWD ="";
    private Connection cnx;
    private static myDb myDb ;
    private  myDb(){
    try{
        cnx = DriverManager.getConnection(URL,USER,PWD);
        System.out.println("Connected to database");
    }
    catch (SQLException e){
        System.err.println(e.getMessage());
    }
    }
    public static myDb getmyDb()
    {
        if(myDb == null)
            myDb = new myDb();
        return myDb;
    }

    public Connection getCnx() {
        return cnx;
    }
}
