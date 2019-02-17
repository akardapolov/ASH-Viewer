package store.dao.olap;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.olap.AshAggrMinData;
import store.entity.olap.CompositeKey;

public class AshAggrMinDataDAO {

    private EntityStore store;
    private AggrDAO aggrDAO;

    private PrimaryIndex<CompositeKey, AshAggrMinData> ashAggrMinDataPrimaryIndex;
    private SecondaryIndex<Long, CompositeKey, AshAggrMinData> ashAggrMinDataSecondaryIndexDateId;

    public AshAggrMinDataDAO(EntityStore store, AggrDAO aggrDAO) throws DatabaseException {
        this.store = store;
        this.aggrDAO = aggrDAO;

        this.ashAggrMinDataPrimaryIndex = store.getPrimaryIndex(CompositeKey.class, AshAggrMinData.class);
        this.ashAggrMinDataSecondaryIndexDateId = store.getSecondaryIndex(ashAggrMinDataPrimaryIndex, Long.class, "dateId");
    }

    public boolean putDataNoOverwrite(AshAggrMinData iAshAggrMinDataDAO) {
        return this.ashAggrMinDataPrimaryIndex.putNoOverwrite(iAshAggrMinDataDAO);
    }

    public void putDataOverwrite(AshAggrMinData iAshAggrMinDataDAO) {
        this.ashAggrMinDataPrimaryIndex.put(iAshAggrMinDataDAO);
    }

    public boolean isCompositeKeyExist(CompositeKey compositeKeyStart){
        return this.ashAggrMinDataPrimaryIndex.contains(compositeKeyStart);
    }

    public AshAggrMinData getAshAggrMinDataRange(long dateId, int paramId) {
        return this.ashAggrMinDataPrimaryIndex.get(new CompositeKey(dateId, paramId));
    }

    public EntityCursor<AshAggrMinData> getAshAggrEntityCursorRangeQuery(long start, long end) {
        return this.aggrDAO.doRangeQuery(this.ashAggrMinDataSecondaryIndexDateId, start, true, end, true);
    }
}
