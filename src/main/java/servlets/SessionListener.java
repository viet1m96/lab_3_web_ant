package servlets;

import data_services.PointDBServices;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

/**
 * Servlet listener that reacts to HTTP session lifecycle events.
 * <p>
 * When a session is destroyed, all point records associated with that session
 * are removed from the database. The listener also logs session creation and
 * destruction events to the server output.
 *
 * @author viet1m96
 * @version 1.0
 */
@WebListener
public class SessionListener implements HttpSessionListener {

    /**
     * Service used to delete session-related point records.
     */
    @Inject
    private PointDBServices pointDBServices;

    /**
     * Removes database records associated with the destroyed HTTP session.
     *
     * @param sessionEvent event containing the destroyed session
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        HttpSession session = sessionEvent.getSession();
        String jSessionId = session.getId();
        pointDBServices.deleteAllBySession(jSessionId);
        System.out.println(jSessionId + " was destroyed!");
    }

    /**
     * Logs the creation of a new HTTP session.
     *
     * @param sessionEvent event containing the created session
     */
    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        System.out.println(sessionEvent.getSession().getId() + " was created!");
    }
}
