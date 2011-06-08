/*
 *-------------------
 * The AshUserIdUsername.java is part of ASH Viewer
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
 * The Class Ash.
 */
@Entity
public
class AshUserIdUsername {

    /** The userid. */
	@PrimaryKey
    long userId;

    /** The username. */
    String Username;

    /**
     * Instantiates a new userIdUsername.
     * 
     * @param userId the user id
     * @param Username the username
     */
    public AshUserIdUsername(long userId,
                      String Username) {
        this.userId = userId;
        this.Username = Username;
    }

	/**
	 * Instantiates a new userIdUsername.
	 */
	private AshUserIdUsername() {} // For bindings.

    /**
     * Gets the user id.
     * 
     * @return the user id
     */
    public long getuserId() {
        return userId;
    }

    /**
     * Gets the username
     * 
     * @return the sample time
     */
    public String getUsername() {
        return Username;
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
	    
	    retValue = "AshUserIdUsername ( "
	        + super.toString() + TAB
	        + "userId = " + this.userId + TAB
	        + "Username = " + this.Username + TAB
	        + " )";
	
	    return retValue;
	}
}
