package smartdoor.dao.impl;

import smartdoor.dao.AdminDao;
import smartdoor.models.Admin;
import smartdoor.utils.ConnectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class AdminDaoImpl implements AdminDao {
    @Override
    public void insert(Admin admin) {
        ConnectionDB conn = null;
        try {
            conn = new ConnectionDB();

            String sql = "INSERT INTO `admin` (`username`, `password`) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(
                    sql,
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, admin.getUsername());
            preparedStatement.setString(2, admin.getPassword());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                admin.setId(resultSet.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			if (conn != null)
				conn.close();
		}
    }

    @Override
    public void update(Admin admin) {
        ConnectionDB conn = null;
        try {
            conn = new ConnectionDB();

            String sql = "UPDATE admin SET " +
                            "username = ?, " +
                            "password = ? " +
                            "WHERE id = ? " +
                            "LIMIT 1 ";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, admin.getUsername());
            preparedStatement.setString(2, admin.getPassword());
            preparedStatement.setInt(3, admin.getId());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			if (conn != null)
				conn.close();
		}
    }

    @Override
    public void delete(Admin admin) {
        ConnectionDB conn = null;
        try {
            conn = new ConnectionDB();

            String sql = "DELETE FROM admin WHERE id = ? LIMIT 1";

            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, admin.getId());
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			if (conn != null)
				conn.close();
		}
    }

    @Override
    public Admin get(String username) {
        ConnectionDB conn = null;
        Admin admin = null;
        try {
            conn = new ConnectionDB();

            String sql = "SELECT * FROM admin where username = ? LIMIT 1";

            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                admin = new Admin();
                admin.setId(resultSet.getInt("id"));
                admin.setUsername(resultSet.getString("username"));
                admin.setHashedPassword(resultSet.getString("password"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
			if (conn != null)
				conn.close();

			return admin;
		}
    }
}
