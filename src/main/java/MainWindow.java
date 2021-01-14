import controllers.HomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import opencv.FaceDetection;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import support.FileSystem;

public class MainWindow extends Application {

    // mvn compile exec:java

    private Stage window;

    private FXMLLoader loader;
    
    private Parent root;

    @Override
    public void start(Stage primaryStage) throws Exception {
        setWindow(primaryStage);

        loader = new FXMLLoader(
                FileSystem.toURL(
                        FileSystem.getFXML("Home")
                )
        );

        Scene scene = new Scene((Parent) loader.load());

        window.setTitle("SmartDoor");
        /*window.initStyle(StageStyle.TRANSPARENT);*/
        window.setScene(scene);
        window.show();

        window.setOnCloseRequest(e -> {
            closeProgram();
        });
    }

    private void setWindow(Stage primaryStage) {
        window = primaryStage;
    }

    private void closeProgram() {
        System.out.println("Bye!");
        ((HomeController) loader.getController()).setClosed();
        window.close();
    }


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
