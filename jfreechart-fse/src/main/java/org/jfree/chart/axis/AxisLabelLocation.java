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
 * ----------------------
 * AxisLabelLocation.java
 * ----------------------
 * (C) Copyright 2013, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes:
 * --------
 * 05-Aug-2013 : Version 1 (DG);
 *
 */

package org.jfree.chart.axis;

import java.awt.geom.Rectangle2D;
import org.jfree.chart.ui.TextAnchor;

/**
 * Used to indicate the location of an axis label on a 2D plot.
 */
public enum AxisLabelLocation {

    /** Axis label at the top. */
    HIGH_END("HIGH_END"),

    /** Axis label at the middle. */
    MIDDLE("MIDDLE"),

    /** Axis label at the bottom. */
    LOW_END("LOW_END");

    /** The name. */
    private String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private AxisLabelLocation(String name) {
        this.name = name;
    }

    /**
     * Returns a string representing the object.
     *
     * @return The string.
     */
    @Override
    public String toString() {
        return this.name;
    }
    
    public double labelLocationX(Rectangle2D dataArea) {
        if (this == LOW_END) {
            return dataArea.getMinX();
        }
        if (this == MIDDLE) {
            return dataArea.getCenterX();
        }
        if (this == HIGH_END) {
            return dataArea.getMaxX();
        }
        throw new IllegalStateException("Unrecognised AxisLabelLocation.");
    }

    public double labelLocationY(Rectangle2D dataArea) {
        if (this == LOW_END) {
            return dataArea.getMaxY();
        }
        if (this == MIDDLE) {
            return dataArea.getCenterY();
        }
        if (this == HIGH_END) {
            return dataArea.getMinY();
        }
        throw new IllegalStateException("Unrecognised AxisLabelLocation.");
    }

    public TextAnchor labelAnchorH() {
        if (this == HIGH_END) {
            return TextAnchor.CENTER_RIGHT;
        }
        if (this == MIDDLE) {
            return TextAnchor.CENTER;
        }
        if (this == LOW_END) {
            return TextAnchor.CENTER_LEFT;
        }
        throw new RuntimeException("Unexpected AxisLabelLocation: " + this);
    }
    
    public TextAnchor labelAnchorV() {
        if (this == HIGH_END) {
            return TextAnchor.CENTER_RIGHT;
        }
        if (this == MIDDLE) {
            return TextAnchor.CENTER;
        }
        if (this == LOW_END) {
            return TextAnchor.CENTER_LEFT;
        }
        throw new RuntimeException("Unexpected AxisLabelLocation: " + this);
    }

}
