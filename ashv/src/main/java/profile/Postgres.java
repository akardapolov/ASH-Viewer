package profile;

import core.manager.ConstantManager;
import lombok.Data;
import org.rtv.Options;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

@Data
public class Postgres implements IProfile {
    String profileName = "Postgres";
    String driverName = "org.postgresql.Driver";

    String sqlTextSysdate = "SELECT now()";
    String sqlTextAsh = "SELECT current_timestamp as SAMPLE_TIME, "
            + "datid, datname, "
            + "pid AS SESSION_ID, pid AS SESSION_SERIAL, usesysid AS USER_ID, "
            + "coalesce(usename, 'unknown') as usename, "
            + "concat(application_name,'::', backend_type, '::', coalesce(client_hostname, client_addr::text, 'localhost')) AS PROGRAM, "
            + "wait_event_type AS WAIT_CLASS, wait_event AS EVENT, query, substring(md5(query) from 0 for 15) AS SQL_ID, "
            + "left(query, strpos(query, ' '))  AS SQL_OPNAME, "
            + "coalesce(query_start, xact_start, backend_start) as query_start, "
            + "1000 * EXTRACT(EPOCH FROM (clock_timestamp()-coalesce(query_start, xact_start, backend_start))) as duration "
            + "from pg_stat_activity "
            // + "where state='active'"; // for test purposes..
            + "where state='active' and pid != pg_backend_pid()";
    String sqlTextUserIdName = "SELECT usesysid AS USER_ID, usename AS USERNAME FROM pg_catalog.pg_user";
    String sqlTextColumn = "QUERY";

    String sampleTimeColName = "SAMPLE_TIME";
    String waitClassColName = "WAIT_CLASS";
    String eventColName = "EVENT";

    String sqlIdColName = "SQL_ID";
    List<String> sqlIdAdditionalColName = new LinkedList<>();

    String sessColName = "SESSION_ID";
    String serialColName = "SESSION_SERIAL";
    List<String> sessAdditionalColName = new LinkedList<>();

    LinkedHashSet<String> uniqueTreeEventListByWaitClass = new LinkedHashSet<>();

    /** Sql detail **/
    String sqlFullText = "SELECT sql_fulltext FROM v$sql WHERE sql_id = ?";
    LinkedHashMap<String, String> sqlStatsQuery = new LinkedHashMap<>();
    String sqlPlanText = "SELECT 1 where '' != ? ";
    String sqlForPlanHashValueList = "SELECT 1 where '' != ? ";

    /** Session detail **/
    LinkedHashMap<String, String> sessionStatsQuery = new LinkedHashMap<>();

    long interval = 1000; // 1 sec

    public Postgres() {
        sqlIdAdditionalColName.add("SQL_OPNAME");

        sessAdditionalColName.add("USER_ID");
        sessAdditionalColName.add("PROGRAM");

        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_CPU);
        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_IO);
        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_LOCK);
        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_LWLOCK);
        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_BUFFERPIN);
        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_ACTIVITY);
        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_EXTENSION);
        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_CLIENT);
        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_IPC);
        uniqueTreeEventListByWaitClass.add(Options.LBL_PG_TIMEOUT);
    }

    @Override
    public LinkedHashMap<String, Color> getWaitClass(){ return Options.getPgMainColor(); }

    @Override
    public byte getWaitClassId(String waitClass) { return ConstantManager.getWaitClassIdPG(waitClass); }

    @Override
    public String getWaitClass(byte waitClassId) { return ConstantManager.getWaitClassPG(waitClassId); }

    @Override
    public String getSqlTextAshOneRow() { return sqlTextAsh; }
}
