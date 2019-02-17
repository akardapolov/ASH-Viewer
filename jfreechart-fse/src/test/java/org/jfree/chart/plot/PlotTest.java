/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2013, by Object Refinery Limited and Contributors.
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
 * --------------
 * PlotTests.java
 * --------------
 * (C) Copyright 2005-2013, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 06-Jun-2005 : Version 1 (DG);
 * 30-Jun-2006 : Extended equals() test to cover new field (DG);
 * 11-May-2007 : Another new field in testEquals() (DG);
 * 17-Jun-2012 : Remove JCommon dependencies (DG);
 *
 */

package org.jfree.chart.plot;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.ui.Align;
import org.jfree.chart.ui.RectangleInsets;
import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.net.URL;
import javax.swing.ImageIcon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Some tests for the {@link Plot} class.
 */
public class PlotTest  {

    private Image testImage;

    private Image getTestImage() {
        if (testImage == null) {
            URL imageURL = getClass().getClassLoader().getResource(
                    "org/jfree/chart/gorilla.jpg");
            if (imageURL != null) {
                ImageIcon temp = new ImageIcon(imageURL);
                // use ImageIcon because it waits for the image to load...
                testImage = temp.getImage();
            }
        }
        return testImage;
    }

    /**
     * Check that the equals() method can distinguish all fields (note that
     * the dataset is NOT considered in the equals() method).
     */
    @Test
    public void testEquals() {
        PiePlot plot1 = new PiePlot();
        PiePlot plot2 = new PiePlot();
        assertEquals(plot1, plot2);
        assertEquals(plot2, plot1);

        // noDataMessage
        plot1.setNoDataMessage("No data XYZ");
        assertFalse(plot1.equals(plot2));
        plot2.setNoDataMessage("No data XYZ");
        assertEquals(plot1, plot2);

        // noDataMessageFont
        plot1.setNoDataMessageFont(new Font("SansSerif", Font.PLAIN, 13));
        assertFalse(plot1.equals(plot2));
        plot2.setNoDataMessageFont(new Font("SansSerif", Font.PLAIN, 13));
        assertEquals(plot1, plot2);

        // noDataMessagePaint
        plot1.setNoDataMessagePaint(new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.BLUE));
        assertFalse(plot1.equals(plot2));
        plot2.setNoDataMessagePaint(new GradientPaint(1.0f, 2.0f, Color.RED,
                3.0f, 4.0f, Color.BLUE));
        assertEquals(plot1, plot2);

        // insets
        plot1.setInsets(new RectangleInsets(1.0, 2.0, 3.0, 4.0));
        assertFalse(plot1.equals(plot2));
        plot2.setInsets(new RectangleInsets(1.0, 2.0, 3.0, 4.0));
        assertEquals(plot1, plot2);

        // outlineVisible
        plot1.setOutlineVisible(false);
        assertFalse(plot1.equals(plot2));
        plot2.setOutlineVisible(false);
        assertEquals(plot1, plot2);

        // outlineStroke
        BasicStroke s = new BasicStroke(1.23f);
        plot1.setOutlineStroke(s);
        assertFalse(plot1.equals(plot2));
        plot2.setOutlineStroke(s);
        assertEquals(plot1, plot2);

        // outlinePaint
        plot1.setOutlinePaint(new GradientPaint(1.0f, 2.0f, Color.yellow,
                3.0f, 4.0f, Color.green));
        assertFalse(plot1.equals(plot2));
        plot2.setOutlinePaint(new GradientPaint(1.0f, 2.0f, Color.yellow,
                3.0f, 4.0f, Color.green));
        assertEquals(plot1, plot2);

        // backgroundPaint
        plot1.setBackgroundPaint(new GradientPaint(1.0f, 2.0f, Color.cyan,
                3.0f, 4.0f, Color.green));
        assertFalse(plot1.equals(plot2));
        plot2.setBackgroundPaint(new GradientPaint(1.0f, 2.0f, Color.cyan,
                3.0f, 4.0f, Color.green));
        assertEquals(plot1, plot2);

        // backgroundImage
        plot1.setBackgroundImage(getTestImage());
        assertFalse(plot1.equals(plot2));
        plot2.setBackgroundImage(getTestImage());
        assertEquals(plot1, plot2);

        // backgroundImageAlignment
        plot1.setBackgroundImageAlignment(Align.BOTTOM_RIGHT);
        assertFalse(plot1.equals(plot2));
        plot2.setBackgroundImageAlignment(Align.BOTTOM_RIGHT);
        assertEquals(plot1, plot2);

        // backgroundImageAlpha
        plot1.setBackgroundImageAlpha(0.77f);
        assertFalse(plot1.equals(plot2));
        plot2.setBackgroundImageAlpha(0.77f);
        assertEquals(plot1, plot2);

        // foregroundAlpha
        plot1.setForegroundAlpha(0.99f);
        assertFalse(plot1.equals(plot2));
        plot2.setForegroundAlpha(0.99f);
        assertEquals(plot1, plot2);

        // backgroundAlpha
        plot1.setBackgroundAlpha(0.99f);
        assertFalse(plot1.equals(plot2));
        plot2.setBackgroundAlpha(0.99f);
        assertEquals(plot1, plot2);

        // drawingSupplier
        plot1.setDrawingSupplier(new DefaultDrawingSupplier(
                new Paint[] {Color.BLUE}, new Paint[] {Color.RED},
                new Stroke[] {new BasicStroke(1.1f)},
                new Stroke[] {new BasicStroke(9.9f)},
                new Shape[] {new Rectangle(1, 2, 3, 4)}));
        assertFalse(plot1.equals(plot2));
        plot2.setDrawingSupplier(new DefaultDrawingSupplier(
                new Paint[] {Color.BLUE}, new Paint[] {Color.RED},
                new Stroke[] {new BasicStroke(1.1f)},
                new Stroke[] {new BasicStroke(9.9f)},
                new Shape[] {new Rectangle(1, 2, 3, 4)}));
        assertEquals(plot1, plot2);
    }

}
