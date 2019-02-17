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
 * -------------
 * XYCursor.java
 * -------------
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
 * A DatasetCursor implementation for xy datasets.
 * @author zinsmaie
 */
public class XYCursor implements DatasetCursor {

    /** a generated serial id */
    private static final long serialVersionUID = -8005904310382047935L;
    
    /** stores the series position */
    public int series;
    /** stores the item position */
    public int item;

    /**
     * creates a cursor without assigned position (the cursor will only
     * be valid if setPosition is called at some time)
     */
    public XYCursor() {
    }
    
    /**
     * Default xy cursor constructor. Sets the cursor position to the specified 
     * values.
     * 
     * @param series
     * @param item
     */
    public XYCursor(int series, int item) {
        this.series = series;
        this.item = item;
    }

    /**
     * Sets the cursor position to the specified values.
     * @param series
     * @param item
     */
    public void setPosition(int series, int item) {
        this.series = series;
        this.item = item;
    }

    //in contrast to the other cursor implementations
    //hasCode and equals for the XYCursor work guaranteed as expected
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + item;
        result = prime * result + series;
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
        XYCursor other = (XYCursor) obj;
        if (item != other.item) {
            return false;
        }
        if (series != other.series) {
            return false;
        }
        return true;
    }

}
