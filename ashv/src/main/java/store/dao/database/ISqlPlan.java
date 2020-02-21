package store.dao.database;

import com.sleepycat.persist.SecondaryIndex;
import store.entity.database.SqlPlan;

public interface ISqlPlan {
    boolean putSqlPlanNoOverwrite(SqlPlan sqlPlan);
    boolean checkSqlIdAndPlanHashValueExist(String sqlId, Long planHashValue);
    SecondaryIndex<String, Long, SqlPlan> getEnityCurSqlId();
    SecondaryIndex<Long, Long, SqlPlan> getEnityCurPlanHashValue();
}
