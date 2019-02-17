package profile;

import java.util.LinkedList;
import java.util.List;

public class OracleSE extends OracleEE{
    String sqlTextMin = "SELECT sysdate FROM dual";

    private String sqlTextAsh = "SELECT * FROM (SELECT sysdate SAMPLE_TIME, vs.sid SESSION_ID, vs.state SESSION_STATE, "
            + "vs.serial# SESSION_SERIAL#, vs.user# USER_ID, vs.sql_id SQL_ID, vs.type SESSION_TYPE, "
            + "vs.event# EVENT#, (CASE WHEN vs.wait_time != 0 THEN 'CPU used' ELSE vs.event END) EVENT, "
            + "vs.seq# SEQ#, vs.p1 P1, vs.p2 P2, vs.p3 P3, "
            + "vs.wait_time WAIT_TIME, vs.wait_class_id WAIT_CLASS_ID, vs.wait_class# WAIT_CLASS#, "
            + "(CASE WHEN vs.wait_time != 0 THEN 'CPU used' ELSE vs.wait_class END) WAIT_CLASS, vss.value TIME_WAITED, "
            + "vs.row_wait_obj# CURRENT_OBJ#, vs.row_wait_file# CURRENT_FILE#, vs.row_wait_block# CURRENT_BLOCK#, "
            + "vs.program PROGRAM, vs.module MODULE, vs.action ACTION, vs.fixed_table_sequence FIXED_TABLE_SEQUENCE, "
            + "nvl(au.name, 'UNKNOWN') COMMAND "
            + "FROM "
            + "v$session vs, v$sesstat vss, audit_actions au "
            + "WHERE vs.sid != ( select distinct sid from v$mystat  where rownum < 2 ) "
            + "and vs.sid = vss.sid and vs.command = au.action(+) "
            + "and vss.statistic# = 12 and (vs.wait_class != 'Idle' or vs.wait_time != 0) )";

    long interval = 1000; // 1 sec

    List<String> SqlIdAddColName = new LinkedList<>();

    public OracleSE() {
        SqlIdAddColName.add("COMMAND");
    }

    @Override
    public String getSqlTextAsh() { return sqlTextAsh; }

    @Override
    public String getSqlTextAshOneRow() { return sqlTextAsh; }

    @Override
    public String getSqlTextMin() { return sqlTextMin; }

    @Override
    public List getSqlIdAddColName() { return SqlIdAddColName; }

    @Override
    public boolean isDeleteOldDataOnStart(){return false;}

    @Override
    public long getInterval() {
        return interval;
    }

}
