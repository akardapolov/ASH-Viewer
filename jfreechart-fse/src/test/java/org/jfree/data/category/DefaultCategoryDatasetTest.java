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
 * DefaultCategoryDatasetTests.java
 * --------------------------------
 * (C) Copyright 2004-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 23-Mar-2004 : Version 1 (DG);
 * 08-Mar-2007 : Added testCloning() (DG);
 * 21-Nov-2007 : Added testBug1835955() method (DG);
 * 09-May-2008 : Added testPublicCloneable() (DG);
 *
 */

package org.jfree.data.category;

import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.UnknownKeyException;
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
 * Tests for the {@link DefaultCategoryDataset} class.
 */
public class DefaultCategoryDatasetTest  {





    /**
     * Some checks for the getValue() method.
     */
    @Test
    public void testGetValue() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        assertEquals(1.0, d.getValue("R1", "C1"));

        try {
            d.getValue("XX", "C1");
            fail("UnknownKeyException should have been thrown on unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Unrecognised rowKey: XX", e.getMessage());
        }

        try {
            d.getValue("R1", "XX");
            fail("UnknownKeyException should have been thrown on unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Unrecognised columnKey: XX", e.getMessage());
        }
    }

    /**
     * A simple check for the getValue(int, int) method.
     */
    @Test
    public void testGetValue2() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();

        try {
            /* Number n =*/ d.getValue(0, 0);
            fail("IndexOutOfBoundsException should have been thrown on getting key from empty set");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 0, Size: 0", e.getMessage());
        }
    }

    /**
     * Some checks for the incrementValue() method.
     */
    @Test
    public void testIncrementValue() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        d.incrementValue(2.0, "R1", "C1");
        assertEquals(3.0, d.getValue("R1", "C1"));

        // increment a null value
        d.addValue(null, "R2", "C1");
        d.incrementValue(2.0, "R2", "C1");
        assertEquals(2.0, d.getValue("R2", "C1"));

        // increment an unknown row
        try {
            d.incrementValue(1.0, "XX", "C1");
            fail("UnknownKeyException should have been thrown on unknown row");
        }
        catch (UnknownKeyException e) {
            assertEquals("Unrecognised rowKey: XX", e.getMessage());
        }

        // increment an unknown column
        try {
            d.incrementValue(1.0, "R1", "XX");
            fail("UnknownKeyException should have been thrown on unknown row");
        }
        catch (UnknownKeyException e) {
            assertEquals("Unrecognised columnKey: XX", e.getMessage());
        }
    }

    /**
     * Some tests for the getRowCount() method.
     */
    @Test
    public void testGetRowCount() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        assertSame(d.getRowCount(), 0);

        d.addValue(1.0, "R1", "C1");
        assertSame(d.getRowCount(), 1);

        d.addValue(1.0, "R2", "C1");
        assertSame(d.getRowCount(), 2);

        d.addValue(2.0, "R2", "C1");
        assertSame(d.getRowCount(), 2);

        // a row of all null values is still counted...
        d.setValue(null, "R2", "C1");
        assertSame(d.getRowCount(), 2);
    }

    /**
     * Some tests for the getColumnCount() method.
     */
    @Test
    public void testGetColumnCount() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        assertSame(d.getColumnCount(), 0);

        d.addValue(1.0, "R1", "C1");
        assertSame(d.getColumnCount(), 1);

        d.addValue(1.0, "R1", "C2");
        assertSame(d.getColumnCount(), 2);

        d.addValue(2.0, "R1", "C2");
        assertSame(d.getColumnCount(), 2);

        // a column of all null values is still counted...
        d.setValue(null, "R1", "C2");
        assertSame(d.getColumnCount(), 2);
    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        DefaultCategoryDataset d1 = new DefaultCategoryDataset();
        d1.setValue(23.4, "R1", "C1");
        DefaultCategoryDataset d2 = new DefaultCategoryDataset();
        d2.setValue(23.4, "R1", "C1");
        assertEquals(d1, d2);
        assertEquals(d2, d1);

        d1.setValue(36.5, "R1", "C2");
        assertFalse(d1.equals(d2));
        d2.setValue(36.5, "R1", "C2");
        assertEquals(d1, d2);

        d1.setValue(null, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.setValue(null, "R1", "C1");
        assertEquals(d1, d2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        DefaultCategoryDataset d1 = new DefaultCategoryDataset();
        d1.setValue(23.4, "R1", "C1");

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
        DefaultCategoryDataset d2 = (DefaultCategoryDataset) in.readObject();
            in.close();

        assertEquals(d1, d2);

    }

    /**
     * Some checks for the addValue() method.
     */
    @Test
    public void testAddValue() {
        DefaultCategoryDataset d1 = new DefaultCategoryDataset();
        d1.addValue(null, "R1", "C1");
        assertNull(d1.getValue("R1", "C1"));
        d1.addValue(new Double(1.0), "R2", "C1");
        assertEquals(1.0, d1.getValue("R2", "C1"));


        try {
            d1.addValue(new Double(1.1), null, "C2");
            fail("UnknownKeyException should have been thrown on unknown row");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'key' argument.", e.getMessage());
        }
    }

    /**
     * Some basic checks for the removeValue() method.
     */
    @Test
    public void testRemoveValue() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.removeValue("R1", "C1");
        d.addValue(new Double(1.0), "R1", "C1");
        d.removeValue("R1", "C1");
        assertEquals(0, d.getRowCount());
        assertEquals(0, d.getColumnCount());

        d.addValue(new Double(1.0), "R1", "C1");
        d.addValue(new Double(2.0), "R2", "C1");
        d.removeValue("R1", "C1");
        assertEquals(2.0, d.getValue(0, 0));


        try {
            d.removeValue(null, "C1");
            fail("IllegalArgumentException should have been thrown on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'key' argument.", e.getMessage());
        }

        try {
            d.removeValue("R1", null);
            fail("IllegalArgumentException should have been thrown on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'key' argument.", e.getMessage());
        }
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        DefaultCategoryDataset d1 = new DefaultCategoryDataset();
        DefaultCategoryDataset d2 = (DefaultCategoryDataset) d1.clone();
        assertNotSame(d1, d2);
        assertSame(d1.getClass(), d2.getClass());
        assertEquals(d1, d2);

        // try a dataset with some content...
        d1.addValue(1.0, "R1", "C1");
        d1.addValue(2.0, "R1", "C2");

        d2 = (DefaultCategoryDataset) d1.clone();

        assertNotSame(d1, d2);
        assertSame(d1.getClass(), d2.getClass());
        assertEquals(d1, d2);

        // check that the clone doesn't share the same underlying arrays.
        d1.addValue(3.0, "R1", "C1");
        assertFalse(d1.equals(d2));
        d2.addValue(3.0, "R1", "C1");
        assertEquals(d1, d2);
    }

    /**
     * Check that this class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        assertTrue(d instanceof PublicCloneable);
    }

    private static final double EPSILON = 0.0000000001;

    /**
     * A test for bug 1835955.
     */
    @Test
    public void testBug1835955() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        d.addValue(2.0, "R2", "C2");
        d.removeColumn("C2");
        d.addValue(3.0, "R2", "C2");
        assertEquals(3.0, d.getValue("R2", "C2").doubleValue(), EPSILON);
    }

    /**
     * Some checks for the removeColumn(Comparable) method.
     */
    @Test
    public void testRemoveColumn() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        d.addValue(2.0, "R2", "C2");
        assertEquals(2, d.getColumnCount());
        d.removeColumn("C2");
        assertEquals(1, d.getColumnCount());

        try {
            d.removeColumn("XXX");
            fail("UnknownKeyException should have been thrown on unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Unknown key: XXX", e.getMessage());
        }

        try {
            d.removeColumn(null);
            fail("IllegalArgumentException should have been thrown on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'columnKey' argument.", e.getMessage());
        }
    }

    /**
     * Some checks for the removeRow(Comparable) method.
     */
    @Test
    public void testRemoveRow() {
        DefaultCategoryDataset d = new DefaultCategoryDataset();
        d.addValue(1.0, "R1", "C1");
        d.addValue(2.0, "R2", "C2");
        assertEquals(2, d.getRowCount());
        d.removeRow("R2");
        assertEquals(1, d.getRowCount());

        try {
            d.removeRow("XXX");
            fail("UnknownKeyException should have been thrown on unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Unknown key: XXX", e.getMessage());
        }

        try {
            d.removeRow(null);
            fail("IllegalArgumentException should have been thrown on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'rowKey' argument.", e.getMessage());
        }
    }

}
