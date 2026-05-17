package mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

@ApplicationScoped
public class JmxMBeanRegistry {
    private static final String POINTS_MBEAN_NAME = "web_lab3:type=PointsStatistics";
    private static final String CLICK_INTERVAL_MBEAN_NAME = "web_lab3:type=ClickIntervalStatistics";

    private final PointsStatistics pointsStatistics = new PointsStatistics();
    private final ClickIntervalStatistics clickIntervalStatistics = new ClickIntervalStatistics();

    private MBeanServer mBeanServer;
    private ObjectName pointsObjectName;
    private ObjectName clickIntervalObjectName;

    @PostConstruct
    public void init() {
        try {
            mBeanServer = ManagementFactory.getPlatformMBeanServer();
            pointsObjectName = new ObjectName(POINTS_MBEAN_NAME);
            clickIntervalObjectName = new ObjectName(CLICK_INTERVAL_MBEAN_NAME);

            register(pointsObjectName, pointsStatistics);
            register(clickIntervalObjectName, clickIntervalStatistics);
        } catch (MalformedObjectNameException
                 | NotCompliantMBeanException
                 | InstanceAlreadyExistsException
                 | MBeanRegistrationException e) {
            throw new IllegalStateException("Failed to register lab #3 MBeans", e);
        }
    }

    public void recordPoint(boolean hit) {
        pointsStatistics.recordPoint(hit);
        clickIntervalStatistics.recordClick();
    }

    @PreDestroy
    public void destroy() {
        unregister(pointsObjectName);
        unregister(clickIntervalObjectName);
    }

    private void register(ObjectName objectName, Object mBean)
            throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException {
        unregister(objectName);
        mBeanServer.registerMBean(mBean, objectName);
    }

    private void unregister(ObjectName objectName) {
        if (mBeanServer == null || objectName == null) {
            return;
        }

        try {
            if (mBeanServer.isRegistered(objectName)) {
                mBeanServer.unregisterMBean(objectName);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to unregister MBean " + objectName, e);
        }
    }
}
