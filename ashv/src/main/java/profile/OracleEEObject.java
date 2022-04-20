package profile;

public class OracleEEObject extends OracleEE implements IProfile {
    String profileName = "OracleEEObject";

    String sqlTextAsh = "select /*+ ALL_ROWS */ a.*, b.object_name, b.object_type, subobject_name "
        + "from v$active_session_history a, all_objects b where a.current_obj# = b.object_id(+)";

    String sqlTextAshOneRow = sqlTextAsh + " and rownum = 1";

    public OracleEEObject() {}

    @Override
    public String getProfileName() { return profileName; }

    @Override
    public String getSqlTextAsh() { return sqlTextAsh; }

    @Override
    public String getSqlTextAshOneRow() { return sqlTextAshOneRow; }
}
