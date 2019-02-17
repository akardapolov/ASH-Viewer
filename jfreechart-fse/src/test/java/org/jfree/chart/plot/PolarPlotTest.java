/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2014, by Object Refinery Limited and Contributors.
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
 * -------------------
 * PolarPlotTests.java
 * -------------------
 * (C) Copyright 2005-2014, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 23-Feb-2005 : Version 1 (DG);
 * 08-Jun-2005 : Extended testEquals() (DG);
 * 07-Feb-2007 : Extended testEquals() and testCloning() (DG);
 * 17-Feb-2008 : Tests for new angleTickUnit field (DG);
 * 09-Dec-2009 : Added new tests (DG);
 * 12-Nov-2011 : Added tests for translateToJava2D (MH);
 * 17-Dec-2011 : Updated testEquals() (DG);
 * 
 */

package org.jfree.chart.plot;

import org.jfree.chart.LegendItem;
import org.jfree.chart.axis.LogAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

/**
 * Some tests for the {@link PolarPlot} class.
 */
public class PolarPlotTest  {





    /**
     * Some checks for the getLegendItems() method.
     */
    @Test
    public void testGetLegendItems() {
        XYSeriesCollection d = new XYSeriesCollection();
        d.addSeries(new XYSeries("A"));
        d.addSeries(new XYSeries("B"));
        DefaultPolarItemRenderer r = new DefaultPolarItemRenderer();
        PolarPlot plot = new PolarPlot();
        plot.setDataset(d);
        plot.setRenderer(r);
        List<LegendItem> items = plot.getLegendItems();
        assertEquals(2, items.size());
        LegendItem item1 = items.get(0);
        assertEquals("A", item1.getLabel());
        LegendItem item2 = items.get(1);
        assertEquals("B", item2.getLabel());
    }

    /**
     * Some checks for the getLegendItems() method with multiple datasets.
     */
    @Test
    public void testGetLegendItems2() {
        XYSeriesCollection d1 = new XYSeriesCollection();
        d1.addSeries(new XYSeries("A"));
        d1.addSeries(new XYSeries("B"));
        XYSeriesCollection d2 = new XYSeriesCollection();
        d2.addSeries(new XYSeries("C"));
        d2.addSeries(new XYSeries("D"));
        DefaultPolarItemRenderer r = new DefaultPolarItemRenderer();
        PolarPlot plot = new PolarPlot();
        plot.setDataset(d1);
        plot.setDataset(1, d2);
        plot.setRenderer(r);
        plot.setRenderer(1, new DefaultPolarItemRenderer());
        List<LegendItem> items = plot.getLegendItems();
        assertEquals(4, items.size());
        LegendItem item1 = items.get(0);
        assertEquals("A", item1.getLabel());
        LegendItem item2 = items.get(1);
        assertEquals("B", item2.getLabel());
        LegendItem item3 = items.get(2);
        assertEquals("C", item3.getLabel());
        LegendItem item4 = items.get(3);
        assertEquals("D", item4.getLabel());
    }

    /**
     * Some checks for the equals() method.
     */
    @Test
    public void testEquals() {
        PolarPlot plot1 = new PolarPlot();
        PolarPlot plot2 = new PolarPlot();
        assertEquals(plot1, plot2);
        assertEquals(plot2, plot1);

        plot1.setAngleGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.BLUE));
        assertFalse(plot1.equals(plot2));
        plot2.setAngleGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.BLUE));
        assertEquals(plot1, plot2);

        Stroke s = new BasicStroke(1.23f);
        plot1.setAngleGridlineStroke(s);
        assertFalse(plot1.equals(plot2));
        plot2.setAngleGridlineStroke(s);
        assertEquals(plot1, plot2);

        plot1.setAngleTickUnit(new NumberTickUnit(11.0));
        assertFalse(plot1.equals(plot2));
        plot2.setAngleTickUnit(new NumberTickUnit(11.0));
        assertEquals(plot1, plot2);

        plot1.setAngleGridlinesVisible(false);
        assertFalse(plot1.equals(plot2));
        plot2.setAngleGridlinesVisible(false);
        assertEquals(plot1, plot2);

        plot1.setAngleLabelFont(new Font("Serif", Font.PLAIN, 9));
        assertFalse(plot1.equals(plot2));
        plot2.setAngleLabelFont(new Font("Serif", Font.PLAIN, 9));
        assertEquals(plot1, plot2);

        plot1.setAngleLabelPaint(new GradientPaint(9.0f, 8.0f, Color.BLUE,
                7.0f, 6.0f, Color.RED));
        assertFalse(plot1.equals(plot2));
        plot2.setAngleLabelPaint(new GradientPaint(9.0f, 8.0f, Color.BLUE,
                7.0f, 6.0f, Color.RED));
        assertEquals(plot1, plot2);

        plot1.setAngleLabelsVisible(false);
        assertFalse(plot1.equals(plot2));
        plot2.setAngleLabelsVisible(false);
        assertEquals(plot1, plot2);

        plot1.setAxis(new NumberAxis("Test"));
        assertFalse(plot1.equals(plot2));
        plot2.setAxis(new NumberAxis("Test"));
        assertEquals(plot1, plot2);

        plot1.setRadiusGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.WHITE,
                3.0f, 4.0f, Color.BLACK));
        assertFalse(plot1.equals(plot2));
        plot2.setRadiusGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.WHITE,
                3.0f, 4.0f, Color.BLACK));
        assertEquals(plot1, plot2);

        plot1.setRadiusGridlineStroke(s);
        assertFalse(plot1.equals(plot2));
        plot2.setRadiusGridlineStroke(s);
        assertEquals(plot1, plot2);

        plot1.setRadiusGridlinesVisible(false);
        assertFalse(plot1.equals(plot2));
        plot2.setRadiusGridlinesVisible(false);
        assertEquals(plot1, plot2);

        plot1.setRadiusMinorGridlinesVisible(false);
        assertFalse(plot1.equals(plot2));
        plot2.setRadiusMinorGridlinesVisible(false);
        assertEquals(plot1, plot2);

        plot1.addCornerTextItem("XYZ");
        assertFalse(plot1.equals(plot2));
        plot2.addCornerTextItem("XYZ");
        assertEquals(plot1, plot2);

        plot1.setMargin(6);
        assertFalse(plot1.equals(plot2));
        plot2.setMargin(6);
        assertEquals(plot1, plot2);

        List<LegendItem> lic1 = new ArrayList<LegendItem>();
        lic1.add(new LegendItem("XYZ", Color.RED));
        plot1.setFixedLegendItems(lic1);
        assertFalse(plot1.equals(plot2));
        List<LegendItem> lic2 = new ArrayList<LegendItem>();
        lic2.add(new LegendItem("XYZ", Color.RED));
        plot2.setFixedLegendItems(lic2);
        assertEquals(plot1, plot2);
    }

    /**
     * Some basic checks for the clone() method.
     * @throws CloneNotSupportedException 
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        PolarPlot p1 = new PolarPlot();
        PolarPlot p2 = (PolarPlot) p1.clone();
        assertNotSame(p1, p2);
        assertSame(p1.getClass(), p2.getClass());
        assertEquals(p1, p2);

        // check independence
        p1.addCornerTextItem("XYZ");
        assertFalse(p1.equals(p2));
        p2.addCornerTextItem("XYZ");
        assertEquals(p1, p2);

        p1 = new PolarPlot(new DefaultXYDataset(), new NumberAxis("A1"),
                new DefaultPolarItemRenderer());
        p2 = (PolarPlot) p1.clone();

        assertNotSame(p1, p2);
        assertSame(p1.getClass(), p2.getClass());
        assertEquals(p1, p2);

        // check independence
        p1.getAxis().setLabel("ABC");
        assertFalse(p1.equals(p2));
        p2.getAxis().setLabel("ABC");
        assertEquals(p1, p2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     * @throws IOException
     * @throws ClassNotFoundException  
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        PolarPlot p1 = new PolarPlot();
        p1.setAngleGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f,
                4.0f, Color.BLUE));
        p1.setAngleLabelPaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f,
                4.0f, Color.BLUE));
        p1.setRadiusGridlinePaint(new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f,
                4.0f, Color.BLUE));

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(buffer);
        out.writeObject(p1);
        out.close();

        ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray()));
        PolarPlot p2 = (PolarPlot) in.readObject();
            in.close();

        assertEquals(p1, p2);
    }

    @Test
    public void testTranslateToJava2D_NumberAxis() {
        
        Rectangle2D dataArea = new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0);
        ValueAxis axis = new NumberAxis();
        axis.setRange(0.0, 20.0);

        PolarPlot plot = new PolarPlot(null, axis, null);
        plot.setMargin(0);
        plot.setAngleOffset(0.0);

        Point point = plot.translateToJava2D(0.0, 10.0, axis, dataArea );
        assertEquals(75.0, point.getX(), 0.5);
        assertEquals(50.0, point.getY(), 0.5);

        point = plot.translateToJava2D(90.0, 5.0, axis, dataArea );
        assertEquals(50.0, point.getX(), 0.5);
        assertEquals(62.5, point.getY(), 0.5);

        point = plot.translateToJava2D(45.0, 20.0, axis, dataArea );
        assertEquals(85.0, point.getX(), 0.5);
        assertEquals(85.0, point.getY(), 0.5);

        point = plot.translateToJava2D(135.0, 20.0, axis, dataArea );
        assertEquals(15.0, point.getX(), 0.5);
        assertEquals(85.0, point.getY(), 0.5);

        point = plot.translateToJava2D(225.0, 15.0, axis, dataArea );
        assertEquals(23.0, point.getX(), 0.5);
        assertEquals(23.0, point.getY(), 0.5);

        point = plot.translateToJava2D(315.0, 15.0, axis, dataArea );
        assertEquals(77.0, point.getX(), 0.5);
        assertEquals(23.0, point.getY(), 0.5);
        
        point = plot.translateToJava2D(21.0, 11.5, axis, dataArea );
        assertEquals(77.0, point.getX(), 0.5);
        assertEquals(60.0, point.getY(), 0.5);
        
        point = plot.translateToJava2D(162.0, 7.0, axis, dataArea );
        assertEquals(33.0, point.getX(), 0.5);
        assertEquals(55.0, point.getY(), 0.5);
        
    }

    @Test
    public void testTranslateToJava2D_NumberAxisAndMargin() {
        Rectangle2D dataArea = new Rectangle2D.Double(10.0, 10.0, 80.0, 80.0);
        ValueAxis axis = new NumberAxis();
        axis.setRange(-2.0, 2.0);

        PolarPlot plot = new PolarPlot(null, axis, null);
        plot.setAngleOffset(0.0);

        Point point = plot.translateToJava2D(0.0, 10.0, axis, dataArea );
        assertEquals(110.0, point.getX(), 0.5);
        assertEquals(50.0, point.getY(), 0.5);

        point = plot.translateToJava2D(90.0, 5.0, axis, dataArea );
        assertEquals(50.0, point.getX(), 0.5);
        assertEquals(85.0, point.getY(), 0.5);

        point = plot.translateToJava2D(45.0, 20.0, axis, dataArea );
        assertEquals(128.0, point.getX(), 0.5);
        assertEquals(128.0, point.getY(), 0.5);

        point = plot.translateToJava2D(135.0, 20.0, axis, dataArea );
        assertEquals(-28.0, point.getX(), 0.5);
        assertEquals(128.0, point.getY(), 0.5);

        point = plot.translateToJava2D(225.0, 15.0, axis, dataArea );
        assertEquals(-10.0, point.getX(), 0.5);
        assertEquals(-10.0, point.getY(), 0.5);

        point = plot.translateToJava2D(315.0, 15.0, axis, dataArea );
        assertEquals(110.0, point.getX(), 0.5);
        assertEquals(-10.0, point.getY(), 0.5);
        
        point = plot.translateToJava2D(21.0, 11.5, axis, dataArea );
        assertEquals(113.0, point.getX(), 0.5);
        assertEquals(74.0, point.getY(), 0.5);
        
        point = plot.translateToJava2D(162.0, 7.0, axis, dataArea );
        assertEquals(7.0, point.getX(), 0.5);
        assertEquals(64.0, point.getY(), 0.5);
    }

    @Test
    public void testTranslateToJava2D_LogAxis() {
        Rectangle2D dataArea = new Rectangle2D.Double(0.0, 0.0, 100.0, 100.0);
        ValueAxis axis = new LogAxis();
        axis.setRange(1.0, 100.0);

        PolarPlot plot = new PolarPlot(null, axis, null);
        plot.setMargin(0);
        plot.setAngleOffset(0.0);

        Point point = plot.translateToJava2D(0.0, 10.0, axis, dataArea );
        assertEquals(75.0, point.getX(), 0.5);
        assertEquals(50.0, point.getY(), 0.5);

        point = plot.translateToJava2D(90.0, 5.0, axis, dataArea );
        assertEquals(50.0, point.getX(), 0.5);
        assertEquals(67.5, point.getY(), 0.5);

        point = plot.translateToJava2D(45.0, 20.0, axis, dataArea );
        assertEquals(73.0, point.getX(), 0.5);
        assertEquals(73.0, point.getY(), 0.5);
    }
}
