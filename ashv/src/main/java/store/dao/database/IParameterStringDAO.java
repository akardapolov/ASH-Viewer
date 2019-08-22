package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import store.entity.database.ParameterString;

public interface IParameterStringDAO {

    int getCheckOrLoadParameter(String parameter);
    String getParameterStrById(int id);
    EntityCursor<ParameterString> getEntityCursorPrimary();
}
