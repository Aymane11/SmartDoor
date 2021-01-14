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

        // compute minimum face size (1% of the frame height, in our case)
        int absoluteFaceSize = 0;
        int height = grayFrame.rows();
        if (Math.round(height * 0.2f) > 0) {
            absoluteFaceSize = Math.round(height * 0.01f);
        }

        // Detect faces
        double scaleFactor = 1.1;
        int minNeighbors = 2;
        int flags = 0 | Objdetect.CASCADE_SCALE_IMAGE;
        Size minSize = new Size(absoluteFaceSize, absoluteFaceSize);
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
