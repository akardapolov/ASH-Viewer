package store.service;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityCursor;
import config.profile.SqlColProfile;
import core.parameter.ParameterBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import store.BerkleyDB;
import store.ConvertManager;
import store.dao.database.*;
import store.entity.database.MainData;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Singleton
public class DatabaseDAO extends QueryService {
    private BerkleyDB berkleyDB;
    private OlapDAO olapDAO;

    @Getter @Setter private ConvertManager convertManager;

    @Getter public IMainDataDAO mainDataDAO;
    @Getter public IParameterStringDAO parameterStringDAO;
    @Getter public IParameterDoubleDAO parameterDoubleDAO;
    @Getter public IParamStringStringDAO paramStringStringDAO;
    @Getter public ISqlPlan iSqlPlan;

    @Inject
    public DatabaseDAO (BerkleyDB berkleyDB,
                        OlapDAO olapDAO) {
        this.berkleyDB = berkleyDB;
        this.olapDAO = olapDAO;
    }

    public void init () throws DatabaseException {
        this.mainDataDAO = new MainDataDAO(berkleyDB.getStore());
        this.parameterStringDAO = new ParameterStringDAO(berkleyDB.getStore());
        this.parameterDoubleDAO = new ParameterDoubleDAO(berkleyDB.getStore());
        this.paramStringStringDAO = new ParamStringStringDAO(berkleyDB.getStore());
        this.iSqlPlan = new SqlPlanDAO(berkleyDB.getStore());
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
                                    & !waitClassValue.equalsIgnoreCase(this.olapDAO.getIProfile().getWaitClass((byte) 0))){
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

}
