package mbeans;

public class ClickIntervalStatistics implements ClickIntervalStatisticsMBean {
    private long clickCount;
    private long intervalCount;
    private long totalIntervalMillis;
    private long lastClickTimestampMillis;

    public synchronized void recordClick() {
        long now = System.currentTimeMillis();

        if (lastClickTimestampMillis != 0) {
            totalIntervalMillis += now - lastClickTimestampMillis;
            intervalCount++;
        }

        lastClickTimestampMillis = now;
        clickCount++;
    }

    @Override
    public synchronized long getClickCount() {
        return clickCount;
    }

    @Override
    public synchronized long getIntervalCount() {
        return intervalCount;
    }

    @Override
    public synchronized double getAverageIntervalMillis() {
        if (intervalCount == 0) {
            return 0.0;
        }
        return (double) totalIntervalMillis / intervalCount;
    }

    @Override
    public synchronized long getLastClickTimestampMillis() {
        return lastClickTimestampMillis;
    }

    @Override
    public synchronized void reset() {
        clickCount = 0;
        intervalCount = 0;
        totalIntervalMillis = 0;
        lastClickTimestampMillis = 0;
    }
}
