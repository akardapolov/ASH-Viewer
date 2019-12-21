package core.thread;

import core.manager.ConnectionManager;
import core.processing.GetFromRemoteAndStore;
import lombok.extern.slf4j.Slf4j;
import store.OlapCacheManager;
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
    private OlapCacheManager olapCacheManager;

    private GetFromRemoteAndStore getFromRemoteAndStore;
    private ConnectionManager connectionManager;

    private Timer timer = new Timer();

    private int DAYS_RETAIN_MAX = 101;

    @Inject
    public ClearDatabaseThread(StoreManager storeManager,
                               OlapCacheManager olapCacheManager,
                               GetFromRemoteAndStore getFromRemoteAndStore,
                               ConnectionManager connectionManager) {
        this.storeManager = storeManager;
        this.olapCacheManager = olapCacheManager;
        this.getFromRemoteAndStore = getFromRemoteAndStore;
        this.connectionManager = connectionManager;
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
            int intDays = connectionManager.getRetainDays();

            long start = 0L;
            long end = getFromRemoteAndStore.getCurrServerTime() - TimeUnit.DAYS.toMillis(intDays); ;

            if (intDays > 0 & intDays < DAYS_RETAIN_MAX) {
                Instant startedAt = Instant.now();
                log.info("Clear of database procedure started ..");

                storeManager.getDatabaseDAO().deleteMainData(start, end);

                Instant endedAt = Instant.now();
                Duration duration = Duration.between(startedAt , endedAt);

                String hms = String.format("%d hour %02d min %02d sec",
                        duration.toHours(),
                        duration.toMinutes(),
                        duration.toMillis()/1000);

                log.info("Clear of database takes " + hms);
            }

        }
    }
}
