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
 * --------------------------
 * DatasetLabelExtension.java
 * --------------------------
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

package org.jfree.data.extension;

import org.jfree.data.general.SelectionChangeListener;

/**
 * Extends a dataset such that each data item has an additional label attribute
 * that assigns a class to this item. Each item is part of exactly one label 
 * class.
 * 
 * @author zinsmaie
 */
public interface DatasetLabelExtension<CURSOR extends DatasetCursor> 
        extends DatasetExtension, 
        WithChangeListener<SelectionChangeListener<CURSOR>> {
    
    /** default class for not labeled data items. */
    public final int NO_LABEL = -1;
    
    /**
     * @param cursor specifies the position of the data item
     * @return the label attribute of the data item
     */
    public int getLabel(CURSOR cursor);
    
    /**
     * @param cursor specifies the position of the data item
     * @param label the new label value for the data item
     */
    public void setLabel(CURSOR cursor, int label);
    
}
