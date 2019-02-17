package store.cache;

public class CompositeKeyCache2 {
    private int paramId;
    private byte paramGrp;

    public CompositeKeyCache2(int paramId, byte paramGrp) {
        this.paramId = paramId;
        this.paramGrp = paramGrp;
    }

    public int getParamId() {
        return paramId;
    }

    public void setParamId(int paramId) {
        this.paramId = paramId;
    }

    public byte getParamGrp() {
        return paramGrp;
    }

    public void setParamGrp(byte paramGrp) {
        this.paramGrp = paramGrp;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof CompositeKeyCache2) {
            CompositeKeyCache2 keyCache = (CompositeKeyCache2)obj;
            return (paramId == keyCache.paramId);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Integer.valueOf((paramGrp + paramId)).hashCode();
    }

}
