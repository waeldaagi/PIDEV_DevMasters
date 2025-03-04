package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDatabase {
    private static MyDatabase instance;
    private final String URL ="jdbc:mysql://127.0.0.1:3306/resourcehumaine";
    private final String USERNAME ="root";
    private final String PASSWORD = "";
    private Connection cnx;

    private MyDatabase() {
        connect();
    }

    private void connect() {
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                System.out.println("✅ Connected to Database...");
            }
        } catch (SQLException e) {
            System.out.println("❌ Database Connection Error: " + e.getMessage());
        }
    }

    public static MyDatabase getInstance() {
        if (instance == null) {
            instance = new MyDatabase();
        }
        return instance;
    }

    public Connection getCnx() {
        try {
            if (cnx == null || cnx.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cnx;
    }
}
