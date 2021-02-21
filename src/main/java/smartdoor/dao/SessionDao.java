package smartdoor.dao;

import smartdoor.models.Session;

import java.util.List;

public interface SessionDao {
    public List<Session> getAll();
    public void create(Session session);
    public void delete(Session session);
}
