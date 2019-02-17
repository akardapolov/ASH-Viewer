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
 * ---------------------------
 * DefaultKeyedValueTests.java
 * ---------------------------
 * (C) Copyright 2003-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 13-Mar-2003 : Version 1 (DG);
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
 * Tests for the {@link DefaultKeyedValue} class.
 */
public class DefaultKeyedValueTest  {





    /**
     * Simple checks for the constructor.
     */
    @Test
    public void testConstructor() {
        DefaultKeyedValue v = new DefaultKeyedValue("A", 1);
        assertEquals("A", v.getKey());
        assertEquals(1, v.getValue());

        // try null key
       try {
            /*v =*/ new DefaultKeyedValue(null, 1);
           fail("IllegalArgumentException should have been thrown on null parameter");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'key' argument.", e.getMessage());
        }


        // try a null value
        v = new DefaultKeyedValue("A", null);
        assertNull(v.getValue());
    }

    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {

        DefaultKeyedValue v1 = new DefaultKeyedValue("Test", 45.5);
        DefaultKeyedValue v2 = new DefaultKeyedValue("Test", 45.5);
        assertEquals(v1, v2);
        assertEquals(v2, v1);

        v1 = new DefaultKeyedValue("Test 1", 45.5);
        v2 = new DefaultKeyedValue("Test 2", 45.5);
        assertFalse(v1.equals(v2));

        v1 = new DefaultKeyedValue("Test", 45.5);
        v2 = new DefaultKeyedValue("Test", 45.6);
        assertFalse(v1.equals(v2));

    }

    /**
     * Some checks for the clone() method.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        DefaultKeyedValue v1 = new DefaultKeyedValue("Test", 45.5);
        DefaultKeyedValue v2 = (DefaultKeyedValue) v1.clone();
        assertNotSame(v1, v2);
        assertSame(v1.getClass(), v2.getClass());
        assertEquals(v1, v2);

        // confirm that the clone is independent of the original
        v2.setValue(12.3);
        assertFalse(v1.equals(v2));
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        DefaultKeyedValue v1 = new DefaultKeyedValue("Test", 25.3);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(v1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        DefaultKeyedValue v2 = (DefaultKeyedValue) in.readObject();
        in.close();
        assertEquals(v1, v2);

    }

}
