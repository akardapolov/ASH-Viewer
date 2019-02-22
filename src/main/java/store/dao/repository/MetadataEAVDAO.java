package store.dao.repository;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.repository.MetadataEAV;

public class MetadataEAVDAO implements IMetadataEAVDAO {
    private EntityStore store;


    private PrimaryIndex<Long, MetadataEAV> mainDataMetaEAVPrimaryIndex;
    private SecondaryIndex<String, Long, MetadataEAV> moduleNameEAVSecondaryIndex;
    private SecondaryIndex<String, Long, MetadataEAV> entityEAVSecondaryIndex;

    public MetadataEAVDAO(EntityStore store) {
        this.store = store;

        this.mainDataMetaEAVPrimaryIndex = store.getPrimaryIndex(Long.class, MetadataEAV.class);
        this.moduleNameEAVSecondaryIndex = store.getSecondaryIndex(mainDataMetaEAVPrimaryIndex, String.class, "moduleName");
        this.entityEAVSecondaryIndex = store.getSecondaryIndex(mainDataMetaEAVPrimaryIndex, String.class, "entity");
    }

    @Override
    public boolean putMainDataEAVWithCheck(String moduleName, String entity, String attribute, String value){
        boolean isDelete = false;
        Long idToDeleteExistingRow = 0L;

        EntityCursor<MetadataEAV> mainDataEAV =
                this.moduleNameEAVSecondaryIndex.subIndex(moduleName).entities();

        try {
            for (MetadataEAV metaEAV : mainDataEAV) {
                if (metaEAV.getModuleName().equalsIgnoreCase(moduleName)&
                        metaEAV.getEntity().equalsIgnoreCase(entity)&
                        metaEAV.getAttribute().equalsIgnoreCase(attribute))

                if (!metaEAV.getValue().equalsIgnoreCase(value)){
                    isDelete = true;
                    idToDeleteExistingRow = metaEAV.getMainDataMetaEAVKey();
                } else {
                    isDelete = false;
                }
            }
        } finally {
            mainDataEAV.close();
        }

        if (isDelete){
            this.mainDataMetaEAVPrimaryIndex.delete(idToDeleteExistingRow);
            this.store.sync();

            putMainDataEAVWithoutCheck(moduleName, entity, attribute, value);
        } else {
            putMainDataEAVWithoutCheck(moduleName, entity, attribute, value);
        }

        return isDelete;
    }

    @Override
    public void putMainDataEAVOverwrite(String moduleName, String entity, String attribute, String value) {
        MetadataEAV metadataEAV =
                new MetadataEAV(0, moduleName, entity, attribute, value);
        this.mainDataMetaEAVPrimaryIndex.put(metadataEAV);
        this.store.sync();
    }

    @Override
    public void putMainDataEAVWithoutCheck(String moduleName, String entity, String attribute, String value) {
        MetadataEAV metadataEAV =
                new MetadataEAV(0, moduleName, entity, attribute, value);
        this.mainDataMetaEAVPrimaryIndex.put(metadataEAV);
        this.store.sync();
    }

    @Override
    public void deleteMainDataEAVWithCheck(String moduleName, String entity, String attribute){
        Long id = 0L;
        EntityCursor<MetadataEAV> mainDataEAV =
                this.moduleNameEAVSecondaryIndex.subIndex(moduleName).entities();

        try {
            for (MetadataEAV metaEAV : mainDataEAV) {
                if (metaEAV.getModuleName().equalsIgnoreCase(moduleName)&
                        metaEAV.getEntity().equalsIgnoreCase(entity)&
                        metaEAV.getAttribute().equalsIgnoreCase(attribute))
                    id = metaEAV.getMainDataMetaEAVKey();
            }
        } finally {
            mainDataEAV.close();
        }
        this.mainDataMetaEAVPrimaryIndex.delete(id);
        this.store.sync();
    }

    @Override
    public void deleteMainDataEAVWithCheck(String moduleName, String entity){
        Long id = 0L;
        EntityCursor<MetadataEAV> mainDataEAV =
                this.moduleNameEAVSecondaryIndex.subIndex(moduleName).entities();

        try {
            for (MetadataEAV metaEAV : mainDataEAV) {
                if (metaEAV.getModuleName().equalsIgnoreCase(moduleName)&&
                        metaEAV.getEntity().equalsIgnoreCase(entity))
                    id = metaEAV.getMainDataMetaEAVKey();
                    this.mainDataMetaEAVPrimaryIndex.delete(id);
            }
        } finally {
            mainDataEAV.close();
        }
        this.store.sync();
    }

    @Override
    public void deleteMainDataEAVByModule(String moduleName){
        EntityCursor<MetadataEAV> mainDataEAV =
                this.moduleNameEAVSecondaryIndex.subIndex(moduleName).entities();
        try {
            for (MetadataEAV metaEAV : mainDataEAV) {
                if (metaEAV.getModuleName().equalsIgnoreCase(moduleName))
                    this.mainDataMetaEAVPrimaryIndex.delete(metaEAV.getMainDataMetaEAVKey());
            }
        } finally {
            mainDataEAV.close();
        }
        this.store.sync();
    }

    @Override
    public EntityCursor<MetadataEAV> getEntityCursorPrimary() {
        return this.mainDataMetaEAVPrimaryIndex.entities();
    }

    @Override
    public EntityCursor<MetadataEAV> getModuleCursorSecondary() { return this.moduleNameEAVSecondaryIndex.entities(); }

    @Override
    public EntityCursor<MetadataEAV> getEntityCursorSecondary() { return this.entityEAVSecondaryIndex.entities(); }

    @Override
    public SecondaryIndex<String, Long, MetadataEAV> getModuleSecondaryIndex() { return this.moduleNameEAVSecondaryIndex; }

    @Override
    public SecondaryIndex<String, Long, MetadataEAV> getEntitySecondaryIndex() { return this.entityEAVSecondaryIndex; }

}
