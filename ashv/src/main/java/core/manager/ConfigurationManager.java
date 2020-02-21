package core.manager;

import config.profile.ConfigProfile;
import config.profile.ConnProfile;
import config.profile.SqlColProfile;
import config.yaml.YamlConfig;
import core.parameter.ConnectionBuilder;
import excp.SqlColMetadataException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import profile.*;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.*;

@Slf4j
@Singleton
public class ConfigurationManager {
    private YamlConfig yamlConfig;
    private HashMap<String, ConfigProfile> configList;

    @Getter @Setter private ConfigProfile currentConfiguration;
    @Getter @Setter private String configurationName;
    @Getter @Setter private IProfile iProfile;

    @Inject
    public ConfigurationManager(YamlConfig yamlConfig,
                                @Named("ConfigList") HashMap<String, ConfigProfile> configList) {
        this.yamlConfig = yamlConfig;
        this.configList = configList;
    }

    public void loadCurrentConfiguration(String configurationName) {
        ConfigProfile configProfile = getConnProfileList().stream()
                .filter(e -> e.getConfigName().equalsIgnoreCase(configurationName))
                .findAny().get();

        loadProfile(configProfile.getConnProfile().getProfileName());

        setConfigurationName(configurationName);
        setCurrentConfiguration(configProfile);
    }

    public void loadSqlColumnMetadata(List<SqlColProfile> profilesDb) throws SqlColMetadataException {
       Optional<List<SqlColProfile>> profileCurr = Optional.ofNullable(getCurrentConfiguration().getSqlColProfileList());

       if (!profileCurr.isPresent()) {
           getCurrentConfiguration().setSqlColProfileList(profilesDb);
           loadConfigToFile(getCurrentConfiguration());
       } else {
           if (profilesDb.size() != profileCurr.get().size()) {
               throw new SqlColMetadataException("ASH sql column metadata changes detected.. " +
                       "Create the new configuration profile!");
           }
       }
    }

    public void loadConfigToFile(ConfigProfile configuration) {
        yamlConfig.saveConfigToFile(configuration);
    }

    public List<ConfigProfile> getConnProfileList() {
        return (List<ConfigProfile>) new ArrayList(configList.values());
    }

    public void deleteConfig(String configurationName) {
        yamlConfig.deleteConfig(configurationName);
        yamlConfig.loadConfigsFromFs();
    }

    public void loadProfile(String profileName) {
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

    public ConnectionBuilder getConnectionParameters(String connName) {
        Map.Entry<String,ConfigProfile> orElseEntry =
                new AbstractMap.SimpleImmutableEntry<>(connName, new ConfigProfile());
        orElseEntry.getValue().setConfigName(connName);
        orElseEntry.getValue().setConnProfile(new ConnProfile());

        Map.Entry<String, ConfigProfile> cfg = configList.entrySet().stream()
                .filter(e -> e.getValue().getConfigName().equalsIgnoreCase(connName))
                .findAny().orElse(orElseEntry);

        ConnProfile connOut = cfg.getValue().getConnProfile();

        return new ConnectionBuilder.Builder(connName)
                .userName(connOut.getUserName())
                .password(connOut.getPassword())
                .url(connOut.getUrl())
                .jar(connOut.getJar())
                .profile(connOut.getProfileName())
                .rawRetainDays(String.valueOf(cfg.getValue().getRawRetainDays()))
                .olapRetainDays(String.valueOf(cfg.getValue().getOlapRetainDays()))
                .build();
    }

    public void saveConnection(ConnectionBuilder connIn) {
        Map.Entry<String,ConfigProfile> orElseEntry = new AbstractMap.SimpleImmutableEntry<>("", new ConfigProfile());
        orElseEntry.getValue().setConnProfile(new ConnProfile());
        orElseEntry.getValue().setConfigName(connIn.getConnectionName());

        Map.Entry<String, ConfigProfile> cfg = configList.entrySet().stream()
                .filter(e -> e.getValue().getConfigName().equalsIgnoreCase(connIn.getConnectionName()))
                .findAny().orElse(orElseEntry);
        ConnProfile connOut = cfg.getValue().getConnProfile();

        connOut.setConnName(connIn.getConnectionName());
        connOut.setUserName(connIn.getUserName());
        connOut.setPassword(connIn.getPassword());
        connOut.setUrl(connIn.getUrl());
        connOut.setJar(connIn.getJar());
        connOut.setProfileName(connIn.getProfile());
        connOut.setDriver(connIn.getDriverName());

        cfg.getValue().setRawRetainDays(getRawRetainDays());
        cfg.getValue().setOlapRetainDays(getOlapRetainDays());

        yamlConfig.saveConfigToFile(cfg.getValue());
    }

    public int getRawRetainDays() {
        return ConstantManager.RETAIN_DAYS_MAX;
    }

    public int getOlapRetainDays() {
        return ConstantManager.RETAIN_DAYS_MAX;
    }

}
