package smartdoor.controllers;

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

import smartdoor.models.Admin;
import smartdoor.support.ConnectionDB;
import smartdoor.support.FileSystem;

import java.net.URL;
import java.util.ResourceBundle;

import java.sql.*;

public class LoginController implements Initializable {
    @FXML
    private Label labelErrors;

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passField;

    @FXML
    private Button loginBtn;

    @FXML
    private Hyperlink goBack;

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

            if (maximized) {
                stage.setMaximized(true);
            }
            // stage.close();

            Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML(FXMLFile))));
            stage.setScene(scene);
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == loginBtn) {
            // Check if the credentiels are correct!
            if (loginAction() == true) {
                changeView(event,"Dashboard",false);
            }
        }

        if (event.getSource() == goBack){
            changeView(event,"Home",false);
        }
    }

    @FXML
    private boolean loginAction() {
        String username = userField.getText();
        String password = passField.getText();

        if(username.isEmpty() || password.isEmpty()) {
            setLblError("Username and password cannot be empty.");
            return false;
        } else {
            //Add user to database (beta)
            /*
            Admin newAdmin = new Admin();
            newAdmin.setUsername("othmane");
            newAdmin.setPassword("password");
            newAdmin.insert();
             */

            Admin admin = Admin.get(username);
            System.out.println(admin);
            if ( admin == null || ! BCrypt.checkpw(password, admin.getPassword())) {
                setLblError("Wrong email or password.");
                return false;
            } else {
                return true;
            }
        }
    }

    private void setLblError(String text) {
        labelErrors.setText(text);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login Started!");
    }
}