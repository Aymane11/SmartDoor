package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.mindrot.jbcrypt.BCrypt;

import support.ConnectionDB;
import support.FileSystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import java.sql.*;

public class LoginController implements Initializable {
    @FXML private Label labelErrors;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Button loginBtn;
    @FXML private Hyperlink goBack;

    Connection con;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public LoginController() {
        con = ConnectionDB.getConnection();
    }

    public void changeView(MouseEvent event, String FXMLFile, boolean maximized){
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            if (maximized) stage.setMaximized(true);
            Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML(FXMLFile))));
            stage.close();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == loginBtn && loginAction()) { changeView(event,"Dashboard",false);}
        if (event.getSource() == goBack){ changeView(event,"Home",false);}
    }

    @FXML
    private boolean loginAction() {
        String email = userField.getText();
        String password = passField.getText();
        if(email.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Empty credentials");
            return false;
        } else {
            //Add user to database (beta)
//            try {
//                String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
//                preparedStatement = con.prepareStatement(sql);
//                preparedStatement.setString(1, email);
//                String hashed = BCrypt.hashpw(password,BCrypt.gensalt());
//                preparedStatement.setString(2, hashed);
//                int a = preparedStatement.executeUpdate();
//                System.out.println(a);
//            }
//            catch(Exception ex){
//                ex.printStackTrace();
//            }
            //change query to exact credentials
            String query = "SELECT password FROM admin WHERE username = ?";
            try {
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, email);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    setLblError(Color.TOMATO, "Enter Correct Email/Password");
                    return false;
                } else {
                    String candidate = resultSet.getString("password");
                    if (BCrypt.checkpw(password, candidate)){
                        setLblError(Color.GREEN, "Login Successful..Redirecting..");
                        return true;
                    } else {
                        setLblError(Color.TOMATO, "Enter Correct Email/Password");
                        return false;
                    }
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                return false;
            }
        }
    }

    private void setLblError(Color color, String text) {
        labelErrors.setTextFill(color);
        labelErrors.setText(text);
        System.out.println(text);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login Started");
    }
}