/*
 *-------------------
 * The AshIdTime.java is part of ASH Viewer
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
 * The Class Ash.
 */
@Entity
public
class AshIdTime {

    /** The sample id. */
    @PrimaryKey
    long sampleId;

    /** The sample time. */
    @SecondaryKey(relate = MANY_TO_ONE)
    double sampleTime;

    /**
     * Instantiates a new ash.
     * 
     * @param sampleId the sample id
     * @param sampleTime the sample time
     */
    public AshIdTime(long sampleId,
                      double sampleTime) {
        this.sampleId = sampleId;
        this.sampleTime = sampleTime;
    }

	/**
	 * Instantiates a new ash.
	 */
	private AshIdTime() {} // For bindings.

    /**
     * Gets the sample id.
     * 
     * @return the sample id
     */
    public long getsampleId() {
        return sampleId;
    }

    /**
     * Gets the sample time.
     * 
     * @return the sample time
     */
    public double getsampleTime() {
        return sampleTime;
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
	    
	    retValue = "AshIdTime ( "
	        + super.toString() + TAB
	        + "sampleId = " + this.sampleId + TAB
	        + "sampleTime = " + this.sampleTime + TAB
	        + " )";
	
	    return retValue;
	}
}
