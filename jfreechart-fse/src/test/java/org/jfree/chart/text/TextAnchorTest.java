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
 * ------------------------
 * CompositeTitleTests.java
 * ------------------------
 * (C) Copyright 2013, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 31-Jul-2013 : Version 1 (DG);
 *
 */

package org.jfree.chart.text;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.jfree.chart.ui.TextAnchor;
import org.junit.Test;

/**
 * Some tests for the {@link TextAnchor} enum.
 */
public class TextAnchorTest {
 
    @Test
    public void checkTOP_LEFT() {
        assertTrue(TextAnchor.TOP_LEFT.isTop());
        assertFalse(TextAnchor.TOP_LEFT.isHalfHeight());
        assertFalse(TextAnchor.TOP_LEFT.isHalfAscent());
        assertFalse(TextAnchor.TOP_LEFT.isBaseline());
        assertFalse(TextAnchor.TOP_LEFT.isBottom());
        
        assertTrue(TextAnchor.TOP_LEFT.isHorizontalLeft());
        assertFalse(TextAnchor.TOP_LEFT.isHorizontalCenter());
        assertFalse(TextAnchor.TOP_LEFT.isHorizontalRight());
    }

    @Test
    public void checkTOP_CENTER() {
        assertTrue(TextAnchor.TOP_CENTER.isTop());
        assertFalse(TextAnchor.TOP_CENTER.isHalfHeight());
        assertFalse(TextAnchor.TOP_CENTER.isHalfAscent());
        assertFalse(TextAnchor.TOP_CENTER.isBaseline());
        assertFalse(TextAnchor.TOP_CENTER.isBottom());
        
        assertFalse(TextAnchor.TOP_CENTER.isHorizontalLeft());
        assertTrue(TextAnchor.TOP_CENTER.isHorizontalCenter());
        assertFalse(TextAnchor.TOP_CENTER.isHorizontalRight());
    }

    @Test
    public void checkTOP_RIGHT() {
        assertTrue(TextAnchor.TOP_RIGHT.isTop());
        assertFalse(TextAnchor.TOP_RIGHT.isHalfHeight());
        assertFalse(TextAnchor.TOP_RIGHT.isHalfAscent());
        assertFalse(TextAnchor.TOP_RIGHT.isBaseline());
        assertFalse(TextAnchor.TOP_RIGHT.isBottom());
        
        assertFalse(TextAnchor.TOP_RIGHT.isHorizontalLeft());
        assertFalse(TextAnchor.TOP_RIGHT.isHorizontalCenter());
        assertTrue(TextAnchor.TOP_RIGHT.isHorizontalRight());
    }

    @Test
    public void checkHALF_ASCENT_LEFT() {
        assertFalse(TextAnchor.HALF_ASCENT_LEFT.isTop());
        assertFalse(TextAnchor.HALF_ASCENT_LEFT.isHalfHeight());
        assertTrue(TextAnchor.HALF_ASCENT_LEFT.isHalfAscent());
        assertFalse(TextAnchor.HALF_ASCENT_LEFT.isBaseline());
        assertFalse(TextAnchor.HALF_ASCENT_LEFT.isBottom());
        
        assertTrue(TextAnchor.HALF_ASCENT_LEFT.isHorizontalLeft());
        assertFalse(TextAnchor.HALF_ASCENT_LEFT.isHorizontalCenter());
        assertFalse(TextAnchor.HALF_ASCENT_LEFT.isHorizontalRight());
    }

    @Test
    public void checkHALF_ASCENT_CENTER() {
        assertFalse(TextAnchor.HALF_ASCENT_CENTER.isTop());
        assertFalse(TextAnchor.HALF_ASCENT_CENTER.isHalfHeight());
        assertTrue(TextAnchor.HALF_ASCENT_CENTER.isHalfAscent());
        assertFalse(TextAnchor.HALF_ASCENT_CENTER.isBaseline());
        assertFalse(TextAnchor.HALF_ASCENT_CENTER.isBottom());
        
        assertFalse(TextAnchor.HALF_ASCENT_CENTER.isHorizontalLeft());
        assertTrue(TextAnchor.HALF_ASCENT_CENTER.isHorizontalCenter());
        assertFalse(TextAnchor.HALF_ASCENT_CENTER.isHorizontalRight());
    }

    @Test
    public void checkHALF_ASCENT_RIGHT() {
        assertFalse(TextAnchor.HALF_ASCENT_RIGHT.isTop());
        assertFalse(TextAnchor.HALF_ASCENT_RIGHT.isHalfHeight());
        assertTrue(TextAnchor.HALF_ASCENT_RIGHT.isHalfAscent());
        assertFalse(TextAnchor.HALF_ASCENT_RIGHT.isBaseline());
        assertFalse(TextAnchor.HALF_ASCENT_RIGHT.isBottom());
        
        assertFalse(TextAnchor.HALF_ASCENT_RIGHT.isHorizontalLeft());
        assertFalse(TextAnchor.HALF_ASCENT_RIGHT.isHorizontalCenter());
        assertTrue(TextAnchor.HALF_ASCENT_RIGHT.isHorizontalRight());
    }

    @Test
    public void checkCENTER_LEFT() {
        assertFalse(TextAnchor.CENTER_LEFT.isTop());
        assertTrue(TextAnchor.CENTER_LEFT.isHalfHeight());
        assertFalse(TextAnchor.CENTER_LEFT.isHalfAscent());
        assertFalse(TextAnchor.CENTER_LEFT.isBaseline());
        assertFalse(TextAnchor.CENTER_LEFT.isBottom());
        
        assertTrue(TextAnchor.CENTER_LEFT.isHorizontalLeft());
        assertFalse(TextAnchor.CENTER_LEFT.isHorizontalCenter());
        assertFalse(TextAnchor.CENTER_LEFT.isHorizontalRight());
    }

    @Test
    public void checkCENTER() {
        assertFalse(TextAnchor.CENTER.isTop());
        assertTrue(TextAnchor.CENTER.isHalfHeight());
        assertFalse(TextAnchor.CENTER.isHalfAscent());
        assertFalse(TextAnchor.CENTER.isBaseline());
        assertFalse(TextAnchor.CENTER.isBottom());
        
        assertFalse(TextAnchor.CENTER.isHorizontalLeft());
        assertTrue(TextAnchor.CENTER.isHorizontalCenter());
        assertFalse(TextAnchor.CENTER.isHorizontalRight());
    }

    @Test
    public void checkCENTER_RIGHT() {
        assertFalse(TextAnchor.CENTER_RIGHT.isTop());
        assertTrue(TextAnchor.CENTER_RIGHT.isHalfHeight());
        assertFalse(TextAnchor.CENTER_RIGHT.isHalfAscent());
        assertFalse(TextAnchor.CENTER_RIGHT.isBaseline());
        assertFalse(TextAnchor.CENTER_RIGHT.isBottom());
        
        assertFalse(TextAnchor.CENTER_RIGHT.isHorizontalLeft());
        assertFalse(TextAnchor.CENTER_RIGHT.isHorizontalCenter());
        assertTrue(TextAnchor.CENTER_RIGHT.isHorizontalRight());
    }

    @Test
    public void checkBASELINE_LEFT() {
        assertFalse(TextAnchor.BASELINE_LEFT.isTop());
        assertFalse(TextAnchor.BASELINE_LEFT.isHalfHeight());
        assertFalse(TextAnchor.BASELINE_LEFT.isHalfAscent());
        assertTrue(TextAnchor.BASELINE_LEFT.isBaseline());
        assertFalse(TextAnchor.BASELINE_LEFT.isBottom());
        
        assertTrue(TextAnchor.BASELINE_LEFT.isHorizontalLeft());
        assertFalse(TextAnchor.BASELINE_LEFT.isHorizontalCenter());
        assertFalse(TextAnchor.BASELINE_LEFT.isHorizontalRight());
    }
    
    @Test
    public void checkBASELINE_CENTER() {
        assertFalse(TextAnchor.BASELINE_CENTER.isTop());
        assertFalse(TextAnchor.BASELINE_CENTER.isHalfHeight());
        assertFalse(TextAnchor.BASELINE_CENTER.isHalfAscent());
        assertTrue(TextAnchor.BASELINE_CENTER.isBaseline());
        assertFalse(TextAnchor.BASELINE_CENTER.isBottom());
        
        assertFalse(TextAnchor.BASELINE_CENTER.isHorizontalLeft());
        assertTrue(TextAnchor.BASELINE_CENTER.isHorizontalCenter());
        assertFalse(TextAnchor.BASELINE_CENTER.isHorizontalRight());
    }

    @Test
    public void checkBASELINE_RIGHT() {
        assertFalse(TextAnchor.BASELINE_RIGHT.isTop());
        assertFalse(TextAnchor.BASELINE_RIGHT.isHalfHeight());
        assertFalse(TextAnchor.BASELINE_RIGHT.isHalfAscent());
        assertTrue(TextAnchor.BASELINE_RIGHT.isBaseline());
        assertFalse(TextAnchor.BASELINE_RIGHT.isBottom());
        
        assertFalse(TextAnchor.BASELINE_RIGHT.isHorizontalLeft());
        assertFalse(TextAnchor.BASELINE_RIGHT.isHorizontalCenter());
        assertTrue(TextAnchor.BASELINE_RIGHT.isHorizontalRight());
    }
    

    @Test
    public void checkBOTTOM_LEFT() {
        assertFalse(TextAnchor.BOTTOM_LEFT.isTop());
        assertFalse(TextAnchor.BOTTOM_LEFT.isHalfHeight());
        assertFalse(TextAnchor.BOTTOM_LEFT.isHalfAscent());
        assertFalse(TextAnchor.BOTTOM_LEFT.isBaseline());
        assertTrue(TextAnchor.BOTTOM_LEFT.isBottom());
        
        assertTrue(TextAnchor.BOTTOM_LEFT.isHorizontalLeft());
        assertFalse(TextAnchor.BOTTOM_LEFT.isHorizontalCenter());
        assertFalse(TextAnchor.BOTTOM_LEFT.isHorizontalRight());
    }
    
    @Test
    public void checkBOTTOM_CENTER() {
        assertFalse(TextAnchor.BOTTOM_CENTER.isTop());
        assertFalse(TextAnchor.BOTTOM_CENTER.isHalfHeight());
        assertFalse(TextAnchor.BOTTOM_CENTER.isHalfAscent());
        assertFalse(TextAnchor.BOTTOM_CENTER.isBaseline());
        assertTrue(TextAnchor.BOTTOM_CENTER.isBottom());
        
        assertFalse(TextAnchor.BOTTOM_CENTER.isHorizontalLeft());
        assertTrue(TextAnchor.BOTTOM_CENTER.isHorizontalCenter());
        assertFalse(TextAnchor.BOTTOM_CENTER.isHorizontalRight());
    }

    @Test
    public void checkBOTTOM_RIGHT() {
        assertFalse(TextAnchor.BOTTOM_RIGHT.isTop());
        assertFalse(TextAnchor.BOTTOM_RIGHT.isHalfHeight());
        assertFalse(TextAnchor.BOTTOM_RIGHT.isHalfAscent());
        assertFalse(TextAnchor.BOTTOM_RIGHT.isBaseline());
        assertTrue(TextAnchor.BOTTOM_RIGHT.isBottom());
        
        assertFalse(TextAnchor.BOTTOM_RIGHT.isHorizontalLeft());
        assertFalse(TextAnchor.BOTTOM_RIGHT.isHorizontalCenter());
        assertTrue(TextAnchor.BOTTOM_RIGHT.isHorizontalRight());
    }
}
