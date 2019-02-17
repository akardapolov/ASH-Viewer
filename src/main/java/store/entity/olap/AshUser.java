package store.entity.olap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class AshUser {
    @PrimaryKey
    private int userId;
    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private String userName;

    public AshUser(){}

    public AshUser(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }
}
