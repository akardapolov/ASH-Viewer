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
 * LabelIRS.java
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

package org.jfree.chart.renderer.item;

import java.awt.Font;
import java.io.Serializable;

import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.renderer.AbstractRenderer;

/**
 * Defines an interface to control the rendering of labels for individual items.
 * Implementing classes can be used together with subclasses of 
 * {@link AbstractRenderer} to control the rendering process.<br>
 * Works however only if the descendant of {@link AbstractRenderer} uses per 
 * item methods like {@link AbstractRenderer#getItemLabelFont(int, int)}
 * 
 * @author zinsmaie
 */
public interface LabelIRS extends Serializable {

    /**
     * Specifies an individual item by row, column and returns the font for its
     * label.
     * 
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     * 
     * @return a Font (never <code>null<code>)
     */
    public Font getItemLabelFont(int row, int column);

    /**
     * Specifies an individual item by row, column and returns true if its 
     * label should be rendered.
     * 
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     * 
     * @return true if the label should be rendered
     */
    public boolean isItemLabelVisible(int row, int column);

    /**
     * Specifies an individual item by row, column and returns the label 
     * position. 
     * 
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     * 
     * @return The item label position (never <code>null</code>).
     */
    public ItemLabelPosition getPositiveItemLabelPosition(int row, int column);

    /**
     * Specifies an individual item by row, column and returns the label 
     * position. 
     * 
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     * 
     * @return The item label position (never <code>null</code>).
     */
    public ItemLabelPosition getNegativeItemLabelPosition(int row, int column);

}
