package smartdoor.controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;
import smartdoor.actions.SessionAction;
import smartdoor.opencv.FaceMaskDetection;
import smartdoor.utils.FileSystem;
import smartdoor.utils.OpenCV;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HomeController implements Initializable {
    // the id of the camera to be used
    private static final int cameraId = 0;
    // the OpenCV object that realizes the video capture
    private final VideoCapture capture = new VideoCapture();
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

    private int tries = 0;

    private final int MAX_TRIES = 12;

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
                //frame = new FaceMaskDetection().detect(frame);

                // Check if the mask detected or not
                int maskDetectedValue = new FaceMaskDetection().detect(frame);

                // The user is wearing a mask
                if (maskDetectedValue == 1) {
                    maskDetectedValue = 2;
                    tries++;
                    // First let's check more than one time if the user is wearing a mask
                    if (tries >= MAX_TRIES) {
                        // Save the session, because the user wears a mask
                        SessionAction sessionAction = new SessionAction(frame);
                        sessionAction.save();

                        tries = 0;
                        // Block the camera for a while. Then re-open it
                        frame = OpenCV.image2Mat(FileSystem.getImageResource("blackbg.jpg"));
                        maskDetectedValue = 1;
                    }

                } else {
                    tries = 0;
                }

                // Update the info bar
                this.updateAlertMsg(maskDetectedValue);

                // convert and show the frame
                Image imageToShow = OpenCV.mat2Image(frame);
                this.updateImageView(webCam, imageToShow);
                if(maskDetectedValue==1){
                    try {
                        Thread.sleep(1000);
                        this.updateAlertMsg(3);
                        Thread.sleep(2000);
                        startCamera();
                    } catch (Exception e) {
                    }
                }
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

    public void updateAlertMsg(int maskDetectedValue) {
        Platform.runLater( () -> {
            ObservableList<String> styleClass = alertBackground.getStyleClass();

            if (maskDetectedValue == 1) {
                this.setClosed();

                styleClass.removeAll("denied", "verifying");
                styleClass.add("success");
                this.maskMessage.setText("Access granted");

            } else if (maskDetectedValue == 0) {

                styleClass.removeAll("verifying", "success");
                styleClass.add("denied");
                maskMessage.setText("Access denied! Wear a mask.");

            } else if (maskDetectedValue == -1) {

                styleClass.removeAll("denied", "success");
                styleClass.add("verifying");
                this.maskMessage.setText("Waiting for people.");

            } else if (maskDetectedValue == 2) {

                styleClass.removeAll("denied", "success");
                styleClass.add("verifying");

                this.maskMessage.setText("Processing hold on " + (int) tries*100/MAX_TRIES + "%");

            } else if (maskDetectedValue == 3) {
                styleClass.removeAll("denied", "verifying", "success");
                // styleClass.add("success");

                this.maskMessage.setText("The door is open.");
            }
        });
    }
}
