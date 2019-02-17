package profile;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public interface IProfile {

    String getProfileName();
    String getDriverName();

    String getSqlTextSysdate();
    String getSqlTextSysdateCol();

    String getSqlTextAsh();
    String getSqlTextAshOneRow();
    String getSqlTextMin();
    String getSqlTextUserIdName();

    String getSampleTimeColName();

    String getWaitClassColName();
    String getEventColName();

    String getSqlIdColName();
    List<String> getSqlIdAddColName();

    String getSessColName();
    String getSerialColName();
    List<String> getSessAddColName();

    LinkedHashSet<String> getUniqueTreeEventListByWaitClass();

    LinkedHashMap<String, Color> getWaitClass();

    /** Sql detail **/
    LinkedHashMap<String, String> getSqlStatsQuery();
    String getSqlFullText();
    String getSqlPlanText();
    String getSqlForPlanHashValueList();
    /** Sql detail **/

    /** Session detail **/
    LinkedHashMap<String, String> getSessionStatsQuery();
    /** Session detail **/

    String getSqlTextColumn();

    byte getWaitClassId(String waitClass);
    String getWaitClass(byte waitClassId);

    boolean isDeleteOldDataOnStart();

    long getInterval();
}
