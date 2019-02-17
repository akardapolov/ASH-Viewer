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
 * QuarterDateFormatTests.java
 * ---------------------------
 * (C) Copyright 2005-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 10-May-2005 : Version 1 (DG);
 * 08-Jun-2007 : Added check for new field in testEquals() (DG);
 *
 */

package org.jfree.chart.axis;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Tests for the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest  {





    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        QuarterDateFormat qf2 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        assertEquals(qf1, qf2);
        assertEquals(qf2, qf1);

        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"1", "2", "3", "4"});
        assertFalse(qf1.equals(qf2));
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"1", "2", "3", "4"});
        assertEquals(qf1, qf2);

        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"A", "2", "3", "4"});
        assertFalse(qf1.equals(qf2));
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"A", "2", "3", "4"});
        assertEquals(qf1, qf2);

        qf1 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"A", "2", "3", "4"}, true);
        assertFalse(qf1.equals(qf2));
        qf2 = new QuarterDateFormat(TimeZone.getTimeZone("PST"),
                new String[] {"A", "2", "3", "4"}, true);
        assertEquals(qf1, qf2);
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashCode() {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        QuarterDateFormat qf2 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        assertEquals(qf1, qf2);
        int h1 = qf1.hashCode();
        int h2 = qf2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});
        QuarterDateFormat qf2 = (QuarterDateFormat) qf1.clone();
        assertNotSame(qf1, qf2);
        assertSame(qf1.getClass(), qf2.getClass());
        assertEquals(qf1, qf2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        QuarterDateFormat qf1 = new QuarterDateFormat(TimeZone.getTimeZone(
                "GMT"), new String[] {"1", "2", "3", "4"});

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(qf1);
            out.close();

            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(
                    buffer.toByteArray()));
            QuarterDateFormat qf2 = (QuarterDateFormat) in.readObject();
            in.close();

        assertEquals(qf1, qf2);
    }

}
