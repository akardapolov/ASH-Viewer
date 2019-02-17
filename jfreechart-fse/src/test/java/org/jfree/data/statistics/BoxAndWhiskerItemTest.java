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
 * BoxAndWhiskerItemTests.java
 * ---------------------------
 * (C) Copyright 2004-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 01-Mar-2004 : Version 1 (DG);
 *
 */

package org.jfree.data.statistics;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link BoxAndWhiskerItem} class.
 */
public class BoxAndWhiskerItemTest  {





    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {

        BoxAndWhiskerItem i1 = new BoxAndWhiskerItem(
                1.0, 2.0, 3.0, 4.0,
                5.0, 6.0, 7.0, 8.0,
            new ArrayList()
        );
        BoxAndWhiskerItem i2 = new BoxAndWhiskerItem(
                1.0, 2.0, 3.0, 4.0,
                5.0, 6.0, 7.0, 8.0,
            new ArrayList()
        );
        assertEquals(i1, i2);
        assertEquals(i2, i1);

    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        BoxAndWhiskerItem i1 = new BoxAndWhiskerItem(
                1.0, 2.0, 3.0, 4.0,
                5.0, 6.0, 7.0, 8.0,
            new ArrayList()
        );

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(i1);
        out.close();

        ObjectInput in = new ObjectInputStream(
            new ByteArrayInputStream(buffer.toByteArray())
        );
        BoxAndWhiskerItem i2 = (BoxAndWhiskerItem) in.readObject();
        in.close();
        assertEquals(i1, i2);

    }

}
