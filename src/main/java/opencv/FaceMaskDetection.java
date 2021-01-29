package opencv;

import org.datavec.image.loader.NativeImageLoader;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.ImagePreProcessingScaler;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import support.FileSystem;

public class FaceMaskDetection {
    public Mat detect(Mat frame) {
        try {

            // load the model
            ComputationGraph model = KerasModelImport.importKerasModelAndWeights(
                    FileSystem.getModelResource("mask_detector.model")
            );

            FaceDetection faceDetection = new FaceDetection(frame).detect();

            for (Rect face : faceDetection.getFacesArray()) {
                Mat subFace = frame.submat(face);

                Mat subFaceRGB = subFace;
                //Imgproc.cvtColor(subFace, subFaceRGB, Imgproc.COLOR_BGR2RGB);

                Mat subFaceResized = new Mat();
                Imgproc.resize(subFaceRGB, subFaceResized, new Size(224, 224));

                INDArray faceINDArray = new NativeImageLoader().asMatrix(subFaceResized);
                new ImagePreProcessingScaler().	preProcess(faceINDArray);

                INDArray[] result = model.output(faceINDArray.reshape(1, 224, 224, 3));
                double mask = result[0].getDouble(0);
                double withoutMask = result[0].getDouble(1);

                Scalar color = new Scalar(255, 0, 0);
                String label = "No Mask";
                if (mask > withoutMask) {
                    color = new Scalar(0, 255, 0);
                    label = "Mask";
                }

                Imgproc.putText(frame, label, new Point(face.x, face.y - 10), Imgproc.FONT_HERSHEY_SIMPLEX, 0.45, color, 2);
                Imgproc.rectangle(frame, face.tl(), face.br(), color, 2);
                // System.out.println( mask > withoutMask ? "Mask" : "No Mask");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return frame;
    }
}
