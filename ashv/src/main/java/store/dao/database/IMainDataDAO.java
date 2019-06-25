package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;
import store.entity.database.MainData;

public interface IMainDataDAO {
    public boolean putMainDataNoOverwrite(MainData mainData);
    EntityCursor<MainData> getEntityCursorPrimary();
    PrimaryIndex<Long, MainData> getPrimaryIndex();
}
