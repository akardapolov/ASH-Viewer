package config.profile;

import lombok.Data;

@Data
public class SqlColProfile {
    private int colId;
    private String colName;
    private String colDbTypeName;
    private String sqlAndColName;
    private int colSizeDisplay;
    private int colSizeSqlType;
}
