package store.entity.database;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class ParameterString {
    @PrimaryKey(sequence="StringParameterSeq")
    private int paramId;
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String paramValue;

    public ParameterString(){}

    public ParameterString(int paramId, String paramValue){
        this.paramId = paramId;
        this.paramValue = paramValue;
    }
    public int getParamId() {
        return paramId;
    }
    public String getParamValue() {
        return paramValue;
    }
}
