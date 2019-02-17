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
 * -----------------------
 * AbstractBlockTests.java
 * -----------------------
 * (C) Copyright 2007-2012, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 16-Mar-2007 : Version 1 (DG);
 * 17-Jun-2012 : Removed JCommon dependencies (DG);
 * 
 */

package org.jfree.chart.block;

import org.jfree.chart.ui.RectangleInsets;
import org.junit.Test;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
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

/**
 * Tests for the {@link AbstractBlock} class.
 */
public class AbstractBlockTest  {





    /**
     * Confirm that the equals() method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        EmptyBlock b1 = new EmptyBlock(1.0, 2.0);
        EmptyBlock b2 = new EmptyBlock(1.0, 2.0);
        assertEquals(b1, b2);
        assertEquals(b2, b2);

        b1.setID("Test");
        assertFalse(b1.equals(b2));
        b2.setID("Test");
        assertEquals(b1, b2);

        b1.setMargin(new RectangleInsets(1.0, 2.0, 3.0, 4.0));
        assertFalse(b1.equals(b2));
        b2.setMargin(new RectangleInsets(1.0, 2.0, 3.0, 4.0));
        assertEquals(b1, b2);

        b1.setFrame(new BlockBorder(Color.RED));
        assertFalse(b1.equals(b2));
        b2.setFrame(new BlockBorder(Color.RED));
        assertEquals(b1, b2);

        b1.setPadding(new RectangleInsets(2.0, 4.0, 6.0, 8.0));
        assertFalse(b1.equals(b2));
        b2.setPadding(new RectangleInsets(2.0, 4.0, 6.0, 8.0));
        assertEquals(b1, b2);

        b1.setWidth(1.23);
        assertFalse(b1.equals(b2));
        b2.setWidth(1.23);
        assertEquals(b1, b2);

        b1.setHeight(4.56);
        assertFalse(b1.equals(b2));
        b2.setHeight(4.56);
        assertEquals(b1, b2);

        b1.setBounds(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertFalse(b1.equals(b2));
        b2.setBounds(new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertEquals(b1, b2);

        b1 = new EmptyBlock(1.1, 2.0);
        assertFalse(b1.equals(b2));
        b2 = new EmptyBlock(1.1, 2.0);
        assertEquals(b1, b2);

        b1 = new EmptyBlock(1.1, 2.2);
        assertFalse(b1.equals(b2));
        b2 = new EmptyBlock(1.1, 2.2);
        assertEquals(b1, b2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        EmptyBlock b1 = new EmptyBlock(1.0, 2.0);
        Rectangle2D bounds1 = new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0);
        b1.setBounds(bounds1);
        EmptyBlock b2 = (EmptyBlock) b1.clone();
        assertNotSame(b1, b2);
        assertSame(b1.getClass(), b2.getClass());
        assertEquals(b1, b2);

        bounds1.setFrame(2.0, 4.0, 6.0, 8.0);
        assertFalse(b1.equals(b2));
        b2.setBounds(new Rectangle2D.Double(2.0, 4.0, 6.0, 8.0));
        assertEquals(b1, b2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        EmptyBlock b1 = new EmptyBlock(1.0, 2.0);

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(b1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        EmptyBlock b2 = (EmptyBlock) in.readObject();
        in.close();
        assertEquals(b1, b2);
    }

}
