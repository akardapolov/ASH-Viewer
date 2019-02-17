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
 * --------------------------------
 * XYDatasetSelectionExtension.java
 * --------------------------------
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

import org.jfree.data.extension.DatasetCursor;
import org.jfree.data.extension.DatasetIterator;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.IterableSelection;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.SelectionChangeListener;
import org.jfree.data.xy.XYDataset;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Extends a xy dataset with a selection state for each data item. 
 * 
 * @author zinsmaie
 */
public class XYDatasetSelectionExtension extends
        AbstractDatasetSelectionExtension<XYCursor, XYDataset> 
        implements IterableSelection<XYCursor> {

    /** A generated serial id. */
    private static final long serialVersionUID = 4859712483757720877L;

    /** private ref to the stored dataset to avoid casting same as ({@link AbstractDatasetSelectionExtension#dataset})*/
    private XYDataset dataset;
    
    /** 
     * Storage for the selection attributes of the data items (one list for 
     * each series). 
     */
    private ArrayList<Boolean>[] selectionData;
    
    /**
     * Creates a separate selection extension for the specified dataset.
     * 
     * @param dataset  the underlying dataset (<code>null</code> not permitted).
     */
    @SuppressWarnings("unchecked") //can't instantiate selection data with 
            //correct type (array limits)
    public XYDatasetSelectionExtension(XYDataset dataset) {
        super(dataset);
        this.dataset = dataset;
        //this.selectionData = new ArrayList[dataset.getSeriesCount()];
        this.selectionData = new ArrayList[50];
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
    public XYDatasetSelectionExtension(XYDataset dataset,
            SelectionChangeListener<XYCursor> initialListener) {
        super(dataset);
        addChangeListener(initialListener);
    }
    
    /**
     * A change of the underlying dataset clears the selection and 
     * reinitializes it.
     * 
     * @param event  the event details.
     */
    public void datasetChanged(DatasetChangeEvent event) {
        initSelection();
    }

    /**
     * {@link DatasetSelectionExtension#isSelected(DatasetCursor)}
     */
    public boolean isSelected(XYCursor cursor) {
        return (selectionData[cursor.series].get(cursor.item));
    }

    /**
     * {@link DatasetSelectionExtension#setSelected(DatasetCursor, boolean)}
     */
    public void setSelected(XYCursor cursor, boolean selected) {
        selectionData[cursor.series].set(cursor.item, 
                Boolean.valueOf(selected));
        notifyIfRequired();
    }

    /**
     * {@link DatasetSelectionExtension#clearSelection()}
     */
    public void clearSelection() {
        initSelection();
    }

    /**
     * inits the selection attribute storage and sets all data items to unselected
     */
    private void initSelection() {
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            selectionData[i] = new ArrayList<Boolean>(dataset.getItemCount(i));
            for (int j = 0; j < dataset.getItemCount(i); j++) {
                selectionData[i].add(Boolean.FALSE);
            }
        }
        notifyIfRequired();
    }

    // ITERATOR IMPLEMENTATION

    /**
     * {@link IterableSelection#getIterator()}
     */
    public DatasetIterator<XYCursor> getIterator() {
        return new XYDatasetSelectionIterator();
    }

    /**
     * {@link IterableSelection#getSelectionIterator(boolean)}
     */
    public DatasetIterator<XYCursor> getSelectionIterator(boolean selected) {
        return new XYDatasetSelectionIterator(selected);
    }

    /**
     * Allows to iterate over all data items or the selected / unselected data 
     * items.  Provides on each iteration step a DatasetCursor that defines the 
     * position of the data item.
     * 
     * @author zinsmaie
     */
    private class XYDatasetSelectionIterator 
            implements DatasetIterator<XYCursor> {
        
        // could be improved wtr speed by storing selected elements directly 
        // for faster access however storage efficiency would decrease
        
        /** A generated serial id. */
        private static final long serialVersionUID = 125607273863837608L;
        
        /** current series position */
        private int series = 0;
        
        /** current item position initialized before the start of the dataset */
        private int item = -1;
        
        /** 
         * return all data item positions (null), only the selected (true) or 
         * only the unselected (false) 
         */
        private Boolean filter = null;

        /**
         * Creates an iterator over all data item positions.
         */
        public XYDatasetSelectionIterator() {
        }

        /** 
         * Creates an iterator that iterates either over all selected or all 
         * unselected data item positions.
         * 
         * @param selected if true the iterator will iterate over the selected 
         *     data item positions
         */
        public XYDatasetSelectionIterator(boolean selected) {
            this.filter = Boolean.valueOf(selected);
        }

        /** {@link Iterator#hasNext() */
        public boolean hasNext() {
            if (nextPosition()[0] != -1) {
                return true;
            }
            return false;
        }

        /**
         * {@link Iterator#next()}
         */
        public XYCursor next() {
            int[] newPos = nextPosition();
            this.series = newPos[0];
            this.item = newPos[1];
            return new XYCursor(this.series, this.item);
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
         * @return an array holding the next position [series, item] 
         */
        private int[] nextPosition() {
            int pSeries = this.series;
            int pItem = this.item;
            
            while (pSeries < selectionData.length) {
                if ((pItem+1) >= selectionData[pSeries].size()) {
                    pSeries++;
                    pItem = -1;
                    continue;
                }                
                if (filter != null) {
                    if (!(filter.equals(selectionData[pSeries].get(pItem + 1)))) {
                        pItem++;
                        continue;
                    }
                }
                
                //success
                return new int[] {pSeries, (pItem+1)};
            }
            
            return new int[] {-1,-1};
        }
    }

}
