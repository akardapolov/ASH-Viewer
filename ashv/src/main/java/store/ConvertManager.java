package store;

import config.Labels;
import core.manager.ConstantManager;
import lombok.extern.slf4j.Slf4j;
import config.profile.SqlColProfile;
import store.entity.database.MainData;
import store.service.DatabaseDAO;
import utility.BinaryDisplayConverter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.sql.Clob;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import static java.lang.String.valueOf;

@Slf4j
@Singleton
public class ConvertManager {
    private StoreManager storeManager;
    private enum OracleType {CLOB,VARCHAR2,CHAR,RAW,NUMBER,INT4,FLOAT8,DATE,TIMESTAMP,TIMESTAMPTZ,OID,TEXT,NAME}

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
                return valueOf(obj);
            case TEXT: // PG
            case NAME: // PG
            case VARCHAR2:
            case CHAR:
                return (String) obj;
            case INT4:
                Integer int0 = (Integer) obj;
                return valueOf(int0.intValue());
            case FLOAT8:
                Double dbl0 = (Double) obj;
                return valueOf(dbl0.intValue());
            case NUMBER:
                BigDecimal bgDec = (BigDecimal) obj;
                    return valueOf(bgDec.longValue());
            case DATE:
            case TIMESTAMP:
            case TIMESTAMPTZ:
                Timestamp dt = (Timestamp) obj;
                return valueOf(dt.getTime());
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

    public int convertFromRawToInt(SqlColProfile sqlColProfile, Object obj){

        switch (OracleType.valueOf(sqlColProfile.getColDbTypeName())) {
            case OID: // PG
                return storeManager.getDatabaseDAO()
                        .getParameterStringDAO().getCheckOrLoadParameter (obj == null ? Labels.getLabel("local.null") : valueOf(obj));
            case TEXT: // PG
            case NAME: // PG
            case VARCHAR2:
            case CHAR:
                return storeManager.getDatabaseDAO()
                        .getParameterStringDAO().getCheckOrLoadParameter (obj == null ? Labels.getLabel("local.null") : (String) obj);
            case INT4:// PG
                Integer int0 = (Integer) obj;
                return int0 == null ? intNullValue :
                        ((int0 >= 0) ?  int0 :
                                -1 * storeManager.getDatabaseDAO()
                                        .getParameterDoubleDAO().getCheckOrLoadParameter(int0.doubleValue())
                        );
            case FLOAT8:// PG
                Double dbl0 = (Double) obj;
                return dbl0 == null ? intNullValue :
                    ((dbl0.intValue() >= 0) ?  dbl0.intValue() :
                            -1 * storeManager.getDatabaseDAO()
                                    .getParameterDoubleDAO().getCheckOrLoadParameter(dbl0)
                    );
            case NUMBER:
                BigDecimal bigDecimal = (BigDecimal) obj;
                return bigDecimal == null ? intNullValue :
                        ((bigDecimal.intValue() >= 0) ?  bigDecimal.intValue() :
                                -1 * storeManager.getDatabaseDAO()
                                        .getParameterDoubleDAO().getCheckOrLoadParameter(bigDecimal.doubleValue())
                        );
            case DATE:
            case TIMESTAMP:
            case TIMESTAMPTZ:
                Timestamp dt = (Timestamp) obj;
                return (int) (dt == null ? intNullValue : dt.getTime()/1000);
            case RAW:
                return storeManager.getDatabaseDAO()
                        .getParameterStringDAO().getCheckOrLoadParameter (obj == null ? Labels.getLabel("local.null") : getByte(obj));
            default:
                return intNullValue;
        }

    }

    public Object getMatrixDataForJTable(String columnTypeCategoryId, int columnCategoryId, MainData sl, int row) {
        DatabaseDAO dao = storeManager.getDatabaseDAO();

        int intval = sl.getMainMatrix()[row][columnCategoryId-1];
        if (intval == intNullValue) return Labels.getLabel("local.null");

        switch (OracleType.valueOf(columnTypeCategoryId)) {
            case OID: // PG
            case TEXT: // PG
            case NAME: // PG
            case VARCHAR2:
            case CHAR:
            case RAW:
                return dao.getParameterStringDAO().getParameterStrById(intval);
            case INT4:
            case FLOAT8:
            case NUMBER:
                if (intval < 0) {
                    return valueOf0(dao.parameterDoubleDAO.getParameterStrById(-1 * intval));
                } else {
                    return String.valueOf(intval);
                }
            case DATE:
            case TIMESTAMP:
            case TIMESTAMPTZ:
                return getDateForLongShorted(intval);
            default:
                return intval;
        }
    }

    private String getDateForLongShorted(int longDate){
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Labels.getLabel("gui.table.tabledatapanel.dateformat"));
        Date dtDate= new Date(((long)longDate)*1000L);
        return simpleDateFormat.format(dtDate);
    }

    public String valueOf0(double value)
    {
        DecimalFormat formatter;
        if(value - (int)value > 0.0)
            formatter = new DecimalFormat("0");
        else
            formatter = new DecimalFormat("0");

        return formatter.format(value);
    }
}
