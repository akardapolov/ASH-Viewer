package remote;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbcp2.BasicDataSource;
import pojo.ConnectionMetadata;

import javax.inject.Inject;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class RemoteDBManager {

    private ConnectionMetadata connectionMetadata;
    @Getter private BasicDataSource basicDataSource;

    @Inject
    public RemoteDBManager(){ }

    public void init(ConnectionMetadata connectionMetadata) {
        try {
            this.connectionMetadata = connectionMetadata;
            this.basicDataSource = new BasicDataSource();
            this.basicDataSource.setDriverClassLoader(getClassLoader());
            this.basicDataSource.setDriverClassName(this.connectionMetadata.getDriver());
            this.basicDataSource.setUrl(this.connectionMetadata.getUrl());
            this.basicDataSource.setUsername(this.connectionMetadata.getUserName());
            this.basicDataSource.setPassword(this.connectionMetadata.getPassword());
            this.basicDataSource.setInitialSize(2);

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
        Connection connection = this.basicDataSource.getConnection();
        return connection;
    }

    private ClassLoader getClassLoader()throws ClassNotFoundException, InstantiationException, IllegalAccessException, MalformedURLException {
        URL url = new URL("file:/" + this.connectionMetadata.getJar());
        URLClassLoader ucl = new URLClassLoader(new URL[]{url});
        return ucl;
    }

}
