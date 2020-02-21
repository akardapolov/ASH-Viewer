package store.dao.database;

import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;
import store.entity.database.ParamStringString;

import java.util.Optional;

public class ParamStringStringDAO implements IParamStringStringDAO {
    private EntityStore store;

    private PrimaryIndex<String, ParamStringString> rdaStringParameterPrimIndex;
    private SecondaryIndex<String, String, ParamStringString> rdaStringParameterSecIndex;

    public ParamStringStringDAO(EntityStore store){
        this.store = store;

        this.rdaStringParameterPrimIndex = store.getPrimaryIndex(String.class, ParamStringString.class);
        this.rdaStringParameterSecIndex = store.getSecondaryIndex(rdaStringParameterPrimIndex, String.class, "paramValue");
    }

    @Override
    public void putNoOverwrite(String parameter, String value){
        this.rdaStringParameterPrimIndex.putNoOverwrite(
                new ParamStringString(parameter, value)
        );
    }

    @Override
    public String getValue (String parameter){ return this.rdaStringParameterPrimIndex.get(parameter).getParamValue(); }

    @Override
    public boolean isExistValueByParameter (String parameter) {

        Optional<ParamStringString> opt = Optional.ofNullable(this.rdaStringParameterPrimIndex.get(parameter));

        if (opt.isPresent()){
             return !this.rdaStringParameterPrimIndex.get(parameter).getParamId().isEmpty();
         } else {
             return false;
         }
    }
}
