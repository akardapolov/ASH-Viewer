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
 * -------------------------
 * SelectionChangeEvent.java
 * -------------------------
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

package org.jfree.data.general;

import java.util.EventObject;

import org.jfree.data.extension.DatasetCursor;
import org.jfree.data.extension.DatasetSelectionExtension;

/**
 * A change event that notifies about a change to the selection state of data 
 * items.
 */
public class SelectionChangeEvent<CURSOR extends DatasetCursor> 
        extends EventObject {
     
    /** a generated serial id*/
     private static final long serialVersionUID = 5217307402196957331L;

     /**
     * Constructs a new event. 
     *
     * @param source the source of the event aka the selection extension that 
     *     changed (the object has to be of type DatasetSelectionExtension and 
     * must not be null!)
     */
     public SelectionChangeEvent(Object selectionExtension) {
          super(selectionExtension);
     }

     /**
      * @return the selection selection extension that triggered the event.
      */
     public DatasetSelectionExtension<CURSOR> getSelectionExtension() {
        if (this.getSource() instanceof DatasetSelectionExtension) {
             return (DatasetSelectionExtension<CURSOR>)this.getSource();
        }
        
        //implementation error
        return null;
    }

}
