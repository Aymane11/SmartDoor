package opencv;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.tensorflow.TensorFlow;
import org.tensorflow.proto.framework.OpList;
import support.FileSystem;

public class MaskDetector {

    public void detect() {
        // load our serialized face detector model from disk
        System.out.println("[INFO] loading face detector model...");
        Net net = Dnn.readNet(
                FileSystem.getTensorflowResource("face_detector/deploy.prototxt"),
                FileSystem.getTensorflowResource("face_detector/res10_300x300_ssd_iter_140000.caffemodel")
        );
        // load the face mask detector model from disk
        System.out.println("[INFO] loading face mask detector model...");

        OpList model = TensorFlow.loadLibrary(FileSystem.getTensorflowResource("mask_detector.tflite"));

        // load the input image from disk, clone it, and grab the image spatial
        // dimensions
        Mat image = Imgcodecs.imread(FileSystem.getOpenCVResource("images/girl-with-mask.png"));
        int w = image.cols(), h = image.rows();

        // Construct a blob from the image
        Mat blob = Dnn.blobFromImage(
                image,
                1.0,
                new Size(300, 300),
                new Scalar(104.0, 177.0, 123.0)
        );

        // pass the blob through the network and obtain the face detections
        System.out.println("[INFO] computing face detections...");
        net.setInput(blob);
        Mat detections = net.forward();
        for (int i = 0; i < detections.cols(); i++) {
            // extract the confidence (i.e., probability) associated with
            // the detection
            float confidence = (float) detections.get(0, 0, new int[] {i, 2});
            // filter out weak detections by ensuring the confidence is
            // greater than the minimum confidence
            if (confidence > 0.5f) {
                System.out.println(detections);
                // compute the (x, y)-coordinates of the bounding box for
                // the box

                //detections.submat(0, 0, i, );
                //box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
                //(startX, startY, endX, endY) = box.astype("int")
            }
        }

    }

    public static void main(String[] args) {
        Loader.load(opencv_java.class);
        new MaskDetector().detect();
    }
}
