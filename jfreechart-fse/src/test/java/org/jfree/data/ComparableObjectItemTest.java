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
 * ------------------------------
 * ComparableObjectItemTests.java
 * ------------------------------
 * (C) Copyright 2006-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 20-Oct-2006 : Version 1 (DG);
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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link ComparableObjectItem} class.
 */
public class ComparableObjectItemTest  {





    /**
     * Some checks for the constructor.
     */
    @Test
    public void testConstructor() {
        // check null argument 1
        try {
            /* ComparableObjectItem item1 = */ new ComparableObjectItem(null,
                    "XYZ");
            fail("Should have thrown an IllegalArgumentException on null parameter");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'x' argument.", e.getMessage());
        }
    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        ComparableObjectItem item1 = new ComparableObjectItem(1,
                "XYZ");
        ComparableObjectItem item2 = new ComparableObjectItem(1,
                "XYZ");
        assertEquals(item1, item2);
        assertEquals(item2, item1);

        item1 = new ComparableObjectItem(2, "XYZ");
        assertFalse(item1.equals(item2));
        item2 = new ComparableObjectItem(2, "XYZ");
        assertEquals(item1, item2);

        item1 = new ComparableObjectItem(2, null);
        assertFalse(item1.equals(item2));
        item2 = new ComparableObjectItem(2, null);
        assertEquals(item1, item2);
    }

    /**
     * Some checks for the clone() method.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        ComparableObjectItem item1 = new ComparableObjectItem(1,
                "XYZ");
        ComparableObjectItem item2 = (ComparableObjectItem) item1.clone();
        assertNotSame(item1, item2);
        assertSame(item1.getClass(), item2.getClass());
        assertEquals(item1, item2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ComparableObjectItem item1 = new ComparableObjectItem(1,
                "XYZ");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(item1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        ComparableObjectItem item2 = (ComparableObjectItem) in.readObject();
        in.close();
        assertEquals(item1, item2);
    }

    /**
     * Some checks for the compareTo() method.
     */
    @Test
    public void testCompareTo() {
        ComparableObjectItem item1 = new ComparableObjectItem(1,
                "XYZ");
        ComparableObjectItem item2 = new ComparableObjectItem(2,
                "XYZ");
        ComparableObjectItem item3 = new ComparableObjectItem(3,
                "XYZ");
        ComparableObjectItem item4 = new ComparableObjectItem(1,
                "XYZ");
        assertTrue(item2.compareTo(item1) > 0);
        assertTrue(item3.compareTo(item1) > 0);
        assertSame(item4.compareTo(item1), 0);
        assertTrue(item1.compareTo(item2) < 0);
    }

}
