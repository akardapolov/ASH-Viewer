package core.parameter;

import gui.model.ContainerType;
import gui.model.EncryptionType;

public class ConnectionBuilder {
    private final String connectionName;
    private final String userName;
    private String password;
    private final String url;
    private final String jar;
    private final String profile;
    private final String driverName;
    private final String initialLoading;
    private final String rawRetainDays;
    private final String olapRetainDays;
    private final EncryptionType encryptionType;
    private final ContainerType containerType;

    public String getConnectionName() { return connectionName; }
    public String getUserName() { return userName; }
    public String getPassword() { return password; }
    public String getUrl() { return url; }
    public String getJar() { return jar; }
    public String getProfile() { return profile; }
    public String getDriverName() { return driverName; }
    public String getInitialLoading() { return initialLoading; }
    public String getRawRetainDays() { return rawRetainDays; }
    public String getOlapRetainDays() { return olapRetainDays; }
    public EncryptionType getEncryptionType() { return encryptionType; }
    public ContainerType getContainerType() { return containerType; }

    public void cleanPassword() {
        password = "";
    }

    public static class Builder {
        private final String connectionName;
        private String userName;
        private String password;
        private String url;
        private String jar;
        private String profile;
        private String driverName;
        private String initialLoading;
        private String rawRetainDays;
        private String olapRetainDays;
        private EncryptionType encryptionType;
        private ContainerType containerType;

        public Builder(String connectionName) {
            this.connectionName = connectionName;
        }

        public Builder userName(String un) { userName = un; return this; }
        public Builder password(String pw) { password = pw; return this; }
        public Builder url(String ul) { url = ul; return this; }
        public Builder jar(String jr) { jar = jr; return this; }
        public Builder profile(String pr) { profile = pr; return this; }
        public Builder driverName(String dr) { driverName = dr; return this; }
        public Builder initialLoading(String il) { initialLoading = il; return this; }
        public Builder rawRetainDays(String raw) { rawRetainDays = raw; return this; }
        public Builder olapRetainDays(String raw) { olapRetainDays = raw; return this; }
        public Builder encryptionType(EncryptionType raw) { encryptionType = raw; return this; }
        public Builder containerType(ContainerType raw) { containerType = raw; return this; }

        public ConnectionBuilder build() {
            return new ConnectionBuilder(this);
        }
    }

    private ConnectionBuilder(Builder builder){
        connectionName = builder.connectionName;
        userName = builder.userName;
        password = builder.password;
        url = builder.url;
        jar = builder.jar;
        profile = builder.profile;
        driverName = builder.driverName;
        initialLoading = builder.initialLoading;
        rawRetainDays = builder.rawRetainDays;
        olapRetainDays = builder.olapRetainDays;
        encryptionType = builder.encryptionType;
        containerType = builder.containerType;
    }

}
