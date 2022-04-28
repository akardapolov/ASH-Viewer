package remote;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.commons.dbcp2.BasicDataSource;

import config.profile.ConnProfile;
import config.security.PassConfig;
import core.manager.ConstantManager.Profile;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class RemoteDBManager {
    private PassConfig passConfig;
    private ConnProfile connProfile;
    @Getter private BasicDataSource basicDataSource;

    @Inject
    public RemoteDBManager(PassConfig passConfig){
        this.passConfig = passConfig;
    }

    public void init(ConnProfile connProfile) {
        try {
            this.connProfile = connProfile;
            this.basicDataSource = new BasicDataSource();
            this.basicDataSource.setDriverClassLoader(getClassLoader());
            this.basicDataSource.setDriverClassName(this.connProfile.getDriver());
            this.basicDataSource.setUrl(this.connProfile.getUrl());
            this.basicDataSource.setUsername(this.connProfile.getUserName());
            this.basicDataSource.setPassword(passConfig.decrypt(this.connProfile.getPassword()));
            this.basicDataSource.setInitialSize(3);
            this.basicDataSource.setMaxTotal(5);

        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    public Connection getConnection() throws SQLException {
        Connection connection = this.basicDataSource.getConnection();
        bootstrapConnection(connection);
        return connection;
    }

    private void bootstrapConnection(Connection connection) throws SQLException {
      Profile profile = Profile.getValue(connProfile.getProfileName());
      switch (profile) {
        case OracleEE :
        case OracleEE10g :
        case OracleEEObject :
        case OracleSE :
          log.debug("Setting optimizer_mode = 'ALL_ROWS'");
          try (Statement stmt = connection.createStatement() ) {
            stmt.executeUpdate("ALTER SESSION SET optimizer_mode = 'ALL_ROWS'");
          }
          break;
        case Postgres :
          // no action
          break;
        case Postgres96 :
          // no action
          break;
        default :
          throw new IllegalArgumentException("Unsupported profile name: " + connProfile.getProfileName());
      }
    }

    private ClassLoader getClassLoader() throws MalformedURLException {
        URL url = new File(this.connProfile.getJar().trim()).toURI().toURL();
        return new URLClassLoader(new URL[]{url});
    }

}
