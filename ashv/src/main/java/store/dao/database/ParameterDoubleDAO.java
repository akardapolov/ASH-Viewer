package store.dao.database;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.database.ParameterDouble;

public class ParameterDoubleDAO implements IParameterDoubleDAO {
    private EntityStore store;

    public PrimaryIndex<Integer, ParameterDouble> rdaStringParameterPrimIndex;
    public SecondaryIndex<Double, Integer, ParameterDouble> rdaStringParameterSecIndex;

    public ParameterDoubleDAO(EntityStore store){
        this.store = store;

        this.rdaStringParameterPrimIndex = store.getPrimaryIndex(Integer.class, ParameterDouble.class);
        this.rdaStringParameterSecIndex = store.getSecondaryIndex(rdaStringParameterPrimIndex, Double.class, "paramDValue");
    }

    @Override
    public int getCheckOrLoadParameter(double parameter){
        if (!this.rdaStringParameterSecIndex.contains(parameter)){
            this.rdaStringParameterPrimIndex.putNoOverwrite(
                    new ParameterDouble(0, parameter)
            );
        }
        return this.rdaStringParameterSecIndex.get(parameter).getParamId();
    }

    @Override public double getParameterStrById(int id) { return this.rdaStringParameterPrimIndex.get(id).getParamDValue(); }
    @Override public EntityCursor<ParameterDouble> getEntityCursorPrimary() { return this.rdaStringParameterPrimIndex.entities(); }

}
