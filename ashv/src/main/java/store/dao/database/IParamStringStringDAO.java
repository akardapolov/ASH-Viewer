package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import store.entity.database.ParamStringString;

public interface IParamStringStringDAO {

    void putNoOverwrite(String parameter, String value);
    String getValue (String parameter);
    boolean isExistValueByParameter (String parameter);
    EntityCursor<ParamStringString> getEntityCursorPrimary();
}
