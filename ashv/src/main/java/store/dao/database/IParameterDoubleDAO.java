package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import store.entity.database.ParameterDouble;

public interface IParameterDoubleDAO {

    int getCheckOrLoadParameter(double parameter);
    double getParameterStrById(int id);
    EntityCursor<ParameterDouble> getEntityCursorPrimary();
}
