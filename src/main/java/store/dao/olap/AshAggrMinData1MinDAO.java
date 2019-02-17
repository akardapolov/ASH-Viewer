package store.dao.olap;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.olap.AshAggrMinData1Min;
import store.entity.olap.CompositeKey;

public class AshAggrMinData1MinDAO {

    private EntityStore store;
    private AggrDAO aggrDAO;

    private PrimaryIndex<CompositeKey, AshAggrMinData1Min> ashAggrMinDataPrimaryIndex;
    private SecondaryIndex<Long, CompositeKey, AshAggrMinData1Min> ashAggrMinDataSecondaryIndexDateId;

    public AshAggrMinData1MinDAO(EntityStore store, AggrDAO aggrDAO) throws DatabaseException {
        this.store = store;
        this.aggrDAO = aggrDAO;

        this.ashAggrMinDataPrimaryIndex = store.getPrimaryIndex(CompositeKey.class, AshAggrMinData1Min.class);
        this.ashAggrMinDataSecondaryIndexDateId = store.getSecondaryIndex(ashAggrMinDataPrimaryIndex, Long.class, "dateId");
    }

    public boolean putDataNoOverwrite(AshAggrMinData1Min iAshAggrMinDataDAO) {
        return this.ashAggrMinDataPrimaryIndex.putNoOverwrite(iAshAggrMinDataDAO);
    }

    public void putDataOverwrite(AshAggrMinData1Min iAshAggrMinDataDAO) {
        this.ashAggrMinDataPrimaryIndex.put(iAshAggrMinDataDAO);
    }

    public boolean isCompositeKeyExist(CompositeKey compositeKeyStart){
        return this.ashAggrMinDataPrimaryIndex.contains(compositeKeyStart);
    }

    public AshAggrMinData1Min getAshAggrMinDataRange(long dateId, int paramId) {
        return this.ashAggrMinDataPrimaryIndex.get(new CompositeKey(dateId, paramId));
    }

    public EntityCursor<AshAggrMinData1Min> getAshAggrEntityCursorRangeQuery(long start, long end) {
        return this.aggrDAO.doRangeQuery(this.ashAggrMinDataSecondaryIndexDateId, start, true, end, true);
    }
}
