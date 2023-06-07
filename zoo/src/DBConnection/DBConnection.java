package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection con = null;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/sys";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "123456";
    public DBConnection() {}

    static {
        try {
            con = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    public static Connection getDbConnection() {
        return con;
    }
}
