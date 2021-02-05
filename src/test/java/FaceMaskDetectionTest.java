import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import smartdoor.opencv.FaceMaskDetection;
import smartdoor.utils.OpenCV;

import static org.junit.jupiter.api.Assertions.*;

public class FaceMaskDetectionTest extends TestCase {
	private FaceMaskDetection faceMaskDetection = new FaceMaskDetection();

	{
		Loader.load(opencv_java.class);
	}

	@Test
	public void test_it_can_detect_if_the_person_wears_a_mask() {
		String imagePath = "girl-with-mask.jpeg";

		Mat image = OpenCV.image2Mat(getResource(imagePath));

		assertEquals(1, faceMaskDetection.detect(image));
	}

	@Test
	public void test_it_can_detect_if_the_person_doesnt_wear_a_mask() {
		String imagePath = "girl-without-mask.jpg";

		Mat image = OpenCV.image2Mat(getResource(imagePath));

		assertEquals(0, faceMaskDetection.detect(image));
	}

	@Test
	public void test_it_can_detect_if_there_is_no_face() {
		String imagePath = "no-face.jpg";

		Mat image = OpenCV.image2Mat(getResource(imagePath));

		assertEquals(-1, faceMaskDetection.detect(image));
	}
}
