package smartdoor;

import smartdoor.controllers.HomeController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import smartdoor.support.FileSystem;

public class HomeWindow extends Application {
    protected Stage window;
    protected FXMLLoader loader;

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;

        window.setMinHeight(400);
        window.setMinWidth(400);
        loader = new FXMLLoader(FileSystem.toURL(FileSystem.getFXML("Home")));

        Scene scene = new Scene((Parent) loader.load());

        window.setTitle("SmartDoor");
        /*window.initStyle(StageStyle.TRANSPARENT);*/
        window.setScene(scene);
        window.show();
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                closeWindow();
            }
        });
    }

    protected void closeWindow() {
        if (loader.getController() instanceof HomeController) {
            ((HomeController) loader.getController()).setClosed();
        }
        window.close();
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        Loader.load(opencv_java.class);

        launch(args);
    }
}
