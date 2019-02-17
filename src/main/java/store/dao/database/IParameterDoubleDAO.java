package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.database.ParameterDouble;

public interface IParameterDoubleDAO {

    int getCheckOrLoadParameter(double parameter);
    double getParameterStrById(int id);

    PrimaryIndex<Integer, ParameterDouble> getRdaStringParameterPrimIndex();
    SecondaryIndex<Double, Integer, ParameterDouble> getRdaStringParameterSecIndex();

    EntityCursor<ParameterDouble> getEntityCursorPrimary();
    EntityCursor<ParameterDouble> getEntityCursorSecondary();
}
