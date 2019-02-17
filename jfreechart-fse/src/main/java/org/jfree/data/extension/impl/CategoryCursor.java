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
 * -------------------
 * CategoryCursor.java
 * -------------------
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
 * A DatasetCursor implementation for category datasets.
 * 
 * @author zinsmaie
 */
public class CategoryCursor<ROW_KEY extends Comparable<ROW_KEY>, 
        COLUMN_KEY extends Comparable<COLUMN_KEY>> implements DatasetCursor {

    /** a generated serial id. */
    private static final long serialVersionUID = 7086987028899208483L;
    
    /** stores the key of the row position */
    public ROW_KEY rowKey;
    
    /** stores the key of the column position */
    public COLUMN_KEY columnKey;

    /**
     * creates a cursor without assigned position (the cursor will only
     * be valid if setPosition is called at some time)
     */
    public CategoryCursor() {
    }
    
    /**
     * Default category cursor constructor. Sets the cursor position to the 
     * specified values.
     * 
     * @param rowKey
     * @param columnKey
     */
    public CategoryCursor(ROW_KEY rowKey, COLUMN_KEY columnKey) {
        this.rowKey = rowKey;
        this.columnKey = columnKey;
    }

    /**
     * Sets the cursor position to the specified values.
     * 
     * @param rowKey
     * @param columnKey
     */
    public void setPosition(ROW_KEY rowKey, COLUMN_KEY columnKey) {
        this.rowKey = rowKey;
        this.columnKey = columnKey;
    }
    
    //depend on the implementation of comparable
    //if the keys overrides hashCode and equals these methods will function
    //for the cursor (e.g. String, Integer, ...)

    @Override
    public int hashCode() {
        int result = 31 + ((columnKey == null) ? 0 : columnKey.hashCode());
        result = 31 * result + ((rowKey == null) ? 0 : rowKey.hashCode());
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
        CategoryCursor other = (CategoryCursor) obj;
        if (columnKey == null) {
            if (other.columnKey != null) {
                return false;
            }
        } else if (!columnKey.equals(other.columnKey)) {
            return false;
        }
        if (rowKey == null) {
            if (other.rowKey != null) {
                return false;
            }
        } else if (!rowKey.equals(other.rowKey)) {
            return false;
        }
        return true;
    }

}
