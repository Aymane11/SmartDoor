package smartdoor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import smartdoor.dao.impl.SessionDaoImpl;
import smartdoor.models.Session;
import smartdoor.utils.FileSystem;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    @FXML
    private TableView<Session> sessionsTable = new TableView<>();
    @FXML
    private Button addAdminBtn;
    @FXML
    private Button editBtn;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn dateInCol;
    @FXML
    private TableColumn fileNameCol;

    private ObservableList<Session> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = FXCollections.observableArrayList(new SessionDaoImpl().getAll());

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateInCol.setCellValueFactory(new PropertyValueFactory<>("date_in"));
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("filenameLink"));

        sessionsTable.setItems(data);
    }

    public void logout(MouseEvent event) {
        try {
            LoginController.currentAdmin = null;

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();

            Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML("Home"))));
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleClick(MouseEvent event) throws IOException {
        if (event.getSource() == addAdminBtn) {
            // Show add admin dialog
            showDialog("AddAdmin", "Add admin", event);
        }
        if (event.getSource() == editBtn) {
            // Show edit dialog
            showDialog("Edit", "Edit Details", event);
        }
    }

    public void showDialog(String fxmlFile, String title, MouseEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle(title);

        Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML(fxmlFile))));

        dialog.setScene(scene);
        dialog.show();
        dialog.resizableProperty().setValue(Boolean.FALSE);
    }

}