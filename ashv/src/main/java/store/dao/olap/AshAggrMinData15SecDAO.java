package store.dao.olap;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.olap.AshAggrMinData15Sec;
import store.entity.olap.CompositeKey;

public class AshAggrMinData15SecDAO {

    private EntityStore store;
    private AggrDAO aggrDAO;

    private PrimaryIndex<CompositeKey, AshAggrMinData15Sec> ashAggrMinDataPrimaryIndex;
    private SecondaryIndex<Long, CompositeKey, AshAggrMinData15Sec> ashAggrMinDataSecondaryIndexDateId;

    public AshAggrMinData15SecDAO(EntityStore store, AggrDAO aggrDAO) throws DatabaseException {
        this.store = store;
        this.aggrDAO = aggrDAO;

        this.ashAggrMinDataPrimaryIndex = store.getPrimaryIndex(CompositeKey.class, AshAggrMinData15Sec.class);
        this.ashAggrMinDataSecondaryIndexDateId = store.getSecondaryIndex(ashAggrMinDataPrimaryIndex, Long.class, "dateId");
    }

    public boolean putDataNoOverwrite(AshAggrMinData15Sec iAshAggrMinDataDAO) {
        return this.ashAggrMinDataPrimaryIndex.putNoOverwrite(iAshAggrMinDataDAO);
    }

    public void putDataOverwrite(AshAggrMinData15Sec iAshAggrMinDataDAO) {
        this.ashAggrMinDataPrimaryIndex.put(iAshAggrMinDataDAO);
    }

    public boolean isCompositeKeyExist(CompositeKey compositeKeyStart){
        return this.ashAggrMinDataPrimaryIndex.contains(compositeKeyStart);
    }

    public AshAggrMinData15Sec getAshAggrMinDataRange(long dateId, int paramId) {
        return this.ashAggrMinDataPrimaryIndex.get(new CompositeKey(dateId, paramId));
    }

    public EntityCursor<AshAggrMinData15Sec> getAshAggrEntityCursorRangeQuery(long start, long end) {
        return this.aggrDAO.doRangeQuery(this.ashAggrMinDataSecondaryIndexDateId, start, true, end, true);
    }
}
