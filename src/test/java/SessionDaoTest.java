import org.junit.jupiter.api.*;
import smartdoor.dao.impl.SessionDaoImpl;
import smartdoor.models.Session;
import smartdoor.utils.ConnectionDB;

import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionDaoTest {
    Session session = null;

    @Test
    @Order(1)
    public void insert_a_session_to_db() {
        session = new Session();
        session.setFilename();
        session.setDate_in();
        new SessionDaoImpl().create(session);

        assertTrue(new SessionDaoImpl().get(session.getFilename()) != null);
    }

    @Test
    @Order(2)
    public void test_it_gets_all_the_sessions() {
        assertTrue(new SessionDaoImpl().getAll().size() > 0);
    }

    @AfterAll
    public void clearTable() {
        ConnectionDB con = new ConnectionDB();

        try {
            String query = "DELETE FROM session WHERE file = ?";
            PreparedStatement preparedStatement = con.getConnection().prepareStatement(
                    query
            );
            preparedStatement.setString(1, session.getFilename());

            preparedStatement.executeUpdate();
        } catch (Exception e) {
            //
        } finally {
            con.close();
        }
    }
}
