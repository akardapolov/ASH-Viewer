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
 * --------------------------
 * LookupPaintScaleTests.java
 * --------------------------
 * (C) Copyright 2006-2013, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 05-Jul-2006 : Version 1 (DG);
 * 31-Jan-2007 : Additional serialization tests (DG);
 * 07-Mar-2007 : Added new tests (DG);
 * 09-Mar-2007 : Check independence in testCloning() (DG);
 *
 */

package org.jfree.chart.renderer;

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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link LookupPaintScale} class.
 */
public class LookupPaintScaleTest  {

    /**
     * A test for the equals() method.
     */
    @Test
    public void testEquals() {
        LookupPaintScale g1 = new LookupPaintScale();
        LookupPaintScale g2 = new LookupPaintScale();
        assertEquals(g1, g2);
        assertEquals(g2, g1);

        g1 = new LookupPaintScale(1.0, 2.0, Color.RED);
        assertFalse(g1.equals(g2));
        g2 = new LookupPaintScale(1.0, 2.0, Color.RED);
        assertEquals(g1, g2);

        g1.add(1.5, new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f,
                Color.BLUE));
        assertFalse(g1.equals(g2));
        g2.add(1.5, new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f,
                Color.BLUE));
        assertEquals(g1, g2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        LookupPaintScale g1 = new LookupPaintScale();
        LookupPaintScale g2 = (LookupPaintScale) g1.clone();
        assertNotSame(g1, g2);
        assertSame(g1.getClass(), g2.getClass());
        assertEquals(g1, g2);

        // check independence
        g1.add(0.5, Color.RED);
        assertFalse(g1.equals(g2));
        g2.add(0.5, Color.RED);
        assertEquals(g1, g2);

        // try with gradient paint
        g1 = new LookupPaintScale(1.0, 2.0, new GradientPaint(1.0f, 2.0f,
                Color.RED, 3.0f, 4.0f, Color.green));
        g1.add(1.5, new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f,
                Color.BLUE));
        g2 = (LookupPaintScale) g1.clone();

        assertNotSame(g1, g2);
        assertSame(g1.getClass(), g2.getClass());
        assertEquals(g1, g2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        LookupPaintScale g1 = new LookupPaintScale();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(g1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
        LookupPaintScale g2 = (LookupPaintScale) in.readObject();
        in.close();

        assertEquals(g1, g2);

        g1 = new LookupPaintScale(1.0, 2.0, new GradientPaint(1.0f, 2.0f,
                Color.RED, 3.0f, 4.0f, Color.yellow));
        g1.add(1.5, new GradientPaint(1.1f, 2.2f, Color.RED, 3.3f, 4.4f,
                Color.BLUE));
        buffer = new ByteArrayOutputStream();
        out = new ObjectOutputStream(buffer);
        out.writeObject(g1);
        out.close();

        in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
        g2 = (LookupPaintScale) in.readObject();
        in.close();

        assertEquals(g1, g2);
    }

    private static final double EPSILON = 0.0000000001;

    /**
     * Some checks for the default constructor.
     */
    @Test
    public void testConstructor1() {
        LookupPaintScale s = new LookupPaintScale();
        assertEquals(0.0, s.getLowerBound(), EPSILON);
        assertEquals(1.0, s.getUpperBound(), EPSILON);
    }

    /**
     * Some checks for the other constructor.
     */
    @Test
    public void testConstructor2() {
        LookupPaintScale s = new LookupPaintScale(1.0, 2.0, Color.RED);
        assertEquals(1.0, s.getLowerBound(), EPSILON);
        assertEquals(2.0, s.getUpperBound(), EPSILON);
        assertEquals(Color.RED, s.getDefaultPaint());
    }

    /**
     * Some general checks for the lookup table.
     */
    @Test
    public void testGeneral() {

        LookupPaintScale s = new LookupPaintScale(0.0, 100.0, Color.BLACK);
        assertEquals(Color.BLACK, s.getPaint(-1.0));
        assertEquals(Color.BLACK, s.getPaint(0.0));
        assertEquals(Color.BLACK, s.getPaint(50.0));
        assertEquals(Color.BLACK, s.getPaint(100.0));
        assertEquals(Color.BLACK, s.getPaint(101.0));

        s.add(50.0, Color.BLUE);
        assertEquals(Color.BLACK, s.getPaint(-1.0));
        assertEquals(Color.BLACK, s.getPaint(0.0));
        assertEquals(Color.BLUE, s.getPaint(50.0));
        assertEquals(Color.BLUE, s.getPaint(100.0));
        assertEquals(Color.BLACK, s.getPaint(101.0));

        s.add(50.0, Color.RED);
        assertEquals(Color.BLACK, s.getPaint(-1.0));
        assertEquals(Color.BLACK, s.getPaint(0.0));
        assertEquals(Color.RED, s.getPaint(50.0));
        assertEquals(Color.RED, s.getPaint(100.0));
        assertEquals(Color.BLACK, s.getPaint(101.0));

        s.add(25.0, Color.green);
        assertEquals(Color.BLACK, s.getPaint(-1.0));
        assertEquals(Color.BLACK, s.getPaint(0.0));
        assertEquals(Color.green, s.getPaint(25.0));
        assertEquals(Color.RED, s.getPaint(50.0));
        assertEquals(Color.RED, s.getPaint(100.0));
        assertEquals(Color.BLACK, s.getPaint(101.0));

        s.add(75.0, Color.yellow);
        assertEquals(Color.BLACK, s.getPaint(-1.0));
        assertEquals(Color.BLACK, s.getPaint(0.0));
        assertEquals(Color.green, s.getPaint(25.0));
        assertEquals(Color.RED, s.getPaint(50.0));
        assertEquals(Color.yellow, s.getPaint(75.0));
        assertEquals(Color.yellow, s.getPaint(100.0));
        assertEquals(Color.BLACK, s.getPaint(101.0));
    }

    /**
     * Some checks for special values in the getPaint() method.
     */
    @Test
    public void testSpecialValues() {
        LookupPaintScale s = new LookupPaintScale(0.0, 100.0, Color.black);
        s.add(25.0, Color.green);
        s.add(50.0, Color.blue);
        s.add(75.0, Color.yellow);
        assertEquals(Color.black, s.getPaint(Double.NaN));
        assertEquals(Color.black, s.getPaint(Double.POSITIVE_INFINITY));
        assertEquals(Color.black, s.getPaint(Double.NEGATIVE_INFINITY));

    }

}
