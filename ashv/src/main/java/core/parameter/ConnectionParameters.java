package core.parameter;

public class ConnectionParameters {
    private final String connectionName;
    private final String userName;
    private final String password;
    private final String url;
    private final String jar;
    private final String profile;
    private final String driverName;
    private final String rawRetainDays;

    public String getConnectionName() { return connectionName; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getUrl() { return url; }
    public String getJar() { return jar; }
    public String getProfile() { return profile; }
    public String getDriverName() { return driverName; }
    public String getRawRetainDays() { return rawRetainDays; }

    public static class Builder {
        private final String connectionName;
        private String userName;
        private String password;
        private String url;
        private String jar;
        private String profile;
        private String driverName;
        private String rawRetainDays;

        public Builder(String connectionName) {
            this.connectionName = connectionName;
        }

        public Builder userName(String un) { userName = un; return this; }
        public Builder password(String pw) { password = pw; return this; }
        public Builder url(String ul) { url = ul; return this; }
        public Builder jar(String jr) { jar = jr; return this; }
        public Builder profile(String pr) { profile = pr; return this; }
        public Builder driverName(String dr) { driverName = dr; return this; }
        public Builder rawRetainDays(String raw) { rawRetainDays = raw; return this; }

        public ConnectionParameters build() {
            return new ConnectionParameters(this);
        }
    }

    private ConnectionParameters(Builder builder){
        connectionName = builder.connectionName;
        userName = builder.userName;
        password = builder.password;
        url = builder.url;
        jar = builder.jar;
        profile = builder.profile;
        driverName = builder.driverName;
        rawRetainDays = builder.rawRetainDays;
    }

}
