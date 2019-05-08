package pojo;

import lombok.Data;

@Data
public class SqlPlanPojo {
    // Address of the handle to the parent for this cursor
    String address;

    // Hash value of the parent statement in the library cache
    Double hash_value;

    // SQL identifier of the parent cursor in the library cache
    String sql_id;

    // Numerical representation of the SQL plan for the cursor
    long plan_hash_value;

    // Address of the child cursor
    String child_address;

    // Number of the child cursor that uses this execution plan
    long child_number;

    // Date and time when the execution plan was generated
    long timestamp;

    // Name of the internal operation performed in this step (for example, TABLE ACCESS)
    String operation;

    // A variation on the operation described in the OPERATION column (for example, FULL)
    String options;

    // Name of the database link used to reference the object (a table name or view name)
    String object_node;

    // Object number of the table or the index !!! object# !!!
    Double obj;

    // Name of the user who owns the schema containing the table or index
    String object_owner;

    // Name of the table or index
    String object_name;

    // Alias for the object
    String object_alias;

    // Type of the object
    String object_type;

    // Current mode of the optimizer for the first row in the plan (statement line), for example, CHOOSE
    String optimizer;

    // A number assigned to each step in the execution plan
    long id;

    // ID of the next execution step that operates on the output of the current step
    long parent_id;

    // Depth (or level) of the operation in the tree
    long depth;

    // Order of processing for all operations that have the same PARENT_ID
    long position;

    // Number of index columns with start and stop keys (that is, the number of columns with matching predicates)
    long search_columns;

    // Cost of the operation as estimated by the optimizer's cost-based approach
    double cost;

    // Estimate, by the cost-based optimizer, of the number of rows produced by the operation
    double cardinality;

    // Estimate, by the cost-based optimizer, of the number of bytes produced by the operation
    double bytes;

    // Describes the contents of the OTHER column. See EXPLAIN PLAN for values
    String other_tag;

    // Start partition of a range of accessed partitions
    String partition_start;

    // Stop partition of a range of accessed partitions
    String partition_stop;

    // Step that computes the pair of values of the PARTITION_START and PARTITION_STOP columns
    double partition_id;

    // Other information specific to the execution step that users may find useful. See EXPLAIN PLAN for values
    String other;

    // Stores the method used to distribute rows from producer query servers to consumer query servers
    String distribution;

    // CPU cost of the operation as estimated by the optimizer's cost-based approach
    double cpu_cost;

    // I/O cost of the operation as estimated by the optimizer's cost-based approach
    double io_cost;

    // Temporary space usage of the operation (sort or hash-join) as estimated by the optimizer's cost-based approach
    double temp_space;

    // Predicates used to locate rows in an access structure. For example, start or stop predicates for an index range scan
    String access_predicates;

    // Predicates used to filter rows before producing them
    String filter_predicates;

    // Expressions produced by the operation
    String projection;

    // Elapsed time (in seconds) of the operation as estimated by the optimizer's cost-based approach
    double time;

    // Name of the query block
    String qblock_name;

    // Remarks
    String remarks;
}
