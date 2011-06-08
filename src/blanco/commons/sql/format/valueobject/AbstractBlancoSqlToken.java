package blanco.commons.sql.format.valueobject;

public class AbstractBlancoSqlToken {
  
    private int fType;

  
    private String fString;

  
    private int fPos = -1;

 
    public void setType(final int argType) {
        fType = argType;
    }

    public int getType() {
        return fType;
    }

 
    public void setString(final String argString) {
        fString = argString;
    }

 
    public String getString() {
        return fString;
    }

  
    public void setPos(final int argPos) {
        fPos = argPos;
    }

 
    public int getPos() {
        return fPos;
    }

  
    public String toString() {
        final StringBuffer buf = new StringBuffer();
        buf.append("blanco.commons.sql.format.valueobject.AbstractBlancoSqlToken[");
        buf.append("type=" + fType);
        buf.append(",string=" + fString);
        buf.append(",pos=" + fPos);
        buf.append("]");
        return buf.toString();
    }
}
