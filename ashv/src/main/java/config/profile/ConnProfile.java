package config.profile;

import lombok.Data;

@Data
public class ConnProfile {
    private String connName;
    private String userName;
    private String password;
    private String url;
    private String jar;
    private String driver;
    private String profileName;
}
