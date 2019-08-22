package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.database.SqlPlan;

public interface ISqlPlan {
    public boolean putSqlPlanNoOverwrite(SqlPlan sqlPlan);
    public boolean checkSqlIdAndPlanHashValueExist(String sqlId, Long planHashValue);
    public EntityCursor<SqlPlan> getEntityCursorPrimary();
    public SecondaryIndex<String, Long, SqlPlan> getEnityCurSqlId();
    public SecondaryIndex<Long, Long, SqlPlan> getEnityCurPlanHashValue();
}
