package smartdoor.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import smartdoor.actions.SessionAction;
import smartdoor.dao.impl.SessionDAOImpl;
import smartdoor.models.Session;
import smartdoor.opencv.FaceMaskDetection;
import smartdoor.utils.OpenCV;
import smartdoor.utils.FileSystem;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardController implements Initializable {
    @FXML
    private TableView<Session> sessionsTable = new TableView<>();
    private ObservableList<Session> data ;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data=        FXCollections.observableArrayList(new SessionDAOImpl().getAll());
        TableColumn idCol = new TableColumn("ID");
        idCol.setMinWidth(100);
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));

        TableColumn dateInCol = new TableColumn("Date In");
        dateInCol.setMinWidth(100);
        dateInCol.setCellValueFactory(
                new PropertyValueFactory<>("date_in"));

        TableColumn fileNameCol = new TableColumn("File name");
        fileNameCol.setMinWidth(200);
        fileNameCol.setCellValueFactory(
                new PropertyValueFactory<>("filename"));

        sessionsTable.setItems(data);
        sessionsTable.getColumns().addAll(idCol, dateInCol, fileNameCol);
    }
}