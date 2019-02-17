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
 * --------------------------------
 * CategoryLineAnnotationTests.java
 * --------------------------------
 * (C) Copyright 2005-2009, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 29-Jul-2005 : Version 1 (DG);
 * 23-Apr-2008 : Added testPublicCloneable() (DG);
 *
 */

package org.jfree.chart.annotations;

import org.jfree.chart.util.PublicCloneable;
import org.junit.Test;

import java.awt.BasicStroke;
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
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link CategoryLineAnnotationTest} class.
 */
public class CategoryLineAnnotationTest  {





    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {

        BasicStroke s1 = new BasicStroke(1.0f);
        BasicStroke s2 = new BasicStroke(2.0f);
        CategoryLineAnnotation a1 = new CategoryLineAnnotation("Category 1",
                1.0, "Category 2", 2.0, Color.RED, s1);
        CategoryLineAnnotation a2 = new CategoryLineAnnotation("Category 1",
                1.0, "Category 2", 2.0, Color.RED, s1);
        assertEquals(a1, a2);
        assertEquals(a2, a1);

        // category 1
        a1.setCategory1("Category A");
        assertFalse(a1.equals(a2));
        a2.setCategory1("Category A");
        assertEquals(a1, a2);

        // value 1
        a1.setValue1(0.15);
        assertFalse(a1.equals(a2));
        a2.setValue1(0.15);
        assertEquals(a1, a2);

        // category 2
        a1.setCategory2("Category B");
        assertFalse(a1.equals(a2));
        a2.setCategory2("Category B");
        assertEquals(a1, a2);

        // value 2
        a1.setValue2(0.25);
        assertFalse(a1.equals(a2));
        a2.setValue2(0.25);
        assertEquals(a1, a2);

        // paint
        a1.setPaint(Color.yellow);
        assertFalse(a1.equals(a2));
        a2.setPaint(Color.yellow);
        assertEquals(a1, a2);

        // stroke
        a1.setStroke(s2);
        assertFalse(a1.equals(a2));
        a2.setStroke(s2);
        assertEquals(a1, a2);
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashcode() {
        CategoryLineAnnotation a1 = new CategoryLineAnnotation("Category 1", 
                1.0, "Category 2", 2.0, Color.RED, new BasicStroke(1.0f));
        CategoryLineAnnotation a2 = new CategoryLineAnnotation("Category 1", 
                1.0, "Category 2", 2.0, Color.RED, new BasicStroke(1.0f));
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
        CategoryLineAnnotation a1 = new CategoryLineAnnotation("Category 1", 
                1.0, "Category 2", 2.0, Color.RED, new BasicStroke(1.0f));
        CategoryLineAnnotation a2 = (CategoryLineAnnotation) a1.clone();

        assertNotSame(a1, a2);
        assertSame(a1.getClass(), a2.getClass());
        assertEquals(a1, a2);
    }

    /**
     * Checks that this class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        CategoryLineAnnotation a1 = new CategoryLineAnnotation(
                "Category 1", 1.0, "Category 2", 2.0, Color.RED,
                new BasicStroke(1.0f));
        assertTrue(a1 instanceof PublicCloneable);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        CategoryLineAnnotation a1 = new CategoryLineAnnotation("Category 1", 
                1.0, "Category 2", 2.0, Color.RED, new BasicStroke(1.0f));
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(a1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        CategoryLineAnnotation a2 = (CategoryLineAnnotation) in.readObject();
        in.close();

        assertEquals(a1, a2);
    }

}
