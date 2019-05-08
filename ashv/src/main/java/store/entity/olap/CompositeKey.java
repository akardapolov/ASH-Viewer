package store.entity.olap;

import com.sleepycat.persist.model.KeyField;
import com.sleepycat.persist.model.Persistent;

@Persistent
public class CompositeKey {
    @KeyField(1)
    private long dateId;
    @KeyField(2)
    private int paramId;

    CompositeKey() {}

    public CompositeKey(long dateId, int paramId) {
        this.dateId = dateId;
        this.paramId = paramId;
    }

    public long getDateId() {
        return this.dateId;
    }

    public int getParamId() {
        return this.paramId;
    }
}
