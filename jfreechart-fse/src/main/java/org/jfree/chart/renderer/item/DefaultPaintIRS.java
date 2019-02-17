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
 * --------------------
 * DefaultPaintIRS.java
 * --------------------
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

import java.awt.Paint;

import org.jfree.chart.renderer.AbstractRenderer;

/**
 * Implements a per series default item rendering strategy for the item paint. 
 * {@link DefaultItemRenderingStrategy}
 * 
 * @author zinsmaie
 */
public class DefaultPaintIRS extends DefaultItemRenderingStrategy 
        implements PaintIRS {

    /** a generated serial id */
    private static final long serialVersionUID = 2211937902401233033L;

    /**
     * Creates a new rendering strategy for the submitted renderer using its 
     * per series properties.
     * 
     * @param renderer
     */
    public DefaultPaintIRS(AbstractRenderer renderer) {
        super(renderer);
    }

    /**
     * @return the item paint the renderer defines for the series
     */
    public Paint getItemPaint(int row, int column) {
        return renderer.lookupSeriesPaint(row);
    }

    /**
     * @return the item fill paint the renderer defines for the series
     */
    public Paint getItemFillPaint(int row, int column) {
        return renderer.lookupSeriesFillPaint(row);
    }

    /**
     * @return the item outline paint the renderer defines for the series
     */
    public Paint getItemOutlinePaint(int row, int column) {
        return renderer.lookupSeriesOutlinePaint(row);
    }

}
