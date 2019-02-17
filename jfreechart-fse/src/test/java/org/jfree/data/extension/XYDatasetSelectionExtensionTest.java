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
 * ------------------------------------
 * XYDatasetSelectionExtensionTest.java
 * ------------------------------------
 * (C) Copyright 2013, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 18-Sep-2013 : Version 1 (DG);
 *
 */

package org.jfree.data.extension;

import org.jfree.data.extension.impl.XYCursor;
import org.jfree.data.extension.impl.XYDatasetSelectionExtension;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Some tests for the {@link XYDatasetSelectionExtension} class.
 */
public class XYDatasetSelectionExtensionTest {

    @Test
    public void testGeneral() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries s1 = new XYSeries("S1");
        s1.add(1.0, 21.0);
        s1.add(2.0, 22.0);
        s1.add(3.0, 23.0);
        dataset.addSeries(s1);
        XYDatasetSelectionExtension ext = new XYDatasetSelectionExtension(
                dataset);
        XYCursor cursor = new XYCursor(0, 2);
        assertFalse(ext.isSelected(cursor));
        ext.setSelected(cursor, true);
        assertTrue(ext.isSelected(cursor));
        cursor.setPosition(0, 0);
        assertFalse(ext.isSelected(cursor));
        s1.remove(0);
        assertFalse(ext.isSelected(cursor));
        
        cursor.setPosition(1, 99);
        // fetching the value for a key that does not exist
        try {
            ext.isSelected(cursor);
            fail("Expected an ArrayIndexOutOfBoundsException.");
        } catch (ArrayIndexOutOfBoundsException e) {
            // expected in this case
        } catch (NullPointerException e) {
            // expected in this case
            // bad
        }
    }

}