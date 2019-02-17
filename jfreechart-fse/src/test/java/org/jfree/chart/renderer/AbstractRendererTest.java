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
 * --------------------------
 * AbstractRendererTests.java
 * --------------------------
 * (C) Copyright 2003-2012, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 23-Oct-2003 : Version 1 (DG);
 * 01-Mar-2004 : Added serialization test (DG);
 * 19-Feb-2007 : Added testCloning (DG);
 * 28-Feb-2007 : Added checks for cloning (DG);
 * 17-Jun-2008 : Added new fields to testEquals() and testCloning() (DG);
 * 28-Jan-2009 : Updated testEquals() (DG);
 * 28-Apr-2009 : Updated testEquals() (DG);
 * 17-Jun-2012 : Removed JCommon dependencies (DG);
 *
 */

package org.jfree.chart.renderer;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link AbstractRenderer} class.
 */
public class AbstractRendererTest  {





    /**
     * Test that the equals() method distinguishes all fields.
     */
    @Test
    public void testEquals() {
        // have to use a concrete subclass...
        BarRenderer r1 = new BarRenderer();
        BarRenderer r2 = new BarRenderer();
        assertEquals(r1, r2);
        assertEquals(r2, r1);

        // seriesVisibleList
        r1.setSeriesVisible(2, Boolean.TRUE);
        assertFalse(r1.equals(r2));
        r2.setSeriesVisible(2, Boolean.TRUE);
        assertEquals(r1, r2);

        // defaultSeriesVisible
        r1.setDefaultSeriesVisible(false);
        assertFalse(r1.equals(r2));
        r2.setDefaultSeriesVisible(false);
        assertEquals(r1, r2);

        // seriesVisibleInLegendList
        r1.setSeriesVisibleInLegend(1, Boolean.TRUE);
        assertFalse(r1.equals(r2));
        r2.setSeriesVisibleInLegend(1, Boolean.TRUE);
        assertEquals(r1, r2);

        // baseSeriesVisibleInLegend
        r1.setDefaultSeriesVisibleInLegend(false);
        assertFalse(r1.equals(r2));
        r2.setDefaultSeriesVisibleInLegend(false);
        assertEquals(r1, r2);

        // paintList
        r1.setSeriesPaint(0, new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.WHITE));
        assertFalse(r1.equals(r2));
        r2.setSeriesPaint(0, new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.WHITE));
        assertEquals(r1, r2);

        // basePaint
        r1.setDefaultPaint(new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertFalse(r1.equals(r2));
        r2.setDefaultPaint(new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertEquals(r1, r2);

        // fillPaintList
        r1.setSeriesFillPaint(0, new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertFalse(r1.equals(r2));
        r2.setSeriesFillPaint(0, new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertEquals(r1, r2);

        // baseFillPaint
        r1.setDefaultFillPaint(new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertFalse(r1.equals(r2));
        r2.setDefaultFillPaint(new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertEquals(r1, r2);

        // outlinePaintList
        r1.setSeriesOutlinePaint(0, new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertFalse(r1.equals(r2));
        r2.setSeriesOutlinePaint(0, new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertEquals(r1, r2);

        // baseOutlinePaint
        r1.setDefaultOutlinePaint(new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertFalse(r1.equals(r2));
        r2.setDefaultOutlinePaint(new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertEquals(r1, r2);

        // strokeList
        Stroke s = new BasicStroke(3.21f);
        r1.setSeriesStroke(1, s);
        assertFalse(r1.equals(r2));
        r2.setSeriesStroke(1, s);
        assertEquals(r1, r2);

        // baseStroke
        r1.setDefaultStroke(s);
        assertFalse(r1.equals(r2));
        r2.setDefaultStroke(s);
        assertEquals(r1, r2);

        // outlineStrokeList
        r1.setSeriesOutlineStroke(0, s);
        assertFalse(r1.equals(r2));
        r2.setSeriesOutlineStroke(0, s);
        assertEquals(r1, r2);

        // baseOutlineStroke
        r1.setDefaultOutlineStroke(s);
        assertFalse(r1.equals(r2));
        r2.setDefaultOutlineStroke(s);
        assertEquals(r1, r2);

        // shapeList
        r1.setSeriesShape(1, new Ellipse2D.Double(1, 2, 3, 4));
        assertFalse(r1.equals(r2));
        r2.setSeriesShape(1, new Ellipse2D.Double(1, 2, 3, 4));
        assertEquals(r1, r2);

        // baseShape
        r1.setDefaultShape(new Ellipse2D.Double(1, 2, 3, 4));
        assertFalse(r1.equals(r2));
        r2.setDefaultShape(new Ellipse2D.Double(1, 2, 3, 4));
        assertEquals(r1, r2);

        // itemLabelsVisibleList
        r1.setSeriesItemLabelsVisible(1, Boolean.TRUE);
        assertFalse(r1.equals(r2));
        r2.setSeriesItemLabelsVisible(1, Boolean.TRUE);
        assertEquals(r1, r2);

        // baseItemLabelsVisible
        r1.setDefaultItemLabelsVisible(true);
        assertFalse(r1.equals(r2));
        r2.setDefaultItemLabelsVisible(true);
        assertEquals(r1, r2);

        // itemLabelFontList
        r1.setSeriesItemLabelFont(1, new Font("Serif", Font.BOLD, 9));
        assertFalse(r1.equals(r2));
        r2.setSeriesItemLabelFont(1, new Font("Serif", Font.BOLD, 9));
        assertEquals(r1, r2);

        // baseItemLabelFont
        r1.setDefaultItemLabelFont(new Font("Serif", Font.PLAIN, 10));
        assertFalse(r1.equals(r2));
        r2.setDefaultItemLabelFont(new Font("Serif", Font.PLAIN, 10));
        assertEquals(r1, r2);

        // itemLabelPaintList
        r1.setSeriesItemLabelPaint(0, new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.gray));
        assertFalse(r1.equals(r2));
        r2.setSeriesItemLabelPaint(0, new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.gray));
        assertEquals(r1, r2);

        // baseItemLabelPaint
        r1.setDefaultItemLabelPaint(new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.gray));
        assertFalse(r1.equals(r2));
        r2.setDefaultItemLabelPaint(new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.gray));
        assertEquals(r1, r2);

        // positiveItemLabelPositionList;
        r1.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition());
        assertFalse(r1.equals(r2));
        r2.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition());
        assertEquals(r1, r2);

        // basePositiveItemLabelPosition;
        r1.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_RIGHT));
        assertFalse(r1.equals(r2));
        r2.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_RIGHT));
        assertEquals(r1, r2);

        // negativeItemLabelPositionList;
        r1.setSeriesNegativeItemLabelPosition(1, new ItemLabelPosition(
                ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_RIGHT));
        assertFalse(r1.equals(r2));
        r2.setSeriesNegativeItemLabelPosition(1, new ItemLabelPosition(
                ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_RIGHT));
        assertEquals(r1, r2);

        // baseNegativeItemLabelPosition;
        r1.setDefaultNegativeItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_RIGHT));
        assertFalse(r1.equals(r2));
        r2.setDefaultNegativeItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.INSIDE10, TextAnchor.BASELINE_RIGHT));
        assertEquals(r1, r2);

        // itemLabelAnchorOffset
        r1.setItemLabelAnchorOffset(3.0);
        assertFalse(r1.equals(r2));
        r2.setItemLabelAnchorOffset(3.0);
        assertEquals(r1, r2);

        // createEntitiesList;
        r1.setSeriesCreateEntities(0, Boolean.TRUE);
        assertFalse(r1.equals(r2));
        r2.setSeriesCreateEntities(0, Boolean.TRUE);
        assertEquals(r1, r2);

        // baseCreateEntities;
        r1.setDefaultCreateEntities(false);
        assertFalse(r1.equals(r2));
        r2.setDefaultCreateEntities(false);
        assertEquals(r1, r2);

        // legendShape
        r1.setLegendShape(0, new Ellipse2D.Double(1.0, 2.0, 3.0, 4.0));
        assertFalse(r1.equals(r2));
        r2.setLegendShape(0, new Ellipse2D.Double(1.0, 2.0, 3.0, 4.0));
        assertEquals(r1, r2);

        // baseLegendShape
        r1.setDefaultLegendShape(new Ellipse2D.Double(5.0, 6.0, 7.0, 8.0));
        assertFalse(r1.equals(r2));
        r2.setDefaultLegendShape(new Ellipse2D.Double(5.0, 6.0, 7.0, 8.0));
        assertEquals(r1, r2);

        // legendTextFont
        r1.setLegendTextFont(0, new Font("Dialog", Font.PLAIN, 7));
        assertFalse(r1.equals(r2));
        r2.setLegendTextFont(0, new Font("Dialog", Font.PLAIN, 7));
        assertEquals(r1, r2);

        // baseLegendTextFont
        r1.setDefaultLegendTextFont(new Font("Dialog", Font.PLAIN, 7));
        assertFalse(r1.equals(r2));
        r2.setDefaultLegendTextFont(new Font("Dialog", Font.PLAIN, 7));
        assertEquals(r1, r2);

        // legendTextPaint
        r1.setLegendTextPaint(0, new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertFalse(r1.equals(r2));
        r2.setLegendTextPaint(0, new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertEquals(r1, r2);

        // baseOutlinePaint
        r1.setDefaultLegendTextPaint(new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertFalse(r1.equals(r2));
        r2.setDefaultLegendTextPaint(new GradientPaint(1.0f, 2.0f, Color.BLUE,
                3.0f, 4.0f, Color.RED));
        assertEquals(r1, r2);

    }

    private static class TestRenderer extends XYLineAndShapeRenderer {
        @Override
        public void setTreatLegendShapeAsLine(boolean flag) {
            super.setTreatLegendShapeAsLine(flag);
        }
    }

    /**
     * Check that the treatLegendShapeAsLine flag is included in the equals()
     * comparison.
     */
    @Test
    public void testEquals2() {
        TestRenderer r1 = new TestRenderer();
        TestRenderer r2 = new TestRenderer();
        assertEquals(r1, r2);
        r1.setTreatLegendShapeAsLine(true);
        assertFalse(r1.equals(r2));
        r2.setTreatLegendShapeAsLine(true);
        assertEquals(r1, r2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        LineAndShapeRenderer r1 = new LineAndShapeRenderer();

        Rectangle2D baseShape = new Rectangle2D.Double(11.0, 12.0, 13.0, 14.0);
        r1.setDefaultShape(baseShape);
        r1.setDefaultLegendShape(new Rectangle(4, 3, 2, 1));
        r1.setDefaultLegendTextFont(new Font("Dialog", Font.PLAIN, 3));
        r1.setDefaultLegendTextPaint(new Color(1, 2, 3));

        LineAndShapeRenderer r2 = (LineAndShapeRenderer) r1.clone();
        assertNotSame(r1, r2);
        assertSame(r1.getClass(), r2.getClass());
        assertEquals(r1, r2);

        r1.setSeriesVisible(0, Boolean.FALSE);
        assertFalse(r1.equals(r2));
        r2.setSeriesVisible(0, Boolean.FALSE);
        assertEquals(r1, r2);

        r1.setSeriesVisibleInLegend(0, Boolean.FALSE);
        assertFalse(r1.equals(r2));
        r2.setSeriesVisibleInLegend(0, Boolean.FALSE);
        assertEquals(r1, r2);

        r1.setSeriesPaint(0, Color.BLACK);
        assertFalse(r1.equals(r2));
        r2.setSeriesPaint(0, Color.BLACK);
        assertEquals(r1, r2);

        r1.setSeriesFillPaint(0, Color.yellow);
        assertFalse(r1.equals(r2));
        r2.setSeriesFillPaint(0, Color.yellow);
        assertEquals(r1, r2);

        r1.setSeriesOutlinePaint(0, Color.yellow);
        assertFalse(r1.equals(r2));
        r2.setSeriesOutlinePaint(0, Color.yellow);
        assertEquals(r1, r2);

        r1.setSeriesStroke(0, new BasicStroke(2.2f));
        assertFalse(r1.equals(r2));
        r2.setSeriesStroke(0, new BasicStroke(2.2f));
        assertEquals(r1, r2);

        r1.setSeriesOutlineStroke(0, new BasicStroke(2.2f));
        assertFalse(r1.equals(r2));
        r2.setSeriesOutlineStroke(0, new BasicStroke(2.2f));
        assertEquals(r1, r2);

        baseShape.setRect(4.0, 3.0, 2.0, 1.0);
        assertFalse(r1.equals(r2));
        r2.setDefaultShape(new Rectangle2D.Double(4.0, 3.0, 2.0, 1.0));
        assertEquals(r1, r2);

        r1.setSeriesShape(0, new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertFalse(r1.equals(r2));
        r2.setSeriesShape(0, new Rectangle2D.Double(1.0, 2.0, 3.0, 4.0));
        assertEquals(r1, r2);

        r1.setSeriesItemLabelsVisible(0, Boolean.TRUE);
        assertFalse(r1.equals(r2));
        r2.setSeriesItemLabelsVisible(0, Boolean.TRUE);
        assertEquals(r1, r2);

        r1.setSeriesItemLabelPaint(0, Color.RED);
        assertFalse(r1.equals(r2));
        r2.setSeriesItemLabelPaint(0, Color.RED);
        assertEquals(r1, r2);

        r1.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition());
        assertFalse(r1.equals(r2));
        r2.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition());
        assertEquals(r1, r2);

        r1.setSeriesNegativeItemLabelPosition(0, new ItemLabelPosition());
        assertFalse(r1.equals(r2));
        r2.setSeriesNegativeItemLabelPosition(0, new ItemLabelPosition());
        assertEquals(r1, r2);

        r1.setSeriesCreateEntities(0, Boolean.FALSE);
        assertFalse(r1.equals(r2));
        r2.setSeriesCreateEntities(0, Boolean.FALSE);
        assertEquals(r1, r2);

        r1.setLegendShape(0, new Rectangle(9, 7, 3, 4));
        assertFalse(r1.equals(r2));
        r2.setLegendShape(0, new Rectangle(9, 7, 3, 4));
        assertEquals(r1, r2);

        r1.setDefaultLegendShape(new Rectangle(3, 4, 1, 5));
        assertFalse(r1.equals(r2));
        r2.setDefaultLegendShape(new Rectangle(3, 4, 1, 5));
        assertEquals(r1, r2);

        r1.setLegendTextFont(1, new Font("Dialog", Font.PLAIN, 33));
        assertFalse(r1.equals(r2));
        r2.setLegendTextFont(1, new Font("Dialog", Font.PLAIN, 33));
        assertEquals(r1, r2);

        r1.setDefaultLegendTextFont(new Font("Dialog", Font.PLAIN, 11));
        assertFalse(r1.equals(r2));
        r2.setDefaultLegendTextFont(new Font("Dialog", Font.PLAIN, 11));
        assertEquals(r1, r2);

        r1.setLegendTextPaint(3, Color.RED);
        assertFalse(r1.equals(r2));
        r2.setLegendTextPaint(3, Color.RED);
        assertEquals(r1, r2);

        r1.setDefaultLegendTextPaint(Color.green);
        assertFalse(r1.equals(r2));
        r2.setDefaultLegendTextPaint(Color.green);
        assertEquals(r1, r2);
    }

    /**
     * A utility class for listening to changes to a renderer.
     */
    static class MyRendererChangeListener implements RendererChangeListener {

        /** The last event received. */
        public RendererChangeEvent lastEvent;

        /**
         * Creates a new instance.
         */
        public MyRendererChangeListener() {
            this.lastEvent = null;
        }
        @Override
        public void rendererChanged(RendererChangeEvent event) {
            this.lastEvent = event;
        }
    }

    /**
     * A check for cloning.
     */
    @Test
    public void testCloning2() throws CloneNotSupportedException {
        LineAndShapeRenderer r1 = new LineAndShapeRenderer();
        r1.setDefaultPaint(Color.BLUE);
        r1.setDefaultLegendTextPaint(new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.BLUE));
        LineAndShapeRenderer r2 = (LineAndShapeRenderer) r1.clone();
        assertNotSame(r1, r2);
        assertSame(r1.getClass(), r2.getClass());
        assertEquals(r1, r2);

        MyRendererChangeListener listener = new MyRendererChangeListener();
        r2.addChangeListener(listener);
        r2.setDefaultPaint(Color.RED);
        assertSame(listener.lastEvent.getRenderer(), r2);
        assertFalse(r1.hasListener(listener));
    }

    /**
     * Tests each setter method to ensure that it sends an event notification.
     */
    @Test
    public void testEventNotification() {

        RendererChangeDetector detector = new RendererChangeDetector();
        BarRenderer r1 = new BarRenderer();  // have to use a subclass of
                                             // AbstractRenderer
        r1.addChangeListener(detector);

        // PAINT
        detector.setNotified(false);
        r1.setSeriesPaint(0, Color.RED);
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultPaint(Color.RED);
        assertTrue(detector.getNotified());

        // OUTLINE PAINT
        detector.setNotified(false);
        r1.setSeriesOutlinePaint(0, Color.RED);
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultOutlinePaint(Color.RED);
        assertTrue(detector.getNotified());

        // STROKE
        detector.setNotified(false);
        r1.setSeriesStroke(0, new BasicStroke(1.0f));
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultStroke(new BasicStroke(1.0f));
        assertTrue(detector.getNotified());

        // OUTLINE STROKE
        detector.setNotified(false);
        r1.setSeriesOutlineStroke(0, new BasicStroke(1.0f));
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultOutlineStroke(new BasicStroke(1.0f));
        assertTrue(detector.getNotified());

        // SHAPE
        detector.setNotified(false);
        r1.setSeriesShape(0, new Rectangle2D.Float());
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultShape(new Rectangle2D.Float());
        assertTrue(detector.getNotified());

        // ITEM_LABELS_VISIBLE
        detector.setNotified(false);
        r1.setSeriesItemLabelsVisible(0, Boolean.TRUE);
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultItemLabelsVisible(Boolean.TRUE);
        assertTrue(detector.getNotified());

        // ITEM_LABEL_FONT
        detector.setNotified(false);
        r1.setSeriesItemLabelFont(0, new Font("Serif", Font.PLAIN, 12));
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultItemLabelFont(new Font("Serif", Font.PLAIN, 12));
        assertTrue(detector.getNotified());

        // ITEM_LABEL_PAINT
        detector.setNotified(false);
        r1.setSeriesItemLabelPaint(0, Color.BLUE);
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultItemLabelPaint(Color.BLUE);
        assertTrue(detector.getNotified());

        // POSITIVE ITEM LABEL POSITION
        detector.setNotified(false);
        r1.setSeriesPositiveItemLabelPosition(0, new ItemLabelPosition(
                ItemLabelAnchor.CENTER, TextAnchor.CENTER));
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.CENTER, TextAnchor.CENTER));
        assertTrue(detector.getNotified());

        // NEGATIVE ITEM LABEL ANCHOR
        detector.setNotified(false);
        r1.setSeriesNegativeItemLabelPosition(0, new ItemLabelPosition(
                ItemLabelAnchor.CENTER, TextAnchor.CENTER));
        assertTrue(detector.getNotified());

        detector.setNotified(false);
        r1.setDefaultNegativeItemLabelPosition(new ItemLabelPosition(
                ItemLabelAnchor.CENTER, TextAnchor.CENTER));
        assertTrue(detector.getNotified());

    }

    /**
     * Serialize an instance, restore it, and check for equality.  In addition,
     * test for a bug that was reported where the listener list is 'null' after
     * deserialization.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        BarRenderer r1 = new BarRenderer();
        r1.setDefaultLegendTextFont(new Font("Dialog", Font.PLAIN, 4));
        r1.setDefaultLegendTextPaint(new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.green));
        r1.setDefaultLegendShape(new Line2D.Double(1.0, 2.0, 3.0, 4.0));

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(r1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                    new ByteArrayInputStream(buffer.toByteArray()));
        BarRenderer r2 = (BarRenderer) in.readObject();
            in.close();

        assertEquals(r1, r2);
        r2.notifyListeners(new RendererChangeEvent(r2));


    }

    /**
     * Some checks for the autoPopulate flag default values.
     */
    @Test
    public void testAutoPopulateFlagDefaults() {
        BarRenderer r = new BarRenderer();
        assertEquals(true, r.getAutoPopulateSeriesPaint());
        assertEquals(false, r.getAutoPopulateSeriesFillPaint());
        assertEquals(false, r.getAutoPopulateSeriesOutlinePaint());
        assertEquals(true, r.getAutoPopulateSeriesStroke());
        assertEquals(false, r.getAutoPopulateSeriesOutlineStroke());
        assertEquals(true, r.getAutoPopulateSeriesShape());
    }

    /**
     * Some checks for the paint lookup mechanism.
     */
    @Test
    public void testPaintLookup() {
        BarRenderer r = new BarRenderer();
        assertEquals(Color.BLUE, r.getDefaultPaint());

        // first check that autoPopulate==false works as expected
        r.setAutoPopulateSeriesPaint(false);
        assertEquals(Color.BLUE, r.lookupSeriesPaint(0));
        assertNull(r.getSeriesPaint(0));

        // now check autoPopulate==true
        r.setAutoPopulateSeriesPaint(true);
        /*CategoryPlot plot =*/ new CategoryPlot(null, new CategoryAxis(
                "Category"), new NumberAxis("Value"), r);
        assertEquals(DefaultDrawingSupplier.DEFAULT_PAINT_SEQUENCE[0],
                r.lookupSeriesPaint(0));
        assertNotNull(r.getSeriesPaint(0));
    }

    /**
     * Some checks for the fill paint lookup mechanism.
     */
    @Test
    public void testFillPaintLookup() {
        BarRenderer r = new BarRenderer();
        assertEquals(Color.WHITE, r.getDefaultFillPaint());

        // first check that autoPopulate==false works as expected
        r.setAutoPopulateSeriesFillPaint(false);
        assertEquals(Color.WHITE, r.lookupSeriesFillPaint(0));
        assertNull(r.getSeriesFillPaint(0));

        // now check autoPopulate==true
        r.setAutoPopulateSeriesFillPaint(true);
        /*CategoryPlot plot =*/ new CategoryPlot(null, new CategoryAxis(
                "Category"), new NumberAxis("Value"), r);
        assertEquals(DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE[0],
                r.lookupSeriesFillPaint(0));
        assertNotNull(r.getSeriesFillPaint(0));
    }

    /**
     * Some checks for the outline paint lookup mechanism.
     */
    @Test
    public void testOutlinePaintLookup() {
        BarRenderer r = new BarRenderer();
        assertEquals(Color.gray, r.getDefaultOutlinePaint());

        // first check that autoPopulate==false works as expected
        r.setAutoPopulateSeriesOutlinePaint(false);
        assertEquals(Color.gray, r.lookupSeriesOutlinePaint(0));
        assertNull(r.getSeriesOutlinePaint(0));

        // now check autoPopulate==true
        r.setAutoPopulateSeriesOutlinePaint(true);
        /*CategoryPlot plot =*/ new CategoryPlot(null, new CategoryAxis(
                "Category"), new NumberAxis("Value"), r);
        assertEquals(DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE[0],
                r.lookupSeriesOutlinePaint(0));
        assertNotNull(r.getSeriesOutlinePaint(0));
    }

}
