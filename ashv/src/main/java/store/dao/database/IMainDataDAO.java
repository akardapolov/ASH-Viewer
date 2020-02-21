package store.dao.database;

import com.sleepycat.persist.PrimaryIndex;
import store.entity.database.MainData;

public interface IMainDataDAO {
    boolean putMainDataNoOverwrite(MainData mainData);
    PrimaryIndex<Long, MainData> getPrimaryIndex();
}
