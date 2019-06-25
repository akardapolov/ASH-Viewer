package store;

import config.Labels;
import core.ConstantManager;
import lombok.extern.slf4j.Slf4j;
import pojo.SqlColMetadata;
import utility.BinaryDisplayConverter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;

@Slf4j
@Singleton
public class ConvertManager {
    private StoreManager storeManager;
    private enum OracleType {CLOB,VARCHAR2,CHAR,RAW,NUMBER,DATE,TIMESTAMP,OID,TEXT}

    private int intNullValue = Integer.MIN_VALUE;

    @Inject
    public ConvertManager(StoreManager storeManager){
        this.storeManager = storeManager;
    }

    public String convertFromRawToString(String colType,  Object obj) {
        if (obj == null){
            return ConstantManager.NULL_VALUE;
        }

        switch (OracleType.valueOf(colType)) {
            case OID: // PG
                return String.valueOf(obj);
            case TEXT: // PG
            case VARCHAR2:
            case CHAR:
                return (String) obj;
            case NUMBER:
                BigDecimal bgDec = (BigDecimal) obj;
                    return String.valueOf(bgDec.longValue());
            case DATE:
            case TIMESTAMP:
                Timestamp dt = (Timestamp) obj;
                return String.valueOf(dt.getTime());
            case RAW:
                return getByte(obj);
            case CLOB:
                Clob clobVal = (Clob) obj;
                try {
                    return clobVal.getSubString(1, (int) clobVal.length());
                } catch (SQLException e) {
                    log.info("No data found while clob processing");
                    return "No clob data";
                }
            default:
                return ConstantManager.NULL_VALUE;
        }
    }

    private String getByte(Object obj){
        Byte[] useValue;
        byte[] bytes = (byte[]) obj;
        useValue = new Byte[bytes.length];
        for (int m=0; m<bytes.length; m++) {
            useValue[m] = Byte.valueOf(bytes[m]);
        }
        return BinaryDisplayConverter.convertToString(useValue,
                BinaryDisplayConverter.HEX, false);
    }

    public int convertFromRawToInt(SqlColMetadata sqlColMetadata, Object obj){

        switch (OracleType.valueOf(sqlColMetadata.getColDbTypeName())) {
            case OID: // PG
                return storeManager.getDatabaseDAO()
                        .getParameterStringDAO().getCheckOrLoadParameter (obj == null ? Labels.getLabel("local.null") : String.valueOf(obj));
            case TEXT: // PG
            case VARCHAR2:
            case CHAR:
                return storeManager.getDatabaseDAO()
                        .getParameterStringDAO().getCheckOrLoadParameter (obj == null ? Labels.getLabel("local.null") : (String) obj);
            case NUMBER:
                BigDecimal bigDecimal = (BigDecimal) obj;
                return bigDecimal == null ? intNullValue :
                        ((bigDecimal.intValue() >= 0) ?  bigDecimal.intValue() :
                                -1 * storeManager.getDatabaseDAO()
                                        .getParameterDoubleDAO().getCheckOrLoadParameter(bigDecimal.doubleValue())
                        );
            case DATE:
                Timestamp dt = (Timestamp) obj;
                return (int) (dt == null ? intNullValue : dt.getTime()/1000);
            case TIMESTAMP:
                Timestamp timestamp = (Timestamp) obj;
                return (int) (timestamp == null ? intNullValue : timestamp.getTime()/1000);
            case RAW:
                return storeManager.getDatabaseDAO()
                        .getParameterStringDAO().getCheckOrLoadParameter (obj == null ? Labels.getLabel("local.null") : getByte(obj));
            default:
                return intNullValue;
        }

    }
}
