package core.manager;

import config.Labels;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import store.StoreManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class ConnectionManager {
    private StoreManager storeManager;

    @Getter @Setter
    private String connectionName;

    @Inject
    public ConnectionManager(StoreManager storeManager) {
        this.storeManager = storeManager;
    }

    public int getRetainDays(){
        int intDays = ConstantManager.RETAIN_DAYS_MAX;

        String strDays = storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                Labels.getLabel("local.sql.metadata.connection"),
                connectionName, Labels.getLabel("local.sql.metadata.connection.other.raw"));

        try {
            intDays = Integer.parseInt(strDays);
        } catch (NumberFormatException ex) {
            log.info("Raw data days retain text field contains char data or empty");
        }

        return intDays;
    }

    public void setRetentionDays(String days){
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connectionName,
                Labels.getLabel("local.sql.metadata.connection.other.raw"), days);
    }

}
