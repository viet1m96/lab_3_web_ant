package mbeans;

public interface PointsStatisticsMBean {
    long getTotalPoints();

    long getHitPoints();

    long getMissPoints();

    void reset();
}
