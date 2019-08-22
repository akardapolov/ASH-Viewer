package store.dao.olap;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.OlapDAO;
import store.entity.olap.AshAggrMinData15Sec;
import store.entity.olap.CompositeKey;

public class AshAggrMinData15SecDAO implements IAshAggrMinData15SecDAO {
    private EntityStore store;
    private OlapDAO olapDAO;

    private PrimaryIndex<CompositeKey, AshAggrMinData15Sec> ashAggrMinDataPrimaryIndex;
    private SecondaryIndex<Long, CompositeKey, AshAggrMinData15Sec> ashAggrMinDataSecondaryIndexDateId;

    public AshAggrMinData15SecDAO(EntityStore store, OlapDAO olapDAO) throws DatabaseException {
        this.store = store;
        this.olapDAO = olapDAO;

        this.ashAggrMinDataPrimaryIndex = store.getPrimaryIndex(CompositeKey.class, AshAggrMinData15Sec.class);
        this.ashAggrMinDataSecondaryIndexDateId = store.getSecondaryIndex(ashAggrMinDataPrimaryIndex, Long.class, "dateId");
    }

    @Override
    public boolean putDataNoOverwrite(AshAggrMinData15Sec iAshAggrMinDataDAO) {
        return this.ashAggrMinDataPrimaryIndex.putNoOverwrite(iAshAggrMinDataDAO);
    }

    @Override
    public AshAggrMinData15Sec getAshAggrMinDataRange(long dateId, int paramId) {
        return this.ashAggrMinDataPrimaryIndex.get(new CompositeKey(dateId, paramId));
    }

    @Override
    public EntityCursor<AshAggrMinData15Sec> getAshAggrEntityCursorRangeQuery(long start, long end) {
        return this.olapDAO.doRangeQuery(this.ashAggrMinDataSecondaryIndexDateId, start, true, end, true);
    }
}
