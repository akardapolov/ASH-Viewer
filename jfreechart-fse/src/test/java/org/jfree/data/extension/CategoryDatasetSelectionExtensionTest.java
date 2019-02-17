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
 * ------------------------------------------
 * CategoryDatasetSelectionExtensionTest.java
 * ------------------------------------------
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.extension.impl.CategoryCursor;
import org.jfree.data.extension.impl.CategoryDatasetSelectionExtension;
import org.junit.Test;

/**
 * Some tests for the {@link CategoryDatasetSelectionExtension} class.
 */
public class CategoryDatasetSelectionExtensionTest {

    @Test
    public void testGeneral() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(1.0, "R1", "C1");
        dataset.addValue(2.0, "R1", "C2");
        dataset.addValue(3.0, "R1", "C3");
        CategoryDatasetSelectionExtension<String, String> ext = new 
                CategoryDatasetSelectionExtension<String, String>(dataset);
        CategoryCursor cursor = new CategoryCursor("R1", "C1");
        assertFalse(ext.isSelected(cursor));
        ext.setSelected(cursor, true);
        assertTrue(ext.isSelected(cursor));
        cursor.setPosition("R1", "C2");
        assertFalse(ext.isSelected(cursor));
        dataset.removeColumn("C1");
        assertFalse(ext.isSelected(cursor));

    }

}
