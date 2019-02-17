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
 * -----------------------------------
 * CircularRegionSelectionHandler.java
 * -----------------------------------
 * (C) Copyright 2013, by Michael Zinsmaier and Contributors.
 *
 * Original Author:  Michael Zinsmaier;                 
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * Changes:
 * --------
 * 17-Sep-2013 : Version 1 from MZ (DG);
 * 18-Sep-2013 : Allow circle to grow beyound plot bounds, but crop the
 *               selection region (DG);
 *
 */

package org.jfree.chart.panel.selectionhandler;

import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartPanel;

/**
 * A mouse handler that allows data items to be selected. The selection shape
 * is a circle that can be expanded by dragging the mouse away from the starting
 * point. Will only work together with a <code>ChartPanel</code> as the event 
 * source.
 * 
 * @author zinsmaie
 */
public class CircularRegionSelectionHandler extends RegionSelectionHandler {

    /** A generated serial id. */
    private static final long serialVersionUID = 5627320657050195767L;

    /**
     * The selection path (in Java2D space).
     */
    private Ellipse2D selectionCircle;

    /**
     * The start mouse point.
     */
    private Point2D startPoint;

    /**
     * Creates a new default instance.
     */
    public CircularRegionSelectionHandler() {
        super();
        this.selectionCircle = null;
        this.startPoint = null;
    }
    
    /**
     * Creates a new instance with a modifier restriction.
     * 
     * @param modifier  the modifier (for example, shift has to be pressed 
     *     InputEvents.SHIFT_MASK).
     */
    public CircularRegionSelectionHandler(int modifier) {
        super(modifier);
        this.selectionCircle = null;
        this.startPoint = null;
    }

    /**
     * Creates a new selection handler with the specified attributes.
     * 
     * @param outlineStroke  the outline stroke.
     * @param outlinePaint  the outline paint.
     * @param fillPaint  the fill paint.
     */
    public CircularRegionSelectionHandler(Stroke outlineStroke,
            Paint outlinePaint, Paint fillPaint) {
        super(outlineStroke, outlinePaint, fillPaint);
        this.selectionCircle = null;
        this.startPoint = null;
    }

    /**
     * Starts the circle selection by fixing the center of the circle.
     * 
     * @param e the event.
     */
    public void mousePressed(MouseEvent e) {
        if (!(e.getSource() instanceof ChartPanel)) {
            return;
        }
        ChartPanel panel = (ChartPanel) e.getSource();
        Rectangle2D dataArea = panel.getScreenDataArea();
        if (dataArea.contains(e.getPoint())) {
            SelectionManager selectionManager = panel.getSelectionManager();
            if (selectionManager != null) {
                if (!e.isShiftDown()) {
                    selectionManager.clearSelection();
                }
                Point pt = e.getPoint();
                this.startPoint = new Point(pt);
                this.selectionCircle = new Ellipse2D.Double(pt.x, pt.y, 1, 1);
            }
        }
    }

    /**
     * Adjusts the radius of the circle according to the actual mouse position.
     * 
     * @param e the event.
     */
    public void mouseDragged(MouseEvent e) {
        ChartPanel panel = (ChartPanel) e.getSource();
        if (this.startPoint == null) {
            panel.clearLiveMouseHandler();
            return; // we never started a selection
        }
        Point pt2 = e.getPoint();
        double r = this.startPoint.distance(pt2);

        if (r <= 0) {
            r = 1.0;
        }

        this.selectionCircle = new Ellipse2D.Double(this.startPoint.getX() - r,
                this.startPoint.getY() - r, 2.0 * r, 2.0 * r);
        Area selectionArea = new Area(this.selectionCircle);
        selectionArea.intersect(new Area(panel.getScreenDataArea()));
        panel.setSelectionShape(selectionArea);
        panel.setSelectionFillPaint(this.fillPaint);
        panel.setSelectionOutlinePaint(this.outlinePaint);
        panel.setSelectionOutlineStroke(this.outlineStroke);
        panel.repaint();
    }

    /**
     * Finishes the selection and calls the {@link SelectionManager} of
     * the event source. The SelectionManager is then responsible for the 
     * processing of the geometric selection.
     * 
     * @param event  the event.
     */
    public void mouseReleased(MouseEvent e) {
        ChartPanel panel = (ChartPanel) e.getSource();
        if (this.startPoint == null) {
            panel.clearLiveMouseHandler();
            return; // we never started a selection
        }
        SelectionManager selectionManager = panel.getSelectionManager();

        // do something with the selection shape
        if (selectionManager != null) {
            selectionManager.select(new GeneralPath(this.selectionCircle));
        }

        panel.setSelectionShape(null);
        this.selectionCircle = null;
        this.startPoint = null;
        panel.repaint();
        panel.clearLiveMouseHandler();
    }

}
