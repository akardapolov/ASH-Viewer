/*
 *-------------------
 * The AshWaitClass10g1.java is part of ASH Viewer
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

/**
 * Store waitClass and waitClassId for EventId. For 10g1
 */
@Entity
public
class AshWaitClass10g1 {

    /** The event id. */
    @PrimaryKey
    long eventId;

    /** The wait_class value */
    String valueWaitClass;
    
    /** The wait_class_id value */
    double valueWaitClassId;

	/**
     * Instantiates a new object
     * 
     * @param eventId the event Id
     * @param valueWaitClass the value of WaitClass
     * @param valueWaitClassId the value for WaitClassId
     */
    public AshWaitClass10g1(long eventId,
                      		String valueWaitClass,
                      		double valueWaitClassId) {
        this.eventId = eventId;
        this.valueWaitClass = valueWaitClass;
        this.valueWaitClassId = valueWaitClassId;
    }

	/**
	 * Instantiates a new sqlIdTypeText.
	 */
	private AshWaitClass10g1() {} // For bindings.

	/**
	 * @return the eventId
	 */
	public long getEventId() {
		return eventId;
	}

	/**
	 * @return the ValueWaitClass
	 */
	public String getValueWaitClass() {
		return valueWaitClass;
	}

	/**
	 * @return the ValueWaitClassId
	 */
	public double getValueWaitClassId() {
		return valueWaitClassId;
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
	    
	    retValue = "AshWaitClass10g1 ( "
	        + super.toString() + TAB
	        + "eventId = " + this.eventId + TAB
	        + "valueWaitClass = " + this.valueWaitClass + TAB
	        + "valueWaitClassId = " + this.valueWaitClassId + TAB
	        + " )";
	
	    return retValue;
	}
}
