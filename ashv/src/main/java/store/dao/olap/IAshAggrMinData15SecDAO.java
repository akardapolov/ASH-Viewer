package store.dao.olap;

import com.sleepycat.persist.EntityCursor;
import store.entity.olap.AshAggrMinData15Sec;

public interface IAshAggrMinData15SecDAO {
    boolean putDataNoOverwrite(AshAggrMinData15Sec iAshAggrMinDataDAO);
    AshAggrMinData15Sec getAshAggrMinDataRange(long dateId, int paramId);
    EntityCursor<AshAggrMinData15Sec> getAshAggrEntityCursorRangeQuery(long start, long end);
}
