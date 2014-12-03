-- Create monitor user and grant select and execute priviliges to connect to Oracle DB (ASH Viewer)
create user monitor identified by monitor;

grant connect to monitor;

grant select on dba_users to monitor;
grant select on v_$sql to monitor;
grant select on v_$sql_plan to monitor;
grant select on v_$active_session_history to monitor;
grant select on v_$parameter to monitor;
grant select on v_$database to monitor;
grant select on v_$instance to monitor;

grant select_catalog_role to monitor;

grant execute on DBMS_APPLICATION_INFO to monitor;
grant execute on DBMS_WORKLOAD_REPOSITORY to monitor;
grant execute on DBMS_XPLAN to monitor;
