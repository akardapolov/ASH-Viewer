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
 * ---------------
 * TextAnchor.java
 * ---------------
 * (C) Copyright 2003-2012, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes:
 * --------
 * 10-Jun-2003 : Version 1 (DG);
 * 11-Jan-2005 : Removed deprecated code (DG);
 *
 */

package org.jfree.chart.ui;

/**
 * Used to indicate the position of an anchor point for a text string.  This is
 * frequently used to align a string to a fixed point in some coordinate space.
 */
public enum TextAnchor {

    /** Top/left. */
    TOP_LEFT("TextAnchor.TOP_LEFT"),

    /** Top/center. */
    TOP_CENTER("TextAnchor.TOP_CENTER"),

    /** Top/right. */
    TOP_RIGHT("TextAnchor.TOP_RIGHT"),

    /** Half-ascent/left. */
    HALF_ASCENT_LEFT("TextAnchor.HALF_ASCENT_LEFT"),

    /** Half-ascent/center. */
    HALF_ASCENT_CENTER("TextAnchor.HALF_ASCENT_CENTER"),

    /** Half-ascent/right. */
    HALF_ASCENT_RIGHT("TextAnchor.HALF_ASCENT_RIGHT"),

    /** Middle/left. */
    CENTER_LEFT("TextAnchor.CENTER_LEFT"),

    /** Middle/center. */
    CENTER("TextAnchor.CENTER"),

    /** Middle/right. */
    CENTER_RIGHT("TextAnchor.CENTER_RIGHT"),

    /** Baseline/left. */
    BASELINE_LEFT("TextAnchor.BASELINE_LEFT"),

    /** Baseline/center. */
    BASELINE_CENTER("TextAnchor.BASELINE_CENTER"),

    /** Baseline/right. */
    BASELINE_RIGHT("TextAnchor.BASELINE_RIGHT"),

    /** Bottom/left. */
    BOTTOM_LEFT("TextAnchor.BOTTOM_LEFT"),

    /** Bottom/center. */
    BOTTOM_CENTER("TextAnchor.BOTTOM_CENTER"),

    /** Bottom/right. */
    BOTTOM_RIGHT("TextAnchor.BOTTOM_RIGHT");

    /** The name. */
    private String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private TextAnchor(final String name) {
        this.name = name;
    }

    /**
     * Returns <code>true</code> if this anchor is at the left side of the
     * text bounds, and <code>false</code> otherwise.
     * 
     * @return A boolean. 
     */
    public boolean isHorizontalLeft() {
        return this == TOP_LEFT || this == CENTER_LEFT 
                || this == HALF_ASCENT_LEFT || this == BASELINE_LEFT 
                || this == BOTTOM_LEFT;    
    }
    
    /**
     * Returns <code>true</code> if this anchor is horizontally at the center 
     * of the text bounds, and <code>false</code> otherwise.
     * 
     * @return A boolean. 
     */
    public boolean isHorizontalCenter() {
        return this == TOP_CENTER || this == CENTER 
                || this == HALF_ASCENT_CENTER || this == BASELINE_CENTER 
                || this == BOTTOM_CENTER;
    }
    
    /**
     * Returns <code>true</code> if this anchor is at the right side of the
     * text bounds, and <code>false</code> otherwise.
     * 
     * @return A boolean. 
     */
    public boolean isHorizontalRight() {
        return this == TOP_RIGHT || this == CENTER_RIGHT 
                || this == HALF_ASCENT_RIGHT || this == BASELINE_RIGHT 
                || this == BOTTOM_RIGHT;    
    }
    
    /**
     * Returns <code>true</code> if this anchor is at the top of the
     * text bounds, and <code>false</code> otherwise.
     * 
     * @return A boolean. 
     */
    public boolean isTop() {
        return this == TOP_LEFT || this == TOP_CENTER || this == TOP_RIGHT;
    }
    
    /**
     * Returns <code>true</code> if this anchor is at the half-ascent level of 
     * the text bounds, and <code>false</code> otherwise.
     * 
     * @return A boolean. 
     */
    public boolean isHalfAscent() {
        return this == HALF_ASCENT_LEFT || this == HALF_ASCENT_CENTER 
                || this == HALF_ASCENT_RIGHT;
    }
    
    /**
     * Returns <code>true</code> if this anchor is at the half-height level of 
     * the text bounds, and <code>false</code> otherwise.
     * 
     * @return A boolean. 
     */
    public boolean isHalfHeight() {
        return this == CENTER_LEFT || this == CENTER || this == CENTER_RIGHT;
    }
    
    /**
     * Returns <code>true</code> if this anchor is at the baseline level of 
     * the text bounds, and <code>false</code> otherwise.
     * 
     * @return A boolean. 
     */
    public boolean isBaseline() {
        return this == BASELINE_LEFT || this == BASELINE_CENTER 
                || this == BASELINE_RIGHT;
    }
    
    /**
     * Returns <code>true</code> if this anchor is at the bottom of 
     * the text bounds, and <code>false</code> otherwise.
     * 
     * @return A boolean. 
     */
    public boolean isBottom() {
        return this == BOTTOM_LEFT || this == BOTTOM_CENTER 
                || this == BOTTOM_RIGHT;
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

}
