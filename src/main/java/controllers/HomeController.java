package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import opencv.FaceMaskDetection;
import support.OpenCV;
import support.FileSystem;

import opencv.FaceDetection;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeController implements Initializable {

    @FXML
    private ImageView webCam;

    @FXML
    private GridPane bigContainer;

    @FXML
    private BorderPane webCamContainer;

    private double widthRatio = 1;
    private double heightRatio = 1;

    // a timer for acquiring the video stream
    private ScheduledExecutorService timer;

    // the OpenCV object that realizes the video capture
    private VideoCapture capture = new VideoCapture();

    // a flag to change the button behavior
    private boolean cameraActive = false;

    // the id of the camera to be used
    private static int cameraId = 0;

    /**
     * The action triggered by pushing the button on the GUI
     */
    @FXML
    protected void startCamera() {
        if (true) {
            // start the video capture
            this.capture.open(cameraId);

            // is the video stream available?
            if (this.capture.isOpened()) {
                this.cameraActive = true;

                // grab a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = new Runnable() {

                    @Override
                    public void run() {
                        // effectively grab and process a single frame
                        Mat frame = grabFrame();
                        frame = new FaceMaskDetection().detect(frame);
                        //frame = new FaceDetection(frame).detect().animateFrame().getFrame();

                        // convert and show the frame
                        Image imageToShow = OpenCV.mat2Image(frame);
                        updateImageView(webCam, imageToShow);
                    }
                };

                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
            } else {
                // log the error
                System.err.println("Impossible to open the camera connection...");
            }
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

                // if the frame is not empty, process it
                /*if (!frame.empty()) {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY);
                }*/

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
        System.out.println("Programme started!");
        startCamera();
        bigContainer.widthProperty().addListener((obs, oldVal, newVal) -> {
            webCam.setFitWidth(webCamContainer.getWidth() * 0.8);
        });

        bigContainer.heightProperty().addListener((obs, oldVal, newVal) -> {
            webCam.setFitHeight(webCamContainer.getHeight() * 0.8);
        });
    }

    @FXML
    public void gotoLogin(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(FileSystem.toURL(FileSystem.getFXML("Home")));
            Parent root = loader.load();
            HomeController home_controller = loader.getController();
            home_controller.setClosed();
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            //stage.setMaximized(true);
            stage.close();
            Scene scene = new Scene(FXMLLoader.load(FileSystem.toURL(FileSystem.getFXML("Login"))));
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}
