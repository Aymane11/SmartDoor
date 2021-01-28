package support;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class ConnectionDB {

    private static Connection conn;

    public static Connection getConnection() {
        try {
            Dotenv dotenv = Dotenv.configure().load();
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + dotenv.get("DB_HOST") + "/" + dotenv.get("DB_NAME"), dotenv.get("DB_USER"), dotenv.get("DB_PW"));
            System.out.println("Connected Successfully");
            return conn;
        } catch (Exception e) {
            System.err.println("ConnectionUtil : " + e.getMessage());
            return null;
        }
    }
}
