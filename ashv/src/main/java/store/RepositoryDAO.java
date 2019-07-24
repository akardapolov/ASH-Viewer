package store;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import config.Labels;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import pojo.ConnectionMetadata;
import pojo.SqlColMetadata;
import store.dao.repository.IMetadataEAVDAO;
import store.dao.repository.MetadataEAVDAO;
import store.entity.repository.MetadataEAV;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Singleton
public class RepositoryDAO {
    private BerkleyDB berkleyDB;
    @Getter
    public IMetadataEAVDAO metadataEAVDAO;

    @Inject
    public RepositoryDAO(BerkleyDB berkleyDB) throws DatabaseException {
        this.berkleyDB = berkleyDB;
        this.metadataEAVDAO = new MetadataEAVDAO(this.berkleyDB.getStore());
    }

    public List<ConnectionMetadata> getModuleMetadata(String moduleName){
        List<ConnectionMetadata> out = new ArrayList<>();
        Set<String> list = new HashSet<>();

        EntityCursor<MetadataEAV> mainDataEAV =
                this.metadataEAVDAO.getModuleSecondaryIndex().subIndex(moduleName).entities();

        try {
            for (MetadataEAV metaEAV : mainDataEAV) {
                if (metaEAV.getModuleName().equalsIgnoreCase(moduleName) &&
                        metaEAV.getAttribute().equalsIgnoreCase(Labels.getLabel("local.sql.metadata.connection.name"))) {
                    list.add(metaEAV.getValue());
                }
            }
        } finally {
            mainDataEAV.close();
        }

        list.stream().forEach(e -> {
            ConnectionMetadata sqlColMetadata = new ConnectionMetadata();
            sqlColMetadata.setConnName(this.getMetaDataAttributeValue(moduleName, e, Labels.getLabel("local.sql.metadata.connection.name")));
            sqlColMetadata.setUserName(this.getMetaDataAttributeValue(moduleName, e, Labels.getLabel("local.sql.metadata.connection.username")));
            sqlColMetadata.setPassword(this.getMetaDataAttributeValue(moduleName, e, Labels.getLabel("local.sql.metadata.connection.password")));
            sqlColMetadata.setUrl(this.getMetaDataAttributeValue(moduleName, e, Labels.getLabel("local.sql.metadata.connection.url")));
            sqlColMetadata.setJar(this.getMetaDataAttributeValue(moduleName, e, Labels.getLabel("local.sql.metadata.connection.jar")));
            sqlColMetadata.setProfileName(this.getMetaDataAttributeValue(moduleName, e, Labels.getLabel("local.sql.metadata.connection.profile")));
            sqlColMetadata.setDriver(this.getMetaDataAttributeValue(moduleName, e, Labels.getLabel("local.sql.metadata.connection.driver")));

            out.add(sqlColMetadata);
        });

        return out;
    }

    public String getMetaDataAttributeValue(String moduleName,
                                            String entity,
                                            String attribute){

        String out = "";
        EntityCursor<MetadataEAV> mainDataEAV = this.metadataEAVDAO.getEntitySecondaryIndex().subIndex(entity).entities();

        try {
            for (MetadataEAV metaEAV : mainDataEAV) {
                if (metaEAV.getModuleName().equalsIgnoreCase(moduleName)&&
                        metaEAV.getEntity().equalsIgnoreCase(entity)&&
                        metaEAV.getAttribute().equalsIgnoreCase(attribute))
                    out = metaEAV.getValue();
            }
        } finally {
            mainDataEAV.close();
        }

        return out;
    }


    public List<SqlColMetadata> getSqlColDbTypeMetadata(String moduleName){
        List<SqlColMetadata> out = new ArrayList<>();
        Set<String> list = new HashSet<>();

        EntityCursor<MetadataEAV> mainDataEAV =
                this.metadataEAVDAO.getModuleSecondaryIndex().subIndex(moduleName).entities();

        try {
            for (MetadataEAV metaEAV : mainDataEAV) {
                if (metaEAV.getModuleName().equalsIgnoreCase(moduleName) &&
                        metaEAV.getAttribute().equalsIgnoreCase(Labels.getLabel("local.sql.metadata.columnId"))) {
                    list.add(metaEAV.getEntity());
                }
            }
        } finally {
            mainDataEAV.close();
        }

        list.forEach(e -> {

            SqlColMetadata sqlColMetadata = new SqlColMetadata();
            sqlColMetadata.setColId(Integer.parseInt(this.getMetaDataAttributeValue(moduleName, e, Labels.getLabel("local.sql.metadata.columnId")))); sqlColMetadata.setColName(e);
            sqlColMetadata.setColDbTypeName(this.getMetaDataAttributeValue(moduleName, e, Labels.getLabel("local.sql.metadata.columnType")));

            out.add(sqlColMetadata);
        });

        return out;
    }


}
