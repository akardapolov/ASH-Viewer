package store.dao.repository;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.repository.MetadataEAV;

public interface IMetadataEAVDAO {

    boolean putMainDataEAVWithCheck(String moduleName, String entity, String attribute, String value);
    void putMainDataEAVOverwrite(String moduleName, String entity, String attribute, String value);
    void putMainDataEAVWithoutCheck(String moduleName, String entity, String attribute, String value);
    void deleteMainDataEAVWithCheck(String moduleName, String entity, String attribute);
    void deleteMainDataEAVWithCheck(String moduleName, String entity);
    void deleteMainDataEAVByModule(String moduleName);

    EntityCursor<MetadataEAV> getEntityCursorPrimary();
    EntityCursor<MetadataEAV> getModuleCursorSecondary();
    EntityCursor<MetadataEAV> getEntityCursorSecondary();

    SecondaryIndex<String, Long, MetadataEAV> getModuleSecondaryIndex();
    SecondaryIndex<String, Long, MetadataEAV> getEntitySecondaryIndex();
}
