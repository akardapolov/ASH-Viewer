package pojo;

import lombok.Data;

@Data
public class SqlPojo {
    // SQL identifier of the parent cursor in the library cache
    String sql_id;
    // Numerical representation of the SQL plan for the cursor
    long plan_hash_value;
    // Address of the child cursor
    String child_address;
}
