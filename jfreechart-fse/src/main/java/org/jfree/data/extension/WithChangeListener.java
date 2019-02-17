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
 * -----------------------
 * WithChangeListener.java
 * -----------------------
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

package org.jfree.data.extension;

import java.util.EventListener;

import org.jfree.data.general.SelectionChangeEvent;

/**
 * DatasetExtensions that support change listeners can implement this interface.
 *  
 * @author zinsmaie
 */
public interface WithChangeListener<T extends EventListener> {

    /**
     * adds a change listener to the dataset extension<br>
     * <br>
     * The listener is triggered if if changes occur except 
     * the notify flag is set to false (@link #setNotify(boolean)).
     * In the latter case a change event should be triggered as soon as the 
     * notify flag is set to true again.
     *  
     * @param listener
     */
    public void addChangeListener(T listener);

    /**
     * removes a change listener from the dataset extension<br>
     *  
     * @param listener
     */
    public void removeChangeListener(T listener);
    
    /**
     * Sets a flag that controls whether or not listeners receive
     * {@link SelectionChangeEvent} notifications.
     *
     * @param notify If the flag is set to false the listeners are no longer 
     *     informed about changes.  If the flag is set to true and some changes
     *     occurred an event should be triggered. 
     */
    public void setNotify(boolean notify);
    
    /**
     * @return true if the notification flag is active 
     */
    public boolean isNotify();
    
}
