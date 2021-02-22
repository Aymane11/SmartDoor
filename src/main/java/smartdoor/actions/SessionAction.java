package smartdoor.actions;

import org.opencv.core.Mat;
import smartdoor.dao.impl.SessionDaoImpl;
import smartdoor.models.Session;
import smartdoor.utils.FileSystem;
import smartdoor.utils.OpenCV;

import java.util.UUID;

public class SessionAction {
    private final Mat frame;

    public SessionAction(Mat frame) {
        this.frame = frame;
    }

    public void save() {
        // Save the frame
        String filename = generateFilename();
        OpenCV.mat2File(frame, FileSystem.getDataResource(filename));

        Session  session = new Session();
        session.setDate_in();
        session.setFilename(filename);

        new SessionDaoImpl().create(session);
    }

    public String generateFilename() {
        return UUID.randomUUID().toString() + ".jpg";
    }
}
