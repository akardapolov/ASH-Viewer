package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.database.ParameterString;

public class ParameterStringDAO implements IParameterStringDAO {
    private EntityStore store;

    private PrimaryIndex<Integer, ParameterString> rdaStringParameterPrimIndex;
    private SecondaryIndex<String, Integer, ParameterString> rdaStringParameterSecIndex;

    public ParameterStringDAO(EntityStore store){
        this.store = store;

        this.rdaStringParameterPrimIndex = store.getPrimaryIndex(Integer.class, ParameterString.class);
        this.rdaStringParameterSecIndex = store.getSecondaryIndex(rdaStringParameterPrimIndex, String.class, "paramValue");
    }

    @Override
    public int getCheckOrLoadParameter(String parameter){
        if (!this.rdaStringParameterSecIndex.contains(parameter)){
            this.rdaStringParameterPrimIndex.putNoOverwrite(
                    new ParameterString(0, parameter)
            );
        }
        return this.rdaStringParameterSecIndex.get(parameter).getParamId();
    }

    @Override
    public String getParameterStrById(int id){ return this.rdaStringParameterPrimIndex.get(id).getParamValue(); }

    @Override
    public EntityCursor<ParameterString> getEntityCursorPrimary() { return this.rdaStringParameterPrimIndex.entities(); }
}
