package smartdoor.dao;

import smartdoor.models.Session;

import java.util.List;

public interface SessionDao {
    public List<Session> getAll();
    public Session get(String filename);
    public Session get(int id);
    public void create(Session session);
}
