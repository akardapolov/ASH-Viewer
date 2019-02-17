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
 * -------------------------
 * MonthDateFormatTests.java
 * -------------------------
 * (C) Copyright 2005-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 10-May-2005 : Version 1 (DG);
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
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Some tests for the {@link MonthDateFormat} class.
 */
public class MonthDateFormatTest  {





    /**
     * Confirm that the equals method can distinguish all the required fields.
     */
    @Test
    public void testEquals() {
        MonthDateFormat mf1 = new MonthDateFormat();
        MonthDateFormat mf2 = new MonthDateFormat();
        assertEquals(mf1, mf2);
        assertEquals(mf2, mf1);

        boolean[] showYear1 = new boolean [12];
        showYear1[0] = true;
        boolean[] showYear2 = new boolean [12];
        showYear1[1] = true;

        // time zone
        mf1 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.US, 1,
            showYear1, new SimpleDateFormat("yy"));
        assertFalse(mf1.equals(mf2));
        mf2 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.US, 1,
            showYear1, new SimpleDateFormat("yy"));
        assertEquals(mf1, mf2);

        // locale
        mf1 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.FRANCE, 1,
            showYear1, new SimpleDateFormat("yy"));
        assertFalse(mf1.equals(mf2));
        mf2 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.FRANCE, 1,
            showYear1, new SimpleDateFormat("yy"));
        assertEquals(mf1, mf2);

        // chars
        mf1 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.FRANCE, 2,
            showYear1, new SimpleDateFormat("yy"));
        assertFalse(mf1.equals(mf2));
        mf2 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.FRANCE, 2,
            showYear1, new SimpleDateFormat("yy"));
        assertEquals(mf1, mf2);

        // showYear[]
        mf1 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.FRANCE, 2,
            showYear2, new SimpleDateFormat("yy"));
        assertFalse(mf1.equals(mf2));
        mf2 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.FRANCE, 2,
            showYear2, new SimpleDateFormat("yy"));
        assertEquals(mf1, mf2);

        // yearFormatter
        mf1 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.FRANCE, 2,
            showYear2, new SimpleDateFormat("yyyy"));
        assertFalse(mf1.equals(mf2));
        mf2 = new MonthDateFormat(TimeZone.getTimeZone("PST"), Locale.FRANCE, 2,
            showYear2, new SimpleDateFormat("yyyy"));
        assertEquals(mf1, mf2);

    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashCode() {
        MonthDateFormat mf1 = new MonthDateFormat();
        MonthDateFormat mf2 = new MonthDateFormat();
        assertEquals(mf1, mf2);
        int h1 = mf1.hashCode();
        int h2 = mf2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        MonthDateFormat mf1 = new MonthDateFormat();
        MonthDateFormat mf2 = (MonthDateFormat) mf1.clone();
        assertNotSame(mf1, mf2);
        assertSame(mf1.getClass(), mf2.getClass());
        assertEquals(mf1, mf2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        MonthDateFormat mf1 = new MonthDateFormat();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(mf1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        MonthDateFormat mf2 = (MonthDateFormat) in.readObject();
        in.close();
        assertEquals(mf1, mf2);
    }

}
