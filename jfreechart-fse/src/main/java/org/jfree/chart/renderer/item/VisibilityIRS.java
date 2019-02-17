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
 * ------------------
 * VisibilityIRS.java
 * ------------------
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

import java.io.Serializable;

import org.jfree.chart.renderer.AbstractRenderer;

/**
 * Defines an interface to control the visibility of individual items during 
 * rendering.  Implementing classes can be used together with subclasses of 
 * {@link AbstractRenderer} to control the rendering process.<br>
 * Works however only if the descendant of {@link AbstractRenderer} uses the 
 * per item method {@link AbstractRenderer#getItemVisible(int, int)}
 * 
 * @author zinsmaie
 */
public interface VisibilityIRS extends Serializable {

    /**
     * Specifies an individual item by row, column and returns true if it is 
     * visible.
     * 
     * @param row  the row (or series) index (zero-based).
     * @param column  the column (or category) index (zero-based).
     * 
     * @return true if the item is visible
     */
    public boolean getItemVisible(int row, int column);

}
