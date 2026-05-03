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


@Named("listBean")
@ApplicationScoped
public class ListBean {
    @Inject private PointDBServices svc;
    @Inject @Named("paramsBean") private ParamsBean params;
    @Inject private PointSessionList pointSessionList;

    @Getter
    private LazyDataModel<Point> model;
    @Named("paramsBean")
    @Inject
    private ParamsBean paramsBean;
    @Inject
    private ParametersChecker parametersChecker;
    private String sid() {
        return FacesContext.getCurrentInstance()
                .getExternalContext().getSessionId(false);
    }
    @PostConstruct
    public void init() {
        model = new LazyDataModel<Point>() {

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                return (int)svc.countBySession(sid());
            }

            @Override
            public List<Point> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                return svc.findRangeBySession(sid(), first, pageSize);
            }
        };
    }

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
