package profile;

import core.ConstantManager;
import org.rtv.Options;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class OracleEE implements IProfile{

    String profileName = "OracleEE";
    String driverName = "oracle.jdbc.driver.OracleDriver";

    String sqlTextSysdate = "SELECT sysdate FROM dual";
    String sqlTextSysdateCol = "SYSDATE";

    String sqlTextAsh = "SELECT * FROM v$active_session_history";
    String sqlTextAshOneRow = "SELECT * FROM v$active_session_history WHERE rownum = 1";
    String sqlTextMin = "select min(SAMPLE_TIME) FROM v$active_session_history";

    String sqlTextUserIdName = "SELECT user_id, username FROM dba_users";

    String sampleTimeColName = "SAMPLE_TIME";

    String waitClassColName = "WAIT_CLASS";
    String eventColName = "EVENT";

    String sqlIdColName = "SQL_ID";
    List<String> SqlIdAddColName = new LinkedList<>();

    String sessionIdColName = "SESSION_ID";
    String sessionSerialIdColName = "SESSION_SERIAL#";
    List<String> SessAddColName = new LinkedList<>();

    LinkedHashSet<String> eventList = new LinkedHashSet<>();

    /** Sql detail **/
    String sqlText = "SELECT sql_fulltext FROM v$sql WHERE sql_id = ? and rownum < 2";
    LinkedHashMap<String, String> statsSqlQuery = new LinkedHashMap<>();
    String sqlPlanText = "SELECT address, hash_value, sql_id, plan_hash_value, child_number," +
            " operation, options, object_node, object# obj, object_owner, object_name, object_alias," +
            " object_type, optimizer, id, parent_id, depth, position, search_columns, cost," +
            " cardinality, bytes, other_tag, partition_start, partition_stop, partition_id," +
            " other, distribution, cpu_cost, io_cost, temp_space, access_predicates, filter_predicates," +
            " projection, time, qblock_name, remarks" +
            " FROM v$sql_plan " +
            " WHERE sql_id = ?";
    String sqlForPlanHashValueList = "SELECT sql_id, plan_hash_value, child_address FROM v$sql WHERE sql_id = ?";
    /** Sql detail **/

    /** Session detail **/
    LinkedHashMap<String, String> statsSessQuery = new LinkedHashMap<>();
    /** Session detail **/

    String sqlTextColumn = "QUERY";

    long interval = 5000; // 10 sec

    public OracleEE() {
        SqlIdAddColName.add("SQL_OPNAME");
        SessAddColName.add("USER_ID");
        SessAddColName.add("PROGRAM");

        eventList.add(Options.LBL_CPU);
        eventList.add(Options.LBL_SCHEDULER);
        eventList.add(Options.LBL_USERIO);
        eventList.add(Options.LBL_SYSTEMIO);
        eventList.add(Options.LBL_CONCURRENCY);
        eventList.add(Options.LBL_APPLICATION);
        eventList.add(Options.LBL_COMMIT);
        eventList.add(Options.LBL_CONFIGURATION);
        eventList.add(Options.LBL_ADMINISTRATIVE);
        eventList.add(Options.LBL_NETWORK);
        eventList.add(Options.LBL_QUEUEING);
        eventList.add(Options.LBL_CLUSTER);
        eventList.add(Options.LBL_OTHER);
        eventList.add(Options.LBL_IDLE);

        statsSqlQuery.put("V$SQL", "SELECT * FROM v$sql WHERE sql_id = ? and child_address = ?");

        // Danger !!!
        //statsSqlQuery.put("V$SQLAREA", "SELECT sa.* FROM v$sqlarea sa WHERE sa.sql_id in (select s.sql_id from v$sql s where s.sql_id = ? and s.child_address = ? and rownum < 2)");
        // Danger !!!

        statsSessQuery.put("V$SESSION", "SELECT * FROM v$session WHERE sid = ? and serial# = ?");
        statsSessQuery.put("V$PROCESS", "select p.* from v$process p where p.addr in (select s.paddr from v$session s where s.sid = ? and s.serial# = ?)");
    }

    @Override
    public String getProfileName() { return profileName; }

    @Override
    public String getDriverName() { return driverName; }

    @Override
    public String getSqlTextSysdate() { return sqlTextSysdate; }

    @Override
    public String getSqlTextSysdateCol() { return sqlTextSysdateCol; }

    @Override
    public String getSqlTextAsh() { return sqlTextAsh; }

    @Override
    public String getSqlTextAshOneRow() { return sqlTextAshOneRow; }

    @Override
    public String getSqlTextMin() { return sqlTextMin; }

    @Override
    public String getSqlTextUserIdName() { return sqlTextUserIdName; }

    @Override
    public String getSampleTimeColName() { return sampleTimeColName; }

    @Override
    public String getWaitClassColName() { return waitClassColName; }

    @Override
    public String getEventColName() { return eventColName; }

    @Override
    public String getSqlIdColName() { return sqlIdColName; }

    @Override
    public List getSqlIdAddColName() { return SqlIdAddColName; }

    @Override
    public String getSessColName() { return sessionIdColName; }

    @Override
    public String getSerialColName() { return sessionSerialIdColName; }

    @Override
    public List getSessAddColName() { return SessAddColName; }

    @Override
    public LinkedHashMap<String, Color> getWaitClass(){ return Options.getInstance().getOracleMainColor(); }

    @Override
    public boolean isDeleteOldDataOnStart(){return true;}

    @Override
    public byte getWaitClassId(String waitClass) { return ConstantManager.getWaitClassId(waitClass); }

    @Override
    public String getWaitClass(byte waitClassId) { return ConstantManager.getWaitClass(waitClassId); }

    @Override public LinkedHashSet<String> getUniqueTreeEventListByWaitClass() { return eventList; }

    /** Sql detail **/
    @Override
    public String getSqlFullText(){ return sqlText;}
    @Override
    public LinkedHashMap<String, String> getSqlStatsQuery(){ return statsSqlQuery; }
    @Override
    public String getSqlPlanText(){ return sqlPlanText;}
    @Override
    public String getSqlForPlanHashValueList(){ return sqlForPlanHashValueList;}
    /** Sql detail **/

    /** Session detail **/
    @Override
    public LinkedHashMap<String, String> getSessionStatsQuery() { return statsSessQuery; }
    /** Session detail **/

    @Override public String getSqlTextColumn() { return sqlTextColumn; }

    @Override
    public long getInterval() {
        return interval;
    }


}
