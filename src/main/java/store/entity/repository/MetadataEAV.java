package store.entity.repository;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

// advanced EAV (eEAV)-> module, entity, attribute, value
@Entity
public class MetadataEAV {

    @PrimaryKey(sequence="mainDataMetaEAV")
    long mainDataMetaEAVKey;

    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String moduleName;

    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String entity;

    private String attribute;
    private String value;

    public MetadataEAV(){}

    public MetadataEAV(long mainDataMetaEAVKey, String moduleName, String entity,
                       String attribute, String value){
        this.mainDataMetaEAVKey = mainDataMetaEAVKey;
        this.moduleName = moduleName;
        this.entity = entity;
        this.attribute = attribute;
        this.value = value;
    }

    public long getMainDataMetaEAVKey() {
        return mainDataMetaEAVKey;
    }

    public String getModuleName() {
        return moduleName;
    }

    public String getEntity() {
        return entity;
    }

    public String getAttribute() {
        return attribute;
    }

    public String getValue() {
        return value;
    }
}
