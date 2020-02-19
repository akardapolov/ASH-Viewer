package core.thread;

import core.manager.ConfigurationManager;
import core.manager.ConstantManager;
import core.processing.GetFromRemoteAndStore;
import lombok.extern.slf4j.Slf4j;
import store.StoreManager;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.Duration;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Slf4j
@Singleton
public class ClearDatabaseThread {
    private StoreManager storeManager;

    private GetFromRemoteAndStore getFromRemoteAndStore;
    private ConfigurationManager configurationManager;

    private Timer timer = new Timer();

    @Inject
    public ClearDatabaseThread(StoreManager storeManager,
                               GetFromRemoteAndStore getFromRemoteAndStore,
                               ConfigurationManager configurationManager) {
        this.storeManager = storeManager;
        this.getFromRemoteAndStore = getFromRemoteAndStore;
        this.configurationManager = configurationManager;
    }

    public void schedulerTimer(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 22);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date time = calendar.getTime();

        timer.schedule(new clearDatabase(), time);
    }

    class clearDatabase extends TimerTask {
        public void run() {
            int intRawDays = configurationManager.getRawRetainDays();
            int intOlapDays = configurationManager.getOlapRetainDays();

            long start = 0L;
            long endRaw = getFromRemoteAndStore.getCurrServerTime() - TimeUnit.DAYS.toMillis(intRawDays);
            long endOlap = getFromRemoteAndStore.getCurrServerTime() - TimeUnit.DAYS.toMillis(intOlapDays);

            boolean isRawDataClean = (intRawDays > 0) & (intRawDays < ConstantManager.RETAIN_DAYS_MAX);
            boolean isOlapDataClean = (intOlapDays > 0) & (intOlapDays < ConstantManager.RETAIN_DAYS_MAX);

            if (isRawDataClean) {
                Instant startedAt = Instant.now();
                log.info("Clearing of raw data started ..");

                storeManager.getDatabaseDAO().deleteMainData(start, endRaw);

                Instant endedAt = Instant.now();
                Duration duration = Duration.between(startedAt , endedAt);

                String hms = String.format("%d hour %02d min %02d sec",
                        duration.toHours(),
                        duration.toMinutes(),
                        duration.toMillis()/1000);

                log.info("Clearing of raw data takes " + hms);
            }

            if (isOlapDataClean) {
                Instant startedAt = Instant.now();
                log.info("Clearing of OLAP data started ..");

                storeManager.getDatabaseDAO().getOlapDAO().deleteOlapData(start, endOlap);

                Instant endedAt = Instant.now();
                Duration duration = Duration.between(startedAt , endedAt);

                String hms = String.format("%d hour %02d min %02d sec",
                        duration.toHours(),
                        duration.toMinutes(),
                        duration.toMillis()/1000);

                log.info("Clearing of OLAP data takes " + hms);
            }

        }
    }
}
