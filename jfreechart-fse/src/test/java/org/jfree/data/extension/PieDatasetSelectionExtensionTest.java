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
 * -------------------------------------
 * PieDatasetSelectionExtensionTest.java
 * -------------------------------------
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

import org.jfree.data.UnknownKeyException;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jfree.data.extension.impl.PieCursor;
import org.jfree.data.extension.impl.PieDatasetSelectionExtension;
import org.jfree.data.general.DefaultPieDataset;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 * Some tests for the {@link PieDatasetSelectionExtension} class.
 */
public class PieDatasetSelectionExtensionTest {

    @Test
    public void testGeneral() {
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("A", 1.0);
        dataset.setValue("B", 2.0);
        dataset.setValue("C", 3.0);
        PieDatasetSelectionExtension<String> ext = new 
                PieDatasetSelectionExtension<String>(dataset);
        PieCursor cursor = new PieCursor("B");
        assertFalse(ext.isSelected(cursor));
        ext.setSelected(cursor, true);
        assertTrue(ext.isSelected(cursor));
        cursor.setPosition("A");
        assertFalse(ext.isSelected(cursor));
        dataset.remove("B");
        assertFalse(ext.isSelected(cursor));
        cursor.setPosition("B");
        // fetching the value for a key that does not exist
        try {
            ext.isSelected(cursor);
            fail("Expected an UnknownKeyException.");
        } catch (UnknownKeyException e) {
            // expected in this case
        }
    }

}