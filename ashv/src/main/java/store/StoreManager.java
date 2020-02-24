package store;

import com.sleepycat.je.DatabaseException;
import config.FileConfig;
import core.manager.ConfigurationManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import store.service.DatabaseDAO;
import store.service.OlapDAO;
import utility.StackTraceUtil;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Slf4j
@Singleton
public class StoreManager {
    private FileConfig fileConfig;
    @Getter private OlapCacheManager olapCacheManager;
    @Getter private ConfigurationManager configurationManager;

    @Getter private BerkleyDB berkleyDB;
    @Getter private DatabaseDAO databaseDAO;
    @Getter private OlapDAO olapDAO;

    @Getter @Setter private long lastLoadTimeMark;

    @Inject
    public StoreManager(FileConfig fileConfig,
                        OlapCacheManager olapCacheManager,
                        ConfigurationManager configurationManager,
                        BerkleyDB berkleyDB,
                        DatabaseDAO databaseDAO,
                        OlapDAO olapDAO) {
        try {
            this.fileConfig = fileConfig;
            this.olapCacheManager = olapCacheManager;
            this.configurationManager = configurationManager;
            this.berkleyDB = berkleyDB;
            this.databaseDAO = databaseDAO;
            this.olapDAO = olapDAO;
        } catch (DatabaseException e) {
            log.error(StackTraceUtil.getCustomStackTrace(e));
            System.exit(-1);
        }
    }

    public void setUpBDBAndDAO(String connName) throws IOException {
        String connNameDir = FileConfig.DATABASE_DIR + FileConfig.FILE_SEPARATOR + connName;

        fileConfig.setUpDirectory(connNameDir);

        berkleyDB.init(connNameDir);
        databaseDAO.init();
        olapDAO.init();
    }

    public void syncBdb(){
        this.berkleyDB.getStore().sync();
    }

    public void closeDb() {
        if (this.berkleyDB.getStore() != null) {
            try {
                this.berkleyDB.getStore().close();
            } catch (DatabaseException dbe) { log.error("Error closing store: " + dbe.toString()); }
        }
        /*if (env != null) {
            try { env.close(); } catch (DatabaseException dbe) { log.error("Error closing env: " + dbe.toString()); }
        }*/
    }
}
