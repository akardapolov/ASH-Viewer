package store.entity.olap;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;
import core.manager.ConstantManager;

import java.util.LinkedList;

/*****
 * <pre>
 * 1. CompositeKey contains dateId and paramId (sqlId, sessionId+serial, etc)
 * 2. dateId is secondary key
 * 3. waitId and sum contains matrix of WaitEventId and sum of occurrence in composite key
 * </pre>
 ****/
@Entity
public class AshAggrMinData {

    @PrimaryKey
    private CompositeKey compositeKey;

    @SecondaryKey(relate = Relationship.MANY_TO_ONE)
    private long dateId;

    private int[] waitId;
    private int[] waitClass;
    private int[] sum;

    public AshAggrMinData(){}

    public AshAggrMinData(long dateId, int paramId, int[] waitId, int[] waitClass, int[] sum){
        this.compositeKey = new CompositeKey(dateId, paramId);
        this.dateId = dateId;
        this.waitId = waitId;
        this.waitClass = waitClass;
        this.sum = sum;
    }

    public CompositeKey getCompositeKey(){
        return this.compositeKey;
    }

    public int [][] getMatrixValues(){
        int[][] mtrx = new int[3][this.waitId.length];

        mtrx [0] = this.waitId;
        mtrx [1] = this.waitClass;
        mtrx [2] = this.sum;

        return mtrx;
    }

    public int getSumValueByWaitClassId(int waitClassId){
        int resOut = 0;
        for(int i=0;i<this.waitClass.length;i++){
            if(this.waitClass[i]==waitClassId){
                resOut = resOut + this.sum[i];
            }
        }
        return resOut;
    }

    public int[] getWaitId() {
        return waitId;
    }

    public LinkedList<Integer> getWaitId(int waitClassId) {
        if (waitClassId == -1)
            return ConstantManager.toList(this.getWaitId());

        LinkedList<Integer> l = new LinkedList<>();
        for(int i=0;i<this.waitClass.length;i++){
            if(this.waitClass[i]==waitClassId){
                l.add(this.waitId[i]);
            }
        }
        return l;
    }

    public int[] getWaitClass() {
        return waitClass;
    }

    public int[] getSum() {
        return sum;
    }

    public LinkedList<Integer> getSum(int waitClassId) {
        if (waitClassId == -1)
            return ConstantManager.toList(this.getSum());

        LinkedList<Integer> l = new LinkedList<>();
        for(int i=0;i<this.waitClass.length;i++){
            if(this.waitClass[i]==waitClassId){
                l.add(this.sum[i]);
            }
        }
        return l;
    }

}