package store;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import store.dao.database.*;

@Slf4j
public class DatabaseDAO {
    private BerkleyDB berkleyDB;

    @Getter public IMainDataDAO mainDataDAO;

    @Getter public IParameterStringDAO parameterStringDAO;
    @Getter public IParameterDoubleDAO parameterDoubleDAO;

    @Getter public ISqlPlan iSqlPlan;

    @Getter public IParamStringStringDAO paramStringStringDAO;

    @Getter public OlapDAO olapDAO;

    public DatabaseDAO (BerkleyDB berkleyDB) throws DatabaseException {
        this.berkleyDB = berkleyDB;
        this.mainDataDAO = new MainDataDAO(berkleyDB.getStore());

        this.parameterStringDAO = new ParameterStringDAO(berkleyDB.getStore());
        this.parameterDoubleDAO = new ParameterDoubleDAO(berkleyDB.getStore());

        this.iSqlPlan = new SqlPlanDAO(berkleyDB.getStore());

        this.paramStringStringDAO = new ParamStringStringDAO(berkleyDB.getStore());

        this.olapDAO = new OlapDAO(berkleyDB);
    }

    public <K, V> EntityCursor<V> doRangeQuery(EntityIndex<K, V> index,
                                               K fromKey,
                                               boolean fromInclusive,
                                               K toKey,
                                               boolean toInclusive)
            throws DatabaseException {

        assert (index != null);

        return index.entities(fromKey,
                fromInclusive,
                toKey,
                toInclusive);
    }

}
