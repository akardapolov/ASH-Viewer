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
 * -------------------------------
 * CategoryLabelPositionTests.java
 * -------------------------------
 * (C) Copyright 2004-2012, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 17-Feb-2004 : Version 1 (DG);
 * 07-Jan-2005 : Improved testEquals() code and added hashCode() test (DG);
 * 17-Jun-2012 : Removed JCommon dependencies (DG);
 *
 */

package org.jfree.chart.axis;

import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link CategoryLabelPosition} class.
 */
public class CategoryLabelPositionTest  {





    /**
     * Check that the equals() method can distinguish all fields.
     */
    @Test
    public void testEquals() {
        CategoryLabelPosition p1 = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        CategoryLabelPosition p2 = new CategoryLabelPosition(
                RectangleAnchor.BOTTOM_LEFT, TextBlockAnchor.CENTER_RIGHT,
                TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);
        assertEquals(p2, p1);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT,
                Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER_RIGHT, TextAnchor.BASELINE_LEFT,
                Math.PI / 4.0, CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.BASELINE_LEFT, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 4.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.RANGE, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.44f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.44f);
        assertEquals(p1, p2);

        p1 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER,  Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.55f);
        assertFalse(p1.equals(p2));
        p2 = new CategoryLabelPosition(RectangleAnchor.TOP,
                TextBlockAnchor.CENTER, TextAnchor.CENTER, Math.PI / 6.0,
                CategoryLabelWidthType.CATEGORY, 0.55f);
        assertEquals(p1, p2);
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashCode() {
        CategoryLabelPosition a1 = new CategoryLabelPosition();
        CategoryLabelPosition a2 = new CategoryLabelPosition();
        assertEquals(a1, a2);
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        CategoryLabelPosition p1 = new CategoryLabelPosition();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(p1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        CategoryLabelPosition p2 = (CategoryLabelPosition) in.readObject();
        in.close();

        assertEquals(p1, p2);
    }

}
