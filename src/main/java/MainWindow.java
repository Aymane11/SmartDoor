import controllers.HomeController;
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
import support.FileSystem;

import java.io.IOException;

public class MainWindow extends Application {

    // mvn compile exec:java

    private Stage window;

    private FXMLLoader loader;
    
    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
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
                System.out.println("Bye!");
                ((HomeController) loader.getController()).setClosed();
                Platform.exit();
                System.exit(0);
            }
        });
    }

//    public void changeView(String FXMLFile,boolean maximized){
//        try {
//            window.setMaximized(maximized);
//            window.close();
//            Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML(FXMLFile))));
//            window.setScene(scene);
//            window.show();
//        } catch (IOException ex) {
//            System.err.println(ex.getMessage());
//        }
//    }

    public static void main(String[] args) {
        Loader.load(opencv_java.class);

        /*new FaceDetection(FileSystem.getOpenCVResource("images/girl-with-mask.png"))
                .detect()
                .animateFrame()
                .saveFrame(FileSystem.getOpenCVResource("images/mask.png"));
         */

        launch(args);
    }
}
