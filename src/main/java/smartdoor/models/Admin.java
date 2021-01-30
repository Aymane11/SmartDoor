package smartdoor.models;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Admin extends Model {
    private int id;
    private String username;
    private String password;

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static Admin get(String username) {
        Admin result = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM admin where username IN (?)"
            );
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result.setId(resultSet.getInt("id"));
                result.setUsername(resultSet.getString("username"));
                result.setPassword(resultSet.getString("password"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Admin insert() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO `admin` (`username`, `password`) VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            //preparedStatement.setInt(1, id);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                setId(resultSet.getInt(1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public Admin update() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE admin SET " +
                            "username = ?, " +
                            "password = ? " +
                            "WHERE id = ? " +
                            "LIMIT 1"
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public void delete() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM admin WHERE id = ? LIMIT 1"
            );
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
