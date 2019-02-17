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
 * ---------------------------------
 * PieDatasetSelectionExtension.java
 * ---------------------------------
 * (C) Copyright 2013, by Michael Zinsmaier and Contributors.
 *
 * Original Author:  Michael Zinsmaier;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * Changes
 * -------
 * 17-Sep-2013 : Version 1 (MZ);
 *
 */

package org.jfree.data.extension.impl;

import java.util.HashMap;
import org.jfree.data.UnknownKeyException;

import org.jfree.data.extension.DatasetCursor;
import org.jfree.data.extension.DatasetIterator;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.IterableSelection;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.SelectionChangeListener;

/**
 * Extends a pie dataset with a selection state for each data item. 
 * 
 * @author zinsmaie
 */
public class PieDatasetSelectionExtension<KEY extends Comparable<KEY>>
       extends AbstractDatasetSelectionExtension<PieCursor<KEY>, PieDataset>
       implements IterableSelection<PieCursor<KEY>> {

    /** a generated serial id */
    private static final long serialVersionUID = -1735271052194147081L;

    /** 
     * private ref to the stored dataset to avoid casting same as 
     * ({@link AbstractDatasetSelectionExtension#dataset})
     */
    private PieDataset dataset;
    
    /** Storage for the selection attributes of the data items. */
    private HashMap<KEY, Boolean> selectionData;

    /**
     * Creates a separate selection extension for the specified dataset.
     * @param dataset
     */
    public PieDatasetSelectionExtension(PieDataset dataset) {
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
    public PieDatasetSelectionExtension(PieDataset dataset, 
            SelectionChangeListener initialListener) {
        super(dataset);
        addChangeListener(initialListener);
    }

    /**
     * {@link DatasetSelectionExtension#isSelected(DatasetCursor)}
     */
    public boolean isSelected(PieCursor<KEY> cursor) {
        Boolean b = this.selectionData.get(cursor.key);
        if (b == null) {
            throw new UnknownKeyException("Unrecognised key " + cursor.key);
        }
        return Boolean.TRUE.equals(b);
    }

    /**
     * {@link DatasetSelectionExtension#setSelected(DatasetCursor, boolean)}
     */
    public void setSelected(PieCursor<KEY> cursor, boolean selected) {
        this.selectionData.put(cursor.key, Boolean.valueOf(selected));
        notifyIfRequired();
    }

    /**
     * {@link DatasetSelectionExtension#clearSelection()}
     */
    public void clearSelection() {
        initSelection();
    }
    
    /**
     * A change of the underlying dataset clears the selection and reinitializes
     * it
     */
    public void datasetChanged(DatasetChangeEvent event) {
        initSelection();
    }
    
    /**
     * Inits the selection attribute storage and sets all data items to 
     * unselected.
     */
    private void initSelection() {
        this.selectionData = new HashMap<KEY, Boolean>();

        // pie datasets are not yet typed therefore the cast is necessary 
        // (and may fail)
        for (Comparable key : this.dataset.getKeys()) {
            this.selectionData.put((KEY) key, Boolean.FALSE);
        }

        notifyIfRequired();
    }
    
    
    //ITERATOR IMPLEMENTATION
    
    /**
     * {@link IterableSelection#getIterator()}
     */
    public DatasetIterator<PieCursor<KEY>> getIterator() {
        return new PieDatasetSelectionIterator();
    }

    /**
     * {@link IterableSelection#getSelectionIterator(boolean)}
     */
    public DatasetIterator<PieCursor<KEY>> getSelectionIterator(
            boolean selected) {
        return new PieDatasetSelectionIterator(selected);
    }

    
    /**
     * Allows to iterate over all data items or the selected / unselected data 
     * items.  Provides on each iteration step a DatasetCursor that defines 
     * the position of the data item.
     * 
     * @author zinsmaie
     */
    private class PieDatasetSelectionIterator 
            implements DatasetIterator<PieCursor<KEY>> {

        // could be improved wtr speed by storing selected elements directly for 
        // faster access however storage efficiency would decrease
        
        /** a generated serial id */
        private static final long serialVersionUID = -9037547822331524470L;
        
        /** current section initialized before the start of the dataset */
        private int section = -1;
        
        /** 
         * return all data item positions (null), only the selected (true) or 
         * only the unselected (false) 
         */
        private Boolean filter = null;
        
        /**
         * creates an iterator over all data item positions
         */
        public PieDatasetSelectionIterator() {
        }
        
        /** 
         * Creates an iterator that iterates either over all selected or all 
         * unselected data item positions.
         * 
         * @param selected if true the iterator will iterate over the selected 
         *     data item positions
         */
        public PieDatasetSelectionIterator(boolean selected) {
            filter = Boolean.valueOf(selected);
        }

        /** {@link Iterator#hasNext() */
        public boolean hasNext() {
            if (nextPosition() != -1) {
                return true;
            }             
            return false;
        }

        /**
         * {@link Iterator#next()}
         */
        public PieCursor<KEY> next() {
            this.section = nextPosition();
            //pie datasets are not yet typed therefore the cast is necessary 
            //(and may fail)
            KEY key = (KEY)dataset.getKey(this.section);
            return new PieCursor<KEY>(key);
        }

        /**
         * iterator remove operation is not supported
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        /**
         * Calculates the next position based on the current position
         * and the filter status.
         * @return an array holding the next position section 
         */
        private int nextPosition() {
            int pSection = this.section;
            while ((pSection+1) < dataset.getItemCount()) {
                if (filter != null) {
                    Comparable<?> key = dataset.getKey((pSection+1));
                    if (!(filter.equals(selectionData.get(key)))) {
                        pSection++;
                        continue;
                    }                
                }

                //success
                return (pSection+1);
            }

            return -1;
        }
    }
}
