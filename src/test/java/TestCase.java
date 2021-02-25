import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

import java.nio.file.Paths;

public class TestCase {
    static {
		Loader.load(opencv_java.class);
	}

    final private static String userDir = System.getProperty("user.dir", "");

    public static String getResourcesPath() {
        return Paths.get(userDir, "src", "test", "resources").toString();
    }

    public String getResource(String path) {
        return Paths.get(getResourcesPath(), path.split("/")).toString();
    }
}
