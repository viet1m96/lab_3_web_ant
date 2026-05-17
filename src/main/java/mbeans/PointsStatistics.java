package mbeans;

import javax.management.MBeanNotificationInfo;
import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.concurrent.atomic.AtomicLong;

public class PointsStatistics extends NotificationBroadcasterSupport implements PointsStatisticsMBean {
    public static final String NOTIFICATION_TYPE = "web_lab3.points.total.multiple_of_5";

    private final AtomicLong totalPoints = new AtomicLong();
    private final AtomicLong hitPoints = new AtomicLong();
    private final AtomicLong sequence = new AtomicLong();

    public void recordPoint(boolean hit) {
        long total = totalPoints.incrementAndGet();
        if (hit) {
            hitPoints.incrementAndGet();
        }

        if (total % 5 == 0) {
            Notification notification = new Notification(
                    NOTIFICATION_TYPE,
                    this,
                    sequence.incrementAndGet(),
                    System.currentTimeMillis(),
                    "The number of submitted points became a multiple of 5: " + total
            );
            notification.setUserData(total);
            sendNotification(notification);
        }
    }

    @Override
    public long getTotalPoints() {
        return totalPoints.get();
    }

    @Override
    public long getHitPoints() {
        return hitPoints.get();
    }

    @Override
    public long getMissPoints() {
        return getTotalPoints() - getHitPoints();
    }

    @Override
    public void reset() {
        totalPoints.set(0);
        hitPoints.set(0);
        sequence.set(0);
    }

    @Override
    public MBeanNotificationInfo[] getNotificationInfo() {
        String[] types = {NOTIFICATION_TYPE};
        String name = Notification.class.getName();
        String description = "Notification is sent when the total number of submitted points is divisible by 5.";
        return new MBeanNotificationInfo[]{
                new MBeanNotificationInfo(types, name, description)
        };
    }
}
