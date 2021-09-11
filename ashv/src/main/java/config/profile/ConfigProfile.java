package config.profile;

import core.manager.ConstantManager;
import lombok.Data;

import java.util.List;

@Data
public class ConfigProfile {
    private String configName;
    private boolean isRunning;

    private int initialLoading = ConstantManager.INITIAL_LOADING;

    private int rawRetainDays = ConstantManager.RETAIN_DAYS_MAX;
    private int olapRetainDays = ConstantManager.RETAIN_DAYS_MAX;

    private ConnProfile connProfile;
    private List<SqlColProfile> sqlColProfileList;
}
