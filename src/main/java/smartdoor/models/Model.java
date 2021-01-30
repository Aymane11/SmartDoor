package smartdoor.models;

import smartdoor.support.ConnectionDB;

import java.sql.Connection;

public class Model {
    static Connection connection = ConnectionDB.getConnection();
}
