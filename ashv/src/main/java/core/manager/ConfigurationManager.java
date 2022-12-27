package core.manager;

import com.github.windpapi4j.InitializationFailedException;
import com.github.windpapi4j.WinAPICallFailedException;
import com.github.windpapi4j.WinDPAPI;
import com.github.windpapi4j.WinDPAPI.CryptProtectFlag;
import config.profile.ConfigProfile;
import config.profile.ConnProfile;
import config.profile.SqlColProfile;
import config.security.BCFipsConfig;
import config.security.ContainerConfig;
import config.security.PassConfig;
import config.yaml.YamlConfig;
import core.manager.ConstantManager.Profile;
import core.parameter.ConnectionBuilder;
import excp.SqlColMetadataException;
import gui.model.ContainerType;
import gui.model.EncryptionType;
import gui.model.RegistryKey;
import java.security.GeneralSecurityException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import javax.crypto.SecretKey;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import profile.IProfile;
import profile.OracleEE;
import profile.OracleEE10g;
import profile.OracleEEObject;
import profile.OracleSE;
import profile.Postgres;
import profile.Postgres96;

@Slf4j
@Singleton
public class ConfigurationManager {
    private YamlConfig yamlConfig;
    private PassConfig passConfig;
    private BCFipsConfig bcFipsConfig;
    private ContainerConfig containerConfig;
    private TreeMap<String, ConfigProfile> configList;

    @Getter @Setter private ConfigProfile currentConfiguration;
    @Getter @Setter private String configurationName;
    @Getter @Setter private IProfile iProfile;

    @Inject
    public ConfigurationManager(YamlConfig yamlConfig,
                                PassConfig passConfig,
                                BCFipsConfig bcFipsConfig,
                                ContainerConfig containerConfig,
                                @Named("ConfigList") TreeMap<String, ConfigProfile> configList) {
        this.yamlConfig = yamlConfig;
        this.passConfig = passConfig;
        this.bcFipsConfig = bcFipsConfig;
        this.containerConfig = containerConfig;
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
        Map.Entry<String,ConfigProfile> other = new AbstractMap.SimpleImmutableEntry<>(connName, new ConfigProfile());
        other.getValue().setConfigName(connName);
        other.getValue().setConnProfile(new ConnProfile());
        other.getValue().getConnProfile().setPassword("");

        Map.Entry<String, ConfigProfile> cfg = configList.entrySet().stream()
                .filter(e -> e.getValue().getConfigName().equalsIgnoreCase(connName))
                .findAny().orElse(other);

        ConnProfile connOut = cfg.getValue().getConnProfile();

        return new ConnectionBuilder.Builder(connName)
                .userName(connOut.getUserName())
                .password(getPassword(cfg.getValue()))
                .url(connOut.getUrl())
                .jar(connOut.getJar())
                .profile(connOut.getProfileName())
                .initialLoading(String.valueOf(cfg.getValue().getInitialLoading()))
                .rawRetainDays(String.valueOf(cfg.getValue().getRawRetainDays()))
                .olapRetainDays(String.valueOf(cfg.getValue().getOlapRetainDays()))
                .containerType(cfg.getValue().getContainerType())
                .encryptionType(cfg.getValue().getEncryptionType())
                .build();
    }

    public void saveConnection(ConnectionBuilder connIn) {
        Map.Entry<String, ConfigProfile> other = new AbstractMap.SimpleImmutableEntry<>("", new ConfigProfile());
        other.getValue().setConnProfile(new ConnProfile());
        other.getValue().setConfigName(connIn.getConnectionName());

        Map.Entry<String, ConfigProfile> cfg = configList.entrySet().stream()
                .filter(e -> e.getValue().getConfigName().equalsIgnoreCase(connIn.getConnectionName()))
                .findAny().orElse(other);

        try {
          savePassword(connIn, cfg);
        } catch (GeneralSecurityException e) {
          log.error(e.getMessage());
          throw new RuntimeException(e);
        }
        connIn.cleanPassword();

        ConnProfile connOut = cfg.getValue().getConnProfile();

        connOut.setConnName(connIn.getConnectionName());
        connOut.setUserName(connIn.getUserName());
        connOut.setUrl(connIn.getUrl());
        connOut.setJar(connIn.getJar());
        connOut.setProfileName(connIn.getProfile());
        connOut.setDriver(connIn.getDriverName());

        cfg.getValue().setInitialLoading(Integer.parseInt(connIn.getInitialLoading()));
        cfg.getValue().setRawRetainDays(getRawRetainDays());
        cfg.getValue().setOlapRetainDays(getOlapRetainDays());
        cfg.getValue().setEncryptionType(connIn.getEncryptionType());
        cfg.getValue().setContainerType(connIn.getContainerType());

        yamlConfig.saveConfigToFile(cfg.getValue());
    }

    private void savePassword(ConnectionBuilder connIn, Map.Entry<String, ConfigProfile> cfg) throws GeneralSecurityException {
      EncryptionType encryptionTypeCurrent = connIn.getEncryptionType();
      ContainerType containerTypeCurrent = connIn.getContainerType();

      ConnProfile connOut = cfg.getValue().getConnProfile();

      // Clean values from previous configuration
      ContainerType containerTypePrev = cfg.getValue().getContainerType();
      EncryptionType encryptionTypePrev = cfg.getValue().getEncryptionType();
      if (containerTypeCurrent != null && encryptionTypeCurrent != null
          && ContainerType.CONFIGURATION.equals(containerTypePrev)
          && containerTypeCurrent.equals(containerTypePrev)
          && !encryptionTypeCurrent.equals(encryptionTypePrev)) {
        cfg.getValue().setKey(null);
        cfg.getValue().setIv(null);
        connOut.setPassword(null);
      }

      // Save current values
      if (EncryptionType.PBE.equals(encryptionTypeCurrent)) {
        String passwordEncrypted = passConfig.encrypt(connIn.getPassword());
        if (ContainerType.DPAPI.equals(containerTypeCurrent)) {
          if(WinDPAPI.isPlatformSupported()) {
            try {
              WinDPAPI winDPAPI = WinDPAPI.newInstance(CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);
              byte[] cipherTextBytes = winDPAPI.protectData(passwordEncrypted.getBytes());
              cfg.getValue().setKey(containerConfig.convertByteToString(cipherTextBytes));

              cfg.getValue().setIv(null);
              connOut.setPassword(null);
            } catch (InitializationFailedException | WinAPICallFailedException e) {
              throw new RuntimeException(e);
            }
          } else {
            throw new RuntimeException("Windows Data Protection API (DPAPI) as secure container is not supported");
          }
        } else if (ContainerType.REGISTRY.equals(containerTypeCurrent)) {
          containerConfig.setRegistryValue(cfg.getValue().getConfigName(), RegistryKey.PASSWORD, passwordEncrypted);

          cfg.getValue().setKey(null);
          cfg.getValue().setIv(null);
          connOut.setPassword(null);
        } else if (ContainerType.CONFIGURATION.equals(containerTypeCurrent)) {
          connOut.setPassword(passwordEncrypted);

          cfg.getValue().setKey(null);
          cfg.getValue().setIv(null);
        }
      } else if (EncryptionType.AES.equals(encryptionTypeCurrent)) {
        SecretKey secretKey = bcFipsConfig.generateKey();
        byte[][] passwordEncrypted = bcFipsConfig.cfbEncrypt(secretKey, connIn.getPassword().getBytes());

        if (ContainerType.DPAPI.equals(containerTypeCurrent)) {
          if(WinDPAPI.isPlatformSupported()) {
            try {
              WinDPAPI winDPAPI = WinDPAPI.newInstance(CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);
              byte[] cipherKeyBytes = winDPAPI.protectData(containerConfig.convertSecretKeyToString(secretKey).getBytes());
              byte[] cipherIvBytes = winDPAPI.protectData(containerConfig.convertByteToString(passwordEncrypted[0]).getBytes());
              byte[] cipherPassEncBytes = winDPAPI.protectData(containerConfig.convertByteToString(passwordEncrypted[1]).getBytes());

              cfg.getValue().setKey(containerConfig.convertByteToString(cipherKeyBytes));
              cfg.getValue().setIv(containerConfig.convertByteToString(cipherIvBytes));
              connOut.setPassword(containerConfig.convertByteToString(cipherPassEncBytes));
            } catch (InitializationFailedException | WinAPICallFailedException e) {
              throw new RuntimeException(e);
            }
          } else {
            throw new RuntimeException("Windows Data Protection API (DPAPI) as secure container is not supported");
          }
        } else if (ContainerType.REGISTRY.equals(containerTypeCurrent)) {
          String cipherKey = containerConfig.convertSecretKeyToString(secretKey);
          String cipherIv = containerConfig.convertByteToString(passwordEncrypted[0]);
          String cipherPassEnc = containerConfig.convertByteToString(passwordEncrypted[1]);

          containerConfig.setRegistryValue(cfg.getValue().getConfigName(), RegistryKey.KEY, cipherKey);
          containerConfig.setRegistryValue(cfg.getValue().getConfigName(), RegistryKey.IV, cipherIv);
          containerConfig.setRegistryValue(cfg.getValue().getConfigName(), RegistryKey.PASSWORD, cipherPassEnc);

          cfg.getValue().setKey(null);
          cfg.getValue().setIv(null);
          connOut.setPassword(null);
        } else if (ContainerType.CONFIGURATION.equals(containerTypeCurrent)) {
          cfg.getValue().setKey(containerConfig.convertSecretKeyToString(secretKey));
          cfg.getValue().setIv(containerConfig.convertByteToString(passwordEncrypted[0]));
          connOut.setPassword(containerConfig.convertByteToString(passwordEncrypted[1]));
        }
      }
    }

    public String getPassword(ConfigProfile configProfile) {
      EncryptionType encryptionType = configProfile.getEncryptionType();
      ContainerType containerType = configProfile.getContainerType();

      if (encryptionType == null && containerType == null) {
        return passConfig.decrypt(configProfile.getConnProfile().getPassword());
      }

      String password = "";

      if (EncryptionType.PBE.equals(encryptionType)) {
        if (ContainerType.DPAPI.equals(containerType)) {
          if(WinDPAPI.isPlatformSupported()) {
            try {
              WinDPAPI winDPAPI = WinDPAPI.newInstance(CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);
              byte[] decryptedBytes = winDPAPI.unprotectData(containerConfig.convertStringToByte(configProfile.getKey()));
              password = passConfig.decrypt(new String(decryptedBytes));
            } catch (InitializationFailedException | WinAPICallFailedException e) {
              throw new RuntimeException(e);
            }
          } else {
            throw new RuntimeException("Windows Data Protection API (DPAPI) as secure container is not supported");
          }
        } else if (ContainerType.REGISTRY.equals(containerType)) {
          password = passConfig.decrypt(containerConfig.getRegistryValue(configProfile.getConfigName(), RegistryKey.PASSWORD));
        } else if (ContainerType.CONFIGURATION.equals(containerType)) {
          password = passConfig.decrypt(configProfile.getConnProfile().getPassword());
        }
      } else if (EncryptionType.AES.equals(encryptionType)) {
        if (ContainerType.DPAPI.equals(containerType)) {
            if(WinDPAPI.isPlatformSupported()) {
              try {
                WinDPAPI winDPAPI = WinDPAPI.newInstance(CryptProtectFlag.CRYPTPROTECT_UI_FORBIDDEN);

                String secretKeyStr = new String(winDPAPI.unprotectData(containerConfig.convertStringToByte(configProfile.getKey())));
                String ivStr = new String(winDPAPI.unprotectData(containerConfig.convertStringToByte(configProfile.getIv())));
                String pwdStr = new String(winDPAPI.unprotectData(containerConfig.convertStringToByte(configProfile.getConnProfile().getPassword())));

                try {
                  password = new String(bcFipsConfig.cfbDecrypt(containerConfig.convertStringToSecretKey(secretKeyStr),
                      containerConfig.convertStringToByte(ivStr), containerConfig.convertStringToByte(pwdStr)));
                } catch (GeneralSecurityException e) {
                  throw new RuntimeException(e);
                }

              } catch (InitializationFailedException | WinAPICallFailedException e) {
                throw new RuntimeException(e);
              }
            } else {
              throw new RuntimeException("Windows Data Protection API (DPAPI) as secure container is not supported");
            }
        } else if (ContainerType.REGISTRY.equals(containerType)) {
          String secretKeyStr = containerConfig.getRegistryValue(configProfile.getConfigName(), RegistryKey.KEY);
          String ivStr = containerConfig.getRegistryValue(configProfile.getConfigName(), RegistryKey.IV);
          String pwdStr = containerConfig.getRegistryValue(configProfile.getConfigName(), RegistryKey.PASSWORD);

          try {
            password = new String(bcFipsConfig.cfbDecrypt(containerConfig.convertStringToSecretKey(secretKeyStr),
                containerConfig.convertStringToByte(ivStr), containerConfig.convertStringToByte(pwdStr)));
          } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
          }
        } else if (ContainerType.CONFIGURATION.equals(containerType)) {
          SecretKey secretKey = containerConfig.convertStringToSecretKey(configProfile.getKey());
          byte[] iv = containerConfig.convertStringToByte(configProfile.getIv());
          byte[] passwordEncrypted = containerConfig.convertStringToByte(configProfile.getConnProfile().getPassword());

          try {
            password = new String(bcFipsConfig.cfbDecrypt(secretKey, iv, passwordEncrypted));
          } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
          }
        }

      }

      return password;
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
      Profile profile = Profile.getValue(profileName);
      if (profile == null) {
        throw new IllegalArgumentException("Invalid profile name: " + profileName);
      }
      switch (profile) {
        case OracleEE:
          return new OracleEE();
        case OracleEE10g:
          return new OracleEE10g();
        case OracleEEObject:
          return new OracleEEObject();
        case OracleSE:
          return new OracleSE();
        case Postgres:
          return new Postgres();
        case Postgres96:
          return new Postgres96();
        default :
          throw new IllegalArgumentException("Unsupported profile name: " + profileName);
      }
    }

}
