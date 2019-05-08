package store.entity.database;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class ParamStringString {
    @PrimaryKey
    private String paramId;
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String paramValue;

    public ParamStringString(){}

    public ParamStringString(String paramId, String paramValue){
        this.paramId = paramId;
        this.paramValue = paramValue;
    }
    public String getParamId() {
        return paramId;
    }
    public String getParamValue() {
        return paramValue;
    }
}
