/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2014, by Object Refinery Limited and Contributors.
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
 * ---------------------
 * WaferMapRenderer.java
 * ---------------------
 * (C) Copyright 2003-2014, by Robert Redburn and Contributors.
 *
 * Original Author:  Robert Redburn;
 * Contributor(s):   David Gilbert (for Object Refinery Limited);
 *
 * Changes
 * -------
 * 25-Nov-2003 : Version 1, contributed by Robert Redburn.  Changes have been
 *               made to fit the JFreeChart coding style (DG);
 * 20-Apr-2005 : Small update for changes to LegendItem class (DG);
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 02-Feb-2007 : Removed author tags from all over JFreeChart sources (DG);
 * 10-Mar-2014 : Removed LegendItemCollection (DG);
 * 
 */

package org.jfree.chart.renderer;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jfree.chart.LegendItem;
import org.jfree.chart.plot.DrawingSupplier;
import org.jfree.chart.plot.WaferMapPlot;
import org.jfree.data.general.WaferMapDataset;

/**
 * A renderer for wafer map plots.  Provides color management facilities.
 */
public class WaferMapRenderer extends AbstractRenderer {

    /** paint index */
    private Map<Number, Integer> paintIndex;

    /** plot */
    private WaferMapPlot plot;

    /** paint limit */
    private int paintLimit;

    /** default paint limit */
    private static final int DEFAULT_PAINT_LIMIT = 35;

    /** default multivalue paint calculation */
    public static final int POSITION_INDEX = 0;

    /** The default value index. */
    public static final int VALUE_INDEX = 1;

    /** paint index method */
    private int paintIndexMethod;

    /**
     * Creates a new renderer.
     */
    public WaferMapRenderer() {
        this(null, null);
    }

    /**
     * Creates a new renderer.
     *
     * @param paintLimit  the paint limit.
     * @param paintIndexMethod  the paint index method.
     */
    public WaferMapRenderer(Integer paintLimit, Integer paintIndexMethod) {

        super();
        this.paintIndex = new HashMap<Number, Integer>();

        if (paintLimit == null) {
            this.paintLimit = DEFAULT_PAINT_LIMIT;
        }
        else {
            this.paintLimit = paintLimit;
        }

        this.paintIndexMethod = VALUE_INDEX;
        if (paintIndexMethod != null) {
            if (isMethodValid(paintIndexMethod)) {
                this.paintIndexMethod = paintIndexMethod;
            }
        }
    }

    /**
     * Verifies that the passed paint index method is valid.
     *
     * @param method  the method.
     *
     * @return <code>true</code> or </code>false</code>.
     */
    private boolean isMethodValid(int method) {
        switch (method) {
            case POSITION_INDEX: return true;
            case VALUE_INDEX:    return true;
            default: return false;
        }
    }

    /**
     * Returns the drawing supplier from the plot.
     *
     * @return The drawing supplier.
     */
    @Override
    public DrawingSupplier getDrawingSupplier() {
        DrawingSupplier result = null;
        WaferMapPlot p = getPlot();
        if (p != null) {
            result = p.getDrawingSupplier();
        }
        return result;
    }

    /**
     * Returns the plot.
     *
     * @return The plot.
     */
    public WaferMapPlot getPlot() {
        return this.plot;
    }

    /**
     * Sets the plot and build the paint index.
     *
     * @param plot  the plot.
     */
    public void setPlot(WaferMapPlot plot) {
        this.plot = plot;
        makePaintIndex();
    }

    /**
     * Returns the paint for a given chip value.
     *
     * @param value  the value.
     *
     * @return The paint.
     */
    public Paint getChipColor(Number value) {
        return getSeriesPaint(getPaintIndex(value));
    }

    /**
     * Returns the paint index for a given chip value.
     *
     * @param value  the value.
     *
     * @return The paint index.
     */
    private int getPaintIndex(Number value) {
        return this.paintIndex.get(value);
    }

    /**
     * Builds a map of chip values to paint colors.
     * paintlimit is the maximum allowed number of colors.
     */
    private void makePaintIndex() {
        if (this.plot == null) {
            return;
        }
        WaferMapDataset data = this.plot.getDataset();
        Number dataMin = data.getMinValue();
        Number dataMax = data.getMaxValue();
        Set<Number> uniqueValues = data.getUniqueValues();
        if (uniqueValues.size() <= this.paintLimit) {
            int count = 0; // assign a color for each unique value
            for (Number uniqueValue : uniqueValues) {
                this.paintIndex.put(uniqueValue, count++);
            }
        }
        else {
            // more values than paints so map
            // multiple values to the same color
            switch (this.paintIndexMethod) {
                case POSITION_INDEX:
                    makePositionIndex(uniqueValues);
                    break;
                case VALUE_INDEX:
                    makeValueIndex(dataMax, dataMin, uniqueValues);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Builds the paintindex by assigning colors based on the number
     * of unique values: totalvalues/totalcolors.
     *
     * @param uniqueValues  the set of unique values.
     */
    private void makePositionIndex(Set<Number> uniqueValues) {
        int valuesPerColor = (int) Math.ceil(
            (double) uniqueValues.size() / this.paintLimit
        );
        int count = 0; // assign a color for each unique value
        int paint = 0;
        for (Number uniqueValue : uniqueValues) {
            this.paintIndex.put(uniqueValue, paint);
            if (++count % valuesPerColor == 0) {
                paint++;
            }
            if (paint > this.paintLimit) {
                paint = this.paintLimit;
            }
        }
    }

    /**
     * Builds the paintindex by assigning colors evenly across the range
     * of values:  maxValue-minValue/totalcolors
     *
     * @param max  the maximum value.
     * @param min  the minumum value.
     * @param uniqueValues  the unique values.
     */
    private void makeValueIndex(Number max, Number min, Set<Number> uniqueValues) {
        double valueRange = max.doubleValue() - min.doubleValue();
        double valueStep = valueRange / this.paintLimit;
        int paint = 0;
        double cutPoint = min.doubleValue() + valueStep;
        for (Number value : uniqueValues) {
            while (value.doubleValue() > cutPoint) {
                cutPoint += valueStep;
                paint++;
                if (paint > this.paintLimit) {
                    paint = this.paintLimit;
                }
            }
            this.paintIndex.put(value, paint);
        }
    }

    /**
     * Builds the list of legend entries.  called by getLegendItems in
     * WaferMapPlot to populate the plot legend.
     *
     * @return The legend items.
     */
    public List<LegendItem> getLegendCollection() {
        List<LegendItem> result = new ArrayList<LegendItem>();
        if (this.paintIndex != null && this.paintIndex.size() > 0) {
            if (this.paintIndex.size() <= this.paintLimit) {
                for (Map.Entry<Number, Integer> entry : this.paintIndex.entrySet()) {
                    // in this case, every color has a unique value
                    String label = entry.getKey().toString();
                    String description = label;
                    Shape shape = new Rectangle2D.Double(1d, 1d, 1d, 1d);
                    Paint paint = lookupSeriesPaint(
                            entry.getValue());
                    Paint outlinePaint = Color.BLACK;
                    Stroke outlineStroke = DEFAULT_STROKE;

                    result.add(new LegendItem(label, description, null,
                            null, shape, paint, outlineStroke, outlinePaint));

                }
            }
            else {
                // in this case, every color has a range of values
                Set<Integer> unique = new HashSet<Integer>();
                for (Map.Entry<Number, Integer> entry : this.paintIndex.entrySet()) {
                    if (unique.add(entry.getValue())) {
                        String label = getMinPaintValue(
                                entry.getValue()).toString()
                                + " - " + getMaxPaintValue(
                                entry.getValue()).toString();
                        String description = label;
                        Shape shape = new Rectangle2D.Double(1d, 1d, 1d, 1d);
                        Paint paint = getSeriesPaint(
                                entry.getValue()
                        );
                        Paint outlinePaint = Color.BLACK;
                        Stroke outlineStroke = DEFAULT_STROKE;

                        result.add(new LegendItem(label, description,
                                null, null, shape, paint, outlineStroke,
                                outlinePaint));
                    }
                } // end foreach map entry
            } // end else
        }
        return result;
    }

    /**
     * Returns the minimum chip value assigned to a color
     * in the paintIndex
     *
     * @param index  the index.
     *
     * @return The value.
     */
    private Number getMinPaintValue(Integer index) {
        double minValue = Double.POSITIVE_INFINITY;
        for (Map.Entry<Number, Integer> entry : this.paintIndex.entrySet()) {
            if (entry.getValue().equals(index)) {
                if (entry.getKey().doubleValue() < minValue) {
                    minValue = entry.getKey().doubleValue();
                }
            }
        }
        return minValue;
    }

    /**
     * Returns the maximum chip value assigned to a color
     * in the paintIndex
     *
     * @param index  the index.
     *
     * @return The value
     */
    private Number getMaxPaintValue(Integer index) {
        double maxValue = Double.NEGATIVE_INFINITY;
        for (Map.Entry<Number, Integer> entry : this.paintIndex.entrySet()) {
            if (entry.getValue().equals(index)) {
                if (entry.getKey().doubleValue() > maxValue) {
                    maxValue = entry.getKey().doubleValue();
                }
            }
        }
        return maxValue;
    }


} // end class wafermaprenderer
