package smartdoor.dao;

import smartdoor.models.Session;

import java.util.List;

public interface SessionDAO {
    public List<Session> getAll();
    public void create(Session session);
    public void delete(Session session);
}
