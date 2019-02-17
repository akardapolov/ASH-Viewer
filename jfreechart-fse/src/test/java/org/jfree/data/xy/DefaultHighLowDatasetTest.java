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
 * DefaultHighLowDatasetTests.java
 * -------------------------------
 * (C) Copyright 2006-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 28-Nov-2006 : Version 1 (DG);
 * 22-Apr-2008 : Added testPublicCloneable (DG);
 *
 */

package org.jfree.data.xy;

import org.jfree.chart.util.PublicCloneable;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link DefaultHighLowDataset} class.
 */
public class DefaultHighLowDatasetTest  {





    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        DefaultHighLowDataset d1 = new DefaultHighLowDataset("Series 1",
                new Date[0], new double[0], new double[0], new double[0],
                new double[0], new double[0]);
        DefaultHighLowDataset d2 = new DefaultHighLowDataset("Series 1",
                new Date[0], new double[0], new double[0], new double[0],
                new double[0], new double[0]);
        assertEquals(d1, d2);
        assertEquals(d2, d1);

        d1 = new DefaultHighLowDataset("Series 2",
                new Date[0], new double[0], new double[0], new double[0],
                new double[0], new double[0]);
        assertFalse(d1.equals(d2));
        d2 = new DefaultHighLowDataset("Series 2",
                new Date[0], new double[0], new double[0], new double[0],
                new double[0], new double[0]);
        assertEquals(d1, d2);

        d1 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[1], new double[1],
                new double[1], new double[1], new double[1]);
        assertFalse(d1.equals(d2));
        d2 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[1], new double[1],
                new double[1], new double[1], new double[1]);
        assertEquals(d1, d2);

        d1 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2}, new double[1],
                new double[1], new double[1], new double[1]);
        assertFalse(d1.equals(d2));
        d2 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2}, new double[1],
                new double[1], new double[1], new double[1]);
        assertEquals(d1, d2);

        d1 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[1], new double[1],
                new double[1]);
        assertFalse(d1.equals(d2));
        d2 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[1], new double[1],
                new double[1]);
        assertEquals(d1, d2);

        d1 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[] {5.6}, new double[1],
                new double[1]);
        assertFalse(d1.equals(d2));
        d2 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[] {5.6}, new double[1],
                new double[1]);
        assertEquals(d1, d2);

        d1 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[] {5.6}, new double[] {7.8},
                new double[1]);
        assertFalse(d1.equals(d2));
        d2 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[] {5.6}, new double[] {7.8},
                new double[1]);
        assertEquals(d1, d2);

        d1 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[] {5.6}, new double[] {7.8},
                new double[] {99.9});
        assertFalse(d1.equals(d2));
        d2 = new DefaultHighLowDataset("Series 2",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[] {5.6}, new double[] {7.8},
                new double[] {99.9});
        assertEquals(d1, d2);

    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        DefaultHighLowDataset d1 = new DefaultHighLowDataset("Series 1",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[] {5.6}, new double[] {7.8},
                new double[] {99.9});
        DefaultHighLowDataset d2 = (DefaultHighLowDataset) d1.clone();
        assertNotSame(d1, d2);
        assertSame(d1.getClass(), d2.getClass());
        assertEquals(d1, d2);
    }

    /**
     * Verify that this class implements {@link PublicCloneable}.
     */
    @Test
    public void testPublicCloneable() {
        DefaultHighLowDataset d1 = new DefaultHighLowDataset("Series 1",
                new Date[0], new double[0], new double[0], new double[0],
                new double[0], new double[0]);
        assertTrue(d1 instanceof PublicCloneable);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        DefaultHighLowDataset d1 = new DefaultHighLowDataset("Series 1",
                new Date[] {new Date(123L)}, new double[] {1.2},
                new double[] {3.4}, new double[] {5.6}, new double[] {7.8},
                new double[] {99.9});

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(d1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
        DefaultHighLowDataset d2 = (DefaultHighLowDataset) in.readObject();
            in.close();

        assertEquals(d1, d2);
    }

}
