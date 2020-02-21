package store;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;
import lombok.Getter;

import java.io.File;

public class BerkleyDB {
    String directory;
    @Getter
    private EnvironmentConfig envConfig;
    @Getter
    private Environment env;
    @Getter
    private StoreConfig storeConfig;
    @Getter
    private EntityStore store;

    public BerkleyDB(String directory) {
        this.directory = directory;
        this.setupEnvConfig();
        this.setupEnvironment();
        this.setupStoreConfig();
    }

    private void setupEnvConfig() {
        this.envConfig = new EnvironmentConfig();
        this.envConfig.setAllowCreate(true);
        this.envConfig.setTransactional(false);
        this.envConfig.setCachePercent(50);
    }

    private void setupEnvironment(){
        this.env = new Environment(new File(this.directory), envConfig);
    }

    private void setupStoreConfig() {
        this.storeConfig = new StoreConfig();
        this.storeConfig.setAllowCreate(true);
        this.storeConfig.setTransactional(false);
        this.storeConfig.setDeferredWrite(true);

        this.store = new EntityStore(this.env, "ash.db", this.storeConfig);
    }


}
