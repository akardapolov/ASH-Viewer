package store.dao.olap;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.OlapDAO;
import store.entity.olap.AshAggrMinData1Min;
import store.entity.olap.CompositeKey;

public class AshAggrMinData1MinDAO implements IAshAggrMinData1MinDAO {

    private EntityStore store;
    private OlapDAO olapDAO;

    private PrimaryIndex<CompositeKey, AshAggrMinData1Min> ashAggrMinDataPrimaryIndex;
    private SecondaryIndex<Long, CompositeKey, AshAggrMinData1Min> ashAggrMinDataSecondaryIndexDateId;

    public AshAggrMinData1MinDAO(EntityStore store, OlapDAO olapDAO) throws DatabaseException {
        this.store = store;
        this.olapDAO = olapDAO;

        this.ashAggrMinDataPrimaryIndex = store.getPrimaryIndex(CompositeKey.class, AshAggrMinData1Min.class);
        this.ashAggrMinDataSecondaryIndexDateId = store.getSecondaryIndex(ashAggrMinDataPrimaryIndex, Long.class, "dateId");
    }

    @Override
    public boolean putDataNoOverwrite(AshAggrMinData1Min iAshAggrMinDataDAO) {
        return this.ashAggrMinDataPrimaryIndex.putNoOverwrite(iAshAggrMinDataDAO);
    }

    @Override
    public AshAggrMinData1Min getAshAggrMinDataRange(long dateId, int paramId) {
        return this.ashAggrMinDataPrimaryIndex.get(new CompositeKey(dateId, paramId));
    }

    @Override
    public EntityCursor<AshAggrMinData1Min> getAshAggrEntityCursorRangeQuery(long start, long end) {
        return this.olapDAO.doRangeQuery(this.ashAggrMinDataSecondaryIndexDateId, start, true, end, true);
    }
}
