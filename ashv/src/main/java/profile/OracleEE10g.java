package profile;

public class OracleEE10g extends OracleEE implements IProfile {
    String profileName = "OracleEE10g";

    String sqlTextAsh = "select ash.*, decode(aa.name, 'UNKNOWN', null, aa.name) sql_opname "
        + "from v$active_session_history ash, audit_actions aa where ash.sql_opcode = aa.action(+)";

    String sqlTextAshOneRow = sqlTextAsh + " and rownum = 1";

    public OracleEE10g() {}

    @Override
    public String getProfileName() { return profileName; }

    @Override
    public String getSqlTextAsh() { return sqlTextAsh; }

    @Override
    public String getSqlTextAshOneRow() { return sqlTextAshOneRow; }
}
