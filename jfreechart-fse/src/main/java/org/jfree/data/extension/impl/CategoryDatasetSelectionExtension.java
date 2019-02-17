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
 * --------------------------------------
 * CategoryDatasetSelectionExtension.java
 * --------------------------------------
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

package org.jfree.data.extension.impl;

import java.util.Iterator;

import org.jfree.data.DefaultKeyedValues2D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.extension.DatasetCursor;
import org.jfree.data.extension.DatasetIterator;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.IterableSelection;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.SelectionChangeListener;

/**
 * Extends a category dataset with a selection state for each data item.
 * 
 * @author zinsmaie
 */
public class CategoryDatasetSelectionExtension<ROW_KEY extends 
        Comparable<ROW_KEY>, COLUMN_KEY extends Comparable<COLUMN_KEY>>
        extends AbstractDatasetSelectionExtension<CategoryCursor<ROW_KEY, 
        COLUMN_KEY>, CategoryDataset> 
        implements IterableSelection<CategoryCursor<ROW_KEY, COLUMN_KEY>> {

    /** a generated serial id */
    private static final long serialVersionUID = 5138359490302459066L;

    /** 
     * private ref to the stored dataset to avoid casting same as 
     * ({@link AbstractDatasetSelectionExtension#dataset})
     */
    private CategoryDataset dataset;
    
    //could improve here by using own bool data structure
    
    /** storage for the selection attributes of the data items. */
    private DefaultKeyedValues2D selectionData;

    /** defines true as byte value */
    private final Number TRUE = new Byte((byte) 1);
    
    /** defines false as byte value */
    private final Number FALSE = new Byte((byte) 0);

    /**
     * Creates a separate selection extension for the specified dataset.
     * 
     * @param dataset  the underlying dataset (<code>null</code> not permitted).
     */
    public CategoryDatasetSelectionExtension(CategoryDataset dataset) {
        super(dataset);
        this.dataset = dataset;
        initSelection();
    }
    
    /**
     * Creates a separate selection extension for the specified dataset. And 
     * adds an initial selection change listener, e.g. a plot that should be 
     * redrawn on selection changes.
     * 
     * @param dataset
     * @param initialListener
     */
    public CategoryDatasetSelectionExtension(CategoryDataset dataset, 
            SelectionChangeListener<CategoryCursor<ROW_KEY, COLUMN_KEY>> 
                    initialListener) {
        super(dataset);
        addChangeListener(initialListener);
    }

    /**
     * Returns the selection status of the data item referenced by the 
     * supplied cursor.
     * 
     * @param cursor  the cursor (<code>null</code> not permitted).
     * 
     * @return The selection status.
     */
    public boolean isSelected(CategoryCursor<ROW_KEY, COLUMN_KEY> cursor) {
        return (TRUE == this.selectionData.getValue(cursor.rowKey, 
                cursor.columnKey));
    }

    /**
     * {@link DatasetSelectionExtension#setSelected(DatasetCursor, boolean)}
     */
    public void setSelected(CategoryCursor<ROW_KEY, COLUMN_KEY> cursor, 
            boolean selected) {
        if (selected) {
            selectionData.setValue(TRUE, cursor.rowKey, cursor.columnKey);
        } else {
            selectionData.setValue(FALSE, cursor.rowKey, cursor.columnKey);
        }
        notifyIfRequired();
    }

    /**
     * {@link DatasetSelectionExtension#clearSelection()}
     */
    public void clearSelection() {
        initSelection();
    }
    
    /**
     * Receives notification of a change to the underlying dataset, this is
     * handled by clearing the selection.
     * 
     * @param event  details of the change event.
     */
    public void datasetChanged(DatasetChangeEvent event) {
        // TODO : we could in fact try to preserve the selection state of
        // items that still remain in the dataset.
        initSelection();
    }
 
    /**
     * inits the selection attribute storage and sets all data items to 
     * unselected
     */
    private void initSelection() {
        this.selectionData = new DefaultKeyedValues2D();
        for (int i = 0; i < dataset.getRowCount(); i++) {
            for (int j= 0; j < dataset.getColumnCount(); j++) {
                if (dataset.getValue(i, j) != null) {
                    selectionData.addValue(FALSE, dataset.getRowKey(i), 
                            dataset.getColumnKey(j));
                }
            }
        }
        notifyIfRequired();
    }

    
    //ITERATOR
    
    /**
     * {@link IterableSelection#getIterator()}
     */
    public DatasetIterator<CategoryCursor<ROW_KEY, COLUMN_KEY>> getIterator() {
        return new CategoryDatasetSelectionIterator();
    }

    /**
     * {@link IterableSelection#getSelectionIterator(boolean)}
     */
    public DatasetIterator<CategoryCursor<ROW_KEY, COLUMN_KEY>> 
            getSelectionIterator(boolean selected) {
        return new CategoryDatasetSelectionIterator(selected);
    }

    /**
     * Allows to iterate over all data items or the selected / unselected data 
     * items.  Provides on each iteration step a DatasetCursor that defines the
     * position of the data item.
     * 
     * @author zinsmaie
     */
    private class CategoryDatasetSelectionIterator implements 
            DatasetIterator<CategoryCursor<ROW_KEY, COLUMN_KEY>> {

        // could be improved wtr speed by storing selected elements directly for 
        // faster access however storage efficiency would decrease

        /** a generated serial id */
        private static final long serialVersionUID = -6861323401482698708L;

        /** current row position */
        private int row = 0;
 
        /**
         * current column position initialized before the start of the 
         * dataset */
        private int column = -1;

        /** 
         * return all data item positions (null), only the selected (true) or 
         * only the unselected (false) 
         */
        private Boolean filter = null;
        
        /**
         * Creates an iterator over all data item positions
         */
        public CategoryDatasetSelectionIterator() {
        }
        
        /** 
         * Creates an iterator that iterates either over all selected or all 
         * unselected data item positions.
         * 
         * @param selected if true the iterator will iterate over the selected 
         *     data item positions
         */
        public CategoryDatasetSelectionIterator(boolean selected) {
            this.filter = Boolean.valueOf(selected);
        }

        /** 
         * {@link Iterator#hasNext() 
         */
        public boolean hasNext() {
            if (nextPosition()[0] != -1) {
                return true;
            }             
            return false;
        }

        /**
         * {@link Iterator#next()}
         */
        public CategoryCursor<ROW_KEY, COLUMN_KEY> next() {
            int[] newPos = nextPosition();
            row = newPos[0];
            column = newPos[1];
            // category datasets are not yet typed therefore the cast is 
            // necessary (and may fail)
            return new CategoryCursor<ROW_KEY, COLUMN_KEY>(
                    (ROW_KEY) dataset.getRowKey(row), 
                    (COLUMN_KEY) dataset.getColumnKey(column));
        }

        /**
         * Iterator remove operation is not supported.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        /**
         * Calculates the next position based on the current position
         * and the filter status.
         * 
         * @return an array holding the next position [row, column] 
         */
        private int[] nextPosition() {
            int pRow = this.row;
            int pColumn = this.column;
            while (pRow < dataset.getRowCount()) {
                if ((pColumn+1) >= selectionData.getColumnCount()) {
                    pRow++;
                    pColumn = -1;
                    continue; 
                }
                if (filter != null) {
                    if (!((filter.equals(Boolean.TRUE) && TRUE.equals(
                            selectionData.getValue(pRow, (pColumn+1)))) 
                            || (filter.equals(Boolean.FALSE) && FALSE.equals(
                            selectionData.getValue(pRow, (pColumn+1)))))) {
                        pColumn++;
                        continue;
                    }
                }
                
                //success
                return new int[]{pRow, (pColumn+1)};
            }
            
            return new int[]{-1,-1};
        }
    }

}
