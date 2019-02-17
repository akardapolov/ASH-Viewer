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
 * DataItemEntity.java
 * -------------------
 * (C) Copyright 2013, by Object Refinery Limited and Contributors.
 *
 * Original Author:  ;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * Changes:
 * --------
 * 17-Sep-2017 : Version 1 from MZ (DG);
 */


package org.jfree.chart.entity;

import java.awt.Shape;

import org.jfree.data.extension.DatasetCursor;
import org.jfree.data.general.Dataset;

/**
 * Super class of all entities, that encapsulate the shapes and meta 
 * information of rendered data items.
 *  
 * @author zinsmaie
 */
public abstract class DataItemEntity extends ChartEntity {

    private static final long serialVersionUID = -3785048574533713069L;

    /**
     * Creates a new chart entity.
     *
     * @param area  the area (<code>null</code> not permitted).
     */
    public DataItemEntity(Shape area) {
        super(area);
    }

    /**
     * Creates a new chart entity.
     *
     * @param area  the area (<code>null</code> not permitted).
     * @param toolTipText  the tool tip text (<code>null</code> permitted).
     */
    public DataItemEntity(Shape area, String toolTipText) {
       super(area, toolTipText, null);
    }

    /**
     * Creates a new entity.
     *
     * @param area  the area (<code>null</code> not permitted).
     * @param toolTipText  the tool tip text (<code>null</code> permitted).
     * @param urlText  the URL text for HTML image maps (<code>null</code>
     *                 permitted).
     */
    public DataItemEntity(Shape area, String toolTipText, String urlText) {
        super(area, toolTipText, urlText);
    }

    /**
     * Returns the dataset this entity refers to.  This can be used to
     * differentiate between items in a chart that displays more than one
     * dataset.<br>
     * Uses the general dataset interface to as return value
     *
     * @return The dataset (never <code>null</code>).
     *
     */
    public abstract Dataset getGeneralDataset();
    
    /**
     * @return a new instance of a {@link DatasetCursor} which points to the 
     *     DataItem
     */
    public abstract DatasetCursor getItemCursor();
}
