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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * ---------------------
 * SelectionManager.java
 * ---------------------
 * (C) Copyright 2013, by Michael Zinsmaier and Contributors.
 *
 * Original Author:  Michael Zinsmaier;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * Changes:
 * --------
 * 17-Sep-2013 : Version 1 from MZ (DG);
 *
 */

package org.jfree.chart.panel.selectionhandler;

import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/**
 * Provides methods to handle data item selection based on a selection region
 * or a selection point and to clear the current selection. 
 * 
 * @author zinsmaie
 */
public interface SelectionManager extends Serializable {

    /**
     * selects the data item at the point x,y
     * @param x
     * @param y
     */
    public void select(double x, double y);
    
    /**
     * @param selection a rectangular selection area
     */
    public void select(Rectangle2D selection);
    
    /**
     * @param selection free defined selection area
     */
    public void select(GeneralPath selection);

    /**
     * clear the current selection (deselect all)
     */
    public void clearSelection();
    
}
