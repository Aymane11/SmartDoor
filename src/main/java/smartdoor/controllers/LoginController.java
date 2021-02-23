package smartdoor.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import org.mindrot.jbcrypt.BCrypt;

import smartdoor.dao.AdminDao;
import smartdoor.dao.impl.AdminDaoImpl;
import smartdoor.models.Admin;
import smartdoor.utils.FileSystem;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    static Admin currentAdmin = null;

    @FXML
    private Label labelErrors;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBtn;
    @FXML
    private Hyperlink goBack;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Login Started!");
    }

    public static Admin login(String username, String password) {
        if (username.isEmpty() || password.isEmpty())
            return null;

        AdminDao adminDao = new AdminDaoImpl();
        Admin admin = adminDao.get(username);

        if (admin == null || !BCrypt.checkpw(password, admin.getPassword()))
            return null;

        return admin;
    }

    /**
     * Change the UI view
     *
     * @param event  the MouseEvent event
     * @param FXMLFile the FXML filename to load
     * @param maximize if set to false it disables maximizing the window
     */
    public void changeView(MouseEvent event, String FXMLFile, boolean maximize) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML(FXMLFile))));
            stage.setScene(scene);
            stage.show();
            if (!maximize)
                stage.resizableProperty().setValue(Boolean.FALSE);
            else
                stage.resizableProperty().setValue(Boolean.TRUE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleClick(MouseEvent event) {
        if (event.getSource() == loginBtn) {
            // Check if the credentials are correct then go to dashboard
            if (loginAction()) {
                changeView(event, "Dashboard", false);
            }
        }

        // Go back to user view
        if (event.getSource() == goBack) {
            changeView(event, "Home", true);
        }
    }

    @FXML
    private boolean loginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            setLblError("Username and password cannot be empty.");
            return false;
        } else {
            Admin admin = login(username, password);
            if (admin == null) {
                setLblError("Wrong email or password.");
                return false;
            } else {
                currentAdmin = admin;
                return true;
            }
        }
    }

    private void setLblError(String text) {
        labelErrors.setText(text);
    }
}