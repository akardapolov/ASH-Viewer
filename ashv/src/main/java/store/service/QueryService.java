package store.service;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;

public abstract class QueryService {
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
