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
 * --------------------------
 * XYTextAnnotationTests.java
 * --------------------------
 * (C) Copyright 2003-2009, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 19-Aug-2003 : Version 1 (DG);
 * 07-Jan-2005 : Added hashCode() test (DG);
 * 26-Jan-2006 : Extended equals() test (DG);
 * 23-Apr-2008 : Added testPublicCloneable() (DG);
 * 12-Feb-2009 : Updated testEquals() (DG);
 *
 */

package org.jfree.chart.annotations;

import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.util.PublicCloneable;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link XYTextAnnotation} class.
 */
public class XYTextAnnotationTest  {





    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        XYTextAnnotation a1 = new XYTextAnnotation("Text", 10.0, 20.0);
        XYTextAnnotation a2 = new XYTextAnnotation("Text", 10.0, 20.0);
        assertEquals(a1, a2);

        // text
        a1 = new XYTextAnnotation("ABC", 10.0, 20.0);
        assertFalse(a1.equals(a2));
        a2 = new XYTextAnnotation("ABC", 10.0, 20.0);
        assertEquals(a1, a2);

        // x
        a1 = new XYTextAnnotation("ABC", 11.0, 20.0);
        assertFalse(a1.equals(a2));
        a2 = new XYTextAnnotation("ABC", 11.0, 20.0);
        assertEquals(a1, a2);

        // y
        a1 = new XYTextAnnotation("ABC", 11.0, 22.0);
        assertFalse(a1.equals(a2));
        a2 = new XYTextAnnotation("ABC", 11.0, 22.0);
        assertEquals(a1, a2);

        // font
        a1.setFont(new Font("Serif", Font.PLAIN, 23));
        assertFalse(a1.equals(a2));
        a2.setFont(new Font("Serif", Font.PLAIN, 23));
        assertEquals(a1, a2);

        // paint
        GradientPaint gp1 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f,
                4.0f, Color.yellow);
        GradientPaint gp2 = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f,
                4.0f, Color.yellow);
        a1.setPaint(gp1);
        assertFalse(a1.equals(a2));
        a2.setPaint(gp2);
        assertEquals(a1, a2);

        // rotation anchor
        a1.setRotationAnchor(TextAnchor.BASELINE_RIGHT);
        assertFalse(a1.equals(a2));
        a2.setRotationAnchor(TextAnchor.BASELINE_RIGHT);
        assertEquals(a1, a2);

        // rotation angle
        a1.setRotationAngle(12.3);
        assertFalse(a1.equals(a2));
        a2.setRotationAngle(12.3);
        assertEquals(a1, a2);

        // text anchor
        a1.setTextAnchor(TextAnchor.BASELINE_RIGHT);
        assertFalse(a1.equals(a2));
        a2.setTextAnchor(TextAnchor.BASELINE_RIGHT);
        assertEquals(a1, a2);

        a1.setBackgroundPaint(gp1);
        assertFalse(a1.equals(a2));
        a2.setBackgroundPaint(gp1);
        assertEquals(a1, a2);

        a1.setOutlinePaint(gp1);
        assertFalse(a1.equals(a2));
        a2.setOutlinePaint(gp1);
        assertEquals(a1, a2);

        a1.setOutlineStroke(new BasicStroke(1.2f));
        assertFalse(a1.equals(a2));
        a2.setOutlineStroke(new BasicStroke(1.2f));
        assertEquals(a1, a2);

        a1.setOutlineVisible(!a1.isOutlineVisible());
        assertFalse(a1.equals(a2));
        a2.setOutlineVisible(a1.isOutlineVisible());
        assertEquals(a1, a2);

    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashCode() {
        XYTextAnnotation a1 = new XYTextAnnotation("Text", 10.0, 20.0);
        XYTextAnnotation a2 = new XYTextAnnotation("Text", 10.0, 20.0);
        assertEquals(a1, a2);
        int h1 = a1.hashCode();
        int h2 = a2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        XYTextAnnotation a1 = new XYTextAnnotation("Text", 10.0, 20.0);
        XYTextAnnotation a2 = (XYTextAnnotation) a1.clone();
        assertNotSame(a1, a2);
        assertSame(a1.getClass(), a2.getClass());
        assertEquals(a1, a2);
    }

    /**
     * Checks that this class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        XYTextAnnotation a1 = new XYTextAnnotation("Text", 10.0, 20.0);
        assertTrue(a1 instanceof PublicCloneable);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        XYTextAnnotation a1 = new XYTextAnnotation("Text", 10.0, 20.0);
        a1.setOutlinePaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f,
                Color.BLUE));


        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(a1);
        out.close();

        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                buffer.toByteArray()));
        XYTextAnnotation a2 = (XYTextAnnotation) in.readObject();
        in.close();
        assertEquals(a1, a2);
    }

}
