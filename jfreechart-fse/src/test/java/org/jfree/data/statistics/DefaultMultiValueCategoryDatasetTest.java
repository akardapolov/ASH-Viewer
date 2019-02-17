/* ===========================================================
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
 * ------------------------------------------
 * DefaultMultiValueCategoryDatasetTests.java
 * ------------------------------------------
 * (C) Copyright 2007, 2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 28-Sep-2007 : Version 1 (DG);
 *
 */

package org.jfree.data.statistics;

import org.jfree.data.UnknownKeyException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link DefaultMultiValueCategoryDataset} class.
 */
public class DefaultMultiValueCategoryDatasetTest  {





    /**
     * Some checks for the getValue() method.
     */
    @Test
    public void testGetValue() {
        DefaultMultiValueCategoryDataset d
                = new DefaultMultiValueCategoryDataset();
        List<Number> values = new ArrayList<Number>();
        values.add(1);
        values.add(2);
        d.add(values, "R1", "C1");
        assertEquals(1.5, d.getValue("R1", "C1"));

        try {
            d.getValue("XX", "C1");
            fail("UnknownKeyException should have been thrown on an unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Row key (XX) not recognised.", e.getMessage());
        }

        try {
            d.getValue("R1", "XX");
            fail("UnknownKeyException should have been thrown on an unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Column key (XX) not recognised.", e.getMessage());
        }
    }

    /**
     * A simple check for the getValue(int, int) method.
     */
    @Test
    public void testGetValue2() {
        DefaultMultiValueCategoryDataset d
                = new DefaultMultiValueCategoryDataset();

        try {
            /* Number n =*/ d.getValue(0, 0);
            fail("IndexOutOfBoundsException should have been thrown on key out of range");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 0, Size: 0", e.getMessage());
        }
    }

    /**
     * Some tests for the getRowCount() method.
     */
    @Test
    public void testGetRowCount() {
        DefaultMultiValueCategoryDataset d
                = new DefaultMultiValueCategoryDataset();
        assertSame(d.getRowCount(), 0);
        List values = new ArrayList();
        d.add(values, "R1", "C1");
        assertSame(d.getRowCount(), 1);

        d.add(values, "R2", "C1");
        assertSame(d.getRowCount(), 2);

        d.add(values, "R2", "C1");
        assertSame(d.getRowCount(), 2);
    }

    /**
     * Some tests for the getColumnCount() method.
     */
    @Test
    public void testGetColumnCount() {
        DefaultMultiValueCategoryDataset d
                = new DefaultMultiValueCategoryDataset();
        assertSame(d.getColumnCount(), 0);

        List values = new ArrayList();
        d.add(values, "R1", "C1");
        assertSame(d.getColumnCount(), 1);

        d.add(values, "R1", "C2");
        assertSame(d.getColumnCount(), 2);

        d.add(values, "R1", "C2");
        assertSame(d.getColumnCount(), 2);

    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        DefaultMultiValueCategoryDataset d1
                = new DefaultMultiValueCategoryDataset();
        DefaultMultiValueCategoryDataset d2
                = new DefaultMultiValueCategoryDataset();
        assertEquals(d1, d2);
        assertEquals(d2, d1);

        List<Number> values = new ArrayList<Number>();
        d1.add(values, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.add(values, "R1", "C1");
        assertEquals(d1, d2);

        values.add(99);
        d1.add(values, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.add(values, "R1", "C1");
        assertEquals(d1, d2);

        values.add(99);
        d1.add(values, "R1", "C2");
        assertFalse(d1.equals(d2));
        d2.add(values, "R1", "C2");
        assertEquals(d1, d2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        DefaultMultiValueCategoryDataset d1
                = new DefaultMultiValueCategoryDataset();



        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(d1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        DefaultMultiValueCategoryDataset d2 = (DefaultMultiValueCategoryDataset) in.readObject();
        in.close();

        assertEquals(d1, d2);

    }

    /**
     * Some checks for the add() method.
     */
    @Test
    public void testAddValue() {
        DefaultMultiValueCategoryDataset d1
                = new DefaultMultiValueCategoryDataset();


        try {
            d1.add(null, "R1", "C1");
            fail("IllegalArgumentException should have been thrown on a null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'values' argument.", e.getMessage());
        }

        List values = new ArrayList();
        d1.add(values, "R2", "C1");
        assertEquals(values, d1.getValues("R2", "C1"));

        try {
            d1.add(values, null, "C2");
            fail("IllegalArgumentException should have been thrown on a null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'rowKey' argument.", e.getMessage());
        }
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        DefaultMultiValueCategoryDataset d1
                = new DefaultMultiValueCategoryDataset();
        DefaultMultiValueCategoryDataset d2 = (DefaultMultiValueCategoryDataset) d1.clone();
        assertNotSame(d1, d2);
        assertSame(d1.getClass(), d2.getClass());
        assertEquals(d1, d2);

        // try a dataset with some content...
        List<Number> values = new ArrayList<Number>();
        values.add(99);
        d1.add(values, "R1", "C1");
        d2 = (DefaultMultiValueCategoryDataset) d1.clone();

        assertNotSame(d1, d2);
        assertSame(d1.getClass(), d2.getClass());
        assertEquals(d1, d2);

        // check that the clone doesn't share the same underlying arrays.
        List<Number> values2 = new ArrayList<Number>();
        values2.add(111);
        d1.add(values2, "R2", "C2");
        assertFalse(d1.equals(d2));
        d2.add(values2, "R2", "C2");
        assertEquals(d1, d2);
    }

}
