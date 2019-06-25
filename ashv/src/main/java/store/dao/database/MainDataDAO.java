package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import store.entity.database.MainData;

public class MainDataDAO implements IMainDataDAO {
    private EntityStore store;

    private PrimaryIndex<Long, MainData> mainDataPrimaryIndex;

    public MainDataDAO(EntityStore store) {
        this.store = store;
        this.mainDataPrimaryIndex = store.getPrimaryIndex(Long.class, MainData.class);
    }

    public boolean putMainDataNoOverwrite(MainData mainData){
        return this.mainDataPrimaryIndex.putNoOverwrite(mainData);
    }

    @Override
    public EntityCursor<MainData> getEntityCursorPrimary() {
        return this.mainDataPrimaryIndex.entities();
    }

    @Override
    public PrimaryIndex<Long, MainData> getPrimaryIndex() {
        return mainDataPrimaryIndex;
    }

}
