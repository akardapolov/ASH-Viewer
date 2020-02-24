package core.processing;

import com.sleepycat.persist.EntityCursor;
import config.Labels;
import core.manager.ColorManager;
import core.manager.ConfigurationManager;
import core.manager.ConstantManager;
import core.parameter.ParameterBuilder;
import gui.chart.CategoryTableXYDatasetRDA;
import gui.chart.ChartDatasetManager;
import gui.chart.panel.NameChartDataset;
import gui.detail.explainplan.ExplainPlanModel10g2;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.jdesktop.swingx.treetable.TreeTableModel;
import org.rtv.Options;
import config.profile.ConnProfile;
import config.profile.SqlColProfile;
import pojo.SqlPlanPojo;
import pojo.SqlPojo;
import profile.IProfile;
import profile.OracleEE;
import profile.Postgres;
import remote.RemoteDBManager;
import store.*;
import store.entity.database.SqlPlan;
import store.entity.olap.AshAggrMinData;
import store.entity.olap.AshUser;
import store.service.OlapDAO;
import utility.StackTraceUtil;
import utility.Utils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.math.BigDecimal;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Singleton
public class GetFromRemoteAndStore {

    private RemoteDBManager remoteDBManager;
    private StoreManager storeManager;
    private ColorManager colorManager;
    private ConvertManager convertManager;
    private ChartDatasetManager chartDatasetManager;
    private RawStoreManager rawStoreManager;
    private ConfigurationManager configurationManager;
    private OlapDAO olapDAO;

    private OlapCacheManager olapCacheManager;

    private boolean isFirstRun = false;
    private long sampleTimeG = 0L;
    @Getter
    private long currServerTime = 0L;

    @Getter
    private ConnProfile connProfile;

    private Connection connection = null;

    private Map<String, List<SqlColProfile>> metadataMap = new HashMap<>();

    private String modNameSysdateSql;
    private String modNameAshSql;

    @Getter @Setter private IProfile iProfile;

    // Detail
    private Map<String, LinkedHashMap<String, Integer>> mapDetail = new TreeMap<>();

    private List<Integer> SqlIdAddColName = new LinkedList();
    private List<Integer> SessAddColName = new LinkedList();

    @Inject
    public GetFromRemoteAndStore(ColorManager colorManager,
                                 RemoteDBManager remoteDBManagers,
                                 StoreManager storeManager,
                                 OlapCacheManager olapCacheManager,
                                 ConvertManager convertManager,
                                 ChartDatasetManager chartDatasetManager,
                                 RawStoreManager rawStoreManager,
                                 ConfigurationManager configurationManager,
                                 OlapDAO olapDAO) {
        this.colorManager = colorManager;
        this.remoteDBManager = remoteDBManagers;
        this.storeManager = storeManager;
        this.olapCacheManager = olapCacheManager;
        this.convertManager = convertManager;
        this.chartDatasetManager = chartDatasetManager;
        this.rawStoreManager = rawStoreManager;
        this.configurationManager = configurationManager;
        this.olapDAO = olapDAO;
    }

    public void initConnection(ConnProfile connProfile) throws SQLException{
        this.chartDatasetManager.setGetFromRemoteAndStore(this);

        this.connProfile = connProfile;
        this.remoteDBManager.init(connProfile);
        this.initializeConnection();
    }

    public void initProfile(IProfile iProfile) {
        this.iProfile = iProfile;
    }

    public void loadSqlsMetadata() {
        if (!this.isFirstRun) {
            this.olapCacheManager.setIProfile(this.iProfile);
            this.olapDAO.setIProfile(this.iProfile);
            //this.storeManager.getDatabaseDAO().getOlapDAO().setIProfile(this.iProfile);

            //this.olapCacheManager.setOlapDAO(storeManager.getDatabaseDAO().getOlapDAO());
            this.loadMetadata();

            this.rawStoreManager.setSqlColMetadatumPojos(metadataMap.get(modNameAshSql));

            iProfile.getSqlIdAddColName().stream().forEach(e -> SqlIdAddColName.add(this.getColumnIdForCol(e)));
            iProfile.getSessAddColName().stream().forEach(e -> SessAddColName.add(this.getColumnIdForCol(e)));
        }
    }

    public void loadDataFromRemoteToLocalStore() {
        this.loadConvertManager();
        this.loadSqlsMetadata();

        log.info("Start loading");

        try {
            if (!this.isFirstRun) {
                this.loadUsername();

                // For main chart
                iProfile.getUniqueTreeEventListByWaitClass().forEach(e-> chartDatasetManager.getMainNameChartDataset()
                        .getStackChartPanel().getStackedChart().setSeriesPaintDynamicDetail(e));
            }

            log.info("Start loading olap");
            this.loadDataToOlap();
            log.info("Start loading stacked chart");
            this.loadToMainStackedChart();
            log.info("Stop loading olap");

            if (!this.isFirstRun) { // resolve the issue with the gap for big data in ASH
                this.isFirstRun = true;
                this.loadDataToOlap();
                this.loadToMainStackedChart();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.storeManager.syncBdb();
    }

    public void loadConvertManager(){
        this.storeManager.getDatabaseDAO().setConvertManager(this.convertManager);
    }

    private void loadUsername() {
        loadUserIdUsernameToLocalDB(iProfile.getSqlTextUserIdName());
    }

    private void loadToMainStackedChart(){
        long min;
        long max;

        if (!this.isFirstRun) {
            max = sampleTimeG;
            if (sampleTimeG < currServerTime) {
                max = currServerTime;
            }
            min = max - ConstantManager.CURRENT_WINDOW;
        } else {
            max = sampleTimeG;
            min = (long) chartDatasetManager.getMainNameChartDataset().getDatasetRDA().getLastDtVal();

            if (sampleTimeG < currServerTime) {
                max = currServerTime;
            }
        }

        // Main
        LinkedList<String> waitClassList = new LinkedList();
        iProfile.getWaitClass().entrySet().forEach(ks -> waitClassList.add(ks.getKey()));

        LinkedHashMap<Long, long[]> hashMap = new LinkedHashMap();
        int waitClassCount = Math.toIntExact(waitClassList.stream().count());

        int range = (int) (ConstantManager.CURRENT_WINDOW / ConstantManager.MAX_POINT_PER_GRAPH);
        chartDatasetManager.getMainNameChartDataset().getDatasetRDA().setLastDtVal(max);

        for (long d = min; d < max; d += range) {

            if (range > ((int) (max - d))) {
                chartDatasetManager.getMainNameChartDataset().getDatasetRDA().setLastDtVal(d);
                continue;
            }

            EntityCursor<AshAggrMinData> cursor = this.olapDAO.getAshAggrMinDataDAO().getAshAggrEntityCursorRangeQuery(d, (d + range));
            Iterator<AshAggrMinData> iterator = cursor.iterator();

            hashMap.putIfAbsent(d, new long[waitClassCount]);

            while (iterator.hasNext()) {
                AshAggrMinData sl = iterator.next();
                    final long finalStart0 = d;

                    // Main
                    waitClassList.iterator().forEachRemaining(k -> {
                        long tmp = hashMap.get(finalStart0)[this.getIProfile().getWaitClassId(k)];
                        hashMap.get(finalStart0)[this.getIProfile().getWaitClassId(k)]
                                = sl.getSumValueByWaitClassId(this.getIProfile().getWaitClassId(k)) + tmp;
                    });

                    // Detail
                    waitClassList.iterator().forEachRemaining(k -> {
                        Map<String, Integer> out = mapDetail.computeIfAbsent(k, m -> new LinkedHashMap<>());

                        Map<Integer, Integer> allWaitId1 =
                                ConstantManager.zipToMap(sl.getWaitId(this.getIProfile().getWaitClassId(k)), //mtrx[0]::waitId;
                                        sl.getSum(this.getIProfile().getWaitClassId(k)));                    //mtrx[2]::sum;

                        allWaitId1.entrySet().stream().forEach(s -> {
                            out.merge(olapDAO.getEventStrValueForWaitEventId(s.getKey()), s.getValue(), Integer::sum);
                        });

                    });
            }

            cursor.close();

            // Load to detail chart
            final double dd = d;
            final long maxmax = max;
            chartDatasetManager.getNameChartDatasetDetail().forEach(e -> mapDetail.entrySet().forEach(k -> {
                if (k.getKey().equalsIgnoreCase(e.getName())){
                    k.getValue().entrySet().stream().forEach(l -> {
                        if (!e.getDatasetRDA().getSeriesNames().isEmpty()){
                            e.getStackChartPanel().getStackedChart().setSeriesPaintDynamicDetail(l.getKey());
                            e.getDatasetRDA().addSeriesValue(dd, (double) l.getValue()/(range / 1000), l.getKey());
                        } else {
                            generateDataUpToMinForDetail(e, l.getKey(), maxmax - ConstantManager.CURRENT_WINDOW, (long) dd, range);
                            e.getDatasetRDA().addSeriesValue(dd, (double) l.getValue()/(range / 1000), l.getKey());
                        }

                    });
                }
            }));

            // Clear mapDetail
            mapDetail.entrySet().stream().forEach(l -> l.getValue().replaceAll((k, v) -> 0));
        }

        // Load to main chart
        final CategoryTableXYDatasetRDA catTabXYDtstRTVId = chartDatasetManager.getMainNameChartDataset().getDatasetRDA();
        hashMap.entrySet().stream().forEach(e -> {
            final double finalD = e.getKey();

            waitClassList.iterator().forEachRemaining(k -> catTabXYDtstRTVId.addSeriesValue(finalD,
                    (double) e.getValue()[this.getIProfile().getWaitClassId(k)] / (range / 1000), k));
        });

        chartDatasetManager.getMainNameChartDataset().getDatasetRDA().deleteValuesFromDatasetDetail(ConstantManager.MAX_POINT_PER_GRAPH);
        chartDatasetManager.getNameChartDatasetDetail().forEach(e -> e.getDatasetRDA().deleteValuesFromDatasetDetail(ConstantManager.MAX_POINT_PER_GRAPH));
    }

    private void generateDataUpToMinForDetail(NameChartDataset nameChartDataset, String key, long min, long dd, int range) {
        nameChartDataset.getStackChartPanel().getStackedChart().setSeriesPaintDynamicDetail(key);

        if (dd > min) {
            for (long d = min; d < dd; d += range) {
                nameChartDataset.getDatasetRDA().addSeriesValue(d, 0, key);
            }
        }
    }

    private void loadMetadata() {
        try {
            modNameSysdateSql = "sysdate" + "_" + iProfile.getProfileName();
            modNameAshSql = "ash" + "_" + iProfile.getProfileName();

            metadataMap.put(modNameSysdateSql, loadSqlMetaData(modNameSysdateSql, iProfile.getSqlTextSysdate()));
            metadataMap.put(modNameAshSql, loadSqlMetaData(modNameAshSql, iProfile.getSqlTextAshOneRow()));

            // Store metadata in local config file
            configurationManager.loadSqlColumnMetadata(loadSqlMetaData(modNameAshSql, iProfile.getSqlTextAshOneRow()));

        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
            log.error(StackTraceUtil.getCustomStackTrace(e));
        }
    }

    private void loadDataToOlap() {
        String LBL_CPU_LOCAL = iProfile instanceof Postgres ? Options.LBL_PG_CPU : Options.LBL_CPU;

        PreparedStatement s = null;
        ResultSet rs = null;

        int sampleTimeColNameId = this.getColumnIdForCol(iProfile.getSampleTimeColName());

        int waitClassColNameId = this.getColumnIdForCol(iProfile.getWaitClassColName());
        int eventColNameId = this.getColumnIdForCol(iProfile.getEventColName());
        int sqlIdColNameId = this.getColumnIdForCol(iProfile.getSqlIdColName());
        int sessionIdColNameId = this.getColumnIdForCol(iProfile.getSessColName());
        int sessionSerialIdColNameId = this.getColumnIdForCol(iProfile.getSerialColName());

        currServerTime = getOneRowOutputDateFromDB(iProfile.getSqlTextSysdate());

        List<Map<Integer, Object>> rows = new ArrayList<>();

        try {
            AtomicInteger currRow = new AtomicInteger(0);

            s = this.getStatementForAsh();
            rs = s.executeQuery();

            rs.setFetchSize(15000); // to speed up loading

            while (rs.next()) {
                AtomicInteger kk = new AtomicInteger(0);

                long sampleTime = 0;
                String waitClass = "";
                String waitEvent = "";
                String sqlId = "";
                String sqlText = "";
                long sessionId = 0;
                long seriailId = 0;

                String[] sqlAddParam = new String[SqlIdAddColName.size()];
                String[] sessAddParam = new String[SessAddColName.size()];

                Map<Integer, String> sqlTmp = new HashMap<>();
                Map<Integer, String> sessTmp = new HashMap<>();

                // Prepare collection of rows to store data
                Map<Integer, Object> columns = new LinkedHashMap<>();

                for (int i = 0; i < metadataMap.get(modNameAshSql).size(); i++) {
                    kk.getAndIncrement();

                    /** Load raw data **/
                    columns.put(i, rs.getObject(i+1));

                    if (kk.get() == sampleTimeColNameId) {
                        Timestamp timestamp = (Timestamp) rs.getObject(i+1);
                        sampleTime = timestamp.getTime();
                        continue;
                    } else if (kk.get() == waitClassColNameId){
                        waitClass = rs.getObject(i+1) != null ? (String) rs.getObject(i+1) : LBL_CPU_LOCAL;
                        continue;
                    } else if (kk.get() == eventColNameId){
                        waitEvent = rs.getObject(i+1) != null ? (String) rs.getObject(i+1) : LBL_CPU_LOCAL;
                        continue;
                    } else if (kk.get() == sqlIdColNameId){
                        sqlId = rs.getObject(i+1) != null ? (String) rs.getObject(i+1) : ConstantManager.NULL_VALUE;
                        continue;
                    } else if (kk.get() == sessionIdColNameId){
                        if (iProfile instanceof Postgres){
                            sessionId = (Integer) rs.getObject(i+1);
                        } else {
                            BigDecimal bigDecimal = (BigDecimal) rs.getObject(i+1);
                            sessionId = bigDecimal.longValue();
                        }
                        continue;
                    } else if (kk.get() == sessionSerialIdColNameId){
                        if (iProfile instanceof Postgres){
                            seriailId = (Integer) rs.getObject(i+1);
                        } else {
                            BigDecimal bigDecimal = (BigDecimal) rs.getObject(i+1);
                            seriailId = bigDecimal.longValue();
                        }
                        continue;
                    } else if (SqlIdAddColName.contains(kk.get())){
                        String colName =
                                metadataMap.get(modNameAshSql).stream().filter(e -> e.getColId() == kk.get()).findFirst().get().getColName();
                        String colType =
                                metadataMap.get(modNameAshSql).stream().filter(e -> e.getColId() == kk.get()).findFirst().get().getColDbTypeName();

                        sqlTmp.put(iProfile.getSqlIdAddColName().indexOf(colName),
                                convertManager.convertFromRawToString(colType, rs.getObject(i+1)));
                        continue;
                    } else if (SessAddColName.contains(kk.get())){
                        String colName =
                                metadataMap.get(modNameAshSql).stream().filter(e -> e.getColId() == kk.get()).findFirst().get().getColName();
                        String colType =
                                metadataMap.get(modNameAshSql).stream().filter(e -> e.getColId() == kk.get()).findFirst().get().getColDbTypeName();

                        sessTmp.put(iProfile.getSessAddColName().indexOf(colName),
                                    convertManager.convertFromRawToString(colType, rs.getObject(i+1)));
                        continue;
                    } else if ((iProfile instanceof Postgres)){
                        if (kk.get() == this.getColumnIdForCol(iProfile.getSqlTextColumn())){
                            sqlText = rs.getObject(i+1) != null ? (String) rs.getObject(i+1) : ConstantManager.NULL_VALUE;
                        }
                        continue;
                    }
                }

                if (iProfile instanceof OracleEE) {
                    if (sampleTime != sampleTimeG) {
                        rawStoreManager.loadData(sampleTimeG, rows);
                        rows.clear();
                    }
                }

                if (configurationManager.getRawRetainDays() > 0){
                    rows.add(columns);
                }

                LocalDateTime sampleTimeDt =
                        LocalDateTime.ofInstant(Instant.ofEpochMilli(sampleTime), ZoneId.systemDefault());

                // SqlId
                if (!sqlId.equalsIgnoreCase(ConstantManager.NULL_VALUE)){
                    sqlTmp.entrySet().stream()
                            .sorted(Map.Entry.comparingByKey()).forEach(e -> sqlAddParam[e.getKey()] = e.getValue());

                    /** Additional dimension for sql detail **/
                    this.olapDAO.getCheckOrLoadParameter(sqlId, sqlAddParam);
                }

                // Sqltext (for postgres db only)
                if (iProfile instanceof Postgres){
                    storeManager.getDatabaseDAO()
                            .getParamStringStringDAO().putNoOverwrite(sqlId, sqlText);
                }

                // SessionId + Serial#
                sessTmp.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey()).forEach(e -> sessAddParam[e.getKey()] = e.getValue());

                // SqlId + SessionId + Serial#
                String[] addParamSqlSess = new String[1+SessAddColName.size()];
                addParamSqlSess[0] = sqlAddParam[0];
                addParamSqlSess[1] = sessAddParam[0];
                addParamSqlSess[2] = sessAddParam[1];

                this.olapCacheManager.loadAshRawData(sampleTimeDt, sqlId + "_" + sessionId + "_" + seriailId,
                        addParamSqlSess, waitEvent, iProfile.getWaitClassId(waitClass));

                /** Additional dimension for session detail **/
                this.olapDAO.getCheckOrLoadParameter(sessionId + "_" + seriailId, sessAddParam);

                sampleTimeG = sampleTime;

                if (!this.isFirstRun & currRow.getAndIncrement() > 6000) {
                    this.olapCacheManager.unloadCacheToDB15(sampleTimeG);
                }
            }

            rawStoreManager.loadData(sampleTimeG, rows);
            rows.clear();

            this.olapCacheManager.unloadCacheToDB();
            this.olapCacheManager.unloadCacheToDB15(getOneRowOutputDateFromDB(iProfile.getSqlTextSysdate()));

            rs.close();
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getLocalizedMessage());
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private PreparedStatement getStatementForAsh() throws SQLException{
        PreparedStatement s;

        String sqlText = iProfile.getSqlTextAsh();
        String where = " WHERE " + iProfile.getSampleTimeColName() + " > ? ";
        String orderBy = " ORDER BY " + iProfile.getSampleTimeColName() + " ASC";

        if (iProfile instanceof Postgres){
            s = connection.prepareStatement(sqlText);
        } else {
            if (!this.isFirstRun) {
                sqlText = sqlText + where + orderBy;
                s = connection.prepareStatement(sqlText);

                ParameterBuilder param = new ParameterBuilder.Builder(currServerTime - ConstantManager.CURRENT_WINDOW, currServerTime).build();
                s.setTimestamp(1, new java.sql.Timestamp(this.storeManager.getDatabaseDAO().getMax(param)));

            } else {
                sqlText = sqlText + where + orderBy;
                s = connection.prepareStatement(sqlText);

                s.setTimestamp(1, new java.sql.Timestamp(sampleTimeG));
            }
        }

        return s;
    }

    private int getColumnIdForCol(String filterCol){
        return metadataMap
                .get(modNameAshSql)
                .stream()
                .filter(e -> e.getColName().equalsIgnoreCase(filterCol))
                .findFirst().get().getColId();
    }

    private void initializeConnection() throws SQLException {
        this.connection = this.remoteDBManager.getConnection();
    }

    private List<SqlColProfile> loadSqlMetaData(String sqlName, String sqlText) {
        Statement s = null;
        ResultSet rs = null;
        List<SqlColProfile> sqlColProfileList = new ArrayList<>();

        try {
            s = connection.createStatement();
            s.executeQuery(sqlText);
            rs = s.getResultSet();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {

                SqlColProfile columnPojo = new SqlColProfile();
                columnPojo.setColId(i);
                columnPojo.setColName(rsmd.getColumnName(i).toUpperCase()); //PG bug here resolved :: lower-upper case
                columnPojo.setColDbTypeName(rsmd.getColumnTypeName(i).toUpperCase()); //PG bug here resolved :: lower-upper case
                columnPojo.setSqlAndColName(sqlName + rsmd.getColumnName(i).toUpperCase());
                columnPojo.setColSizeDisplay(rsmd.getColumnDisplaySize(i));
                columnPojo.setColSizeSqlType(rsmd.getColumnType(i));

                sqlColProfileList.add(i - 1, columnPojo);
            }
            rs.close();
            s.close();
        } catch (Exception e) {
            log.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace(); // need to log it
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return sqlColProfileList;
    }

    private long getOneRowOutputDateFromDB(String statement){
        long out = 0;
        PreparedStatement s = null;
        ResultSet rs = null;

        try {
            s = connection.prepareStatement(statement);
            rs = s.executeQuery();
            while (rs.next()) {
                Timestamp dt = (Timestamp) rs.getObject(1);
                out = dt.getTime();
            }

            rs.close();
            s.close();
        } catch (SQLException e) {
            log.error("SQL error while executing the following statement:" + statement);
            log.error(Arrays.toString(e.getStackTrace()));
        } catch (NullPointerException e) {
            log.error("NullPointerException while executing the following statement:"
                    + statement + ". Check you access rights to table or existence one.");
            log.error(Arrays.toString(e.getStackTrace()));
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return out;
    }

    private void loadUserIdUsernameToLocalDB(String statement){
        PreparedStatement s = null;
        ResultSet rs = null;

        try {
            s = connection.prepareStatement(statement);
            rs = s.executeQuery();
            while (rs.next()) {
                this.olapDAO.putUserIdUsername(new AshUser(rs.getInt(1), rs.getString(2)));
            }

            rs.close();
            s.close();
        } catch (SQLException e) {
            log.error(Arrays.toString(e.getStackTrace()));
            e.printStackTrace(); // need to log it
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Vector getVectorDataForRowToTableForSql(String parameter1, String parameter2, String sqlText){
        Vector data = new Vector();
        QueryRunner run = new QueryRunner(remoteDBManager.getBasicDataSource());

        try {
            HashMap<String, String> result = run.query(sqlText, mapHV, parameter1, parameter2);

            result.entrySet().forEach(e -> {
                Vector row = new Vector();
                row.add(e.getKey());
                row.add(e.getValue());

                data.add(row);
            });

        } catch (SQLException e) {
            log.error("SQL exception::" + e.toString());
            return data;
        } catch (NullPointerException e) {
            log.info("No data found");
            return data;
        }
        return data;
    }

    public String getSqlFullText(String sqlId){
        if (storeManager.getDatabaseDAO().getParamStringStringDAO().isExistValueByParameter(sqlId)){
            return storeManager.getDatabaseDAO().getParamStringStringDAO().getValue(sqlId);
        }

        QueryRunner run = new QueryRunner(remoteDBManager.getBasicDataSource());
        try {
            Object[] result = run.query(iProfile.getSqlFullText(), h, sqlId);
            Clob clobVal = (Clob) result[0];
            String out = clobVal.getSubString(1, (int) clobVal.length());

            storeManager.getDatabaseDAO().getParamStringStringDAO().putNoOverwrite(sqlId, out);

            return out;
        } catch (SQLException e) {
            log.error("SQL exception::" + e.toString());
        } catch (NullPointerException e) {
            log.info("No data found");
            return "";
        }
        return "";
    }

    // Get plan hash value list
    public List<SqlPojo> getSqlPlanHashValue(String sqlId){
        List<SqlPojo> out = new ArrayList<>();
        QueryRunner run = new QueryRunner(remoteDBManager.getBasicDataSource());

        try {
            ResultSetHandler<List<SqlPojo>> resultSetHandler = new BeanListHandler<>(SqlPojo.class);
            return run.query(iProfile.getSqlForPlanHashValueList(), resultSetHandler, sqlId);

        } catch (SQLException e) {
            log.error("SQL exception::" + e.toString());
            return out;
        } catch (NullPointerException e) {
            log.error("No data found");
            return out;
        }
    }

    public String setTrace10046(int sid, int serial, boolean bool) {
        String out = "";
        CallableStatement stmt = null;

        if (iProfile instanceof Postgres){
            return Labels.getLabel("main.notsupported");
        }

        try {
            if (bool) {
                stmt = remoteDBManager.getBasicDataSource().getConnection()
                        .prepareCall("begin " +
                                "SYS.DBMS_MONITOR." +
                                "SESSION_TRACE_ENABLE" +
                                "(?,?,true,true); end;");
                out = "SYS.DBMS_MONITOR.SESSION_TRACE_ENABLE successfully executed!";
            } else {
                stmt = remoteDBManager.getBasicDataSource().getConnection()
                        .prepareCall("begin " +
                                "SYS.DBMS_MONITOR." +
                                "SESSION_TRACE_DISABLE" +
                                "(?,?); end;");
                out = "SYS.DBMS_MONITOR.SESSION_TRACE_DISABLE successfully executed!";
            }

            stmt.setInt(1, sid);
            stmt.setInt(2, serial);
            stmt.execute();

        } catch (SQLException e) {
            log.error("SQL exception::" + e.toString());
            out = e.toString();
        } catch (NullPointerException e) {
            log.info("No data found..");
            out = e.toString();
        } finally {
            try {
                if (stmt != null) {
                    stmt.close(); // close the statement
                }
            } catch (SQLException ex) {
                log.info("Final SQLException..");
                out = ex.toString();
            }
        }

        return out;
    }

    // Create a ResultSetHandler implementation to convert the waitEventId row into an Object[].
    ResultSetHandler<Object[]> h = rs -> {
        if (!rs.next()) {
            return null;
        }

        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();
        Object[] result = new Object[cols];

        for (int i = 0; i < cols; i++) {
            result[i] = rs.getObject(i + 1);
        }

        return result;
    };

    ResultSetHandler<HashMap<String, String>> mapHV = rs -> {
        if (!rs.next()) {
            return null;
        }

        ResultSetMetaData meta = rs.getMetaData();
        int cols = meta.getColumnCount();

        HashMap<String, String> result = new HashMap<>();

        for (int i = 0; i < cols; i++) {
            result.put(meta.getColumnName(i+1),
                    convertManager.convertFromRawToString(meta.getColumnTypeName(i+1),rs.getObject(i+1)));
        }

        return result;
    };

    public void loadSqlPlanHashValueFromRemoteToLocalBDB(String sqlId, long sqlPlanHashValue){
        QueryRunner run = new QueryRunner(remoteDBManager.getBasicDataSource());

        if (!this.storeManager.getDatabaseDAO().getISqlPlan().checkSqlIdAndPlanHashValueExist(sqlId, sqlPlanHashValue)){

            try {
                ResultSetHandler<List<SqlPlanPojo>> resultSetHandler = new BeanListHandler<>(SqlPlanPojo.class);
                List<SqlPlanPojo> planPojoList = run.query(iProfile.getSqlPlanText(), resultSetHandler, sqlId);

                planPojoList.stream().forEach(e -> {
                    if (e.getPlan_hash_value() == sqlPlanHashValue){
                        this.storeManager.getDatabaseDAO().getISqlPlan().putSqlPlanNoOverwrite(
                                new SqlPlan (0, e.getAddress(), e.getHash_value(), sqlId, sqlPlanHashValue,
                                        "", e.getChild_number(),0, e.getOperation(), e.getOptions(),e.getObject_node(),
                                        e.getObj(),e.getObject_owner(),e.getObject_name(), e.getObject_alias(),e.getObject_type(),
                                        e.getOptimizer(), e.getId(), e.getParent_id(), e.getDepth(),
                                        e.getPosition(), e.getSearch_columns(), e.getCost(), e.getCardinality(),
                                        e.getBytes(), e.getOther_tag(), e.getPartition_start(),e.getPartition_stop(),
                                        e.getPartition_id(),e.getOther(), e.getDistribution(),e.getCpu_cost(),
                                        e.getIo_cost(),e.getTemp_space(), e.getAccess_predicates(), e.getFilter_predicates(),
                                        e.getProjection(),e.getTime(), e.getQblock_name(), e.getRemarks()
                                )
                        );
                    }
                });

            } catch (SQLException e) {
                log.error("SQL exception::" + e.toString());
            } catch (NullPointerException e) {
                log.error("No data found");
            }
        }
    }

    public TreeTableModel getSqlPlanModelByPlanHashValue(HashMap<Long, Long> idLevel, Long sqlPlanHashValue, String sqlId) {

        ExplainPlanModel10g2 model = null;
        ExplainPlanModel10g2.ExplainRow lastRow = null;
        long previousLevel = 1;
        boolean isRecalculateDepth = false;
        boolean exitFromCycle = false;
        boolean isChildNumberSaved = false;
        long childNumberBySql = 0;
        long ii = 0;

        try {

            while (!exitFromCycle) {
                exitFromCycle = true;

                try {
                    EntityCursor<SqlPlan> sqlPlanBDB =
                            this.storeManager.getDatabaseDAO().doRangeQuery(
                                    this.storeManager.getDatabaseDAO().getISqlPlan().getEnityCurPlanHashValue(),
                                    sqlPlanHashValue, true, sqlPlanHashValue, true);

                    Iterator<SqlPlan> sqlPlanIterator = sqlPlanBDB.iterator();

                    while (sqlPlanIterator.hasNext()) {

                        SqlPlan sqlPlan = sqlPlanIterator.next();

                        String sqlIdTmp = sqlPlan.getSqlId();
                        long childNumberTmp = sqlPlan.getChildNumber();

                        if (!isChildNumberSaved && sqlId.equalsIgnoreCase(sqlIdTmp)) {
                            childNumberBySql = childNumberTmp;
                            isChildNumberSaved = true;
                        } else {
                            if (!sqlId.equalsIgnoreCase(sqlIdTmp))
                                continue;
                        }

                        Long id = sqlPlan.getId();

                        if (id == ii && childNumberBySql == childNumberTmp) {
                            exitFromCycle = false;
                            String address = Optional.ofNullable(sqlPlan.getAddress()).orElse("");
                            Double hashValue = Optional.ofNullable(sqlPlan.getHashValue()).orElse(0D);
                            String childAddress = Optional.ofNullable(sqlPlan.getChildAddress()).orElse("");
                            Long childNumber = Optional.ofNullable(sqlPlan.getChildNumber()).orElse(0L);
                            String operation = Optional.ofNullable(sqlPlan.getOperation()).orElse("");
                            String options = Optional.ofNullable(sqlPlan.getOptions()).orElse("");
                            String objectNode = Optional.ofNullable(sqlPlan.getObjectNode()).orElse("");
                            Double object = Optional.ofNullable(sqlPlan.getObject()).orElse(0D);

                            String objectOwner = Optional.ofNullable(sqlPlan.getObjectOwner()).orElse("");
                            String objectName = Optional.ofNullable(sqlPlan.getObjectName()).orElse("");
                            String objectAlias = Optional.ofNullable(sqlPlan.getObjectAlias()).orElse("");
                            String objectType = Optional.ofNullable(sqlPlan.getObjectType()).orElse("");
                            String optimizer = Optional.ofNullable(sqlPlan.getOptimizer()).orElse("");
                            Long Id = Optional.ofNullable(sqlPlan.getId()).orElse(0L);
                            Long parentId = Optional.ofNullable(sqlPlan.getParentId()).orElse(0L);

                            /*Depth/Level*/
                            Long level = !isRecalculateDepth
                                    ? sqlPlan.getDepth() + 1
                                    : idLevel.get(id) == null ? sqlPlan.getId():idLevel.get(id) ;
                            /*Depth/Level*/

                            Long position = Optional.ofNullable(sqlPlan.getPosition()).orElse(0L);
                            Long searchColumns = Optional.ofNullable(sqlPlan.getSearchColumns()).orElse(0L);
                            Double cost = Optional.ofNullable(sqlPlan.getCost()).orElse(0D);
                            Double cardinality = Optional.ofNullable(sqlPlan.getCardinality()).orElse(0D);
                            Double bytes = Optional.ofNullable(sqlPlan.getBytes()).orElse(0D);
                            String otherTag = Optional.ofNullable(sqlPlan.getOtherTag()).orElse("");
                            String partitionStart = Optional.ofNullable(sqlPlan.getPartitionStart()).orElse("");
                            String partitionStop = Optional.ofNullable(sqlPlan.getPartitionStop()).orElse("");
                            Double partitionId = Optional.ofNullable(sqlPlan.getPartitionId()).orElse(0D);
                            String other = Optional.ofNullable(sqlPlan.getOther()).orElse("");
                            String distribution = Optional.ofNullable(sqlPlan.getDistribution()).orElse("");
                            Double cpuCost = Optional.ofNullable(sqlPlan.getCpuCost()).orElse(0D);
                            Double ioCost = Optional.ofNullable(sqlPlan.getIoCost()).orElse(0D);
                            Double tempSpace = Optional.ofNullable(sqlPlan.getTempSpace()).orElse(0D);
                            String accessPredicates = Optional.ofNullable(sqlPlan.getAccessPredicates()).orElse("");
                            String filterPredicates = Optional.ofNullable(sqlPlan.getFilterPredicates()).orElse("");
                            String projection = Optional.ofNullable(sqlPlan.getProjection()).orElse("");
                            Double time = Optional.ofNullable(sqlPlan.getTime()).orElse(0D);
                            String qblockName = Optional.ofNullable(sqlPlan.getQblockName()).orElse("");
                            String remarks = Optional.ofNullable(sqlPlan.getRemarks()).orElse("");

                            ExplainPlanModel10g2.ExplainRow parent = null;

                            if (level == 1) {
                                long tmp = 0;
                                ExplainPlanModel10g2.ExplainRow rowRoot = new ExplainPlanModel10g2.ExplainRow(
                                        parent, null, null, null, null, null, null,
                                        null, null, null, null, null, null, null,
                                        null, null, tmp, null, null, null,
                                        null, null, null, null, null, null, null,
                                        null, null, null, null, null, null, null,
                                        null, null, null, null, null);
                                model = new ExplainPlanModel10g2(rowRoot);

                                ExplainPlanModel10g2.ExplainRow row = new ExplainPlanModel10g2.ExplainRow(
                                        rowRoot, address.toString(), hashValue, sqlIdTmp, sqlPlanHashValue,
                                        childAddress.toString(), childNumber, operation, options,
                                        objectNode, object, objectOwner, objectName, objectAlias,
                                        objectType, optimizer, Id, parentId, /*Depth*/level, position,
                                        searchColumns, cost, cardinality, bytes, otherTag,
                                        partitionStart, partitionStop, partitionId, other, distribution,
                                        cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates,
                                        projection, time, qblockName, remarks);

                                rowRoot.addChild(row);
                                lastRow = row;
                                previousLevel = level;
                                continue;
                            } else if (previousLevel == level) {
                                parent = ((ExplainPlanModel10g2.ExplainRow) lastRow
                                        .getParent().getParent())
                                        .findChild(parentId.intValue());
                            } else if (level > previousLevel) {
                                parent = ((ExplainPlanModel10g2.ExplainRow) lastRow
                                        .getParent()).findChild(parentId.intValue());
                            } else if (level < previousLevel) {
                                parent = (ExplainPlanModel10g2.ExplainRow) lastRow.getParent();
                                for (long i = previousLevel - level; i >= 0; i--) {
                                    parent = (ExplainPlanModel10g2.ExplainRow) parent.getParent();
                                }
                                parent = parent.findChild(parentId.intValue());
                            }
                            if (parent == null && idLevel.isEmpty()) {
                                isRecalculateDepth = true;
                                break;
                            }
                            if (parent == null && !idLevel.isEmpty()) {
                                isRecalculateDepth = false;
                                break;
                            }

                            ExplainPlanModel10g2.ExplainRow row = new ExplainPlanModel10g2.ExplainRow(
                                    parent, address.toString(), hashValue, sqlIdTmp, sqlPlanHashValue,
                                    childAddress.toString(), childNumber, operation, options,
                                    objectNode, object, objectOwner, objectName, objectAlias,
                                    objectType, optimizer, Id, parentId, /*Depth*/level, position,
                                    searchColumns, cost, cardinality, bytes, otherTag,
                                    partitionStart, partitionStop, partitionId, other, distribution,
                                    cpuCost, ioCost, tempSpace, accessPredicates, filterPredicates,
                                    projection, time, qblockName, remarks);
                            parent.addChild(row);
                            lastRow = row;
                            previousLevel = level;

                            break;
                        }
                    }

                    sqlPlanBDB.close();
                    ii++;

                } catch (Exception e) {
                    log.error(e.toString());
                }
            }
        } catch (Exception e){
            log.error(e.toString());
        }

        // Recalculate wrong node levels
        if (isRecalculateDepth) {
            HashMap<Long, Long> idParentId = new HashMap<>();
            HashMap<Long, Long> idLevelRcl;

                try {
                    EntityCursor<SqlPlan> sqlPlanBDB =
                            this.storeManager.getDatabaseDAO().doRangeQuery(
                                    this.storeManager.getDatabaseDAO().getISqlPlan().getEnityCurPlanHashValue(),
                                    sqlPlanHashValue, true, sqlPlanHashValue, true);

                    Iterator<SqlPlan> sqlPlanIterator = sqlPlanBDB.iterator();

                    while (sqlPlanIterator.hasNext()){
                        SqlPlan sqlPlan = sqlPlanIterator.next();

                        Long idP = sqlPlan.getId();
                        Long parent_idP = sqlPlan.getParentId();
                        long tmp = -1;
                        if (idP == 0) {
                            idParentId.put(idP, tmp);
                        } else {
                            idParentId.put(idP, parent_idP);
                        }
                    }

                idLevelRcl = Utils.getLevels(idParentId);
                model = (ExplainPlanModel10g2) this.getSqlPlanModelByPlanHashValue(idLevelRcl, sqlPlanHashValue, sqlId);

                sqlPlanBDB.close();

            } catch (Exception e) {
                log.error(e.toString());
            } finally { }
        }

        return model;
    }

}
