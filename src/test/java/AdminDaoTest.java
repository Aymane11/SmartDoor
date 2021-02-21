import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import smartdoor.dao.AdminDao;
import smartdoor.dao.impl.AdminDaoImpl;
import smartdoor.models.Admin;

import static org.junit.jupiter.api.Assertions.*;

public class AdminDaoTest {
    @Test
    @Order(1)
    public void insert_a_user_to_db() {
        Admin admin = new Admin();
        admin.setUsername("hello");
        admin.setPassword("password");

        new AdminDaoImpl().insert(admin);

        assertTrue(new AdminDaoImpl().get("hello") instanceof Admin);
    }

    @Test
    @Order(2)
    public void test_it_updates_a_user() {
        Admin admin = new AdminDaoImpl().get("hello");
        admin.setUsername("hello2");

        new AdminDaoImpl().update(admin);

        assertTrue(new AdminDaoImpl().get("hello2") instanceof Admin);
    }

    @Test
    @Order(3)
    public void test_it_deletes_a_user() {
        Admin admin = new AdminDaoImpl().get("hello2");

        new AdminDaoImpl().delete(admin);

        assertTrue(new AdminDaoImpl().get("hello2") == null);
    }
}
