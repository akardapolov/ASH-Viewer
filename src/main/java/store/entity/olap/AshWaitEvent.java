package store.entity.olap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class AshWaitEvent {
    @PrimaryKey(sequence="AshWaitEventSeq")
    private int eventId;
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String eventValue;
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private byte waitClass;

    public AshWaitEvent(){}

    public AshWaitEvent(int eventId, String eventValue, byte wailClass) {
        this.eventId = eventId;
        this.eventValue = eventValue;
        this.waitClass = wailClass;
    }

    public int getEventId() {
        return eventId;
    }

    public String getEventValue() {
        return eventValue;
    }

    public byte getWaitClass() {
        return waitClass;
    }
}
