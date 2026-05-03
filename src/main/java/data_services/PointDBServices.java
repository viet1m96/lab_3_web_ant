package data_services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import models.Point;

import java.util.List;

/**
 * Persistence service for {@link Point} entities.
 * <p>
 * The service stores calculated points, removes all points belonging to a
 * destroyed HTTP session, counts session records, and loads paginated session
 * data for the PrimeFaces lazy table.
 *
 * @author viet1m96
 * @version 1.0
 */
@ApplicationScoped
public class PointDBServices {
    /**
     * JPQL query for loading all points of a session ordered by calculation time.
     */
    private static final String GET_ALL_DATA_BY_SESSION = "select p from Point p where p.sessionId = :sid order by p.releaseTime asc";

    /**
     * JPQL query for deleting all points associated with a session.
     */
    private static final String DELETE_DATA_BY_SESSION = "delete from Point p where p.sessionId = :sid";

    /**
     * JPQL query for counting points associated with a session.
     */
    private static final String COUNT_BY_SESSION = "select count(p) from Point p where p.sessionId = :sid";

    /**
     * Entity manager provided by the Jakarta Persistence context.
     */
    @PersistenceContext(unitName = "appPU")
    private EntityManager em;

    /**
     * Saves a point entity in the database.
     *
     * @param r point entity to persist
     * @return the same persisted point entity
     */
    @Transactional
    public Point save(Point r) {
        em.persist(r);
        return r;
    }

    /**
     * Deletes all point records associated with the specified HTTP session.
     *
     * @param sessionId HTTP session identifier
     * @return number of deleted records
     */
    @Transactional
    public int deleteAllBySession(String sessionId) {
        return em.createQuery(DELETE_DATA_BY_SESSION)
                .setParameter("sid", sessionId)
                .executeUpdate();
    }

    /**
     * Counts all points stored for the specified session.
     *
     * @param sessionId HTTP session identifier
     * @return number of points belonging to the session
     */
    public long countBySession(String sessionId) {
        return em.createQuery(COUNT_BY_SESSION, Long.class)
                .setParameter("sid", sessionId)
                .getSingleResult();
    }

    /**
     * Loads a page of points for the specified session.
     *
     * @param sessionId HTTP session identifier
     * @param first index of the first row to load
     * @param pageSize maximum number of rows to load
     * @return list of points for the requested page
     */
    public List<Point> findRangeBySession(String sessionId, int first, int pageSize) {
        return em.createQuery(GET_ALL_DATA_BY_SESSION)
                .setParameter("sid", sessionId)
                .setFirstResult(first)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
