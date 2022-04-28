package profile;

import core.manager.ConstantManager;
import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import org.rtv.Options;

@Data
public class OracleEE implements IProfile {
    String profileName = "OracleEE";
    String driverName = "oracle.jdbc.driver.OracleDriver";

    String sqlTextSysdate = "SELECT sysdate FROM dual";
    String sqlTextAsh = "SELECT * FROM v$active_session_history";
    String sqlTextAshOneRow = "SELECT * FROM v$active_session_history WHERE rownum = 1";
    String sqlTextUserIdName = "SELECT user_id, username FROM dba_users";
    String sqlTextColumn = "QUERY";

    String sampleTimeColName = "SAMPLE_TIME";
    String waitClassColName = "WAIT_CLASS";
    String eventColName = "EVENT";

    String sqlIdColName = "SQL_ID";
    List<String> sqlIdAdditionalColName = new LinkedList<>();

    String sessColName = "SESSION_ID";
    String serialColName = "SESSION_SERIAL#";
    List<String> sessAdditionalColName = new LinkedList<>();

    LinkedHashSet<String> uniqueTreeEventListByWaitClass = new LinkedHashSet<>();

    /** Sql detail **/
    String sqlFullText = "SELECT sql_fulltext FROM v$sql WHERE sql_id = ? and rownum < 2";
    LinkedHashMap<String, String> sqlStatsQuery = new LinkedHashMap<>();
    String sqlPlanText = "SELECT address, hash_value, sql_id, plan_hash_value, child_number," +
            " operation, options, object_node, object# obj, object_owner, object_name, object_alias," +
            " object_type, optimizer, id, parent_id, depth, position, search_columns, cost," +
            " cardinality, bytes, other_tag, partition_start, partition_stop, partition_id," +
            " other, distribution, cpu_cost, io_cost, temp_space, access_predicates, filter_predicates," +
            " projection, time, qblock_name, remarks" +
            " FROM v$sql_plan " +
            " WHERE sql_id = ?";
    String sqlForPlanHashValueList = "SELECT sql_id, plan_hash_value, child_address FROM v$sql WHERE sql_id = ?";

    /** Session detail **/
    LinkedHashMap<String, String> sessionStatsQuery = new LinkedHashMap<>();

    long interval = 5000; // 5 sec

    public OracleEE() {
        sqlIdAdditionalColName.add("SQL_OPNAME");
        sessAdditionalColName.add("USER_ID");
        sessAdditionalColName.add("PROGRAM");

        uniqueTreeEventListByWaitClass.add(Options.LBL_CPU);
        uniqueTreeEventListByWaitClass.add(Options.LBL_SCHEDULER);
        uniqueTreeEventListByWaitClass.add(Options.LBL_USERIO);
        uniqueTreeEventListByWaitClass.add(Options.LBL_SYSTEMIO);
        uniqueTreeEventListByWaitClass.add(Options.LBL_CONCURRENCY);
        uniqueTreeEventListByWaitClass.add(Options.LBL_APPLICATION);
        uniqueTreeEventListByWaitClass.add(Options.LBL_COMMIT);
        uniqueTreeEventListByWaitClass.add(Options.LBL_CONFIGURATION);
        uniqueTreeEventListByWaitClass.add(Options.LBL_ADMINISTRATIVE);
        uniqueTreeEventListByWaitClass.add(Options.LBL_NETWORK);
        uniqueTreeEventListByWaitClass.add(Options.LBL_QUEUEING);
        uniqueTreeEventListByWaitClass.add(Options.LBL_CLUSTER);
        uniqueTreeEventListByWaitClass.add(Options.LBL_OTHER);
        uniqueTreeEventListByWaitClass.add(Options.LBL_IDLE);

        sqlStatsQuery.put("V$SQL", "SELECT * FROM v$sql WHERE sql_id = ? and child_address = ?");

        // Do not use
        //statsSqlQuery.put("V$SQLAREA", "SELECT sa.* FROM v$sqlarea sa WHERE sa.sql_id in (select s.sql_id from v$sql s where s.sql_id = ? and s.child_address = ? and rownum < 2)");

        sessionStatsQuery.put("V$SESSION", "SELECT * FROM v$session WHERE sid = ? and serial# = ?");
        sessionStatsQuery.put("V$PROCESS", "select p.* from v$process p where p.addr in (select s.paddr from v$session s where s.sid = ? and s.serial# = ?)");
    }

    @Override
    public LinkedHashMap<String, Color> getWaitClass(){ return Options.getOracleMainColor(); }

    @Override
    public byte getWaitClassId(String waitClass) { return ConstantManager.getWaitClassId(waitClass); }

    @Override
    public String getWaitClass(byte waitClassId) { return ConstantManager.getWaitClass(waitClassId); }
}
