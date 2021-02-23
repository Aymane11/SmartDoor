package smartdoor;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

import smartdoor.utils.FileSystem;
import smartdoor.controllers.HomeController;

import java.io.FileInputStream;

public class HomeWindow extends Application {
    protected Stage primaryStage;
    protected FXMLLoader loader;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;

        primaryStage.setMinHeight(500);
        primaryStage.setMinWidth(500);
        loader = new FXMLLoader(FileSystem.toURL(FileSystem.getFXML("Home")));

        Scene scene = new Scene((Parent) loader.load());

        primaryStage.setTitle("SmartDoor");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(new FileInputStream(FileSystem.getImageResource("icons/icon.png"))));
        primaryStage.show();
        primaryStage.setOnCloseRequest(event -> closeWindow());
    }

    protected void closeWindow() {
        if (loader.getController() instanceof HomeController) {
            ((HomeController) loader.getController()).setClosed();
        }

        primaryStage.close();
        System.exit(0);
    }

    public static void main(String[] args) {
        Loader.load(opencv_java.class);

        launch(args);
    }
}
