package profile;

import core.ConstantManager;
import org.rtv.Options;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

public class Postgres implements IProfile{
    String profileName = "Postgres";
    String driverName = "org.postgresql.Driver";

    String sqlTextSysdate = "select now()";
    String sqlTextSysdateCol = "NOW";

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

    String sqlTextMin = "SELECT current_timestamp as SAMPLE_TIME";

    String sqlTextUserIdName = "SELECT usesysid AS USER_ID, usename AS USERNAME FROM pg_catalog.pg_user";

    String sampleTimeColName = "SAMPLE_TIME";

    String waitClassColName = "WAIT_CLASS";
    String eventColName = "EVENT";

    String sqlIdColName = "SQL_ID";
    List<String> SqlIdAddColName = new LinkedList<>();

    String sessionIdColName = "SESSION_ID";
    String sessionSerialIdColName = "SESSION_SERIAL";
    List<String> SessAddColName = new LinkedList<>();

    long interval = 1000; // 1 sec

    LinkedHashSet<String> out = new LinkedHashSet<>();

    /** Sql detail **/
    String sqlText = "SELECT sql_fulltext FROM v$sql WHERE sql_id = ?";
    LinkedHashMap<String, String> statsSqlQuery = new LinkedHashMap<>();
    String sqlPlanText = "SELECT 1 where '' != ? ";
    String sqlForPlanHashValueList = "SELECT 1 where '' != ? ";
    /** Sql detail **/

    /** Session detail **/
    LinkedHashMap<String, String> statsSessQuery = new LinkedHashMap<>();
    /** Session detail **/

    String sqlTextColumn = "QUERY";

    public Postgres() {
        SqlIdAddColName.add("SQL_OPNAME");

        SessAddColName.add("USER_ID");
        SessAddColName.add("PROGRAM");

        out.add(Options.LBL_PG_CPU);
        out.add(Options.LBL_PG_IO);
        out.add(Options.LBL_PG_LOCK);
        out.add(Options.LBL_PG_LWLOCK);
        out.add(Options.LBL_PG_BUFFERPIN);
        out.add(Options.LBL_PG_ACTIVITY);
        out.add(Options.LBL_PG_EXTENSION);
        out.add(Options.LBL_PG_CLIENT);
        out.add(Options.LBL_PG_IPC);
        out.add(Options.LBL_PG_TIMEOUT);
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
    public String getSqlTextAshOneRow() { return sqlTextAsh; }

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
    public List getSessAddColName() {return SessAddColName; }

    @Override
    public LinkedHashMap<String, Color> getWaitClass(){ return Options.getInstance().getPgMainColor(); }

    @Override
    public boolean isDeleteOldDataOnStart(){return true;}

    @Override
    public byte getWaitClassId(String waitClass) { return ConstantManager.getWaitClassIdPG(waitClass); }

    @Override
    public String getWaitClass(byte waitClassId) { return ConstantManager.getWaitClassPG(waitClassId); }

    @Override
    public LinkedHashSet<String> getUniqueTreeEventListByWaitClass() { return out; }

    @Override
    public long getInterval() {
        return interval;
    }


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

}
