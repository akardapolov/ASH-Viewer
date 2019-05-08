package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import store.entity.database.MainData;

public interface IMainDataDAO {
    public boolean putMainDataNoOverwrite(MainData mainData);
    EntityCursor<MainData> getEntityCursorPrimary();
}
