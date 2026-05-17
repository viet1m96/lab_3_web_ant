package data_services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import models.Point;

import java.util.List;

@ApplicationScoped
public class PointDBServices {
    private static final String GET_ALL_DATA_BY_SESSION = "select p from Point p where p.sessionId = :sid order by p.releaseTime asc";
    private static final String DELETE_DATA_BY_SESSION = "delete from Point p where p.sessionId = :sid";
    private static final String COUNT_BY_SESSION = "select count(p) from Point p where p.sessionId = :sid";
    @PersistenceContext(unitName = "appPU")
    private EntityManager em;

    @Transactional
    public Point save(Point r) {
        em.persist(r);
        return r;
    }


    @Transactional
    public int deleteAllBySession(String sessionId) {
        return em.createQuery(DELETE_DATA_BY_SESSION)
                .setParameter("sid", sessionId)
                .executeUpdate();


    }

    public long countBySession(String sessionId) {
        return em.createQuery(COUNT_BY_SESSION, Long.class)
                .setParameter("sid", sessionId)
                .getSingleResult();
    }

    public List<Point> findRangeBySession(String sessionId, int first, int pageSize) {
        return em.createQuery(GET_ALL_DATA_BY_SESSION)
                .setParameter("sid", sessionId)
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
