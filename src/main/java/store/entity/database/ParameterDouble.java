package store.entity.database;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class ParameterDouble {
    @PrimaryKey(sequence="DoubleParameterSeq")
    private int paramId;
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private double paramDValue;

    public ParameterDouble(){}

    public ParameterDouble(int paramId, double paramDValue){
        this.paramId = paramId;
        this.paramDValue = paramDValue;
    }
    public int getParamId() {
        return paramId;
    }
    public double getParamDValue() {
        return paramDValue;
    }
}
