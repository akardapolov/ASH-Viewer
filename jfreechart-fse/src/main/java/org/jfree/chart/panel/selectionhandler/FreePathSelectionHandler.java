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
 * -----------------------------
 * FreePathSelectionHandler.java
 * -----------------------------
 * (C) Copyright 2013, by Michael Zinsmaier and Contributors.
 *
 * Original Author:  Michael Zinsmaier;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * Changes:
 * --------
 * 17-Sep-2013 : Version 1 from MZ (DG);
 *
 */

package org.jfree.chart.panel.selectionhandler;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.util.ShapeUtilities;

/**
 * A mouse handler that allows data items to be selected. The selection path
 * can be freely manipulated by dragging the mouse away from the starting
 * point. This selection handler is intended for intersection based selection.
 * 
 * Will only work together with a ChartPanel as event source
 */
public class FreePathSelectionHandler extends RegionSelectionHandler {
    
    /** a generated serial id. */
    private static final long serialVersionUID = 8051992918085934799L;

    /**
     * temporary storage for points
     */
    private List<Point> points; 
    
    /**
     * The selection path (in Java2D space).
     */
    private GeneralPath selectionPath;

    /**
     * The start mouse point.
     */
    private Point2D lastPoint;

    /**
     * Creates a new default instance.
     */
    public FreePathSelectionHandler() {
        this.selectionPath = new GeneralPath();
        this.lastPoint = null;
        this.points = new ArrayList<Point>();
        setFillPaint(Color.GRAY);
        setOutlinePaint(new Color(0,160,230));
    }
    
    /**
     * Creates a new instance with a modifier restriction
     * @param modifier e.g. shift has to be pressed InputEvents.SHIFT_MASK
     */
    public FreePathSelectionHandler(int modifier) {
        super(modifier);
        this.selectionPath = new GeneralPath();
        this.lastPoint = null;
        this.points = new ArrayList<Point>();
        setFillPaint(Color.GRAY);
        setOutlinePaint(new Color(0,160,230));
    }

    /**
     * Creates a new selection handler with the specified attributes.
     * 
     * @param outlineStroke  the outline stroke.
     * @param outlinePaint  the outline paint.
     * @param fillPaint  the fill paint.
     */
    public FreePathSelectionHandler(Stroke outlineStroke, Paint outlinePaint,
            Paint fillPaint) {
        super(outlineStroke, outlinePaint, fillPaint);
        this.selectionPath = new GeneralPath();
        this.lastPoint = null;
        this.points = new ArrayList<Point>();
    }
    
    /**
     * starts the selection by fixing the start point of the selection path
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
                this.lastPoint = new Point(pt);
                this.points.add(pt);
            }
        }
    }

    /**
     * adjusts the selection by adding a new line segment from the last to the
     * actual position
     * 
     * @param e the event.
     */
    public void mouseDragged(MouseEvent e) {
        ChartPanel panel = (ChartPanel) e.getSource();
        if (this.lastPoint == null) {
            panel.clearLiveMouseHandler();
            return; // we never started a selection
        }
        Point pt = e.getPoint();
        Point2D pt2 = ShapeUtilities.getPointInRectangle(pt.x, pt.y,
                panel.getScreenDataArea());
        if (pt2.distance(this.lastPoint) > 5) {
            this.points.add(new Point((int)pt2.getX(), (int)pt2.getY()));
            this.lastPoint = pt2;
        }
        
        selectionPath = createPathFromPoints(this.points);
        
        panel.setSelectionShape(selectionPath);
        panel.setSelectionFillPaint(this.fillPaint);
        panel.setSelectionOutlinePaint(this.outlinePaint);
        panel.repaint();
    }

    /**
     * finishes the selection and calls the {@link SelectionManager} of
     * the event source. The SelectionManager is then responsible for the processing
     * of the geometric selection.
     */
    public void mouseReleased(MouseEvent e) {
        ChartPanel panel = (ChartPanel) e.getSource();
        if (this.lastPoint == null) {
            panel.clearLiveMouseHandler();
            return; // we never started a selection
        }
        
        this.selectionPath = createPathFromPoints(this.points);        
        this.selectionPath.closePath();
        SelectionManager selectionManager = panel.getSelectionManager();

        // do something with the selection shape
        if (selectionManager != null) {
            selectionManager.select(this.selectionPath);
        }

        panel.setSelectionShape(null);
        this.selectionPath.reset();
        this.points.clear();
        this.lastPoint = null;
        panel.repaint();
        panel.clearLiveMouseHandler();
    }

    /**
     * creates a line shape from a series of points 
     * 
     * @param points
     * @return the line shape
     */
    private GeneralPath createPathFromPoints(List<Point> points) {
        GeneralPath path = new GeneralPath();
        
        if (points.size() > 0) {
            Point p = points.get(0);
            path.moveTo(p.getX(), p.getY());
        }
        
        for (int i = 1; i < points.size(); i++) {
            Point p = points.get(i);
            path.lineTo((p.getX() - 1), (p.getY() + 1));
            path.lineTo((p.getX() + 1), (p.getY() + 1));
        }

        for (int i = (points.size() -1); i >= 0; i--) {
            Point p = points.get(i);
            path.lineTo((p.getX() + 1), (p.getY() - 1));
            path.lineTo((p.getX() - 1), (p.getY() - 1));
        }

        return path;
    }

}
