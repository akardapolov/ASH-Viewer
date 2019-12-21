package core.manager;

import config.Labels;
import core.parameter.ConnectionParameters;
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

    public ConnectionParameters getConnectionParameters(String connName){
        connectionName = connName;

        return new ConnectionParameters.Builder(connName)
                .userName(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                        Labels.getLabel("local.sql.metadata.connection"),
                        connName, Labels.getLabel("local.sql.metadata.connection.username")))
                .password(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                        Labels.getLabel("local.sql.metadata.connection"),
                        connName, Labels.getLabel("local.sql.metadata.connection.password")))
                .url(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                        Labels.getLabel("local.sql.metadata.connection"),
                        connName, Labels.getLabel("local.sql.metadata.connection.url")))
                .jar(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                        Labels.getLabel("local.sql.metadata.connection"),
                        connName, Labels.getLabel("local.sql.metadata.connection.jar")))
                .profile(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                        Labels.getLabel("local.sql.metadata.connection"),
                        connName, Labels.getLabel("local.sql.metadata.connection.profile")))
                .rawRetainDays(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                        Labels.getLabel("local.sql.metadata.connection"),
                        connName, Labels.getLabel("local.sql.metadata.connection.other.raw")))
        .build();
    }

    public void saveConnection(ConnectionParameters connParameters) {
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connParameters.getConnectionName(),
                Labels.getLabel("local.sql.metadata.connection.name"), connParameters.getConnectionName());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connParameters.getConnectionName(),
                Labels.getLabel("local.sql.metadata.connection.username"), connParameters.getUserName());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connParameters.getConnectionName(),
                Labels.getLabel("local.sql.metadata.connection.password"), connParameters.getPassword());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connParameters.getConnectionName(),
                Labels.getLabel("local.sql.metadata.connection.url"), connParameters.getUrl());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connParameters.getConnectionName(),
                Labels.getLabel("local.sql.metadata.connection.jar"), connParameters.getJar());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connParameters.getConnectionName(),
                Labels.getLabel("local.sql.metadata.connection.profile"), connParameters.getProfile());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connParameters.getConnectionName(),
                Labels.getLabel("local.sql.metadata.connection.driver"), connParameters.getDriverName());
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connParameters.getConnectionName(),
                Labels.getLabel("local.sql.metadata.connection.other.raw"), connParameters.getRawRetainDays());
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


}
