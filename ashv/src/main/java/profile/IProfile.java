package profile;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public interface IProfile {
    String getProfileName();
    String getDriverName();

    String getSqlTextSysdate();
    String getSqlTextAsh();
    String getSqlTextAshOneRow();
    String getSqlTextUserIdName();
    String getSqlTextColumn();

    String getSampleTimeColName();
    String getWaitClassColName();
    String getEventColName();

    String getSqlIdColName();
    List<String> getSqlIdAdditionalColName();

    String getSessColName();
    String getSerialColName();
    List<String> getSessAdditionalColName();

    LinkedHashSet<String> getUniqueTreeEventListByWaitClass();

    LinkedHashMap<String, Color> getWaitClass();

    /** Sql detail **/
    LinkedHashMap<String, String> getSqlStatsQuery();
    String getSqlFullText();
    String getSqlPlanText();
    String getSqlForPlanHashValueList();

    /** Session detail **/
    LinkedHashMap<String, String> getSessionStatsQuery();

    byte getWaitClassId(String waitClass);
    String getWaitClass(byte waitClassId);

    long getInterval();
}
