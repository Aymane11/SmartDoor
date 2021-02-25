package smartdoor.dao;

import smartdoor.models.Admin;

public interface AdminDao {
    /**
     * Insert the passed Admin object into DB
     *
     * @param admin
     * @return void
     */
    public void insert(Admin admin);

    /**
     * Update the passed Admin object in DB
     *
     * @param admin
     * @return void
     */
    public void update(Admin admin);

    /**
     * Delete the passed Admin object from DB
     *
     * @param admin
     * @return void
     */
    public void delete(Admin admin);

    /**
     * Get the Admin object from DB
     *
     * @param username
     * @return Admin
     */
    public Admin get(String username);
}
