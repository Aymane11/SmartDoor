package smartdoor.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import smartdoor.opencv.FaceMaskDetection;
import smartdoor.support.OpenCV;
import smartdoor.support.FileSystem;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeController implements Initializable {
    @FXML
    private BorderPane alertBackground;

    @FXML
    private Label maskMessage;

    @FXML
    private ImageView webCam;

    @FXML
    private GridPane bigContainer;

    @FXML
    private BorderPane webCamContainer;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;

    // the OpenCV object that realizes the video capture
    private final VideoCapture capture = new VideoCapture();

    // the id of the camera to be used
    private static final int cameraId = 0;

    private int counter = 0;

    /**
     * The action triggered by pushing the button on the GUI
     */
    @FXML
    protected void startCamera() {
        // start the video capture
        this.capture.open(cameraId);

        // is the video stream available?
        if (this.capture.isOpened()) {
            // grab a frame every 33 ms (30 frames/sec)
            Runnable frameGrabber = () -> {
                    // effectively grab and process a single frame
                    Mat frame = grabFrame();

                    try {
                        this.counter += 33;
                        if (this.counter == 33*100) {
                            this.counter = 0;

                            this.updateAlertMsg(new FaceMaskDetection().detect(frame));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // convert and show the frame
                    Image imageToShow = OpenCV.mat2Image(frame);
                    this.updateImageView(webCam, imageToShow);
            };

            this.timer = Executors.newSingleThreadScheduledExecutor();
            this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
        } else {
            // log the error
            System.err.println("Impossible to open the camera connection...");
        }
    }

    /**
     * Get a frame from the opened video stream (if any)
     *
     * @return the {@link Mat} to show
     */
    private Mat grabFrame() {
        // init everything
        Mat frame = new Mat();

        // check if the capture is open
        if (this.capture.isOpened()) {
            try {
                // read the current frame
                this.capture.read(frame);

            } catch (Exception e) {
                // log the error
                System.err.println("Exception during the image elaboration: " + e);
            }
        }

        return frame;
    }

    /**
     * Stop the acquisition from the camera and release all the resources
     */
    private void stopAcquisition() {
        if (this.timer != null && !this.timer.isShutdown()) {
            try {
                // stop the timer
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // log any exception
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
        }

        if (this.capture.isOpened()) {
            // release the camera
            this.capture.release();
        }
    }

    /**
     * Update the {@link ImageView} in the JavaFX main thread
     *
     * @param view  the {@link ImageView} to update
     * @param image the {@link Image} to show
     */
    private void updateImageView(ImageView view, Image image) {
        OpenCV.onFXThread(view.imageProperty(), image);
    }

    /**
     * On application close, stop the acquisition from the camera
     */
    public void setClosed() {
        this.stopAcquisition();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Home started!");
        startCamera();

        bigContainer.widthProperty().addListener((obs, oldVal, newVal) ->
                webCam.setFitWidth(webCamContainer.getWidth() * 0.8));

        bigContainer.heightProperty().addListener((obs, oldVal, newVal) ->
                webCam.setFitHeight(webCamContainer.getHeight() * 0.8));
    }

    @FXML
    public void gotoLogin(MouseEvent event) {
        try {
            this.setClosed();
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            //stage.setMaximized(true);
            Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML("Login"))));
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }

    public void updateAlertMsg(boolean success) {
        if (success) {
            this.setClosed();
            this.updateImageView(webCam, new Image(String.valueOf(FileSystem.toURL(FileSystem.getImageResource("open-door.jpg")))));

            ObservableList<String> styleClass = alertBackground.getStyleClass();
            styleClass.add("success");
            styleClass.remove("denied");

            this.maskMessage.setText("Access granted");
        } else {
            ObservableList<String> styleClass = alertBackground.getStyleClass();
            styleClass.remove("success");
            styleClass.add("denied");
            this.counter = 0;

            this.maskMessage.setText("Access denied! Wear a mask.");
        }
    }
}
