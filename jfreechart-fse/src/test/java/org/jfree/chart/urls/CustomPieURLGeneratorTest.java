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
 * CustomPieURLGeneratorTests.java
 * -------------------------------
 * (C) Copyright 2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 11-Apr-2008 : Version 1 (DG);
 * 23-Apr-2008 : Added testPublicCloneable (DG);
 *
 */

package org.jfree.chart.urls;

import org.jfree.chart.util.PublicCloneable;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link CustomPieURLGenerator} class.
 */
public class CustomPieURLGeneratorTest  {





    /**
     * Some checks for the equals() method.
     */
    @Test
    public void testEquals() {
        CustomPieURLGenerator g1 = new CustomPieURLGenerator();
        CustomPieURLGenerator g2 = new CustomPieURLGenerator();
        assertEquals(g1, g2);

        Map<Comparable, String> m1 = new HashMap<Comparable, String>();
        m1.put("A", "http://www.jfree.org/");
        g1.addURLs(m1);
        assertFalse(g1.equals(g2));
        g2.addURLs(m1);
        assertEquals(g1, g2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        CustomPieURLGenerator g1 = new CustomPieURLGenerator();
        Map<Comparable, String> m1 = new HashMap<Comparable, String>();
        m1.put("A", "http://www.jfree.org/");
        g1.addURLs(m1);
        CustomPieURLGenerator g2 = (CustomPieURLGenerator) g1.clone();
        assertNotSame(g1, g2);
        assertSame(g1.getClass(), g2.getClass());
        assertEquals(g1, g2);

        // check independence
        Map<Comparable, String> m2 = new HashMap<Comparable, String>();
        m2.put("B", "XYZ");
        g1.addURLs(m2);
        assertFalse(g1.equals(g2));
    }

    /**
     * Checks that the class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        CustomPieURLGenerator g1 = new CustomPieURLGenerator();
        assertTrue(g1 instanceof PublicCloneable);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        CustomPieURLGenerator g1 = new CustomPieURLGenerator();
        Map<Comparable, String> m1 = new HashMap<Comparable, String>();
        m1.put("A", "http://www.jfree.org/");
        g1.addURLs(m1);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(g1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        CustomPieURLGenerator g2 = (CustomPieURLGenerator) in.readObject();
        in.close();

        assertEquals(g1, g2);
    }

}
