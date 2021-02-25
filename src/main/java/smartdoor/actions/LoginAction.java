package smartdoor.actions;

import org.mindrot.jbcrypt.BCrypt;
import smartdoor.dao.AdminDao;
import smartdoor.dao.impl.AdminDaoImpl;
import smartdoor.models.Admin;

public class LoginAction {

    public Admin login(String username, String password) {
        if (username.isEmpty() || password.isEmpty())
            return null;

        AdminDao adminDao = new AdminDaoImpl();
        Admin admin = adminDao.get(username);

        if (admin == null || !BCrypt.checkpw(password, admin.getPassword()))
            return null;

        return admin;
    }
}
