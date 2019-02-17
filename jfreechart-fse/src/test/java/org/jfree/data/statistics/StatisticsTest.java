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
 * --------------------
 * StatisticsTests.java
 * --------------------
 * (C) Copyright 2004-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 25-Mar-2004 : Version 1 (DG);
 * 04-Oct-2004 : Eliminated NumberUtils usage (DG);
 *
 */

package org.jfree.data.statistics;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Statistics} class.
 */
public class StatisticsTest  {





    /**
     * Some checks for the calculateMean(Number[]) and
     * calculateMean(Number[], boolean) methods.
     */
    @Test
    public void testCalculateMean_Array() {

        // try null array

        try {
            Statistics.calculateMean((Number[]) null);
            fail("IllegalArgumentException should have been thrown on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'values' argument.", e.getMessage());
        }

        try {
            Statistics.calculateMean((Number[]) null, false);
            fail("IllegalArgumentException should have been thrown on negative key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'values' argument.", e.getMessage());
        }

        // try an array containing no items
        assertTrue(Double.isNaN(Statistics.calculateMean(new Number[0])));
        assertTrue(Double.isNaN(Statistics.calculateMean(new Number[0],
                false)));

        // try an array containing a single Number
        Number[] values = new Number[] {1.0};
        assertEquals(1.0, Statistics.calculateMean(values), EPSILON);
        assertEquals(1.0, Statistics.calculateMean(values, true), EPSILON);
        assertEquals(1.0, Statistics.calculateMean(values, false), EPSILON);

        // try an array containing a single Number and a null
        values = new Number[] {1.0, null};
        assertTrue(Double.isNaN(Statistics.calculateMean(values)));
        assertTrue(Double.isNaN(Statistics.calculateMean(values, true)));
        assertEquals(1.0, Statistics.calculateMean(values, false), EPSILON);

        // try an array containing a single Number and a NaN
        values = new Number[] {1.0, Double.NaN};
        assertTrue(Double.isNaN(Statistics.calculateMean(values)));
        assertTrue(Double.isNaN(Statistics.calculateMean(values, true)));
        assertEquals(1.0, Statistics.calculateMean(values, false), EPSILON);
    }

    /**
     * Some checks for the calculateMean(Collection) and
     * calculateMean(Collection, boolean) methods.
     */
    @Test
    public void testCalculateMean_Collection() {

        // try a null collection

        try {
            Statistics.calculateMean((Collection<Number>) null);
            fail("IllegalArgumentException should have been thrown on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'values' argument.", e.getMessage());
        }

        try {
            Statistics.calculateMean((Collection<Number>) null, false);
            fail("IllegalArgumentException should have been thrown on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'values' argument.", e.getMessage());
        }

        // try an empty collection
        List<Number> values = new ArrayList<Number>();
        assertTrue(Double.isNaN(Statistics.calculateMean(values)));
        assertTrue(Double.isNaN(Statistics.calculateMean(values, true)));
        assertTrue(Double.isNaN(Statistics.calculateMean(values, false)));

        // try a collection with a single number
        values.add(9.0);
        assertEquals(9.0, Statistics.calculateMean(values), EPSILON);
        assertEquals(9.0, Statistics.calculateMean(values, true), EPSILON);
        assertEquals(9.0, Statistics.calculateMean(values, false), EPSILON);

        // try a collection with a single number plus a null
        values.add(null);
        assertTrue(Double.isNaN(Statistics.calculateMean(values)));
        assertTrue(Double.isNaN(Statistics.calculateMean(values, true)));
        assertEquals(9.0, Statistics.calculateMean(values, false), EPSILON);

        // try a collection with a single number plus a NaN
        values.clear();
        values.add(9.0);
        values.add(Double.NaN);
        assertTrue(Double.isNaN(Statistics.calculateMean(values)));
        assertTrue(Double.isNaN(Statistics.calculateMean(values, true)));
        assertEquals(9.0, Statistics.calculateMean(values, false), EPSILON);

        // try a collection with several numbers
        values = new ArrayList<Number>();
        values.add(9.0);
        values.add(3.0);
        values.add(2.0);
        values.add(2.0);
        double mean = Statistics.calculateMean(values);
        assertEquals(4.0, mean, EPSILON);

        // a Collection containing a NaN will return Double.NaN for the result
        values.add(Double.NaN);
        assertTrue(Double.isNaN(Statistics.calculateMean(values)));
    }

    static final double EPSILON = 0.0000000001;

    /**
     * Some checks for the calculateMedian(List, boolean) method.
     */
    @Test
    public void testCalculateMedian() {

        // check null list
        assertTrue(Double.isNaN(Statistics.calculateMedian(null, false)));
        assertTrue(Double.isNaN(Statistics.calculateMedian(null, true)));

        // check empty list
        List <Number>list = new ArrayList<Number>();
        assertTrue(Double.isNaN(Statistics.calculateMedian(list, false)));
        assertTrue(Double.isNaN(Statistics.calculateMedian(list, true)));

        // check list containing null
        list.add(null);
        try {
            Statistics.calculateMedian(list, false);
            fail("Should have thrown a NullPointerException");
        }
        catch (NullPointerException e) {
            //we expect ot go in here
        }
        try {
            Statistics.calculateMedian(list, true);
            fail("Should have thrown a NullPointerException");
        }
        catch (NullPointerException e) {
            //we expect to go in here
        }

    }

    /**
     * A test for the calculateMedian() method.
     */
    @Test
    public void testCalculateMedian1() {
        List<Number> values = new ArrayList<Number>();
        values.add(1.0);
        double median = Statistics.calculateMedian(values);
        assertEquals(1.0, median, 0.0000001);
    }

    /**
     * A test for the calculateMedian() method.
     */
    @Test
    public void testCalculateMedian2() {
        List<Number> values = new ArrayList<Number>();
        values.add(2.0);
        values.add(1.0);
        double median = Statistics.calculateMedian(values);
        assertEquals(1.5, median, 0.0000001);
    }

    /**
     * A test for the calculateMedian() method.
     */
    @Test
    public void testCalculateMedian3() {
        List<Number> values = new ArrayList<Number>();
        values.add(1.0);
        values.add(2.0);
        values.add(3.0);
        values.add(6.0);
        values.add(5.0);
        values.add(4.0);
        double median = Statistics.calculateMedian(values);
        assertEquals(3.5, median, 0.0000001);
    }

    /**
     * A test for the calculateMedian() method.
     */
    @Test
    public void testCalculateMedian4() {
        List<Number> values = new ArrayList<Number>();
        values.add(7.0);
        values.add(2.0);
        values.add(3.0);
        values.add(5.0);
        values.add(4.0);
        values.add(6.0);
        values.add(1.0);
        double median = Statistics.calculateMedian(values);
        assertEquals(4.0, median, 0.0000001);
    }

    /**
     * A test using some real data that caused a problem at one point.
     */
    @Test
    public void testCalculateMedian5() {
        List<Number> values = new ArrayList<Number>();
        values.add(11.228692993861783);
        values.add(11.30823353859889);
        values.add(11.75312904769314);
        values.add(11.825102897465314);
        values.add(10.184252778401783);
        values.add(12.207951828057766);
        values.add(10.68841994040566);
        values.add(12.099522004479438);
        values.add(11.508874945056881);
        values.add(12.052517729558513);
        values.add(12.401481645578734);
        values.add(12.185377793028543);
        values.add(10.666372951930315);
        values.add(11.680978041499548);
        values.add(11.06528277406718);
        values.add(11.36876492904596);
        values.add(11.927565516175939);
        values.add(11.39307785978655);
        values.add(11.989603679523857);
        values.add(12.009834360354864);
        values.add(10.653351822461559);
        values.add(11.851776254376754);
        values.add(11.045441544755946);
        values.add(11.993674040560624);
        values.add(12.898219965238944);
        values.add(11.97095782819647);
        values.add(11.73234406745488);
        values.add(11.649006017243991);
        values.add(12.20549704915365);
        values.add(11.799723639384919);
        values.add(11.896208658005628);
        values.add(12.164149111823424);
        values.add(12.042795103513766);
        values.add(12.114839532596426);
        values.add(12.166609097075824);
        values.add(12.183017546225935);
        values.add(11.622009125845342);
        values.add(11.289365786738633);
        values.add(12.462984323671568);
        values.add(11.573494921030598);
        values.add(10.862867940485804);
        values.add(12.018186939664872);
        values.add(10.418046849313018);
        values.add(11.326344465881341);
        double median = Statistics.calculateMedian(values, true);
        assertEquals(11.812413268425116, median, 0.000001);
        Collections.sort((List) values);
        double median2 = Statistics.calculateMedian(values, false);
        assertEquals(11.812413268425116, median2, 0.000001);
    }

    /**
     * A test for the calculateMedian() method.
     */
    @Test
    public void testCalculateMedian6() {
        List<Number> values = new ArrayList<Number>();
        values.add(7.0);
        values.add(2.0);
        values.add(3.0);
        values.add(5.0);
        values.add(4.0);
        values.add(6.0);
        values.add(1.0);
        double median = Statistics.calculateMedian(values, 0, 2);
        assertEquals(3.0, median, 0.0000001);
    }

    /**
     * A simple test for the correlation calculation.
     */
    @Test
    public void testCorrelation1() {
        Number[] data1 = new Number[3];
        data1[0] = (double) 1;
        data1[1] = (double) 2;
        data1[2] = (double) 3;
        Number[] data2 = new Number[3];
        data2[0] = (double) 1;
        data2[1] = (double) 2;
        data2[2] = (double) 3;
        double r = Statistics.getCorrelation(data1, data2);
        assertEquals(1.0, r, 0.00000001);
    }

    /**
     * A simple test for the correlation calculation.
     *
     * http://trochim.human.cornell.edu/kb/statcorr.htm
     */
    @Test
    public void testCorrelation2() {
        Number[] data1 = new Number[20];
        data1[0] = (double) 68;
        data1[1] = (double) 71;
        data1[2] = (double) 62;
        data1[3] = (double) 75;
        data1[4] = (double) 58;
        data1[5] = (double) 60;
        data1[6] = (double) 67;
        data1[7] = (double) 68;
        data1[8] = (double) 71;
        data1[9] = (double) 69;
        data1[10] = (double) 68;
        data1[11] = (double) 67;
        data1[12] = (double) 63;
        data1[13] = (double) 62;
        data1[14] = (double) 60;
        data1[15] = (double) 63;
        data1[16] = (double) 65;
        data1[17] = (double) 67;
        data1[18] = (double) 63;
        data1[19] = (double) 61;
        Number[] data2 = new Number[20];
        data2[0] = 4.1;
        data2[1] = 4.6;
        data2[2] = 3.8;
        data2[3] = 4.4;
        data2[4] = 3.2;
        data2[5] = 3.1;
        data2[6] = 3.8;
        data2[7] = 4.1;
        data2[8] = 4.3;
        data2[9] = 3.7;
        data2[10] = 3.5;
        data2[11] = 3.2;
        data2[12] = 3.7;
        data2[13] = 3.3;
        data2[14] = 3.4;
        data2[15] = 4.0;
        data2[16] = 4.1;
        data2[17] = 3.8;
        data2[18] = 3.4;
        data2[19] = 3.6;
        double r = Statistics.getCorrelation(data1, data2);
        assertEquals(0.7306356862792885, r, 0.000000000001);
    }

    /**
     * Some checks for the getStdDev() method.
     */
    @Test
    public void testGetStdDev() {

        // try null argument

        try {
            Statistics.getStdDev(null);
            fail("IllegalArgumentException should have been thrown on null key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Null 'data' array.", e.getMessage());
        }

        // try zero length array
        try {
            Statistics.getStdDev(new Double[0]);
            fail("IllegalArgumentException should have been thrown on empty key");
        }
        catch (IllegalArgumentException e) {
            assertEquals("Zero length 'data' array.", e.getMessage());
        }

        // try single value
        assertTrue(Double.isNaN(Statistics.getStdDev(new Double[]
                {1.0})));
    }

}
