import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;
import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import smartdoor.opencv.FaceMaskDetection;
import smartdoor.utils.OpenCV;

import static org.junit.jupiter.api.Assertions.*;

public class FaceMaskDetectionTest extends TestCase {
	private FaceMaskDetection faceMaskDetection = new FaceMaskDetection();

	{
		Loader.load(opencv_java.class);
	}

	@Test
	public void test_it_detects_person_with_a_mask() {
		assertEquals(1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("mask2.jpg"))));
		assertEquals(1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("mask3.jpg"))));
		assertEquals(1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("mask4.jpg"))));
	}

	@Test
	public void test_it_detects_the_person_with_no_mask() {
		assertEquals(0, faceMaskDetection.detect(OpenCV.image2Mat(getResource("nomask1.jpg"))));
		assertEquals(0, faceMaskDetection.detect(OpenCV.image2Mat(getResource("nomask2.jpg"))));
		assertEquals(0, faceMaskDetection.detect(OpenCV.image2Mat(getResource("nomask3.jpg"))));
		assertEquals(0, faceMaskDetection.detect(OpenCV.image2Mat(getResource("nomask4.jpg"))));
		assertEquals(0, faceMaskDetection.detect(OpenCV.image2Mat(getResource("nomask5.jpg"))));
	}

	@Test
	public void test_it_can_detect_if_there_is_no_face() {
		assertEquals(-1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("noface1.jpg"))));
		assertEquals(-1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("noface2.jpg"))));
		assertEquals(-1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("noface3.jpg"))));
		assertEquals(-1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("noface4.jpg"))));
	}

	@Test
	public void test_it_detects_people_with_maks() {
		assertEquals(1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("pplmask1.jpg"))));
		assertEquals(1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("pplmask2.jpg"))));
		assertEquals(1, faceMaskDetection.detect(OpenCV.image2Mat(getResource("pplmask3.jpg"))));
//
	}
}
