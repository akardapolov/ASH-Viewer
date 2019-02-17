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
 * -----------------
 * IRSUtilities.java
 * -----------------
 * (C) Copyright 2013, by Michael Zinsmaier.
 *
 * Original Author:  Michael Zinsmaier;
 * Contributor(s):   -;
 *
 * Changes
 * -------
 * 17-Sep-2013 : Version 1 (MZ);
 *
 */

package org.jfree.chart.renderer.item;

import java.awt.Paint;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.util.SerialUtilities;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.extension.DatasetCursor;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.impl.CategoryCursor;
import org.jfree.data.extension.impl.XYCursor;
import org.jfree.data.general.Dataset;
import org.jfree.data.xy.XYDataset;

/**
 * A helper class to define simple item rendering strategies.
 * 
 * Currently provides only support for paint manipulations based on the 
 * selection state of items.
 * 
 * @author zinsmaie
 */
public class IRSUtilities {

    private IRSUtilities() {
        //static helper class
    }

    //Reusable cursors for fast access
    private static DatasetCursor cursor;
    private final static XYCursor xyCursor = new XYCursor();
    //TODO a type save solution would be nice
    private final static CategoryCursor categoryCursor = new CategoryCursor();
    
    //Default methods for PaintIRS handling
    
    /**
     * A helper method that installs a {@link PaintIRS} on the specified 
     * renderer.
     * Highlights selected items by rendering them with the specified paint.
     * Works only if the renderer determines the outline paint 
     * {@link AbstractRenderer#getItemFillPaint(int, int) per item} 
     * 
     * @param renderer renders the dataset and is enhanced with the created IRS
     * @param ext access to the selection state the dataset items
     * @param fillPaint (might be null) defines a highlight color that should 
     *     be used for the interior of selected items
     * @param itemPaint (might be null) defines a highlight color that should 
     *     be used for selected items
     * @param outlinePaint (might be null) defines a highlight color that 
     *     should be used for the outline of selected items
     */
    public static void setSelectedItemPaintIRS(final AbstractRenderer renderer, 
            final DatasetSelectionExtension ext, final Paint fillPaint, 
            final Paint itemPaint, final Paint outlinePaint) {
        
        PaintIRS irs = new DefaultPaintIRS(renderer) {

            /** a generated serial id */
            private static final long serialVersionUID = -7838213904327581272L;
            
            private transient Paint m_fillPaint = fillPaint;
            private transient Paint m_itemPaint = itemPaint;
            private transient Paint m_outlinePaint = outlinePaint;

            private final Dataset dataset = ext.getDataset();
            
            @Override
            public Paint getItemPaint(int row, int column) {
                if (m_itemPaint == null || !isSelected(row, column)) {
                    return super.getItemPaint(row, column);
                } else {
                    return m_itemPaint;
                }
            }
            
            @Override
            public Paint getItemOutlinePaint(int row, int column) {
                if (m_outlinePaint == null || !isSelected(row, column)) {
                    return super.getItemPaint(row, column);
                } else {
                    return m_outlinePaint;
                }
            }
            
            @Override
            public Paint getItemFillPaint(int row, int column) {
                if (m_fillPaint == null || !isSelected(row, column)) {
                    return super.getItemPaint(row, column);
                } else {
                    return m_fillPaint;
                }
            }
            
            private boolean isSelected(int row, int column) {
                //note that pie plots aren't based on the abstract renderer
                //=> threat only xy and category
                
                if (this.dataset instanceof XYDataset) {
                    xyCursor.setPosition(row, column);
                    cursor = xyCursor;
                } else if (this.dataset instanceof CategoryDataset) {
                    CategoryDataset d = (CategoryDataset)dataset; 
                    categoryCursor.setPosition(d.getRowKey(row), 
                            d.getColumnKey(column));
                    cursor = categoryCursor;
                } else { 
                    return false;
                }

                return ext.isSelected(cursor);
            }
            
            
            /**
             * Provides serialization support.
             * 
             * @param stream  the output stream.
             * 
             * @throws IOException  if there is an I/O error.
             */
            private void writeObject(ObjectOutputStream stream) 
                    throws IOException {
                stream.defaultWriteObject();
                SerialUtilities.writePaint(m_outlinePaint, stream);
                SerialUtilities.writePaint(m_fillPaint, stream);
                SerialUtilities.writePaint(m_itemPaint, stream);
            }

            /**
             * Provides serialization support.
             * 
             * @param stream  the input stream.
             * 
             * @throws IOException if there is an I/O error.
             * @throws ClassNotFoundException if there is a classpath problem.
             */
            private void readObject(ObjectInputStream stream) 
                    throws IOException, ClassNotFoundException {
                stream.defaultReadObject();
                m_outlinePaint = SerialUtilities.readPaint(stream);
                m_fillPaint = SerialUtilities.readPaint(stream);
                m_itemPaint = SerialUtilities.readPaint(stream);
            }
            
        };
        
        //this is where the magic happens
        renderer.setPaintIRS(irs);
    }

    /**
     * A helper method that installs a {@link PaintIRS} on the specified 
     * renderer.  Highlights selected items by rendering their interior with 
     * the specified paint.  Works only if the renderer determines the outline
     * paint {@link AbstractRenderer#getItemFillPaint(int, int) per item} 
     * 
     * @param renderer renders the dataset and is enhanced with the created IRS
     * @param ext access to the selection state the dataset items
     * @param fillPaint defines a highlight color that should be used for the 
     *     interior of selected items
     */
    public static void setSelectedItemFillPaint(final AbstractRenderer renderer,
            final DatasetSelectionExtension<?> ext, final Paint fillPaint) {
        setSelectedItemPaintIRS(renderer, ext, fillPaint, null, null);
    }
    
    /**
     * A helper method that installs a {@link PaintIRS} on the specified 
     * renderer.  Highlights selected items by rendering them with the 
     * specified paint.  Works only if the renderer determines the item paint 
     * {@link AbstractRenderer#getItemPaint(int, int) per item} 
     * 
     * @param renderer renders the dataset and is enhanced with the created IRS
     * @param ext access to the selection state the dataset items
     * @param itemPaint defines a highlight color that should be used for 
     *     selected items
     */
    public static void setSelectedItemPaint(final AbstractRenderer renderer, 
            final DatasetSelectionExtension<?> ext, final Paint itemPaint) {
        setSelectedItemPaintIRS(renderer, ext, null, itemPaint, null);
    }
    
    /**
     * A helper method that installs a {@link PaintIRS} on the specified 
     * renderer.  Highlights selected items by rendering their outline with the
     * defined paint.  Works only if the renderer determines the outline paint 
     * {@link AbstractRenderer#getItemOutlinePaint(int, int) per item} 
     * 
     * @param renderer renders the dataset and is enhanced with the created IRS
     * @param ext access to the selection state the dataset items
     * @param outlinePaint defines a highlight color that should be used for 
     *     the outline of selected items
     */
    public static void setSelectedItemOutlinePaint(final AbstractRenderer 
            renderer, final DatasetSelectionExtension<?> ext, 
            final Paint outlinePaint) {
        setSelectedItemPaintIRS(renderer, ext, null, null, outlinePaint);
    }

}
