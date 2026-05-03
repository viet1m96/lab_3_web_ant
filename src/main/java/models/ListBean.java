package models;

import data_services.ParametersChecker;
import data_services.PointDBServices;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * JSF backing bean that manages point submission and table data loading.
 * <p>
 * The bean reads input coordinates from {@link ParamsBean}, checks whether the
 * submitted point is inside the target area, saves the result to the database,
 * and sends callback data back to the PrimeFaces client side.
 *
 * @author viet1m96
 * @version 1.0
 */
@Named("listBean")
@ApplicationScoped
public class ListBean {
    /**
     * Service used to store and load point entities.
     */
    @Inject private PointDBServices svc;

    /**
     * User input parameters from the current session.
     */
    @Inject @Named("paramsBean") private ParamsBean params;

    /**
     * Session-scoped collection of submitted points for client-side graph updates.
     */
    @Inject private PointSessionList pointSessionList;

    /**
     * Lazy PrimeFaces data model used by the result table.
     */
    @Getter
    private LazyDataModel<Point> model;

    /**
     * Session bean containing currently selected point parameters.
     */
    @Named("paramsBean")
    @Inject
    private ParamsBean paramsBean;

    /**
     * Service that checks whether submitted coordinates hit the target area.
     */
    @Inject
    private ParametersChecker parametersChecker;

    /**
     * Returns the current HTTP session identifier.
     *
     * @return current JSF session identifier
     */
    private String sid() {
        return FacesContext.getCurrentInstance()
                .getExternalContext().getSessionId(false);
    }

    /**
     * Initializes the lazy data model after dependency injection is completed.
     */
    @PostConstruct
    public void init() {
        model = new LazyDataModel<Point>() {

            /**
             * Counts database records belonging to the current session.
             *
             * @param filterBy PrimeFaces filter metadata
             * @return number of session records
             */
            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int)svc.countBySession(sid());
            }

            /**
             * Loads a page of results for the current session.
             *
             * @param first first row index
             * @param pageSize number of rows requested by the table
             * @param sortBy PrimeFaces sorting metadata
             * @param filterBy PrimeFaces filtering metadata
             * @return page of points loaded from the database
             */
            @Override
            public List<Point> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return svc.findRangeBySession(sid(), first, pageSize);
            }
        };
    }

    /**
     * Handles point submission from the web page.
     * <p>
     * The method creates a new {@link Point}, calculates hit status and execution
     * time, saves the point in the database, updates the session point list, and
     * sends AJAX callback parameters to the browser.
     */
    public void submit() {
        Point p = new Point();
        p.setX(params.getX());
        p.setY(params.getY());
        p.setR(params.getR());
        p.setSessionId(sid());
        long start = System.nanoTime();
        p.setHit(parametersChecker.checkParams(p.getX(), p.getY(), p.getR()));
        PrimeFaces.current().ajax().addCallbackParam("hit", p.isHit());
        long end = System.nanoTime();
        Double calTime = (end - start) / 1000000.0;
        p.setCalTime(calTime);
        LocalDateTime releaseTime = LocalDateTime.now();
        p.setReleaseTime(releaseTime);
        svc.save(p);
        pointSessionList.add(p.getX(), p.getY());
        PrimeFaces.current().ajax().addCallbackParam("pointsJson", pointSessionList.getJson());
    }
}
