package store;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import config.Labels;
import core.parameter.ParameterBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import config.profile.SqlColProfile;
import store.dao.database.*;
import store.entity.database.MainData;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class DatabaseDAO {
    private BerkleyDB berkleyDB;

    @Getter @Setter private ConvertManager convertManager;

    @Getter public IMainDataDAO mainDataDAO;

    @Getter public IParameterStringDAO parameterStringDAO;
    @Getter public IParameterDoubleDAO parameterDoubleDAO;

    @Getter public ISqlPlan iSqlPlan;

    @Getter public IParamStringStringDAO paramStringStringDAO;

    @Getter public OlapDAO olapDAO;

    public DatabaseDAO (BerkleyDB berkleyDB) throws DatabaseException {
        this.berkleyDB = berkleyDB;

        this.mainDataDAO = new MainDataDAO(berkleyDB.getStore());

        this.parameterStringDAO = new ParameterStringDAO(berkleyDB.getStore());
        this.parameterDoubleDAO = new ParameterDoubleDAO(berkleyDB.getStore());

        this.iSqlPlan = new SqlPlanDAO(berkleyDB.getStore());

        this.paramStringStringDAO = new ParamStringStringDAO(berkleyDB.getStore());

        this.olapDAO = new OlapDAO(berkleyDB);
    }

    public long getMax(ParameterBuilder parameterBuilder) {
        long out = 0;
        long start = (long) parameterBuilder.getBeginTime();
        long end = (long) parameterBuilder.getEndTime();

        EntityCursor<Long> cursor
                = this.mainDataDAO.getPrimaryIndex().keys(start, true, end, true);

        if (cursor != null) {
            try {
                if (cursor.last() != null) out = cursor.last();
            } finally {
                cursor.close();
            }
        }

        return out;
    }

    public List<Object[][]> getMatrixDataForJTable(long begin, long end,
                                                        String waitClassColName, String waitClassValue,
                                                                List<SqlColProfile> colMetadataList){

        List<Object[][]> out = new ArrayList<>();

        EntityCursor<MainData> cursor = getAshAggrEntityCursorRangeQuery(begin, end);
        Iterator<MainData> iterator = cursor.iterator();

        try {
            while (iterator.hasNext()) {
                MainData sl = iterator.next();

                    Object[][] data = new Object[sl.getMainMatrix().length][colMetadataList.size()];

                    for (int row = 0; row < sl.getMainMatrix().length; row++) {
                        if (!waitClassValue.isEmpty()){

                            Stream<SqlColProfile> sqlColMetadataStream
                                    = colMetadataList.stream().filter(x -> x.getColName().equalsIgnoreCase(waitClassColName));

                            SqlColProfile sColMetaD = sqlColMetadataStream.findFirst().get();

                            String waitVal = (String) convertManager.getMatrixDataForJTable(sColMetaD.getColDbTypeName(),
                                    sColMetaD.getColId(), sl, row);

                            if (!waitVal.isEmpty() & !waitVal.equalsIgnoreCase(waitClassValue)){
                                continue;
                            }

                            // CPU used - oracle/pg specific
                            if (waitVal.isEmpty()
                                    & !waitClassValue.equalsIgnoreCase(getOlapDAO().getIProfile().getWaitClass((byte) 0))){
                                continue;
                            }
                        }

                        int rowF = row;
                            colMetadataList.forEach(e -> {
                                data[rowF][e.getColId()-1] =
                                        convertManager.getMatrixDataForJTable(e.getColDbTypeName(), e.getColId(), sl, rowF);
                            });
                    }
                    out.add(data);
            }
        } finally {
            cursor.close();
        }

        return out;
    }

    public EntityCursor<MainData> getAshAggrEntityCursorRangeQuery(long start, long end){
        EntityCursor<MainData> entityCursor =
                doRangeQuery(this.mainDataDAO.getPrimaryIndex(), start, true, end, true);
        return entityCursor;
    }

    public void deleteMainData(long start, long end){
        EntityCursor<MainData> entityCursor =
                doRangeQuery(this.mainDataDAO.getPrimaryIndex(), start, true, end, true);

        try {
            for (MainData entity = entityCursor.first(); entity != null; entity = entityCursor.next()) {
                    entityCursor.delete();
            }
        } catch (Exception e){
            log.error(e.getMessage());
        } finally {
            entityCursor.close();
         }

    }

    private String getDateTimeStr(long dtValue){
        Date td = new Date(dtValue);
        DateFormat df = new SimpleDateFormat(Labels.getLabel("gui.table.tabledatapanel.dateformat"));
        return df.format(td);
    }

    public <K, V> EntityCursor<V> doRangeQuery(EntityIndex<K, V> index,
                                               K fromKey,
                                               boolean fromInclusive,
                                               K toKey,
                                               boolean toInclusive)
            throws DatabaseException {

        assert (index != null);

        return index.entities(fromKey,
                fromInclusive,
                toKey,
                toInclusive);
    }

}
