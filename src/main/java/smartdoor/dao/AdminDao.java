package smartdoor.dao;

import smartdoor.models.Admin;

public interface AdminDao {
    public void insert(Admin admin);
    public void update(Admin admin);
    public void delete(Admin admin);
    public Admin get(String username);
}
