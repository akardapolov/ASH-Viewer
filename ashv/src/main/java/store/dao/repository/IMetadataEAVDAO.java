package store.dao.repository;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.repository.MetadataEAV;

public interface IMetadataEAVDAO {

    boolean putMainDataEAVWithCheck(String moduleName, String entity, String attribute, String value);
    void putMainDataEAVWithoutCheck(String moduleName, String entity, String attribute, String value);
    void deleteMainDataEAVWithCheck(String moduleName, String entity);

    EntityCursor<MetadataEAV> getEntityCursorPrimary();

    SecondaryIndex<String, Long, MetadataEAV> getModuleSecondaryIndex();
    SecondaryIndex<String, Long, MetadataEAV> getEntitySecondaryIndex();
}
