import org.junit.jupiter.api.Test;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import smartdoor.actions.SessionAction;
import smartdoor.dao.impl.SessionDaoImpl;
import smartdoor.utils.ConnectionDB;
import smartdoor.utils.FileSystem;
import smartdoor.utils.OpenCV;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class SessionActionTest extends TestCase {
    @Test
    public void test_it_can_saves_a_session_with_its_image() {
        int fileCount = new File(FileSystem.getDataResourcePath()).list().length;
        int sessionsCount = new SessionDaoImpl().getAll().size();

        Mat frame = OpenCV.image2Mat(getResource("pplmask1.jpg"));
        new SessionAction(frame).save();


        assertEquals( fileCount + 1, new File(FileSystem.getDataResourcePath()).list().length);
        assertEquals(sessionsCount + 1, new SessionDaoImpl().getAll().size());
    }
}
