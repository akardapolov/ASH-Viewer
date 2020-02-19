package core.manager;

import config.Labels;
import config.profile.ConfigProfile;
import config.profile.ConnProfile;
import config.profile.SqlColProfile;
import config.yaml.YamlConfig;
import core.parameter.ConnectionBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import profile.*;
import store.StoreManager;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Singleton
public class ConfigurationManager {
    private StoreManager storeManager;
    private YamlConfig yamlConfig;
    private HashMap<String, ConfigProfile> configList;

    @Getter @Setter
    private ConfigProfile currentConfiguration;

    @Getter @Setter
    private String configurationName;

    @Getter @Setter
    private String connectionName;

    @Getter @Setter
    private IProfile iProfile;

    @Inject
    public ConfigurationManager(StoreManager storeManager,
                                YamlConfig yamlConfig,
                                @Named("ConfigList") HashMap<String, ConfigProfile> configList) {
        this.storeManager = storeManager;
        this.yamlConfig = yamlConfig;
        this.configList = configList;
    }

    public void loadCurrentConfiguration(String configurationName, ConnProfile connection){
        ConfigProfile configProfile = new ConfigProfile();
        configProfile.setConnProfile(connection);
        loadProfile(connection.getProfileName());

        configProfile.setConfigName(configurationName);
        setCurrentConfiguration(configProfile);

        loadConfigToFile(configProfile);
    }

    public void loadSqlColumnMetadata(List<SqlColProfile> columnPojos){
        getCurrentConfiguration().setSqlColProfileList(columnPojos);
        loadConfigToFile(getCurrentConfiguration());
    }

    public void loadConfigToFile(ConfigProfile configuration) {
        yamlConfig.loadConfigToFile(configuration);
    }

    public void loadProfile(String profileName){
        switch (profileName) {
            case "OracleEE":
                setIProfile(new OracleEE());
                break;
            case "OracleSE":
                setIProfile(new OracleSE());
                break;
            case "Postgres":
                setIProfile(new Postgres());
                break;
            case "Postgres96":
                setIProfile(new Postgres96());
                break;
            default:
                throw new IllegalArgumentException("Invalid profile name");
        }
    }

    /**
     * For migration purposes (from BDB store to yaml configs)
     * Delete in future release
     * @param list
     */
    public void unloadConfigFromBdbToFile(List<ConnProfile> list){
        list.stream().forEach(e -> {
            Optional<Map.Entry<String, ConfigProfile>> in = configList.entrySet().stream()
                    .filter(m -> m.getValue().getConfigName()
                            .equalsIgnoreCase(e.getConnName())).findFirst();

            if (!in.isPresent()){
                loadCurrentConfiguration(e.getConnName(), e);
            }

        });
    }

    public ConnectionBuilder getConnectionParameters(String connName){
        connectionName = connName;

        return new ConnectionBuilder.Builder(connName)
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
                .olapRetainDays(this.storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                        Labels.getLabel("local.sql.metadata.connection"),
                        connName, Labels.getLabel("local.sql.metadata.connection.other.olap")))
        .build();
    }

    public void saveConnection(ConnectionBuilder connParameters) {
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
        storeManager.getRepositoryDAO().metadataEAVDAO.putMainDataEAVWithCheck(
                Labels.getLabel("local.sql.metadata.connection"), connParameters.getConnectionName(),
                Labels.getLabel("local.sql.metadata.connection.other.olap"), connParameters.getOlapRetainDays());
    }

    public int getRawRetainDays(){
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

    public int getOlapRetainDays(){
        int intDays = ConstantManager.RETAIN_DAYS_MAX;

        String strDays = storeManager.getRepositoryDAO().getMetaDataAttributeValue(
                Labels.getLabel("local.sql.metadata.connection"),
                connectionName, Labels.getLabel("local.sql.metadata.connection.other.olap"));

        try {
            intDays = Integer.parseInt(strDays);
        } catch (NumberFormatException ex) {
            log.info("Raw data days retain text field contains char data or empty");
        }

        return intDays;
    }


}
