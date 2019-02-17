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
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 *
 * -------------------------------
 * MouseClickSelectionHandler.java
 * -------------------------------
 * (C) Copyright 2013, by Michael Zinsmaier and Contributors.
 *
 * Original Author:  Michael Zinsmaier;                 
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * Changes:
 * --------
 * 17-Sep-2013 : Version 1 (DG);
 *
 */

package org.jfree.chart.panel.selectionhandler;

import java.awt.event.MouseEvent;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.panel.AbstractMouseHandler;

/**
 * An auxiliary mouse handler that selects data items on click.
 *  
 * Will only work together with a ChartPanel as event source 
 *  
 * @author zinsmaie
 */
public class MouseClickSelectionHandler extends AbstractMouseHandler {
        
    /** a generated serial id. */
    private static final long serialVersionUID = 1101598509484156300L;

    /**
     * default constructor 
     */
    public MouseClickSelectionHandler() {
        super();
    }
    
    /**
     * Creates a new instance with a modifier restriction
     * @param modifier e.g. shift has to be pressed InputEvents.SHIFT_MASK
     */
    public MouseClickSelectionHandler(int modifier) {
        super(modifier);
    }

    /**
     * point wise selection
     * <br><br>
     * delegates to the {@link SelectionManager} of the ChartPanel, this
     * listener is paired with.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!(e.getSource() instanceof ChartPanel)) {
            return;
        }
        
        ChartPanel panel = (ChartPanel)e.getSource();
        SelectionManager selectionManager = panel.getSelectionManager();
        if (selectionManager != null) {
            if (!e.isShiftDown()) {
                selectionManager.clearSelection();
                panel.getChart().fireChartChanged();
            }
            selectionManager.select(e.getX(), e.getY());
            panel.getChart().fireChartChanged();
        }
    }

    /**
     * this is not a live handler
     */
    public boolean isLiveHandler() {
        return false;
    }
    
}
