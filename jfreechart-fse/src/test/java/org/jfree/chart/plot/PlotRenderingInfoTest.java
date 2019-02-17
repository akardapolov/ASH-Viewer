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
 * PlotRenderingInfoTests.java
 * ---------------------------
 * (C) Copyright 2004-2008, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 20-May-2004 : Version 1 (DG);
 *
 */

package org.jfree.chart.plot;

import org.jfree.chart.ChartRenderingInfo;
import org.junit.Test;

import java.awt.Rectangle;
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
 * Tests for the {@link PlotRenderingInfo} class.
 */
public class PlotRenderingInfoTest  {





    /**
     * Test the equals() method.
     */
    @Test
    public void testEquals() {
        PlotRenderingInfo p1 = new PlotRenderingInfo(new ChartRenderingInfo());
        PlotRenderingInfo p2 = new PlotRenderingInfo(new ChartRenderingInfo());
        assertEquals(p1, p2);
        assertEquals(p2, p1);

        p1.setPlotArea(new Rectangle(2, 3, 4, 5));
        assertFalse(p1.equals(p2));
        p2.setPlotArea(new Rectangle(2, 3, 4, 5));
        assertEquals(p1, p2);

        p1.setDataArea(new Rectangle(2, 4, 6, 8));
        assertFalse(p1.equals(p2));
        p2.setDataArea(new Rectangle(2, 4, 6, 8));
        assertEquals(p1, p2);

        p1.addSubplotInfo(new PlotRenderingInfo(null));
        assertFalse(p1.equals(p2));
        p2.addSubplotInfo(new PlotRenderingInfo(null));
        assertEquals(p1, p2);

        p1.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));
        assertFalse(p1.equals(p2));
        p2.getSubplotInfo(0).setDataArea(new Rectangle(1, 2, 3, 4));
        assertEquals(p1, p2);
    }

    /**
     * Confirm that cloning works.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        PlotRenderingInfo p1 = new PlotRenderingInfo(new ChartRenderingInfo());
        p1.setPlotArea(new Rectangle2D.Double());
        PlotRenderingInfo p2 = (PlotRenderingInfo) p1.clone();
        assertNotSame(p1, p2);
        assertSame(p1.getClass(), p2.getClass());
        assertEquals(p1, p2);

        // check independence
        p1.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        assertFalse(p1.equals(p2));
        p2.getPlotArea().setRect(1.0, 2.0, 3.0, 4.0);
        assertEquals(p1, p2);

        p1.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertFalse(p1.equals(p2));
        p2.getDataArea().setRect(4.0, 3.0, 2.0, 1.0);
        assertEquals(p1, p2);
    }

    /**
     * Serialize an instance, restore it, and check for equality.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {

        PlotRenderingInfo p1 = new PlotRenderingInfo(new ChartRenderingInfo());

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(buffer);
            out.writeObject(p1);
            out.close();

            ObjectInput in = new ObjectInputStream(
                new ByteArrayInputStream(buffer.toByteArray())
            );
        PlotRenderingInfo p2 = (PlotRenderingInfo) in.readObject();
            in.close();

        assertEquals(p1, p2);

    }

}
