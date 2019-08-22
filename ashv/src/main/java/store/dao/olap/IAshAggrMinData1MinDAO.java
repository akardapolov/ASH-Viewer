package store.dao.olap;

import com.sleepycat.persist.EntityCursor;
import store.entity.olap.AshAggrMinData1Min;

public interface IAshAggrMinData1MinDAO {
    boolean putDataNoOverwrite(AshAggrMinData1Min iAshAggrMinDataDAO);

    AshAggrMinData1Min getAshAggrMinDataRange(long dateId, int paramId);

    EntityCursor<AshAggrMinData1Min> getAshAggrEntityCursorRangeQuery(long start, long end);
}
