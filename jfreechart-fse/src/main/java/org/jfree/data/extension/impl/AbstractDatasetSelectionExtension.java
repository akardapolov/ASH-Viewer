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
 * AbstractDatasetSelectionExtension.java
 * --------------------------------------
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

import javax.swing.event.EventListenerList;
import org.jfree.chart.util.ParamChecks;

import org.jfree.data.extension.DatasetCursor;
import org.jfree.data.extension.DatasetExtension;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.WithChangeListener;
import org.jfree.data.general.Dataset;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.SelectionChangeEvent;
import org.jfree.data.general.SelectionChangeListener;

/**
 * Base class for separate selection extension implementations. Provides 
 * notification handling and listener support. 
 *  
 * @author zinsmaie
 */
public abstract class AbstractDatasetSelectionExtension<CURSOR extends 
        DatasetCursor, DATASET extends Dataset> implements 
        DatasetSelectionExtension<CURSOR>, DatasetChangeListener {

    /** a generated serial id */
    private static final long serialVersionUID = 4206903652292146757L;

    /** Storage for registered listeners. */
    private transient EventListenerList listenerList = new EventListenerList();

    /** notify flag {@link #isNotify()} */
    private boolean notify;
    
    /** 
     * dirty flag true if changes occurred is used to trigger a queued change 
     * event if notify is reset to true.
     */
    private boolean dirty;
    
    /** reference to the extended dataset */
    private final DATASET dataset;
    
    /**
     * Creates a new instance.
     * 
     * @param dataset  the underlying dataset (<code>null</code> not permitted).
     */
    public AbstractDatasetSelectionExtension(DATASET dataset) {
        ParamChecks.nullNotPermitted(dataset, "dataset");
        this.dataset = dataset;
        this.dataset.addChangeListener(this);
    }
    
    /**
     * {@link DatasetExtension#getDataset}
     */
    public DATASET getDataset() {
        return this.dataset;
    }
    
    /** 
     * {@link DatasetSelectionExtension#addChangeListener(org.jfree.data.general.SelectionChangeListener)
     */
    public void addChangeListener(SelectionChangeListener<CURSOR> listener) {
        this.notify = true;
           this.listenerList.add(SelectionChangeListener.class, listener);
    }

    /**
     * {@link WithChangeListener#removeChangeListener(EventListener))
     */
    public void removeChangeListener(SelectionChangeListener<CURSOR> listener) {
           this.listenerList.remove(SelectionChangeListener.class, listener);
    }

    /**
     * {@link WithChangeListener#setNotify(boolean)}
     */
    public void setNotify(boolean notify) {
        if (this.notify != notify) {
            if (notify == false) {
                //switch notification temporary off
                this.dirty = false;
            } else {
                //switch notification on
                if (this.dirty == true) {
                    notifyListeners();    
                }
            }            
            this.notify = notify;
        }
   }
   
    /**
     * {@link WithChangeListener#isNotify()}
     */
   public boolean isNotify() {
       return this.notify;
   }
   
   /**
    * can be called by subclasses to trigger notify events depending on the
    * notify flag.
    */
   protected void notifyIfRequired() {
       if (this.notify) {
           notifyListeners();
       } else {
           this.dirty = true;
       }
   }
    
   /**
    * notifies all registered listeners 
    * @param event
    */
    @SuppressWarnings("unchecked") //add method accepts only SelectionChangeListeners<CURSOR>
    private void notifyListeners() {
        Object[] listeners = this.listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == SelectionChangeListener.class) {
                ((SelectionChangeListener<CURSOR>) listeners[i + 1])
                    .selectionChanged(new SelectionChangeEvent<CURSOR>(this));
            }
        }
   }
}
