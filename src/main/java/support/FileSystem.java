package support;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

final public class FileSystem {

    final private static String userDir = System.getProperty("user.dir", "");

    public static String getResourcesPath() {
        return Paths.get(userDir, "src", "main", "resources").toString();
    }

    public static String getFXMLPath() {
        return Paths.get(getResourcesPath(), "fxml").toString();
    }

    public static String getCSSPath() {
        return Paths.get(getResourcesPath(), "css").toString();
    }

    public static String getFontsPath() {
        return Paths.get(getResourcesPath(), "fonts").toString();
    }

    public static String getImagesPath() {
        return Paths.get(getResourcesPath(), "images").toString();
    }

    public static String getIconsPath() {
        return Paths.get(getResourcesPath(), "icons").toString();
    }

    public static String getOpenCVResourcesPath() {
        return Paths.get(getResourcesPath(), "opencv-resources").toString();
    }

    public static String getOpenCVResource(String path) {
        return Paths.get(getOpenCVResourcesPath(), path.split("/")).toString();
    }

    public static String getTensorflowPath() {
        return Paths.get(getResourcesPath(), "tensorflow").toString();
    }

    public static String getTensorflowResource(String path) {
        return Paths.get(getTensorflowPath(), path.split("/")).toString();
    }

    public static String getFXML(String file) {
        String extension = ".fxml";

        if ( ! file.endsWith(extension) ) {
            file += extension;
        }

        return Paths.get(getFXMLPath(), file).toString();
    }

    public static Path toPath(String path) {
        return Paths.get(path);
    }

    public static URI toUri(String path) {
        return Paths.get(path).toUri();
    }

    public static URL toURL(String path) {
        try {
            return Paths.get(path).toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFileExtension(String name) {
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }
}
