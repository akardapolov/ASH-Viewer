package store.cache;

public class TripleValueCache {

    public final int waitEventId;
    public final byte waitClassId;
    public int sum;

    public int getWaitEventId() {
        return waitEventId;
    }

    public byte getWaitClassId() {
        return waitClassId;
    }

    public int getWaitClassIdInt() {
        return waitClassId;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int iSum) {
        this.sum = this.sum + iSum;
    }

    /**
     * @param waitEventId event id
     * @param waitClassId corresponding with the event id a wait class id
     * @param sum sum of entries by the wait Event id
     */
    public TripleValueCache(int waitEventId, byte waitClassId, int sum) {
        this.waitEventId = waitEventId;
        this.waitClassId = waitClassId;
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if(o != null && o instanceof TripleValueCache) {
            TripleValueCache keyCache = (TripleValueCache)o;
            return (waitEventId == keyCache.waitEventId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf(waitEventId + waitClassId + sum).hashCode();
    }

}
