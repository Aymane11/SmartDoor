import org.junit.jupiter.api.Test;
import smartdoor.actions.LoginAction;
import smartdoor.dao.AdminDao;
import smartdoor.dao.impl.AdminDaoImpl;
import smartdoor.models.Admin;

import static org.junit.jupiter.api.Assertions.*;

public class LoginActionTest extends TestCase{
    private Admin admin;

    LoginActionTest () {
        admin = new Admin();
        admin.setUsername("test");
        admin.setPassword("password");

        AdminDao adminDao = new AdminDaoImpl();
        Admin admin2 = adminDao.get(admin.getUsername());

        if (admin2 != null) {
            adminDao.delete(admin2);
        }
        adminDao.insert(admin);
    }

    @Test
    public void test_it_logs_in_the_user() {
        // test it fails if one the fields is empty
        assertNull(new LoginAction().login("", "password"));
        assertNull(new LoginAction().login("username", ""));
        //test it fails if the username is incorrect
        assertNull(new LoginAction().login("incorrect", "password"));
        //test it fails if the password is incorrect
        assertNull(new LoginAction().login(admin.getUsername(), "incorrect"));
        //Test it works if the credentials are correct
        assertTrue(new LoginAction().login(admin.getUsername(), "password") != null);
    }
}
