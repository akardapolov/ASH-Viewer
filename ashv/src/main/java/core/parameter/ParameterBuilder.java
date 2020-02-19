package core.parameter;

import java.util.List;
import java.util.Map;

public class ParameterBuilder {

    private final double beginTime;
    private final double endTime;
    private Map<String, List<String>> columnNameToData;
    private final String sqlName;
    private final String colNameFilter;

    public double getBeginTime() { return beginTime; }
    public double getEndTime() {
        return endTime;
    }
    public Map<String, List<String>> getColumnNameToData() { return columnNameToData; }
    public String getSqlName() {
        return sqlName;
    }
    public String getColNameFilter() { return colNameFilter; }

    public static class Builder {
        private final double beginTime;
        private final double endTime;
        private Map<String, List<String>> dbaFilesIdList = null;

        private String sqlName;
        private String colNameFilter;

        public Builder(double beginTime, double endTime) {
            this.beginTime = beginTime;
            this.endTime = endTime;
        }

        public Builder dbaFilesIdList(Map<String, List<String>> val) { dbaFilesIdList = val; return this; }
        public Builder sqlName(String val) { sqlName = val; return this; }
        public Builder colNameFilter(String val) { colNameFilter = val; return this; }
        public ParameterBuilder build() {
            return new ParameterBuilder(this);
        }
    }

    private ParameterBuilder(Builder builder){
        beginTime = builder.beginTime;
        endTime = builder.endTime;
        columnNameToData = builder.dbaFilesIdList;
        sqlName = builder.sqlName;
        colNameFilter = builder.colNameFilter;
    }

}
