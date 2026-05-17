package mbeans;

public interface ClickIntervalStatisticsMBean {
    long getClickCount();

    long getIntervalCount();

    double getAverageIntervalMillis();

    long getLastClickTimestampMillis();

    void reset();
}
