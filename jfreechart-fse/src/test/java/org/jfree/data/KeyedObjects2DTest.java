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
 * ------------------------
 * KeyedObjects2DTests.java
 * ------------------------
 * (C) Copyright 2004-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 01-Mar-2004 : Version 1 (DG);
 * 28-Sep-2007 : Added testEquals() and enhanced testClone() (DG);
 * 03-Oct-2007 : Added new tests (DG);
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
import static org.junit.Assert.fail;

/**
 * Tests for the {@link KeyedObjects2D} class.
 */
public class KeyedObjects2DTest  {





    /**
     * Some checks for the equals() method.
     */
    @Test
    public void testEquals() {
        KeyedObjects2D k1 = new KeyedObjects2D();
        KeyedObjects2D k2 = new KeyedObjects2D();
        assertEquals(k1, k2);
        assertEquals(k2, k1);

        k1.addObject(99, "R1", "C1");
        assertFalse(k1.equals(k2));
        k2.addObject(99, "R1", "C1");
        assertEquals(k1, k2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        KeyedObjects2D o1 = new KeyedObjects2D();
        o1.setObject(1, "V1", "C1");
        o1.setObject(null, "V2", "C1");
        o1.setObject(3, "V3", "C2");
        KeyedObjects2D o2 = (KeyedObjects2D) o1.clone();


        assertNotSame(o1, o2);
        assertSame(o1.getClass(), o2.getClass());
        assertEquals(o1, o2);

        // check independence
        o1.addObject("XX", "R1", "C1");
        assertFalse(o1.equals(o2));
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        KeyedObjects2D ko2D1 = new KeyedObjects2D();
        ko2D1.addObject(234.2, "Row1", "Col1");
        ko2D1.addObject(null, "Row1", "Col2");
        ko2D1.addObject(345.9, "Row2", "Col1");
        ko2D1.addObject(452.7, "Row2", "Col2");


        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(ko2D1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        KeyedObjects2D ko2D2 = (KeyedObjects2D) in.readObject();
        in.close();

        assertEquals(ko2D1, ko2D2);

    }

    /**
     * Some checks for the getValue(int, int) method.
     */
    @Test
    public void testGetValueByIndex() {
        KeyedObjects2D data = new KeyedObjects2D();
        data.addObject("Obj1", "R1", "C1");
        data.addObject("Obj2", "R2", "C2");
        assertEquals("Obj1", data.getObject(0, 0));
        assertEquals("Obj2", data.getObject(1, 1));
        assertNull(data.getObject(0, 1));
        assertNull(data.getObject(1, 0));

        // check invalid indices
        try {
            data.getObject(-1, 0);
            fail("Should have thrown IndexOutOfBoundsException on negative key");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("-1", e.getMessage());
        }

        try {
            data.getObject(0, -1);
            fail("Should have thrown IndexOutOfBoundsException on key out of bounds");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("-1", e.getMessage());
        }

        try {
            data.getObject(2, 0);
            fail("Should have thrown IndexOutOfBoundsException on key out of bounds");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 2, Size: 2", e.getMessage());
        }

        try {
            data.getObject(0, 2);
            fail("Should have thrown IndexOutOfBoundsException on key out of bounds");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 2, Size: 2", e.getMessage());
        }
    }

    /**
     * Some checks for the getValue(Comparable, Comparable) method.
     */
    @Test
    public void testGetValueByKey() {
        KeyedObjects2D data = new KeyedObjects2D();
        data.addObject("Obj1", "R1", "C1");
        data.addObject("Obj2", "R2", "C2");
        assertEquals("Obj1", data.getObject("R1", "C1"));
        assertEquals("Obj2", data.getObject("R2", "C2"));
        assertNull(data.getObject("R1", "C2"));
        assertNull(data.getObject("R2", "C1"));

        // check invalid indices
        try {
            data.getObject("XX", "C1");
            fail("Should have thrown UnknownKeyException unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Row key (XX) not recognised.", e.getMessage());
        }

        try {
            data.getObject("R1", "XX");
            fail("Should have thrown UnknownKeyException unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Column key (XX) not recognised.", e.getMessage());
        }

        try {
            data.getObject("XX", "C1");
            fail("Should have thrown UnknownKeyException unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Row key (XX) not recognised.", e.getMessage());
        }

        try {
            data.getObject("R1", "XX");
            fail("Should have thrown UnknownKeyException unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Column key (XX) not recognised.", e.getMessage());
        }
    }

    /**
     * Some checks for the setObject(Object, Comparable, Comparable) method.
     */
    @Test
    public void testSetObject() {
        KeyedObjects2D data = new KeyedObjects2D();
        data.setObject("Obj1", "R1", "C1");
        data.setObject("Obj2", "R2", "C2");
        assertEquals("Obj1", data.getObject("R1", "C1"));
        assertEquals("Obj2", data.getObject("R2", "C2"));
        assertNull(data.getObject("R1", "C2"));
        assertNull(data.getObject("R2", "C1"));

        // confirm overwriting an existing value
        data.setObject("ABC", "R2", "C2");
        assertEquals("ABC", data.getObject("R2", "C2"));

        // try null keys
        try {
            data.setObject("X", null, "C1");
            fail("Should have thrown IllegalArgumentException on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'rowKey' argument.", e.getMessage());
        }

        try {
            data.setObject("X", "R1", null);
            fail("Should have thrown IllegalArgumentException on duplicate key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'columnKey' argument.", e.getMessage());
        }
    }

    /**
     * Some checks for the removeRow(int) method.
     */
    @Test
    public void testRemoveRowByIndex() {
        KeyedObjects2D data = new KeyedObjects2D();
        data.setObject("Obj1", "R1", "C1");
        data.setObject("Obj2", "R2", "C2");
        data.removeRow(0);
        assertEquals(1, data.getRowCount());
        assertEquals("Obj2", data.getObject(0, 1));

        // try negative row index
        try {
            data.removeRow(-1);
            fail("Should have thrown IndexOutOfBoundsException on negative index");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("-1", e.getMessage());
        }

        // try row index too high
        try {
            data.removeRow(data.getRowCount());
            fail("Should have thrown IndexOutOfBoundsException on index out of range");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 1, Size: 1", e.getMessage());
        }
    }

    /**
     * Some checks for the removeColumn(int) method.
     */
    @Test
    public void testRemoveColumnByIndex() {
        KeyedObjects2D data = new KeyedObjects2D();
        data.setObject("Obj1", "R1", "C1");
        data.setObject("Obj2", "R2", "C2");
        data.removeColumn(0);
        assertEquals(1, data.getColumnCount());
        assertEquals("Obj2", data.getObject(1, 0));

        // try negative column index
        try {
            data.removeColumn(-1);
            fail("Should have thrown IndexOutOfBoundsException on negative index");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("-1", e.getMessage());
        }

        // try column index too high
        try {
            data.removeColumn(data.getColumnCount());
            fail("Should have thrown IndexOutOfBoundsException on index out of range");
        }
        catch (IndexOutOfBoundsException e) {
            assertEquals("Index: 1, Size: 1", e.getMessage());
        }
    }

    /**
     * Some checks for the removeRow(Comparable) method.
     */
    @Test
    public void testRemoveRowByKey() {
        KeyedObjects2D data = new KeyedObjects2D();
        data.setObject("Obj1", "R1", "C1");
        data.setObject("Obj2", "R2", "C2");
        data.removeRow("R2");
        assertEquals(1, data.getRowCount());
        assertEquals("Obj1", data.getObject(0, 0));

        // try unknown row key
        try {
            data.removeRow("XXX");
            fail("Should have thrown UnknownKeyException on key that doesn't exist");
        }
        catch (UnknownKeyException e) {
            assertEquals("Row key (XXX) not recognised.", e.getMessage());
        }

        // try null row key
        try {
            data.removeRow(null);
            fail("Should have thrown IndexOutOfBoundsException on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'key' argument.", e.getMessage());
        }
    }

    /**
     * Some checks for the removeColumn(Comparable) method.
     */
    @Test
    public void testRemoveColumnByKey() {
        KeyedObjects2D data = new KeyedObjects2D();
        data.setObject("Obj1", "R1", "C1");
        data.setObject("Obj2", "R2", "C2");
        data.removeColumn("C2");
        assertEquals(1, data.getColumnCount());
        assertEquals("Obj1", data.getObject(0, 0));

        // try unknown column key
        try {
            data.removeColumn("XXX");
            fail("Should have thrown UnknownKeyException on unknown key");
        }
        catch (UnknownKeyException e) {
            assertEquals("Column key (XXX) not recognised.", e.getMessage());
        }


        // try null column key
        try {
            data.removeColumn(null);
            fail("Should have thrown IllegalArgumentException on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'key' argument.", e.getMessage());
        }
    }

    /**
     * A simple check for the removeValue() method.
     */
    @Test
    public void testRemoveValue() {
        KeyedObjects2D data = new KeyedObjects2D();
        data.setObject("Obj1", "R1", "C1");
        data.setObject("Obj2", "R2", "C2");
        data.removeObject("R2", "C2");
        assertEquals(1, data.getRowCount());
        assertEquals(1, data.getColumnCount());
        assertEquals("Obj1", data.getObject(0, 0));
    }

}
