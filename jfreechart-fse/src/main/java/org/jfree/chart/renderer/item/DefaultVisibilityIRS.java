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
 * -------------------------
 * DefaultVisibilityIRS.java
 * -------------------------
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

import org.jfree.chart.renderer.AbstractRenderer;

/**
 * Implements a per series default item rendering strategy for the item visibility. 
 * {@link DefaultItemRenderingStrategy}
 * 
 * @author zinsmaie
 */
public class DefaultVisibilityIRS extends DefaultItemRenderingStrategy implements VisibilityIRS {

    /** a generated serial id */
    private static final long serialVersionUID = 559211600589929630L;

    /**
     * creates a new rendering strategy for the submitted renderer using its per series properties
     * @param renderer
     */
    public DefaultVisibilityIRS(AbstractRenderer renderer) {
        super(renderer);
    }

    /**
     * @return true if the renderer defines the series as visible 
     */
    public boolean getItemVisible(int row, int column) {
        return renderer.isSeriesVisible(row);
    }

}
