package core.manager;

import config.profile.ConfigProfile;
import config.profile.ConnProfile;
import config.profile.SqlColProfile;
import config.security.PassConfig;
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
    private PassConfig passConfig;
    private TreeMap<String, ConfigProfile> configList;

    @Getter @Setter private ConfigProfile currentConfiguration;
    @Getter @Setter private String configurationName;
    @Getter @Setter private IProfile iProfile;

    @Inject
    public ConfigurationManager(YamlConfig yamlConfig,
                                PassConfig passConfig,
                                @Named("ConfigList") TreeMap<String, ConfigProfile> configList) {
        this.yamlConfig = yamlConfig;
        this.passConfig = passConfig;
        this.configList = configList;
    }

    public void loadCurrentConfiguration(String configurationName) {
        ConfigProfile configProfile = getConnProfileList().stream()
                .filter(e -> e.getConfigName().equalsIgnoreCase(configurationName))
                .findAny().get();

        configProfile.setRunning(true);
        loadConfigToFile(configProfile);

        setIProfile(getProfileImpl(configProfile.getConnProfile().getProfileName()));

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
        configList.remove(configurationName);
        yamlConfig.deleteConfig(configurationName);
        yamlConfig.loadConfigsFromFs();
    }

    public ConnectionBuilder getConnectionParameters(String connName) {
        Map.Entry<String,ConfigProfile> orElseEntry =
                new AbstractMap.SimpleImmutableEntry<>(connName, new ConfigProfile());
        orElseEntry.getValue().setConfigName(connName);
        orElseEntry.getValue().setConnProfile(new ConnProfile());
        orElseEntry.getValue().getConnProfile().setPassword("");

        Map.Entry<String, ConfigProfile> cfg = configList.entrySet().stream()
                .filter(e -> e.getValue().getConfigName().equalsIgnoreCase(connName))
                .findAny().orElse(orElseEntry);

        ConnProfile connOut = cfg.getValue().getConnProfile();

        return new ConnectionBuilder.Builder(connName)
                .userName(connOut.getUserName())
                .password(passConfig.decrypt(connOut.getPassword()))
                .url(connOut.getUrl())
                .jar(connOut.getJar())
                .profile(connOut.getProfileName())
                .initialLoading(String.valueOf(cfg.getValue().getInitialLoading()))
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
        connOut.setPassword(passConfig.encrypt(connIn.getPassword()));
        connOut.setUrl(connIn.getUrl());
        connOut.setJar(connIn.getJar());
        connOut.setProfileName(connIn.getProfile());
        connOut.setDriver(connIn.getDriverName());

        cfg.getValue().setInitialLoading(Integer.parseInt(connIn.getInitialLoading()));
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

    public void closeCurrentProfile() {
        if (currentConfiguration != null){
            getCurrentConfiguration().setRunning(false);
            loadConfigToFile(getCurrentConfiguration());
        }
    }

    public IProfile getProfileImpl(String profileName) {
        switch (profileName) {
            case "OracleEE":
                return new OracleEE();
            case "OracleSE":
                return new OracleSE();
            case "OracleEEObject":
                return new OracleEEObject();
            case "OracleEE10g":
                return new OracleEE10g();
            case "Postgres":
                return new Postgres();
            case "Postgres96":
                return new Postgres96();
            default:
                throw new IllegalArgumentException("Invalid profile name");
        }
    }

    /**
     * For migration purposes (encrypt passwords in yaml config)
     * Delete in future release
     */
    @Deprecated
    public void updatePassword() {
        configList.entrySet().stream().forEach(e -> {
            String pass = e.getValue().getConnProfile().getPassword();
            try {
                passConfig.decrypt(pass);
            } catch (Exception e1){
                log.error(e1.getLocalizedMessage());
                e.getValue().getConnProfile().setPassword(passConfig.encrypt(pass));
            }
        });

        TreeMap<String, ConfigProfile> shCopy = new TreeMap<>(configList);

        synchronized (shCopy) {
            Iterator<Map.Entry<String, ConfigProfile>>
                    i = shCopy.entrySet().iterator();
            while (i.hasNext()) {
                loadConfigToFile(i.next().getValue());
            }
        }
    }

}
