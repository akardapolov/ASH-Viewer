package store;

import com.sleepycat.je.DatabaseException;
import config.FileConfig;
import core.manager.ConfigurationManager;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Slf4j
@Singleton
public class StoreManager {
    private FileConfig fileConfig;

    @Getter private BerkleyDB berkleyRepo;
    @Getter private BerkleyDB berkleyDB;

    @Getter private RepositoryDAO repositoryDAO;
    @Getter private DatabaseDAO databaseDAO;

    @Getter private OlapCacheManager olapCacheManager;
    @Getter private ConfigurationManager configurationManager;

    @Getter @Setter private long lastLoadTimeMark;

    @Inject
    public StoreManager(FileConfig fileConfig,
                        BerkleyDB berkleyRepo,
                        RepositoryDAO repositoryDAO,
                        OlapCacheManager olapCacheManager,
                        ConfigurationManager configurationManager) {
        try {
            this.fileConfig = fileConfig;
            this.berkleyRepo = berkleyRepo;
            this.repositoryDAO = repositoryDAO;
            this.olapCacheManager = olapCacheManager;
            this.configurationManager = configurationManager;
        } catch (DatabaseException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void setUpBDBAndDAO(String connName) throws IOException {
        String connNameDir = FileConfig.DATABASE_DIR + FileConfig.FILE_SEPARATOR + connName;
        fileConfig.setUpDirectory(connNameDir);

        berkleyDB = new BerkleyDB(connNameDir);
        berkleyDB.getEnvConfig().setCachePercent(30);

        databaseDAO = new DatabaseDAO(berkleyDB);
    }

    public void syncRepo(){
        this.berkleyRepo.getStore().sync();
    }

    public void syncBdb(){
        this.berkleyDB.getStore().sync();
    }

    public void closeRepo() {
        if (this.berkleyRepo.getStore() != null) {
            try {
                this.berkleyRepo.getStore().close();
            } catch (DatabaseException dbe) { log.error("Error closing store: " + dbe.toString()); }
        }
        /*if (env != null) {
            try { env.close(); } catch (DatabaseException dbe) { log.error("Error closing env: " + dbe.toString()); }
        }*/
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
