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
 * ---------------------------
 * EntitySelectionManager.java
 * ---------------------------
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

import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.DataItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.data.extension.DatasetCursor;
import org.jfree.data.extension.DatasetSelectionExtension;
import org.jfree.data.extension.impl.DatasetExtensionManager;
import org.jfree.data.general.Dataset;

/**
 * Selects data items based on the shape of their rendered entities and a given
 * point or region selection in the ChartPanel plane.<br>
 * <br>
 * region selection:<br>
 * Depending on the rendering mode {@link #intersectionMode} selects either all
 * entities that intersect the selection region or all entities that are
 * completely inside the selection region.<br>
 * <br>
 * point selection:<br>
 * Selects all entities that contain the selection point <br>
 * <br>
 * The entities are retrieved from the ChartPanel
 * 
 * @author zinsmaie
 */
public class EntitySelectionManager implements SelectionManager {

    /** a generated serial id. */
    private static final long serialVersionUID = -8963792184797912675L;
    
    /**
     * if true a entity has to intersect the selection region to be selected if
     * false a entity has to be completely inside the selection region to be
     * selected.
     */
    private boolean intersectionMode;
    
    /** the ChartPanel this manager is registered on */
    private final ChartPanel renderSourcePanel;

    /**
     * couples datasets and selection information of datasets that do not
     * support {@link DatasetSelectionExtension}
     */
    private final DatasetExtensionManager extensionManager;

    /** all datasets that are handled by the manager. */
    private final Dataset[] datasets;

    /**
     * Constructs a new selection manager. Use this constructor if all datasets
     * support {@link DatasetSelectionExtension}
     * 
     * @param renderSourcePanel {@link #renderSourcePanel}
     * @param datasets {@link #datasets}
     */
    public EntitySelectionManager(ChartPanel renderSourcePanel,
            Dataset[] datasets) {
        this.renderSourcePanel = renderSourcePanel;
        this.datasets = datasets;
        // initialize an extension manager without registered extensions
        this.extensionManager = new DatasetExtensionManager();
        this.intersectionMode = false;
    }

    /**
     * constructs a new selection manager and provides a extension manager. Use
     * this constructor if some of the used datasets do not support
     * {@link DatasetSelectionExtension}. These datasets can be coupled with
     * appropriate helper objects by registering them to the extension manager
     * before the constructor call.
     * 
     * @param renderSourcePanel {@link #renderSourcePanel}
     * @param datasets {@link #datasets}
     * @param extensionManager {@link #extensionManager}
     */
    public EntitySelectionManager(ChartPanel renderSourcePanel,
            Dataset[] datasets, DatasetExtensionManager extensionManager) {
        this.renderSourcePanel = renderSourcePanel;
        this.datasets = datasets;
        // set an extension manager that may manager helper objects to extend
        // old datasets
        this.extensionManager = extensionManager;
        this.intersectionMode = false;
    }

    /**
     * @param on  {@link #intersectionMode}
     */
    public void setIntersectionSelection(boolean on) {
        this.intersectionMode = on;
    }

    /**
     * {@link SelectionManager#select(double, double)} <br>
     * Selection based on the shape of the data items
     */
    public void select(double x, double y) {

        // scale if necessary
        double scaleX = this.renderSourcePanel.getScaleX();
        double scaleY = this.renderSourcePanel.getScaleY();
        if (scaleX != 1.0d || scaleY != 1.0d) {
            x = x / scaleX;
            y = y / scaleY;
        }

        if (this.renderSourcePanel.getChartRenderingInfo() != null) {
            EntityCollection entities = this.renderSourcePanel
                    .getChartRenderingInfo().getEntityCollection();

            for (ChartEntity ce : entities.getEntities()) {
                if (ce instanceof DataItemEntity) {
                    DataItemEntity e = (DataItemEntity) ce;

                    // simple check if the entity shape area contains the point
                    if (e.getArea().contains(new Point2D.Double(x, y))) {
                        select(e);
                    }
                }                
            }
        }
    }

    /**
     * 
     * {@link SelectionManager#select(Rectangle2D)} <br>
     * Selection based on the shape of the data items
     */
    public void select(Rectangle2D pSelection) {
        // scale if necessary
        Rectangle2D selection;
        double scaleX = this.renderSourcePanel.getScaleX();
        double scaleY = this.renderSourcePanel.getScaleY();
        if (scaleX != 1.0d || scaleY != 1.0d) {
            AffineTransform st = AffineTransform.getScaleInstance(1.0 / scaleX,
                    1.0 / scaleY);
            Area selectionArea = new Area(pSelection);
            selectionArea.transform(st);
            selection = selectionArea.getBounds2D();
        } else {
            selection = pSelection;
        }

        if (this.renderSourcePanel.getChartRenderingInfo() != null) {
            muteAll();
            {

                EntityCollection entities = this.renderSourcePanel
                        .getChartRenderingInfo().getEntityCollection();

                for (ChartEntity ce : entities.getEntities()) {

                    if (ce instanceof DataItemEntity) {

                        DataItemEntity e = (DataItemEntity) ce;
                        boolean match;

                        if (e.getArea() instanceof Rectangle2D) {
                            Rectangle2D entityRect = (Rectangle2D) e.getArea();
                            // use fast rectangle to rectangle test
                            if (this.intersectionMode) {
                                match = selection.intersects(entityRect);
                            } else {
                                match = selection.contains(entityRect);
                            }
                        } else {
                            // general shape test
                            Area selectionShape = new Area(selection);
                            Area entityShape = new Area(e.getArea());

                            // fast test if completely inside the solution must be true
                            if (selectionShape.contains(entityShape.getBounds())) {
                                match = true;
                            } else {
                                if (this.intersectionMode) {
                                    // test if the shapes intersect
                                    entityShape.intersect(selectionShape);
                                    match = !entityShape.isEmpty();
                                } else {
                                    // test if the entity shape is completely
                                    // covered by the selection
                                    entityShape.subtract(selectionShape);
                                    match = entityShape.isEmpty();
                                }
                            }
                        }

                        if (match) {
                            select(e);
                        }
                    }
                }
            }
            unmuteAndTrigger();
        }
    }

    /**
     * {@link SelectionManager#select(GeneralPath)} <br>
     * Selection based on the shape of the data items
     */
    public void select(GeneralPath pSelection) {
        // scale if necessary
        GeneralPath selection;
        double scaleX = this.renderSourcePanel.getScaleX();
        double scaleY = this.renderSourcePanel.getScaleY();
        if (scaleX != 1.0d || scaleY != 1.0d) {
            AffineTransform st = AffineTransform.getScaleInstance(1.0 / scaleX,
                    1.0 / scaleY);
            Area selectionArea = new Area(pSelection);
            selectionArea.transform(st);
            selection = new GeneralPath(selectionArea);
        } else {
            selection = pSelection;
        }

        if (this.renderSourcePanel.getChartRenderingInfo() != null) {
            muteAll();
            {

                EntityCollection entities = this.renderSourcePanel
                        .getChartRenderingInfo().getEntityCollection();

                for (ChartEntity ce : entities.getEntities()) {
                    if (ce instanceof DataItemEntity) {
                        DataItemEntity e = (DataItemEntity) ce;
                        Area selectionShape = new Area(selection);
                        Area entityShape = new Area(e.getArea());

                        // fast test if completely inside the solution must be true
                        if (selectionShape.contains(entityShape.getBounds())) {
                            select(e);
                        } else {                        
                            if (this.intersectionMode) {
                                // test if the shapes intersect
                                entityShape.intersect(selectionShape);
                                if (!entityShape.isEmpty()) {
                                    select(e);
                                }
                            } else {
                                // test if the entity shape is completely covered by
                                //the selection
                                entityShape.subtract(selectionShape);
                                if (entityShape.isEmpty()) {
                                    select(e);
                                }
                            }
                        }
                    }
                }
            }
            unmuteAndTrigger();
        }
    }

    /**
     * {@link SelectionManager#clearSelection()}
     */
    public void clearSelection() {
        for (int i = 0; i < this.datasets.length; i++) {
            if (this.extensionManager.supports(this.datasets[i],
                    DatasetSelectionExtension.class)) {
                DatasetSelectionExtension<?> selectionExtension 
                        = (DatasetSelectionExtension<?>) this.extensionManager
                            .getExtension(this.datasets[i],
                                DatasetSelectionExtension.class);

                selectionExtension.clearSelection();
            }
        }
    }

    /**
     * tests if the dataset is handled by the selection manager (part of
     * {@link #datasets}) and if it either supports
     * {@link DatasetSelectionExtension} directly or via the extension manager.<br>
     * <br>
     * The selects the specified data item.
     * 
     * @param e
     *            data item that should be selected
     */
    private void select(DataItemEntity e) {
        // to support propper clear functionality we must maintain
        // all datasets that we change!
        boolean handled = false;
        for (int i = 0; i < this.datasets.length; i++) {
            if (datasets[i].equals(e.getGeneralDataset())) {
                handled = true;
                break;
            }
        }

        if (handled) {
            if (this.extensionManager.supports(e.getGeneralDataset(),
                    DatasetSelectionExtension.class)) {

                //TODO a type save solution would be nice
                DatasetCursor cursor = e.getItemCursor();
                DatasetSelectionExtension selectionExtension 
                        = this.extensionManager.getExtension(
                            e.getGeneralDataset(), 
                            DatasetSelectionExtension.class);

                // work on the data
                selectionExtension.setSelected(cursor, true);
            }
        }
    }

    /**
     * mutes the selection change listener for all handled datasets (in
     * {@link #datasets} and supports {@link DatasetSelectionExtension}
     */
    private void muteAll() {
        setNotifyOnListenerExtensions(false);
    }

    /**
     * unmutes the selection change listener for all handled datasets (in
     * {@link #datasets} and supports {@link DatasetSelectionExtension}
     * 
     * unmute should trigger a selection changed event if something happened
     * since mute (but this is controlled by the implementing classes and can
     * only be assumed here)
     */
    private void unmuteAndTrigger() {
        setNotifyOnListenerExtensions(true);
    }

    /**
     * mutes / unmutes the selection change listener for all handled datasets
     * (in {@link #datasets} and supports {@link DatasetSelectionExtension}
     * 
     * unmute should trigger a selection changed event if something happened
     * since mute (but this is controlled by the implementing classes and can
     * only be assumed here)
     * 
     * @param notify
     *            false to mute true to unmute
     */
    private void setNotifyOnListenerExtensions(boolean notify) {
        for (int i = 0; i < this.datasets.length; i++) {
            if (this.extensionManager.supports(datasets[i],
                    DatasetSelectionExtension.class)) {
                DatasetSelectionExtension<?> selectionExtension 
                        = (DatasetSelectionExtension<?>) this.extensionManager
                        .getExtension(datasets[i],
                        DatasetSelectionExtension.class);

                selectionExtension.setNotify(notify);
            }
        }
    }

}
