package store.entity.database;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class MainData {

    @PrimaryKey
    private long key;

    // -1 means no data
    private int [][] mainMatrix;

    public MainData(){}

    public MainData(long key, int [][] mainMatrix){
        this.key = key;
        this.mainMatrix = mainMatrix;
    }

    public long getKey() {
        return key;
    }

    public int [][] getMainMatrix(){
        return mainMatrix;
    }


}
