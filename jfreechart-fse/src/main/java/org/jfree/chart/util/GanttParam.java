package org.jfree.chart.util;

import java.util.List;

public class GanttParam {

    private final double beginTime;
    private final double endTime;

    private String sqlId, sessionId, serial, currentFileR, fileName, tsName, waitClass;

    private boolean isSqlSessionDetial;

    private List<Long> dbaFilesIdList;

    public double getBeginTime() {
        return beginTime;
    }

    public double getEndTime() {
        return endTime;
    }

    public String getSqlId() {
        return sqlId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSerial() {
        return serial;
    }

    public String getCurrentFileR() { return currentFileR; }

    public String getFileName() { return fileName; }

    public String getTsName() { return tsName; }

    public String getWaitClass() { return waitClass; }

    public boolean isSqlSessionDetial() { return isSqlSessionDetial; }

    public List<Long> getDbaFilesIdList() { return dbaFilesIdList; }

    public static class Builder {
        private final double beginTime;
        private final double endTime;

        private String sqlId = "";
        private String sessionId = "";
        private String serial = "";
        private String currentFileR = "";
        private String fileName = "";
        private String tsName = "";
        private String waitClass = "";
        private boolean isSqlSessionDetial = false;

        private List<Long> dbaFilesIdList = null;

        public Builder(double beginTime, double endTime) {
            this.beginTime = beginTime;
            this.endTime = endTime;
        }

        public Builder sqlId(String val) { sqlId = val; return this; }
        public Builder sessionId(String val) { sessionId = val; return this; }
        public Builder serial(String val) { serial = val; return this; }
        public Builder currentFileR(String val) { currentFileR = val; return this; }
        public Builder fileName(String val) { fileName = val; return this; }
        public Builder tsName(String val) { tsName = val; return this; }
        public Builder waitClass(String val) { waitClass = val; return this; }
        public Builder isSqlSessionDetial(boolean val) { isSqlSessionDetial = val; return this; }
        public Builder dbaFilesIdList(List<Long> val) { dbaFilesIdList = val; return this; }

        public GanttParam build() {
            return new GanttParam(this);
        }
    }

    private GanttParam(Builder builder){
        beginTime = builder.beginTime;
        endTime = builder.endTime;
        sqlId = builder.sqlId;
        sessionId = builder.sessionId;
        serial = builder.serial;
        currentFileR = builder.currentFileR;
        fileName = builder.fileName;
        tsName = builder.tsName;
        waitClass = builder.waitClass;
        isSqlSessionDetial = builder.isSqlSessionDetial;
        dbaFilesIdList = builder.dbaFilesIdList;
    }

}
