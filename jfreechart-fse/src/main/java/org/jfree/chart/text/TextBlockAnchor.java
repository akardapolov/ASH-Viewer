/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2012, by Object Refinery Limited and Contributors.
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
 * TextBlockAnchor.java
 * --------------------
 * (C) Copyright 2003-2012, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: TextBlockAnchor.java,v 1.4 2005/10/18 13:17:16 mungady Exp $
 *
 * Changes:
 * --------
 * 06-Nov-2003 : Version 1 (DG);
 * 22-Mar-2004 : Added readResolve() method (DG);
 * 17-Jun-2012 : Moved from JCommon to JFreeChart (DG);
 *
 */

package org.jfree.chart.text;

/**
 * Used to indicate the position of an anchor point for a text block.
 */
public enum TextBlockAnchor {

    /** Top/left. */
    TOP_LEFT("TextBlockAnchor.TOP_LEFT"),

    /** Top/center. */
    TOP_CENTER("TextBlockAnchor.TOP_CENTER"),

    /** Top/right. */
    TOP_RIGHT("TextBlockAnchor.TOP_RIGHT"),

    /** Middle/left. */
    CENTER_LEFT("TextBlockAnchor.CENTER_LEFT"),

    /** Middle/center. */
    CENTER("TextBlockAnchor.CENTER"),

    /** Middle/right. */
    CENTER_RIGHT("TextBlockAnchor.CENTER_RIGHT"),

    /** Bottom/left. */
    BOTTOM_LEFT("TextBlockAnchor.BOTTOM_LEFT"),

    /** Bottom/center. */
    BOTTOM_CENTER("TextBlockAnchor.BOTTOM_CENTER"),

    /** Bottom/right. */
    BOTTOM_RIGHT("TextBlockAnchor.BOTTOM_RIGHT");

    /** The name. */
    private String name;

    /**
     * Private constructor.
     *
     * @param name  the name.
     */
    private TextBlockAnchor(final String name) {
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

}
