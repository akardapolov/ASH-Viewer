package remote;

import config.security.PassConfig;
import javax.inject.Singleton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import config.profile.ConnProfile;

import javax.inject.Inject;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;

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

        } catch (ClassNotFoundException e) {
            log.error(e.toString());
        } catch (MalformedURLException e) {
            log.error(e.toString());
        } catch (InstantiationException e) {
            log.error(e.toString());
        } catch (IllegalAccessException e) {
            log.error(e.toString());
        }
    }

    public Connection getConnection() throws SQLException {
        return this.basicDataSource.getConnection();
    }

    private ClassLoader getClassLoader() throws ClassNotFoundException, InstantiationException, IllegalAccessException, MalformedURLException {
        URL url = new File(this.connProfile.getJar().trim()).toURI().toURL();
        URLClassLoader ucl = new URLClassLoader(new URL[]{url});
        return ucl;
    }

}
