package store.entity.olap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class AshParameter {
    @PrimaryKey(sequence="AshParameterSeq")
    private int paramId;
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String paramValue;

    private String[] additionalParams;

    public AshParameter(){}

    public AshParameter(int paramId, String paramValue, String[] additionalParams) {
        this.paramId = paramId;
        this.paramValue = paramValue;
        this.additionalParams = additionalParams;
    }

    public int getParamId() {
        return paramId;
    }

    public String getParamValue() {
        return paramValue;
    }

    public String[] getAdditionalParams() {return additionalParams;}
}
