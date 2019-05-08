package store.dao.olap;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.*;
import lombok.Getter;
import store.entity.olap.*;

import java.util.Optional;

public class AggrDAO {
    private EntityStore entityStore;

    @Getter public AshAggrMinDataDAO ashAggrMinDataDAO;
    @Getter public AshAggrMinData15SecDAO ashAggrMinData15SecDAO;
    @Getter public AshAggrMinData1MinDAO ashAggrMinData1MinDAO;

    private PrimaryIndex<Integer, AshParameter> ashParameterPrimaryIndex;
    private PrimaryIndex<Integer, AshWaitEvent> ashWaitEventPrimaryIndex;
    private PrimaryIndex<Integer, AshUser> ashUserPrimaryIndex;

    private SecondaryIndex<String, Integer, AshParameter> ashParameterSecondaryIndexStrValue;

    private SecondaryIndex<String, Integer, AshWaitEvent> ashWaitEventSecondaryIndexStrValue;
    private SecondaryIndex<Byte, Integer, AshWaitEvent> ashWaitEventSecondaryIndexByteValue;

    private SecondaryIndex<String, Integer, AshUser> ashUserSecondaryIndexStrValue;

    public AggrDAO(EntityStore store)
            throws DatabaseException {

        this.entityStore = store;

        this.ashAggrMinDataDAO = new AshAggrMinDataDAO(this.entityStore, this);
        this.ashAggrMinData15SecDAO = new AshAggrMinData15SecDAO(this.entityStore, this);
        this.ashAggrMinData1MinDAO = new AshAggrMinData1MinDAO(this.entityStore, this);

        this.ashParameterPrimaryIndex = store.getPrimaryIndex(Integer.class, AshParameter.class);
        this.ashWaitEventPrimaryIndex = store.getPrimaryIndex(Integer.class, AshWaitEvent.class);
        this.ashUserPrimaryIndex = store.getPrimaryIndex(Integer.class, AshUser.class);

        this.ashParameterSecondaryIndexStrValue = store.getSecondaryIndex(ashParameterPrimaryIndex, String.class, "paramValue");

        this.ashWaitEventSecondaryIndexStrValue = store.getSecondaryIndex(ashWaitEventPrimaryIndex, String.class, "eventValue");
        this.ashWaitEventSecondaryIndexByteValue = store.getSecondaryIndex(ashWaitEventPrimaryIndex, Byte.class, "waitClass");

        this.ashUserSecondaryIndexStrValue = store.getSecondaryIndex(ashUserPrimaryIndex, String.class, "userName");

    }

    public void putUserIdUsername(AshUser ashUser){
        this.ashUserPrimaryIndex.putNoOverwrite(ashUser);
    }

    public boolean putParameter (AshParameter ashParameter){
        return this.ashParameterPrimaryIndex.putNoOverwrite(ashParameter);
    }

    public boolean putWaitEvent (AshWaitEvent ashWaitEvent){
        return this.ashWaitEventPrimaryIndex.putNoOverwrite(ashWaitEvent);
    }

    public int getCheckOrLoadParameter(String parameter, String[] additionalParams){
        if (!this.ashParameterSecondaryIndexStrValue.contains(parameter)){
                this.ashParameterPrimaryIndex.putNoOverwrite(
                    new AshParameter(0, parameter, additionalParams)
                );
        }
        return this.ashParameterSecondaryIndexStrValue.get(parameter).getParamId();
    }

    public String getStrParameterValueById(int paramId){
        return this.ashParameterPrimaryIndex.get(paramId).getParamValue();
    }

    public int getParameterIdByStrValue(String paramStrValue){
        return this.ashParameterSecondaryIndexStrValue.get(paramStrValue).getParamId();
    }

    public String[] getAdditStrArrayParameters(int paramId){
        return this.ashParameterPrimaryIndex.get(paramId).getAdditionalParams();
    }

    public int getCheckOrLoadWaitEvent(String waitEvent, byte waitClass){
        if (!this.ashWaitEventSecondaryIndexStrValue.contains(waitEvent)){
            this.ashWaitEventPrimaryIndex.putNoOverwrite(
                    new AshWaitEvent(0, waitEvent, waitClass)
            );
        }
        return this.ashWaitEventSecondaryIndexStrValue.get(waitEvent).getEventId();
    }

    public byte getClassIdForWaitEventId(int waitId){
        return this.ashWaitEventPrimaryIndex.get(waitId).getWaitClass();
    }

    public String getEventStrValueForWaitEventId(int waitEventId){
        return this.ashWaitEventPrimaryIndex.get(waitEventId).getEventValue();
    }

    public String getUsername(int userId){
        Optional<AshUser> opt = Optional.ofNullable(this.ashUserPrimaryIndex.get(userId));

        if (opt.isPresent()){
            return opt.get().getUserName();
        } else {
            return "";
        }
    }

    public int getEventGrp(int eventId){
        if (this.ashWaitEventPrimaryIndex.contains(eventId)){
            return this.ashWaitEventPrimaryIndex.get(eventId).getWaitClass();
        } else {
            return -1;
        }
    }

    public <K, V> EntityCursor<V> doRangeQuery(EntityIndex<K, V> index,
                                               K fromKey,
                                               boolean fromInclusive,
                                               K toKey,
                                               boolean toInclusive)
            throws DatabaseException {

        assert (index != null);

        return index.entities(fromKey,
                fromInclusive,
                toKey,
                toInclusive);
    }

    @Deprecated
    public boolean isParameterExist(String parameter){
        return this.ashParameterSecondaryIndexStrValue.contains(parameter);
    }

    @Deprecated
    public boolean isWaitEventExist(String waitEvent){
        return this.ashWaitEventSecondaryIndexStrValue.contains(waitEvent);
    }

    public void close() {
        this.entityStore.close();
    }
}
