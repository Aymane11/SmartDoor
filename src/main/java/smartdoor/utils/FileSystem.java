package smartdoor.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;

final public class FileSystem {

    final private static String userDir = System.getProperty("user.dir", "");

    public static String getResourcesPath() {
        return Paths.get(userDir, "src", "main", "resources").toString();
    }

    public static String getFXMLPath() {
        return Paths.get(getResourcesPath(), "fxml").toString();
    }

    public static String getImagesPath() {
        return Paths.get(getResourcesPath(), "images").toString();
    }

    public static String getModelResourcePath() {
        return Paths.get(getResourcesPath(), "model").toString();
    }

    public static String getDataResourcePath() {
        return Paths.get(getResourcesPath(), "data").toString();
    }

    public static String getModelResource(String path) { return Paths.get(getModelResourcePath(), path.split("/")).toString(); }

    public static String getFXML(String file) {
        String extension = ".fxml";

        if (!file.endsWith(extension)) {
            file += extension;
        }

        return Paths.get(getFXMLPath(), file).toString();
    }

    public static String getImageResource(String path) { return Paths.get(getImagesPath(), path.split("/")).toString(); }

    public static URL toURL(String path) {
        try {
            return Paths.get(path).toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDataResource(String path) { return Paths.get(getDataResourcePath(), path.split("/")).toString(); }
}
