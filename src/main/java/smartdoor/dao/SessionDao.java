package smartdoor.dao;

import smartdoor.models.Session;

import java.util.List;

public interface SessionDao {
    /**
     * Get all sessions from DB
     *
     * @return List
     */
    public List<Session> getAll();

    /**
     * Get the Session object from DB
     *
     * @param filename
     * @return Session
     */
    public Session get(String filename);

    /**
     * Get the Session object from DB
     *
     * @param id
     * @return Session
     */
    public Session get(int id);

    /**
     * Create the Session object in DB
     *
     * @param session
     * @return void
     */
    public void create(Session session);
}
