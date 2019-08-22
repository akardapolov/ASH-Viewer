package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.database.SqlPlan;

public class SqlPlanDAO implements ISqlPlan{
    private EntityStore store;

    private PrimaryIndex<Long, SqlPlan> mainDataPrimaryIndex;
    private SecondaryIndex<String, Long, SqlPlan> sqlIdSecIndex;
    private SecondaryIndex<Long, Long, SqlPlan> planHashValueSecIndex;

    public SqlPlanDAO(EntityStore store){
        this.store = store;
        this.mainDataPrimaryIndex = store.getPrimaryIndex(Long.class, SqlPlan.class);
        this.sqlIdSecIndex = store.getSecondaryIndex(mainDataPrimaryIndex, String.class, "sqlId");
        this.planHashValueSecIndex = store.getSecondaryIndex(mainDataPrimaryIndex, Long.class, "planHashValue");

    }

    @Override
    public boolean putSqlPlanNoOverwrite(SqlPlan sqlPlan) {
        return this.mainDataPrimaryIndex.putNoOverwrite(sqlPlan);
    }

    @Override
    public boolean checkSqlIdAndPlanHashValueExist(String sqlId, Long planHashValue) {
        boolean isExist = false;

        EntityCursor<SqlPlan> sqlIdData = this.getEnityCurSqlId().subIndex(sqlId).entities();

        try {
            for (SqlPlan metaEAV : sqlIdData) {
                if (metaEAV.getPlanHashValue() == planHashValue)
                    isExist = true;
            }
        } finally {
            sqlIdData.close();
        }

        return isExist;
    }

    @Override
    public EntityCursor<SqlPlan> getEntityCursorPrimary() { return mainDataPrimaryIndex.entities(); }
    @Override
    public SecondaryIndex<String, Long, SqlPlan> getEnityCurSqlId() { return sqlIdSecIndex; }
    @Override
    public SecondaryIndex<Long, Long, SqlPlan> getEnityCurPlanHashValue() { return planHashValueSecIndex; }
}
