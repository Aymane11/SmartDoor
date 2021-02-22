package smartdoor.dao.impl;

import smartdoor.dao.SessionDao;
import smartdoor.models.Session;
import smartdoor.utils.ConnectionDB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SessionDaoImpl implements SessionDao {

    @Override
    public List<Session> getAll() {
        List<Session> sessions = new ArrayList<>();
        ConnectionDB conn = null;
        try {
            conn = new ConnectionDB();

            String query = "SELECT * FROM session";

            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Session session = new Session();
                session.setId(resultSet.getInt("id"));
                session.setDate_in(resultSet.getTimestamp("date_in"));
                session.setFilename(resultSet.getString("file"));
                sessions.add(session);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.close();
        }

        return sessions;
    }

    @Override
    public Session get(String filename) {
        Session session = null;
        ConnectionDB conn = new ConnectionDB();
        try {
            String query = "SELECT * FROM session WHERE file = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(query);
            preparedStatement.setString(1, filename);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                session = new Session();
                session.setId(resultSet.getInt("id"));
                session.setDate_in(resultSet.getTimestamp("date_in"));
                session.setFilename(resultSet.getString("file"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return session;
    }

    @Override
    public Session get(int id) {
        Session session = null;
        ConnectionDB conn = new ConnectionDB();
        try {
            String query = "SELECT * FROM session WHERE id = ?";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                session = new Session();
                session.setId(resultSet.getInt("id"));
                session.setDate_in(resultSet.getTimestamp("date_in"));
                session.setFilename(resultSet.getString("file"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }

        return session;
    }

    @Override
    public void create(Session session) {
        ConnectionDB conn = null;
        try {
            conn = new ConnectionDB();

            String query = "INSERT INTO `session` (`date_in`, `file`) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.getConnection().prepareStatement(
                    query,
                    Statement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setTimestamp(1, session.getDate_inTimestamp());
            preparedStatement.setString(2, session.getFilename());
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                session.setId(resultSet.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null)
                conn.close();
        }
    }
}
