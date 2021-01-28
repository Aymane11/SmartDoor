package opencv;

import support.OpenCV;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;

public class FaceDetection {

    private Mat frame;

    private Mat grayFrame = new Mat();

    private CascadeClassifier faceCascade = new CascadeClassifier();

    private MatOfRect faces = new MatOfRect();

    private Rect[] facesArray;

    public FaceDetection(String input) {
        this(OpenCV.image2Mat(input));
    }

    public FaceDetection(Mat frame) {
        this.frame = frame;

        loadCascadeClassifier();

        generateGrayScale();
    }

    private void loadCascadeClassifier() {
        faceCascade.load(OpenCV.faceDetectionCascadePath());
    }

    private void generateGrayScale() {
        Imgproc.cvtColor(frame, grayFrame, Imgproc.COLOR_BGR2GRAY);
    }

    public FaceDetection detect() {
        // Equalize the frame histogram to improve the result (Normalization)
        Imgproc.equalizeHist(grayFrame, grayFrame);

        int height = grayFrame.rows();

        // Detect faces
        double scaleFactor = 1.3;
        int minNeighbors = 2;
        int flags = 0 | Objdetect.CASCADE_SCALE_IMAGE;
        // minimum face size (120x120)
        Size minSize = new Size(120, 120);
        // maximum face size (frame height x frame height (square))
        Size maxSize = new Size(height, height);
        faceCascade.detectMultiScale(grayFrame, faces, scaleFactor, minNeighbors, flags, minSize, maxSize);

        facesArray = faces.toArray();

        return this;
    }

    public FaceDetection animateFrame() {
        return animateFrame(new Scalar(0, 255, 0));
    }

    public FaceDetection animateFrame(Scalar color) {
        for(Rect face : facesArray) {
            Imgproc.rectangle(frame, face.tl(), face.br(), color, 2);
        }

        return this;
    }

    public FaceDetection saveFrame(String outputFilename) {
        OpenCV.mat2File(frame, outputFilename);

        return this;
    }

    public Rect[] getFacesArray() {
        return facesArray;
    }

    public Mat getFrame() {
        return frame;
    }

    public MatOfRect getFaces() {
        return faces;
    }
}
