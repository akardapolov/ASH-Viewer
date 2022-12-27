package config.profile;

import core.manager.ConstantManager;
import gui.model.ContainerType;
import gui.model.EncryptionType;
import lombok.Data;

import java.util.List;

@Data
public class ConfigProfile {
    private String configName;
    private boolean isRunning;

    private int initialLoading = ConstantManager.INITIAL_LOADING;

    private int rawRetainDays = ConstantManager.RETAIN_DAYS_MAX;
    private int olapRetainDays = ConstantManager.RETAIN_DAYS_MAX;

    private EncryptionType encryptionType;
    private ContainerType containerType;
    private String key;
    private String iv;

    private ConnProfile connProfile;
    private List<SqlColProfile> sqlColProfileList;
}
