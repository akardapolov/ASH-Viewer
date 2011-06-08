/*
 *-------------------
 * The AshParamValue.java is part of ASH Viewer
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
 * Store parameters 
 */
@Entity
public
class AshParamValue {

    /** The parameter id. */
    @PrimaryKey
    String parameterId;

    /** The parameter value */
    String value;

	/**
     * Instantiates a new object
     * 
     * @param sampleId the sample id
     * @param sampleTime the sample time
     */
    public AshParamValue(String parameterId,
                      String value) {
        this.parameterId = parameterId;
        this.value = value;
    }

	/**
	 * Instantiates a new sqlIdTypeText.
	 */
	private AshParamValue() {} // For bindings.

	  /**
	 * @return the sqlId
	 */
	public String getParameterId() {
		return parameterId;
	}

	/**
	 * @return the commandType
	 */
	public String getValue() {
		return value;
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
	    
	    retValue = "AshParamValue ( "
	        + super.toString() + TAB
	        + "parameterId = " + this.parameterId + TAB
	        + "value = " + this.value + TAB
	        + " )";
	
	    return retValue;
	}
}
