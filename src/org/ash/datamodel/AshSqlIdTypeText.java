/*
 *-------------------
 * The AshSqlIdTypeText.java is part of ASH Viewer
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
 * Store sqlid, type of sql and sql_text
 */
@Entity
public
class AshSqlIdTypeText {

    /** The sql id. */
    @PrimaryKey
    String sqlId;

    /** The command type. */
    String commandType;
    
    /** The command type. */
    String sqlText;

	/**
     * Instantiates a new ash.
     * 
     * @param sampleId the sample id
     * @param sampleTime the sample time
     */
    public AshSqlIdTypeText(String sqlId,
                      String commandType,
                      String sqlText) {
        this.sqlId = sqlId;
        this.commandType = commandType;
        this.sqlText = sqlText;
    }

	/**
	 * Instantiates a new sqlIdTypeText.
	 */
	private AshSqlIdTypeText() {} // For bindings.

	  /**
	 * @return the sqlId
	 */
	public String getSqlId() {
		return sqlId;
	}

	/**
	 * @return the commandType
	 */
	public String getCommandType() {
		return commandType;
	}

	/**
	 * @return the sqlText
	 */
	public String getSqlText() {
		return sqlText;
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
	    
	    retValue = "AshSqlIdTypeText ( "
	        + super.toString() + TAB
	        + "sqlId = " + this.sqlId + TAB
	        + "commandType = " + this.commandType + TAB
	        + "sqlText = " + this.sqlText + TAB
	        + " )";
	
	    return retValue;
	}
}
