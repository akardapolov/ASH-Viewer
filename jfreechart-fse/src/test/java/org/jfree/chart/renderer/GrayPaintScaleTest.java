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
 * ------------------------
 * GrayPaintScaleTests.java
 * ------------------------
 * (C) Copyright 2006-2013, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 05-Jul-2006 : Version 1 (DG);
 * 26-Sep-2007 : Added testConstructor() and testGetPaint() (DG);
 * 29-Jan-2009 : Extended testEquals() for new alpha field (DG);
 *
 */

package org.jfree.chart.renderer;

import org.junit.Test;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;


/**
 * Tests for the {@link GrayPaintScale} class.
 */
public class GrayPaintScaleTest  {

    private static final double EPSILON = 0.000000001;

    /**
     * Simple check for the default constructor.
     */
    @Test
    public void testConstructor() {
        GrayPaintScale gps = new GrayPaintScale();
        assertEquals(0.0, gps.getLowerBound(), EPSILON);
        assertEquals(1.0, gps.getUpperBound(), EPSILON);
        assertEquals(255, gps.getAlpha());
    }

    /**
     * Some checks for the getPaint() method.
     */
    @Test
    public void testGetPaint() {
        GrayPaintScale gps = new GrayPaintScale();
        Color c = (Color) gps.getPaint(0.0);
        assertEquals(c, Color.BLACK);
        c = (Color) gps.getPaint(1.0);
        assertEquals(c, Color.WHITE);

        // check lookup values that are outside the bounds - see bug report
        // 1767315
        c = (Color) gps.getPaint(-0.5);
        assertEquals(c, Color.BLACK);
        c = (Color) gps.getPaint(1.5);
        assertEquals(c, Color.WHITE);

        // add a check for Double.NAN
        c = (Color) gps.getPaint(Double.NaN);
        assertEquals(c, Color.BLACK);

        c = (Color) gps.getPaint(Double.POSITIVE_INFINITY);
        assertEquals(c, Color.WHITE);

        c = (Color) gps.getPaint(Double.NEGATIVE_INFINITY);
        assertEquals(c, Color.BLACK);
    }

    /**
     * A test for the equals() method.
     */
    @Test
    public void testEquals() {
        GrayPaintScale g1 = new GrayPaintScale();
        GrayPaintScale g2 = new GrayPaintScale();
        assertEquals(g1, g2);
        assertEquals(g2, g1);

        g1 = new GrayPaintScale(0.0, 1.0);
        g2 = new GrayPaintScale(0.0, 1.0);
        assertEquals(g1, g2);
        g1 = new GrayPaintScale(0.1, 1.0);
        assertFalse(g1.equals(g2));
        g2 = new GrayPaintScale(0.1, 1.0);
        assertEquals(g1, g2);

        g1 = new GrayPaintScale(0.1, 0.9);
        assertFalse(g1.equals(g2));
        g2 = new GrayPaintScale(0.1, 0.9);
        assertEquals(g1, g2);

        g1 = new GrayPaintScale(0.1, 0.9, 128);
        assertFalse(g1.equals(g2));
        g2 = new GrayPaintScale(0.1, 0.9, 128);
        assertEquals(g1, g2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        GrayPaintScale g1 = new GrayPaintScale();
        GrayPaintScale g2 = (GrayPaintScale) g1.clone();
        assertNotSame(g1, g2);
        assertSame(g1.getClass(), g2.getClass());
        assertEquals(g1, g2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        GrayPaintScale g1 = new GrayPaintScale();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(g1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
        GrayPaintScale g2 = (GrayPaintScale) in.readObject();
        in.close();

        assertEquals(g1, g2);
    }

}
