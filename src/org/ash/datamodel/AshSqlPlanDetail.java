/*
 *-------------------
 * The AshSqlPlanDetail.java is part of ASH Viewer
 *-------------------
 * 
 * ASH Viewer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASH Viewer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ASH Viewer.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (c) 2009, Alex Kardapolov, All rights reserved.
 *
 */
package org.ash.datamodel;

import static com.sleepycat.persist.model.DeleteAction.NULLIFY;
import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * Store sql plan for sqlid's
 */
@Entity
public class AshSqlPlanDetail {

	/** The PK */
	@PrimaryKey(sequence="AshSqlPlanId")
	long AshSqlPlanId;
	
	/** Address of the handle to the parent for this cursor */
	String address;
	
	/** Hash value of the parent statement in the library cache */
	Double hashValue;
	
	/** SQL identifier of the parent cursor in the library cache */
	@SecondaryKey(relate = MANY_TO_ONE)
	String sqlId;
	
	/** Numerical representation of the SQL plan for the cursor */
	@SecondaryKey(relate = MANY_TO_ONE, relatedEntity=AshSqlPlanParent.class,
			onRelatedEntityDelete=NULLIFY)
	double planHashValue;

	/** Address of the child cursor*/
	String childAddress;
	
	/** Number of the child cursor that uses this execution plan */
	long childNumber;
	
	/** Date and time when the execution plan was generated */
	long timestamp;
	
	/** Name of the internal operation performed in this step (for example, TABLE ACCESS) */
	String operation;

	/** A variation on the operation described in the OPERATION column (for example, FULL) */
	String options;

	/** Name of the database link used to reference the object (a table name or view name) */
	String objectNode;
	
	/** Object number of the table or the index */
	Double object;

	/** Name of the user who owns the schema containing the table or index */
	String objectOwner;
	
	/** Name of the table or index */
	String objectName;
	
	/** Alias for the object */
	String objectAlias;
	
	/**Type of the object */
	String objectType;
	
	/** Current mode of the optimizer for the first row in the plan (statement line), for example, CHOOSE */
	String optimizer;
		
	/** A number assigned to each step in the execution plan */
	long Id;

	/** ID of the next execution step that operates on the output of the current step */
	long parentId;

	/** Depth (or level) of the operation in the tree */
	long depth;

	/** Order of processing for all operations that have the same PARENT_ID */
	long position;

	/** Number of index columns with start and stop keys (that is, the number of columns with matching predicates) */
	long searchColumns;

	/** Cost of the operation as estimated by the optimizer's cost-based approach */
	double cost;

	/** Estimate, by the cost-based optimizer, of the number of rows produced by the operation */
	double cardinality;

	/** Estimate, by the cost-based optimizer, of the number of bytes produced by the operation */
	double bytes;

	/** Describes the contents of the OTHER column. See EXPLAIN PLAN for values */
	String otherTag;

	/** Start partition of a range of accessed partitions */
	String partitionStart;

	/** Stop partition of a range of accessed partitions */
	String partitionStop;

	/** Step that computes the pair of values of the PARTITION_START and PARTITION_STOP columns*/
	double partitionId;

	/** Other information specific to the execution step that users may find useful. See EXPLAIN PLAN for values */
	String other;

	/** Stores the method used to distribute rows from producer query servers to consumer query servers */
	String distribution;

	/** CPU cost of the operation as estimated by the optimizer's cost-based approach */
	double cpuCost;
	
	/** I/O cost of the operation as estimated by the optimizer's cost-based approach */
	double ioCost;
	
	/** Temporary space usage of the operation (sort or hash-join) as estimated by the optimizer's cost-based approach */
	double tempSpace;
	
	/** Predicates used to locate rows in an access structure. For example, start or stop predicates for an index range scan */
	String accessPredicates;
	
	/** Predicates used to filter rows before producing them */
	String filterPredicates;
	
	/** Expressions produced by the operation */
	String projection;
	
	/** Elapsed time (in seconds) of the operation as estimated by the optimizer's cost-based approach */
	double time;
	
	/** Name of the query block */
	String qblockName;
	
	/** Remarks */
	String remarks;
	
	/***
	 * Instantiates a new object
	 * 
	 * @param AshSqlPlanId
	 * @param address
	 * @param hashValue
	 * @param sqlId
	 * @param planHashValue
	 * @param childAddress
	 * @param childNumber
	 * @param timestamp
	 * @param operations
	 * @param options
	 * @param objectNode
	 * @param object
	 * @param objectOwner
	 * @param objectName
	 * @param objectAlias
	 * @param objectType
	 * @param optimizer
	 * @param Id
	 * @param parentId
	 * @param depth
	 * @param position
	 * @param searchColumns
	 * @param cost
	 * @param cardinality
	 * @param bytes
	 * @param otherTag
	 * @param partitionStart
	 * @param partitionStop
	 * @param partitionId
	 * @param other
	 * @param distribution
	 * @param cpuCost
	 * @param ioCost
	 * @param tempSpace
	 * @param accessPredicates
	 * @param filterPredicates
	 * @param projection
	 * @param time
	 * @param qblockName
	 * @param remarks
	 */
	public AshSqlPlanDetail(long AshSqlPlanId, String address, Double hashValue, String sqlId, double planHashValue,
			String childAddress, long childNumber, long timestamp, String operation, String options,
			String objectNode, Double object, String objectOwner, String objectName, String objectAlias,
			String objectType, String optimizer, long Id, long parentId, long depth, long position,
			long searchColumns, double cost, double cardinality, double bytes, String otherTag,
			String partitionStart, String partitionStop, double partitionId, String other, String distribution,
			double cpuCost, double ioCost, double tempSpace, String accessPredicates, String filterPredicates,
			String projection, double time, String qblockName, String remarks
			) {
		
		this.AshSqlPlanId = AshSqlPlanId;
		this.address = address;
		this.hashValue = hashValue; 
		this.sqlId = sqlId;
		this.planHashValue = planHashValue;
		this.childAddress = childAddress;
		this.childNumber = childNumber;
		this.timestamp = timestamp;
		this.operation = operation; 
		this.options = options;
		this.objectNode = objectNode; 
		this.object = object;
		this.objectOwner = objectOwner;
		this.objectName = objectName;
		this.objectAlias = objectAlias;
		this.objectType = objectType;
		this.optimizer = optimizer;
		this.Id = Id;
		this.parentId = parentId;
		this.depth = depth;
		this.position = position;
		this.searchColumns = searchColumns;
		this.cost = cost;
		this.cardinality = cardinality;
		this.bytes = bytes;
		this.otherTag = otherTag;
		this.partitionStart = partitionStart;
		this.partitionStop = partitionStop;
		this.partitionId = partitionId;
		this.other = other;
		this.distribution = distribution;
		this.cpuCost = cpuCost;
		this.ioCost = ioCost;
		this.tempSpace = tempSpace;
		this.accessPredicates = accessPredicates;
		this.filterPredicates = filterPredicates;
		this.projection = projection;
		this.time = time;
		this.qblockName = qblockName;
		this.remarks = remarks;
	}

	/**
	 * Instantiates a new AshSqlPlan.
	 */
	private AshSqlPlanDetail() {
	} // For bindings.

	/**
	 * @return the ashSqlPlanId
	 */
	public long getAshSqlPlanId() {
		return AshSqlPlanId;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the hashValue
	 */
	public Double getHashValue() {
		return hashValue;
	}

	/**
	 * @return the sqlId
	 */
	public String getSqlId() {
		return sqlId;
	}

	/**
	 * @return the planHashValue
	 */
	public double getPlanHashValue() {
		return planHashValue;
	}

	/**
	 * @return the childAddress
	 */
	public String getChildAddress() {
		return childAddress;
	}

	/**
	 * @return the childNumber
	 */
	public long getChildNumber() {
		return childNumber;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the operations
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * @return the options
	 */
	public String getOptions() {
		return options;
	}

	/**
	 * @return the objectNode
	 */
	public String getObjectNode() {
		return objectNode;
	}

	/**
	 * @return the object
	 */
	public Double getObject() {
		return object;
	}

	/**
	 * @return the objectOwner
	 */
	public String getObjectOwner() {
		return objectOwner;
	}

	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return objectName;
	}

	/**
	 * @return the objectAlias
	 */
	public String getObjectAlias() {
		return objectAlias;
	}

	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return objectType;
	}

	/**
	 * @return the optimizer
	 */
	public String getOptimizer() {
		return optimizer;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return Id;
	}

	/**
	 * @return the parentId
	 */
	public long getParentId() {
		return parentId;
	}

	/**
	 * @return the depth
	 */
	public long getDepth() {
		return depth;
	}

	/**
	 * @return the position
	 */
	public long getPosition() {
		return position;
	}

	/**
	 * @return the searchColumns
	 */
	public long getSearchColumns() {
		return searchColumns;
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @return the cardinality
	 */
	public double getCardinality() {
		return cardinality;
	}

	/**
	 * @return the bytes
	 */
	public double getBytes() {
		return bytes;
	}

	/**
	 * @return the otherTag
	 */
	public String getOtherTag() {
		return otherTag;
	}

	/**
	 * @return the partitionStart
	 */
	public String getPartitionStart() {
		return partitionStart;
	}

	/**
	 * @return the partitionStop
	 */
	public String getPartitionStop() {
		return partitionStop;
	}

	/**
	 * @return the partitionId
	 */
	public double getPartitionId() {
		return partitionId;
	}

	/**
	 * @return the other
	 */
	public String getOther() {
		return other;
	}

	/**
	 * @return the distribution
	 */
	public String getDistribution() {
		return distribution;
	}

	/**
	 * @return the cpuCost
	 */
	public double getCpuCost() {
		return cpuCost;
	}

	/**
	 * @return the ioCost
	 */
	public double getIoCost() {
		return ioCost;
	}

	/**
	 * @return the tempSpace
	 */
	public double getTempSpace() {
		return tempSpace;
	}

	/**
	 * @return the accessPredicates
	 */
	public String getAccessPredicates() {
		return accessPredicates;
	}

	/**
	 * @return the filterPredicates
	 */
	public String getFilterPredicates() {
		return filterPredicates;
	}

	/**
	 * @return the projection
	 */
	public String getProjection() {
		return projection;
	}

	/**
	 * @return the time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * @return the qblockName
	 */
	public String getQblockName() {
		return qblockName;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	@Override
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "AshSqlPlanDetail ( "
	        + super.toString() + TAB
	        + "AshSqlPlanId = " + this.AshSqlPlanId + TAB
	        + "address = " + this.address + TAB
	        + "hashValue = " + this.hashValue + TAB
	        + "sqlId = " + this.sqlId + TAB
	        + "planHashValue = " + this.planHashValue + TAB
	        + "childAddress = " + this.childAddress + TAB
	        + "childNumber = " + this.childNumber + TAB
	        + "timestamp = " + this.timestamp + TAB
	        + "operations = " + this.operation + TAB
	        + "options = " + this.options + TAB
	        + "objectNode = " + this.objectNode + TAB
	        + "object = " + this.object + TAB
	        + "objectOwner = " + this.objectOwner + TAB
	        + "objectName = " + this.objectName + TAB
	        + "objectAlias = " + this.objectAlias + TAB
	        + "objectType = " + this.objectType + TAB
	        + "optimizer = " + this.optimizer + TAB
	        + "Id = " + this.Id + TAB
	        + "parentId = " + this.parentId + TAB
	        + "depth = " + this.depth + TAB
	        + "position = " + this.position + TAB
	        + "searchColumns = " + this.searchColumns + TAB
	        + "cost = " + this.cost + TAB
	        + "cardinality = " + this.cardinality + TAB
	        + "bytes = " + this.bytes + TAB
	        + "otherTag = " + this.otherTag + TAB
	        + "partitionStart = " + this.partitionStart + TAB
	        + "partitionStop = " + this.partitionStop + TAB
	        + "partitionId = " + this.partitionId + TAB
	        + "other = " + this.other + TAB
	        + "distribution = " + this.distribution + TAB
	        + "cpuCost = " + this.cpuCost + TAB
	        + "ioCost = " + this.ioCost + TAB
	        + "tempSpace = " + this.tempSpace + TAB
	        + "accessPredicates = " + this.accessPredicates + TAB
	        + "filterPredicates = " + this.filterPredicates + TAB
	        + "projection = " + this.projection + TAB
	        + "time = " + this.time + TAB
	        + "qblockName = " + this.qblockName + TAB
	        + "remarks = " + this.remarks + TAB
	        + " )";
	
	    return retValue;
	}



}
