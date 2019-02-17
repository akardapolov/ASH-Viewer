/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2009, by Object Refinery Limited and Contributors.
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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * -------------------------
 * AbstractMouseHandler.java
 * -------------------------
 * (C) Copyright 2009, by Object Refinery Limited.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * Changes:
 * --------
 * 18-Jun-2009 : Version 1 (DG);
 *
 */

package org.jfree.chart.panel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;

import org.jfree.chart.ChartPanel;

/**
 * A handler for mouse events in a {@link ChartPanel}.  A handler can be
 * assigned a modifier and installed on the panel to be invoked by the user.
 */
public abstract class AbstractMouseHandler implements MouseListener,
        MouseMotionListener, Serializable {

    /** a generated serial id. */
    private static final long serialVersionUID = -2717376020576340947L;

    /** The modifier used to invoke this handler. */
    private int modifier;

    /**
     * Default constructor.
     */
    public AbstractMouseHandler() {
        this.modifier = 0;
    }
    
    /**
     * Constructing with modifier.
     */
    public AbstractMouseHandler(int modifier) {
        this.modifier = modifier;
    }

    /**
     * Returns the modifier for this handler.
     *
     * @return The modifier.
     */
    public int getModifier() {
        return this.modifier;
    }

    /**
     * Sets the modifier for this handler.
     *
     * @param modifier  the modifier.
     */
    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    /**
     * Handle a mouse pressed event.  This implementation does nothing -
     * subclasses should override if necessary.
     *
     * @param e  the mouse event.
     */
    public void mousePressed(MouseEvent e) {
        // override to do something
    }

    /**
     * Handle a mouse released event.  This implementation does nothing -
     * subclasses should override if necessary.
     *
     * @param e  the mouse event.
     */
    public void mouseReleased(MouseEvent e) {
        // override if you need this method to do something
    }

    /**
     * Handle a mouse clicked event.  This implementation does nothing -
     * subclasses should override if necessary.
     *
     * @param e  the mouse event.
     */
    public void mouseClicked(MouseEvent e) {
        // override if you need this method to do something
    }

    /**
     * Handle a mouse entered event.  This implementation does nothing -
     * subclasses should override if necessary.
     *
     * @param e  the mouse event.
     */
    public void mouseEntered(MouseEvent e) {
        // override if you need this method to do something
    }

    /**
     * Handle a mouse moved event.  This implementation does nothing -
     * subclasses should override if necessary.
     *
     * @param e  the mouse event.
     */
    public void mouseMoved(MouseEvent e) {
        // override if you need this method to do something
    }

    /**
     * Handle a mouse exited event.  This implementation does nothing -
     * subclasses should override if necessary.
     *
     * @param e  the mouse event.
     */
    public void mouseExited(MouseEvent e) {
        // override if you need this method to do something
    }

    /**
     * Handle a mouse dragged event.  This implementation does nothing -
     * subclasses should override if necessary.
     *
     * @param e  the mouse event.
     */
    public void mouseDragged(MouseEvent e) {
        // override if you need this method to do something
    }

    /**
     * A mouse handler is either an live handler or an auxiliary handler. While
     * there can be only one active live handler at a time many auxiliary 
     * handlers can be used together.  E.g. rectangle zooming and rectangle 
     * selection should be implemented as live handlers because they both draw 
     * on the panel and shouldn't be combined. In contrast a click selection 
     * does not interfere with e.g. a double click selection both handlers can 
     * be active at the same time. 
     * 
     * @return true if this is a live handler
     */
    public abstract boolean isLiveHandler();
}
