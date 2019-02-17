/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2013, by Object Refinery Limited and Contributors.
 *
 * Project Info:  http://www.jfree.org/jfreechart/index.html
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301,
 * USA.
 *
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 *
 * --------------
 * PieCursor.java
 * --------------
 * (C) Copyright 2013, by Michael Zinsmaier.
 *
 * Original Author:  Michael Zinsmaier;
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 17-Sep-2013 : Version 1 (MZ);
 *
 */

package org.jfree.data.extension.impl;

import org.jfree.data.extension.DatasetCursor;

/**
 * A DatasetCursor implementation for pie datasets.
 * @author zinsmaie
 */
public class PieCursor<KEY extends Comparable<KEY>> implements DatasetCursor {

    /** a generated serial id */
    private static final long serialVersionUID = -7031433882367850307L;
    
    /** stores the key of the section */
    public KEY key;
    
    /**
     * creates a cursor without assigned position (the cursor will only
     * be valid if setPosition is called at some time)
     */
    public PieCursor() {
    }
    
    /**
     * Default pie cursor constructor. Sets the cursor position to the 
     * specified value.
     * 
     * @param key
     */
    public PieCursor(KEY key) {
        this.key = key;
    }

    /**
     * sets the cursor position to the specified value
     * @param key
     */
    public void setPosition(KEY key) {
        this.key = key;
    }

    //depend on the implementation of comparable
    //if the key overrides hashCode and equals these methods will function
    //for the cursor (e.g. String, Integer, ...)
    
    @Override
    public int hashCode() {
        int result = 31  + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        @SuppressWarnings("rawtypes")
        PieCursor other = (PieCursor) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

}
