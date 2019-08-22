package store.dao.olap;

import com.sleepycat.persist.EntityCursor;
import store.entity.olap.AshAggrMinData;

public interface IAshAggrMinDataDAO {
    boolean putDataNoOverwrite(AshAggrMinData iAshAggrMinDataDAO);
    AshAggrMinData getAshAggrMinDataRange(long dateId, int paramId);
    EntityCursor<AshAggrMinData> getAshAggrEntityCursorRangeQuery(long start, long end);
}
