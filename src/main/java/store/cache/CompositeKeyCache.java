package store.cache;

public class CompositeKeyCache implements Comparable<CompositeKeyCache>{
    private long dateId;
    private int paramId;

    public CompositeKeyCache(long dateId, int paramId) {
        this.dateId = dateId;
        this.paramId = paramId;
    }

    public long getDateId() {
        return dateId;
    }

    public void setDateId(long dateId) {
        this.dateId = dateId;
    }

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof CompositeKeyCache) {
            CompositeKeyCache keyCache = (CompositeKeyCache)obj;
            return (dateId == keyCache.dateId) & (paramId == keyCache.paramId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf((int) (dateId + paramId)).hashCode();
    }

    @Override
    public int compareTo(CompositeKeyCache o) {
        int result = 0;

        if (this.dateId == o.getDateId()){
            result = 0;
        } else if (this.dateId < o.getDateId()){
            result = -1;
        } else if (this.dateId > o.getDateId()){
            result = 1;
        }

        return result;
    }
}
