package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.database.ParameterString;

public interface IParameterStringDAO {

    int getCheckOrLoadParameter(String parameter);
    String getParameterStrById(int id);

    PrimaryIndex<Integer, ParameterString> getRdaStringParameterPrimIndex();
    SecondaryIndex<String, Integer, ParameterString> getRdaStringParameterSecIndex();

    EntityCursor<ParameterString> getEntityCursorPrimary();
    EntityCursor<ParameterString> getEntityCursorSecondary();
}
