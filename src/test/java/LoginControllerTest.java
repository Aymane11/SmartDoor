import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import smartdoor.controllers.LoginController;
import smartdoor.dao.AdminDao;
import smartdoor.dao.impl.AdminDaoImpl;
import smartdoor.models.Admin;

import static org.junit.jupiter.api.Assertions.*;

public class LoginControllerTest {

    private Admin admin;

    LoginControllerTest () {
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
    public void test_it_fails_if_one_the_fields_is_empty() {
        assertFalse(LoginController.login("", "password"));
        assertFalse(LoginController.login("username", ""));
    }

    @Test
    public void test_it_fails_if_the_username_is_incorrect() {

        assertFalse(LoginController.login("incorrect", "password"));
    }

    @Test
    public void test_it_fails_if_the_password_is_incorrect() {

        assertFalse(LoginController.login(admin.getUsername(), "incorrect"));
    }

    @Test
    public void test_it_works_if_the_credentials_are_correct() {

        assertTrue(LoginController.login(admin.getUsername(), "password"));
    }
}
