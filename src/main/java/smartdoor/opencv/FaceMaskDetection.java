package smartdoor.opencv;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import smartdoor.support.FileSystem;

import java.io.IOException;

public class FaceMaskDetection {
    private static ComputationGraph model = null;

    private FaceDetection faceDetection;

    public static ComputationGraph getModelInstance() throws InvalidKerasConfigurationException, IOException, UnsupportedKerasConfigurationException {
        if (model == null) {
            model = KerasModelImport.importKerasModelAndWeights(
                FileSystem.getModelResource("mask_detector.model")
            );
        }

        return model;
    }
    /**
     * Detect if the frame contains a face wearing a mask
     *
     * It returns :
     * -1 if no faces detected
     * 0 if at least one of the faces is not wearing a mask
     * 1 if all the faces are wearing a mask
     *
     * @param frame The image as a Mat
     * @return int
     * */
    public int detect(Mat frame) {
        try {

            // load the model
            model = FaceMaskDetection.getModelInstance();

            // Detect faces
            faceDetection = new FaceDetection(frame).detect();

            // All the detected faces
            Rect[] faces = faceDetection.getFacesArray();

            if (faces.length == 0) {
                return -1;
            }

            int counter = 0;
            for (Rect face : faces) {
                Imgproc.rectangle(frame, face.tl(), face.br(), new Scalar(0, 167, 229), 1);

                Mat subFace = frame.submat(face);

                Mat subFaceResized = new Mat();
                Imgproc.resize(subFace, subFaceResized, new Size(224, 224));

                INDArray faceINDArray = new NativeImageLoader().asMatrix(subFaceResized);
                new ImagePreProcessingScaler().	preProcess(faceINDArray);

                INDArray[] result = model.output(faceINDArray.reshape(1, 224, 224, 3));
                double mask = result[0].getDouble(0);
                double withoutMask = result[0].getDouble(1);

                if (mask > 0.8)
                    counter++;

                /*
                Scalar color = new Scalar(255, 0, 0);
                String label = "No Mask";
                if (mask > withoutMask) {
                    color = new Scalar(0, 255, 0);
                    label = "Mask";
                }

                Imgproc.putText(frame, label, new Point(face.x, face.y - 10), Imgproc.FONT_HERSHEY_SIMPLEX, 0.45, color, 2);
                Imgproc.rectangle(frame, face.tl(), face.br(), color, 2);
                 */

                System.out.println("WithMask = " + mask + " | withoutMask = " + withoutMask);
            }

            if (counter != 0 && counter == faceDetection.getFacesArray().length) {
                return 1;
            } else {
                return 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
    public Mat detect2(Mat frame) {
        try {
            // load our serialized face detector model from disk
            System.out.println("[INFO] loading face detector model...");
            System.out.println(FileSystem.getModelResource("face_detector/deploy.prototxt"));
            Net net = Dnn.readNet(
                    FileSystem.getModelResource("face_detector/deploy.prototxt"),
                    FileSystem.getModelResource("face_detector/res10_300x300_ssd_iter_140000.caffemodel")
            );
            // load the face mask detector model from disk
            System.out.println("[INFO] loading face mask detector model...");

            // load the model
            ComputationGraph model = KerasModelImport.importKerasModelAndWeights(
                    FileSystem.getModelResource("mask_detector.model")
            );

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
            System.out.println(detections.size().width);
            System.out.println(detections.get(0, 0));
            for (int i = 0; i < detections.size().width; i++) {
                // extract the confidence (i.e., probability) associated with
                // the detection
                //float confidence = (float) detections.get(0, 0, new int[] {i, 2});
                // filter out weak detections by ensuring the confidence is
                // greater than the minimum confidence

                //if (confidence > 0.5f) {
                    // compute the (x, y)-coordinates of the bounding box for
                    // the box
                    Mat box = new Mat();
                    System.out.println(detections.submat(0, 0, i, 3));

                    //detections.submat(0, 0, i, );
                    //box = detections[0, 0, i, 3:7] * np.array([w, h, w, h])
                    //(startX, startY, endX, endY) = box.astype("int")
                //}

             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frame;
    }

    public static void main(String[] args) {
        Loader.load(opencv_java.class);

        new FaceMaskDetection().detect2(new Mat());

    }
}