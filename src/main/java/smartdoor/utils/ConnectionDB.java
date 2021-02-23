package smartdoor.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import smartdoor.config.DatabaseConfig;

public class ConnectionDB {
    private Connection conn;

    public ConnectionDB() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            conn = DriverManager.getConnection(
                    getDatabaseUrl(),
                    DatabaseConfig.get("user"),
                    DatabaseConfig.get("password")
            );

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public Connection getConnection() {
        return getConn();
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String getDatabaseUrl() {
        return String.valueOf(
                String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                        DatabaseConfig.get("host"),
                        DatabaseConfig.get("port"),
                        DatabaseConfig.get("dbname")
                )
        );
    }
}
