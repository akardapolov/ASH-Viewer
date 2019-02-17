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
 * ComparableObjectSeriesTests.java
 * --------------------------------
 * (C) Copyright 2006-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 20-Oct-2006 : Version 1 (DG);
 * 31-Oct-2007 : New hashCode() test (DG);
 *
 */

package org.jfree.data;

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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link ComparableObjectSeries} class.
 */
public class ComparableObjectSeriesTest  {

    static class MyComparableObjectSeries extends ComparableObjectSeries {

        /**
         * Creates a new instance.
         *
         * @param key  the series key.
         */
        public MyComparableObjectSeries(Comparable key) {
            super(key);
        }
        /**
         * Creates a new instance.
         *
         * @param key  the series key.
         * @param autoSort  automatically sort by x-value?
         * @param allowDuplicateXValues  allow duplicate values?
         */
        public MyComparableObjectSeries(Comparable key, boolean autoSort,
                boolean allowDuplicateXValues) {
            super(key, autoSort, allowDuplicateXValues);
        }
        @Override
        public void add(Comparable x, Object y) {
            super.add(x, y);
        }

        @Override
        public ComparableObjectItem remove(Comparable x) {
            return super.remove(x);
        }
    }





    /**
     * Some checks for the constructor.
     */
    @Test
    public void testConstructor1() {
        ComparableObjectSeries s1 = new ComparableObjectSeries("s1");
        assertEquals("s1", s1.getKey());
        assertNull(s1.getDescription());
        assertTrue(s1.getAllowDuplicateXValues());
        assertTrue(s1.getAutoSort());
        assertEquals(0, s1.getItemCount());
        assertEquals(Integer.MAX_VALUE, s1.getMaximumItemCount());

        // try null key
        try {
            /*s1 = */new ComparableObjectSeries(null);
            fail("Should have thrown an IllegalArgumentException on null parameter");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'key' argument.", e.getMessage());
        }
    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        MyComparableObjectSeries s1 = new MyComparableObjectSeries("A");
        MyComparableObjectSeries s2 = new MyComparableObjectSeries("A");
        assertEquals(s1, s2);
        assertEquals(s2, s1);

        // key
        s1 = new MyComparableObjectSeries("B");
        assertFalse(s1.equals(s2));
        s2 = new MyComparableObjectSeries("B");
        assertEquals(s1, s2);

        // autoSort
        s1 = new MyComparableObjectSeries("B", false, true);
        assertFalse(s1.equals(s2));
        s2 = new MyComparableObjectSeries("B", false, true);
        assertEquals(s1, s2);

        // allowDuplicateXValues
        s1 = new MyComparableObjectSeries("B", false, false);
        assertFalse(s1.equals(s2));
        s2 = new MyComparableObjectSeries("B", false, false);
        assertEquals(s1, s2);

        // add a value
        s1.add(new Integer(1), "ABC");
        assertFalse(s1.equals(s2));
        s2.add(new Integer(1), "ABC");
        assertEquals(s1, s2);

        // add another value
        s1.add(new Integer(0), "DEF");
        assertFalse(s1.equals(s2));
        s2.add(new Integer(0), "DEF");
        assertEquals(s1, s2);

        // remove an item
        s1.remove(new Integer(1));
        assertFalse(s1.equals(s2));
        s2.remove(new Integer(1));
        assertEquals(s1, s2);
    }

    /**
     * Some checks for the clone() method.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        MyComparableObjectSeries s1 = new MyComparableObjectSeries("A");
        s1.add(new Integer(1), "ABC");
        MyComparableObjectSeries s2 = (MyComparableObjectSeries) s1.clone();
        assertNotSame(s1, s2);
        assertSame(s1.getClass(), s2.getClass());
        assertEquals(s1, s2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        MyComparableObjectSeries s1 = new MyComparableObjectSeries("A");
        s1.add(new Integer(1), "ABC");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(s1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
        MyComparableObjectSeries s2 = (MyComparableObjectSeries) in.readObject();
            in.close();

        assertEquals(s1, s2);
    }

    /**
     * Some simple checks for the hashCode() method.
     */
    @Test
    public void testHashCode() {
        MyComparableObjectSeries s1 = new MyComparableObjectSeries("Test");
        MyComparableObjectSeries s2 = new MyComparableObjectSeries("Test");
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.add("A", "1");
        s2.add("A", "1");
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.add("B", null);
        s2.add("B", null);
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.add("C", "3");
        s2.add("C", "3");
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());

        s1.add("D", "4");
        s2.add("D", "4");
        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

}
