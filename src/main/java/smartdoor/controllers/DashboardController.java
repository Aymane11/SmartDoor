package smartdoor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
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
    private Button logoutBtn;

    @FXML
    private Button addAdminBtn;

    @FXML
    private Button editBtn;

    private ObservableList<Session> data ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = FXCollections.observableArrayList(new SessionDaoImpl().getAll());

        TableColumn idCol = new TableColumn("ID");
        //idCol.setMinWidth(100);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn dateInCol = new TableColumn("Date In");
        //dateInCol.setMinWidth(200);
        dateInCol.setCellValueFactory(new PropertyValueFactory<>("date_in"));

        TableColumn fileNameCol = new TableColumn("File name");
        //fileNameCol.setMinWidth(200);
        fileNameCol.setCellValueFactory(new PropertyValueFactory<>("filenameLink"));

        sessionsTable.setItems(data);
        sessionsTable.getColumns().addAll(idCol, dateInCol, fileNameCol);
    }

    public void logout(MouseEvent event) {
        try {
            LoginController.currentAdmin = null;

            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.close();

            Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML("Home"))));
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addAdmin(MouseEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Add admin");

        Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML("AddAdmin"))));

        dialog.setScene(scene);
        dialog.show();
    }

    public void editDetails(MouseEvent event) throws IOException {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(stage);
        dialog.setTitle("Edit Details");

        Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML("Edit"))));

        dialog.setScene(scene);
        dialog.show();
    }

}