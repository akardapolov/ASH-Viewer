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
 * BoxAndWhiskerRendererTests.java
 * -------------------------------
 * (C) Copyright 2003-2012, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 22-Oct-2003 : Version 1 (DG);
 * 23-Apr-2004 : Extended testEquals() method (DG);
 * 12-Oct-2006 : Added new checks for bug 1572478 (DG);
 * 11-May-2007 : Added testGetLegendItem() (DG);
 * 17-May-2007 : Added testGetLegendItemSeriesIndex() (DG);
 * 08-Oct-2007 : Added tests for null items in dataset (DG);
 * 15-Jan-2008 : Updated testEquals() (DG);
 * 23-Apr-2008 : Added testPublicCloneable() (DG);
 * 21-Jan-2009 : Updated testEquals() for new fields (DG);
 * 17-Jun-2012 : Removed JCommon dependencies (DG);
 *
 */

package org.jfree.chart.renderer.category;

import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.util.PublicCloneable;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.junit.Test;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link BoxAndWhiskerRenderer} class.
 */
public class BoxAndWhiskerRendererTest  {





    /**
     * Test that the equals() method distinguishes all fields.
     */
    @Test
    public void testEquals() {
        BoxAndWhiskerRenderer r1 = new BoxAndWhiskerRenderer();
        BoxAndWhiskerRenderer r2 = new BoxAndWhiskerRenderer();
        assertEquals(r1, r2);

        r1.setArtifactPaint(new GradientPaint(1.0f, 2.0f, Color.yellow,
                3.0f, 4.0f, Color.BLUE));
        assertFalse(r1.equals(r2));
        r2.setArtifactPaint(new GradientPaint(1.0f, 2.0f, Color.yellow,
                3.0f, 4.0f, Color.BLUE));
        assertEquals(r1, r2);

        r1.setFillBox(!r1.getFillBox());
        assertFalse(r1.equals(r2));
        r2.setFillBox(!r2.getFillBox());
        assertEquals(r1, r2);

        r1.setItemMargin(0.11);
        assertFalse(r1.equals(r2));
        r2.setItemMargin(0.11);
        assertEquals(r1, r2);

        r1.setMaximumBarWidth(0.99);
        assertFalse(r1.equals(r2));
        r2.setMaximumBarWidth(0.99);
        assertEquals(r1, r2);

        r1.setMeanVisible(false);
        assertFalse(r1.equals(r2));
        r2.setMeanVisible(false);
        assertEquals(r1, r2);

        r1.setMedianVisible(false);
        assertFalse(r1.equals(r2));
        r2.setMedianVisible(false);
        assertEquals(r1, r2);
    }

    /**
     * Two objects that are equal are required to return the same hashCode.
     */
    @Test
    public void testHashcode() {
        BoxAndWhiskerRenderer r1 = new BoxAndWhiskerRenderer();
        BoxAndWhiskerRenderer r2 = new BoxAndWhiskerRenderer();
        assertEquals(r1, r2);
        int h1 = r1.hashCode();
        int h2 = r2.hashCode();
        assertEquals(h1, h2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        BoxAndWhiskerRenderer r1 = new BoxAndWhiskerRenderer();
        BoxAndWhiskerRenderer r2 = (BoxAndWhiskerRenderer) r1.clone();

        assertNotSame(r1, r2);
        assertSame(r1.getClass(), r2.getClass());
        assertEquals(r1, r2);
    }

    /**
     * Check that this class implements PublicCloneable.
     */
    @Test
    public void testPublicCloneable() {
        BoxAndWhiskerRenderer r1 = new BoxAndWhiskerRenderer();
        assertTrue(r1 instanceof PublicCloneable);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        BoxAndWhiskerRenderer r1 = new BoxAndWhiskerRenderer();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(r1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        BoxAndWhiskerRenderer r2 = (BoxAndWhiskerRenderer) in.readObject();
        in.close();
        assertEquals(r1, r2);

    }

    /**
     * Draws the chart with a <code>null</code> info object to make sure that
     * no exceptions are thrown (particularly by code in the renderer).
     */
    @Test
    public void testDrawWithNullInfo() {

        DefaultBoxAndWhiskerCategoryDataset dataset
            = new DefaultBoxAndWhiskerCategoryDataset();
        dataset.add(new BoxAndWhiskerItem(1.0, 2.0,
                0.0, 4.0, 0.5,
                4.5, -0.5, 5.5,
                null), "S1", "C1");
        CategoryPlot plot = new CategoryPlot(dataset,
                new CategoryAxis("Category"), new NumberAxis("Value"),
                new BoxAndWhiskerRenderer());
        JFreeChart chart = new JFreeChart(plot);
        /* BufferedImage image = */ chart.createBufferedImage(300, 200,
                null);

        //FIXME we should assert a value here
    }

    /**
     * A check for bug 1572478 (for the vertical orientation).
     */
    @Test
    public void testBug1572478Vertical() {
        DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset() {

            @Override
            public Number getQ1Value(int row, int column) {
                return null;
            }

            @Override
            public Number getQ1Value(Comparable rowKey, Comparable columnKey) {
                return null;
            }
        };
        List<Number> values = new ArrayList<Number>();
        values.add(1.0);
        values.add(10.0);
        values.add(100.0);
        dataset.add(values, "row", "column");
        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis("x"),
                new NumberAxis("y"), new BoxAndWhiskerRenderer());
        JFreeChart chart = new JFreeChart(plot);

        BufferedImage image = new BufferedImage(200 , 100,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        chart.draw(g2, new Rectangle2D.Double(0, 0, 200, 100), null,
                new ChartRenderingInfo());
        g2.dispose();

        //FIXME we should really assert a value here
    }

    /**
     * A check for bug 1572478 (for the horizontal orientation).
     */
    @Test
    public void testBug1572478Horizontal() {
        DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset() {

            @Override
            public Number getQ1Value(int row, int column) {
                return null;
            }

            @Override
            public Number getQ1Value(Comparable rowKey, Comparable columnKey) {
                return null;
            }
        };
        List<Number> values = new ArrayList<Number>();
        values.add(1.0);
        values.add(10.0);
        values.add(100.0);
        dataset.add(values, "row", "column");
        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis("x"),
                new NumberAxis("y"), new BoxAndWhiskerRenderer());
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        JFreeChart chart = new JFreeChart(plot);

        BufferedImage image = new BufferedImage(200 , 100,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        chart.draw(g2, new Rectangle2D.Double(0, 0, 200, 100), null,
                new ChartRenderingInfo());
        g2.dispose();

        //FIXME we should assert a value here

    }

    /**
     * Some checks for the getLegendItem() method.
     */
    @Test
    public void testGetLegendItem() {
        DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset();
        List<Number> values = new ArrayList<Number>();
        values.add(1.10);
        values.add(1.45);
        values.add(1.33);
        values.add(1.23);
        dataset.add(values, "R1", "C1");
        BoxAndWhiskerRenderer r = new BoxAndWhiskerRenderer();
        CategoryPlot plot = new CategoryPlot(dataset, new CategoryAxis("x"),
                new NumberAxis("y"), r);
        /*JFreeChart chart =*/ new JFreeChart(plot);
        LegendItem li = r.getLegendItem(0, 0);
        assertNotNull(li);
        r.setSeriesVisibleInLegend(0, Boolean.FALSE);
        li = r.getLegendItem(0, 0);
        assertNull(li);
    }

    /**
     * A check for the datasetIndex and seriesIndex fields in the LegendItem
     * returned by the getLegendItem() method.
     */
    @Test
    public void testGetLegendItemSeriesIndex() {
        DefaultCategoryDataset dataset0 = new DefaultCategoryDataset();
        dataset0.addValue(21.0, "R1", "C1");
        dataset0.addValue(22.0, "R2", "C1");
        DefaultCategoryDataset dataset1 = new DefaultCategoryDataset();
        dataset1.addValue(23.0, "R3", "C1");
        dataset1.addValue(24.0, "R4", "C1");
        dataset1.addValue(25.0, "R5", "C1");
        BoxAndWhiskerRenderer r = new BoxAndWhiskerRenderer();
        CategoryPlot plot = new CategoryPlot(dataset0, new CategoryAxis("x"),
                new NumberAxis("y"), r);
        plot.setDataset(1, dataset1);
        /*JFreeChart chart =*/ new JFreeChart(plot);
        LegendItem li = r.getLegendItem(1, 2);
        assertEquals("R5", li.getLabel());
        assertEquals(1, li.getDatasetIndex());
        assertEquals(2, li.getSeriesIndex());
    }

    /**
     * Draws a chart where the dataset contains a null mean value.
     */
    @Test
    public void testDrawWithNullMean() {
        DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset();
        dataset.add(new BoxAndWhiskerItem(null, 2.0,
                0.0, 4.0, 0.5,
                4.5, -0.5, 5.5,
                null), "S1", "C1");
        CategoryPlot plot = new CategoryPlot(dataset,
                new CategoryAxis("Category"), new NumberAxis("Value"),
                new BoxAndWhiskerRenderer());
        ChartRenderingInfo info = new ChartRenderingInfo();
        JFreeChart chart = new JFreeChart(plot);
        /* BufferedImage image = */ chart.createBufferedImage(300, 200,
                info);
        //FIXME we should really assert a value here
    }

    /**
     * Draws a chart where the dataset contains a null median value.
     */
    @Test
    public void testDrawWithNullMedian() {
            DefaultBoxAndWhiskerCategoryDataset dataset
                    = new DefaultBoxAndWhiskerCategoryDataset();
            dataset.add(new BoxAndWhiskerItem(1.0, null,
                    0.0, 4.0, 0.5,
                    4.5, -0.5, 5.5,
                    null), "S1", "C1");
            CategoryPlot plot = new CategoryPlot(dataset,
                    new CategoryAxis("Category"), new NumberAxis("Value"),
                    new BoxAndWhiskerRenderer());
            ChartRenderingInfo info = new ChartRenderingInfo();
            JFreeChart chart = new JFreeChart(plot);
            /* BufferedImage image = */ chart.createBufferedImage(300, 200,
                    info);
        //FIXME we should really assert a value here
    }

    /**
     * Draws a chart where the dataset contains a null Q1 value.
     */
    @Test
    public void testDrawWithNullQ1() {

            DefaultBoxAndWhiskerCategoryDataset dataset
                    = new DefaultBoxAndWhiskerCategoryDataset();
            dataset.add(new BoxAndWhiskerItem(1.0, 2.0,
                    null, 4.0, 0.5,
                    4.5, -0.5, 5.5,
                    null), "S1", "C1");
            CategoryPlot plot = new CategoryPlot(dataset,
                    new CategoryAxis("Category"), new NumberAxis("Value"),
                    new BoxAndWhiskerRenderer());
            ChartRenderingInfo info = new ChartRenderingInfo();
            JFreeChart chart = new JFreeChart(plot);
            /* BufferedImage image = */ chart.createBufferedImage(300, 200,
                    info);

        //FIXME we should really assert a value here
    }

    /**
     * Draws a chart where the dataset contains a null Q3 value.
     */
    @Test
    public void testDrawWithNullQ3() {
            DefaultBoxAndWhiskerCategoryDataset dataset
                    = new DefaultBoxAndWhiskerCategoryDataset();
            dataset.add(new BoxAndWhiskerItem(1.0, 2.0,
                    3.0, null, 0.5,
                    4.5, -0.5, 5.5,
                    null), "S1", "C1");
            CategoryPlot plot = new CategoryPlot(dataset,
                    new CategoryAxis("Category"), new NumberAxis("Value"),
                    new BoxAndWhiskerRenderer());
            ChartRenderingInfo info = new ChartRenderingInfo();
            JFreeChart chart = new JFreeChart(plot);
            /* BufferedImage image = */ chart.createBufferedImage(300, 200,
                    info);
        //FIXME we should really assert a value here
    }

    /**
     * Draws a chart where the dataset contains a null min regular value.
     */
    @Test
    public void testDrawWithNullMinRegular() {

        DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset();
        dataset.add(new BoxAndWhiskerItem(1.0, 2.0,
                3.0, 4.0, null,
                4.5, -0.5, 5.5,
                null), "S1", "C1");
        CategoryPlot plot = new CategoryPlot(dataset,
                new CategoryAxis("Category"), new NumberAxis("Value"),
                new BoxAndWhiskerRenderer());
        ChartRenderingInfo info = new ChartRenderingInfo();
        JFreeChart chart = new JFreeChart(plot);
        /* BufferedImage image = */ chart.createBufferedImage(300, 200,
                info);

        //FIXME we should assert a value here
    }

    /**
     * Draws a chart where the dataset contains a null max regular value.
     */
    @Test
    public void testDrawWithNullMaxRegular() {
            DefaultBoxAndWhiskerCategoryDataset dataset
                    = new DefaultBoxAndWhiskerCategoryDataset();
            dataset.add(new BoxAndWhiskerItem(1.0, 2.0,
                    3.0, 4.0, 0.5,
                    null, -0.5, 5.5,
                    null), "S1", "C1");
            CategoryPlot plot = new CategoryPlot(dataset,
                    new CategoryAxis("Category"), new NumberAxis("Value"),
                    new BoxAndWhiskerRenderer());
            ChartRenderingInfo info = new ChartRenderingInfo();
            JFreeChart chart = new JFreeChart(plot);
            /* BufferedImage image = */ chart.createBufferedImage(300, 200,
                    info);
        //FIXME we should assert a value here
    }

    /**
     * Draws a chart where the dataset contains a null min outlier value.
     */
    @Test
    public void testDrawWithNullMinOutlier() {
        DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset();
        dataset.add(new BoxAndWhiskerItem(1.0, 2.0,
                3.0, 4.0, 0.5,
                4.5, null, 5.5,
                null), "S1", "C1");
        CategoryPlot plot = new CategoryPlot(dataset,
                new CategoryAxis("Category"), new NumberAxis("Value"),
                new BoxAndWhiskerRenderer());
        ChartRenderingInfo info = new ChartRenderingInfo();
        JFreeChart chart = new JFreeChart(plot);
        /* BufferedImage image = */ chart.createBufferedImage(300, 200,
                info);
        //FIXME we should assert a value here
    }

    /**
     * Draws a chart where the dataset contains a null max outlier value.
     */
    @Test
    public void testDrawWithNullMaxOutlier() {

        DefaultBoxAndWhiskerCategoryDataset dataset
                = new DefaultBoxAndWhiskerCategoryDataset();
        dataset.add(new BoxAndWhiskerItem(1.0, 2.0,
                3.0, 4.0, 0.5,
                4.5, -0.5, null,
                new java.util.ArrayList()), "S1", "C1");
        CategoryPlot plot = new CategoryPlot(dataset,
                new CategoryAxis("Category"), new NumberAxis("Value"),
                new BoxAndWhiskerRenderer());
        ChartRenderingInfo info = new ChartRenderingInfo();
        JFreeChart chart = new JFreeChart(plot);
        /* BufferedImage image = */ chart.createBufferedImage(300, 200,
                info);
        //FIXME we should assert a value here
    }

}
