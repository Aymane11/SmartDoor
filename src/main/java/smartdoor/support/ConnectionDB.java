package smartdoor.support;

import smartdoor.config.DatabaseConfig;

import java.sql.*;

public class ConnectionDB {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn != null) {
            return conn;
        }
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            try {
                Class.forName( "com.mysql.jdbc.Driver" );
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        try {
            conn = DriverManager.getConnection(
                    getDatabaseUrl(),
                    DatabaseConfig.get("user"),
                    DatabaseConfig.get("password")
            );

            return conn;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getDatabaseUrl() {
        /*


         */
        return String.valueOf(
                String.format("jdbc:mysql://%s:%s/%s?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC",
                        DatabaseConfig.get("host"),
                        DatabaseConfig.get("port"),
                        DatabaseConfig.get("dbname")
                )
        );
        // return "jdbc:mysql://" + DatabaseConfig.get("host")  + ":" + DatabaseConfig.get("port") + "/" + DatabaseConfig.get("dbname");
    }
}
