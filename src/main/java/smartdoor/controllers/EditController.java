package smartdoor.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import smartdoor.dao.AdminDao;
import smartdoor.dao.impl.AdminDaoImpl;
import smartdoor.models.Admin;
import smartdoor.utils.FileSystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EditController implements Initializable {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField passwordConfirmField;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private Label labelErrors;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void confirmEdit(MouseEvent event) throws IOException {
        setErrors("");
        String username = usernameField.getText();
        String currentPassword = currentPasswordField.getText();
        String password = passwordField.getText();
        String passwordConfirm = passwordConfirmField.getText();

        // Check if any of the fields is empty
        if (username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            setErrors("Fields cannot be empty.");
            return;
        }

        // Check if password and confirmation are matching
        if (!password.equals(passwordConfirm)) {
            setErrors("Passwords are not matching.");
            return;
        }

        Admin currentAdmin = LoginController.currentAdmin;

        // TODO : Remove ability to edit username
        // Check if the username doesnt exist
        if (new AdminDaoImpl().get(username) != null) {
            setErrors("Username already taken.");
            return;
        }


        // Check if the current password is correct
        if (!BCrypt.checkpw(currentPassword, currentAdmin.getPassword())) {
            setErrors("Current password is incorrect.");
            return;
        }

        // Create new admin and remove the old one
        Admin newAdmin = new Admin();
        newAdmin.setUsername(username);
        newAdmin.setPassword(password);
        AdminDao newAdminDao = new AdminDaoImpl();
        newAdminDao.insert(newAdmin);
        newAdminDao.delete(currentAdmin);

        // Get new admin from db (test)
        System.out.println(new AdminDaoImpl().get(username));

        // Go back to home view

        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();

        Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML("Home"))));
        stage.setScene(scene);
        stage.show();
    }

    private void setErrors(String error) {
        labelErrors.setText(error);
    }
}
