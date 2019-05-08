package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.database.ParamStringString;

public interface IParamStringStringDAO {

    void putNoOverwrite(String parameter, String value);
    String getValue (String parameter);
    boolean isExistValueByParameter (String parameter);

    PrimaryIndex<String, ParamStringString> getRdaStringParameterPrimIndex();
    SecondaryIndex<String, String, ParamStringString> getRdaStringParameterSecIndex();

    EntityCursor<ParamStringString> getEntityCursorPrimary();
    EntityCursor<ParamStringString> getEntityCursorSecondary();
}
