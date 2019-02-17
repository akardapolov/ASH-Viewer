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
 * ---------------------
 * BlockBorderTests.java
 * ---------------------
 * (C) Copyright 2005-2012, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 04-Feb-2005 : Version 1 (DG);
 * 23-Feb-2005 : Extended equals() test (DG);
 * 17-Jun-2012 : Removed JCommon dependencies (DG);
 *
 */

package org.jfree.chart.block;

import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.util.UnitType;
import org.junit.Test;

import java.awt.Color;
import java.awt.GradientPaint;
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
 * Tests for the {@link BlockBorder} class.
 */
public class BlockBorderTest  {





    /**
     * Confirm that the equals() method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        BlockBorder b1 = new BlockBorder(new RectangleInsets(1.0, 2.0, 3.0,
                4.0), Color.RED);
        BlockBorder b2 = new BlockBorder(new RectangleInsets(1.0, 2.0, 3.0,
                4.0), Color.RED);
        assertEquals(b1, b2);
        assertEquals(b2, b2);

        // insets
        b1 = new BlockBorder(new RectangleInsets(UnitType.RELATIVE, 1.0, 2.0,
                3.0, 4.0), Color.RED);
        assertFalse(b1.equals(b2));
        b2 = new BlockBorder(new RectangleInsets(UnitType.RELATIVE, 1.0, 2.0,
                3.0, 4.0), Color.RED);
        assertEquals(b1, b2);

        // paint
        b1 = new BlockBorder(new RectangleInsets(1.0, 2.0, 3.0, 4.0),
                Color.BLUE);
        assertFalse(b1.equals(b2));
        b2 = new BlockBorder(new RectangleInsets(1.0, 2.0, 3.0, 4.0),
                Color.BLUE);
        assertEquals(b1, b2);
    }

    /**
     * Immutable - cloning not necessary.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        BlockBorder b1 = new BlockBorder();
        assertFalse(b1 instanceof Cloneable);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        BlockBorder b1 = new BlockBorder(new RectangleInsets(1.0, 2.0, 3.0,
                4.0), new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f,
                Color.yellow));

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(b1);
        out.close();

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                buffer.toByteArray()));
        BlockBorder b2 = (BlockBorder) in.readObject();
        in.close();
        assertEquals(b1, b2);
    }

}
