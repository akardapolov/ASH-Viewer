package config.profile;

import lombok.Data;

import java.util.List;

@Data
public class ConfigProfile {
    private String configName;
    private boolean isRunning;

    private int rawRetainDays;
    private int olapRetainDays;

    private ConnProfile connProfile;
    private List<SqlColProfile> sqlColProfileList;
}
