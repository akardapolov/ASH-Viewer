package profile;

public class Postgres96 extends Postgres implements IProfile{

    String sqlTextAsh = "SELECT current_timestamp as SAMPLE_TIME, "
            + "datid, datname, "
            + "pid AS SESSION_ID, pid AS SESSION_SERIAL, usesysid AS USER_ID, "
            + "coalesce(usename, 'unknown') as usename, "
            + "concat(application_name,'::', '', '::', coalesce(client_hostname, client_addr::text, 'localhost')) AS PROGRAM, "
            + "wait_event_type AS WAIT_CLASS, wait_event AS EVENT, query, substring(md5(query) from 0 for 15) AS SQL_ID, "
            + "left(query, strpos(query, ' '))  AS SQL_OPNAME, "
            + "coalesce(query_start, xact_start, backend_start) as query_start, "
            + "1000 * EXTRACT(EPOCH FROM (clock_timestamp()-coalesce(query_start, xact_start, backend_start))) as duration "
            + "from pg_stat_activity "
            // + "where state='active'"; // for test purposes..
            + "where state='active' and pid != pg_backend_pid()";

    public Postgres96() {}

    @Override
    public String getSqlTextAsh() { return sqlTextAsh; }

    @Override
    public String getSqlTextAshOneRow() { return sqlTextAsh; }

}
