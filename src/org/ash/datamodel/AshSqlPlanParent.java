/*
 *-------------------
 * The AshSqlPlanParent.java is part of ASH Viewer
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

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.SecondaryKey;
import static com.sleepycat.persist.model.Relationship.MANY_TO_ONE;

/**
 * The Class AshSqlPlanParent.
 */
@Entity
public
class AshSqlPlanParent {

    /** The sample id. */
    @PrimaryKey
    double planHashValue;

    /** The sample time. */
    @SecondaryKey(relate = MANY_TO_ONE)
    String sqlId;

    /**
     * Instantiates a new AshSqlPlanParent.
     * 
     * @param planHashValue
     * @param sqlId
     */
    public AshSqlPlanParent(double planHashValue,
                      String sqlId) {
        this.planHashValue = planHashValue;
        this.sqlId = sqlId;
    }

	/**
	 * Instantiates a new ash.
	 */
	private AshSqlPlanParent() {} // For bindings.

    /**
     * Gets the sql plan hash value
     * 
     * @return the sample id
     */
    public double getPlanHashValue() {
        return planHashValue;
    }

    /**
     * Get sql id
     * 
     * @return
     */
    public String getSqlId() {
        return sqlId;
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
	    
	    retValue = "AshSqlPlanParent ( "
	        + super.toString() + TAB
	        + "planHashValue = " + this.planHashValue + TAB
	        + "sqlId = " + this.sqlId + TAB
	        + " )";
	
	    return retValue;
	}
}
