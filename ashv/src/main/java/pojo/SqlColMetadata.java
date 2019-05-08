package pojo;

import lombok.Data;

@Data
public class SqlColMetadata {
    private int colId;
    private String colName;
    private String colDbTypeName;
    private String sqlAndColName;
    private int colSizeDisplay;
    private int colSizeSqlType;
}
