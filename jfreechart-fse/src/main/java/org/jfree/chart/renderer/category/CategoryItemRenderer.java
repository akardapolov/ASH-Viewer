/* ===========================================================
 * JFreeChart : a free chart library for the Java(tm) platform
 * ===========================================================
 *
 * (C) Copyright 2000-2012, by Object Refinery Limited and Contributors.
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
 * CategoryItemRenderer.java
 * -------------------------
 *
 * (C) Copyright 2001-2012, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   Mark Watson (www.markwatson.com);
 *
 * Changes:
 * For history prior to the release of JFreeChart 1.0.0 in December 2005,
 * please refer to the source files in the JFreeChart 1.0.x release.
 *
 * ------------- JFREECHART 1.0.x ---------------------------------------------
 * 20-Feb-2007 : Updated API docs (DG);
 * 19-Apr-2007 : Deprecated seriesVisible and seriesVisibleInLegend flags (DG);
 * 20-Apr-2007 : Deprecated paint, fillPaint, outlinePaint, stroke,
 *               outlineStroke, shape, itemLabelsVisible, itemLabelFont,
 *               itemLabelPaint, positiveItemLabelPosition,
 *               negativeItemLabelPosition and createEntities override
 *               fields (DG);
 * 26-Jun-2008 : Added new method required for crosshair support - THIS CHANGES
 *               THE API as of version 1.0.11 (DG);
 * 17-Jun-2012 : Removed JCommon dependencies (DG);
 *
 * ------------- JFREECHART Future State Edition (1.2.0?) ---------------------
 *               < under development >
 *
 */

package org.jfree.chart.renderer.category;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.labels.CategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryMarker;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.renderer.Renderer;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.Range;
import org.jfree.data.category.CategoryDataset;

/**
 * A plug-in object that is used by the {@link CategoryPlot} class to display
 * individual data items from a {@link CategoryDataset}.
 * <p>
 * This interface defines the methods that must be provided by all renderers.
 * If you are implementing a custom renderer, you should consider extending the
 * {@link AbstractCategoryItemRenderer} class.
 * <p>
 * Most renderer attributes are defined using a "three layer" approach.  When
 * looking up an attribute (for example, the outline paint) the renderer first
 * checks to see if there is a setting (in layer 0) that applies to ALL items
 * that the renderer draws.  If there is, that setting is used, but if it is
 * <code>null</code> the renderer looks up the next layer, which contains
 * "per series" settings for the attribute (many attributes are defined on a
 * per series basis, so this is the layer that is most commonly used).  If the
 * layer 1 setting is <code>null</code>, the renderer will look up the final
 * layer, which provides a default or "base" setting.  Some attributes allow
 * the base setting to be <code>null</code>, while other attributes enforce
 * non-<code>null</code> values.
 */
public interface CategoryItemRenderer extends Renderer {

  /**
   * Returns the plot that the renderer has been assigned to (where
   * <code>null</code> indicates that the renderer is not currently assigned
   * to a plot).
   *
   * @return The plot (possibly <code>null</code>).
   *
   * @see #setPlot(CategoryPlot)
   */
  public CategoryPlot getPlot();

  /**
   * Sets the plot that the renderer has been assigned to.  This method is
   * usually called by the {@link CategoryPlot}, in normal usage you
   * shouldn't need to call this method directly.
   *
   * @param plot  the plot (<code>null</code> not permitted).
   *
   * @see #getPlot()
   */
  public void setPlot(CategoryPlot plot);

  /**
   * Returns the range of values the renderer requires to display all the
   * items from the specified dataset.
   *
   * @param dataset  the dataset (<code>null</code> permitted).
   *
   * @return The range (or <code>null</code> if the dataset is
   *         <code>null</code> or empty).
   */
  public Range findRangeBounds(CategoryDataset dataset);

  // FIXME: LegendItemGenerator?


  // TOOL TIP GENERATOR

  /**
   * Returns the tool tip generator that should be used for the specified
   * item.  This method looks up the generator using the "three-layer"
   * approach outlined in the general description of this interface.
   *
   * @param row  the row index (zero-based).
   * @param column  the column index (zero-based).
   *
   * @return The generator (possibly <code>null</code>).
   */
  public CategoryToolTipGenerator getToolTipGenerator(int row, int column);

  /**
   * Returns the tool tip generator for the specified series (a "layer 1"
   * generator).
   *
   * @param series  the series index (zero-based).
   *
   * @return The tool tip generator (possibly <code>null</code>).
   *
   * @see #setSeriesToolTipGenerator(int, CategoryToolTipGenerator)
   */
  public CategoryToolTipGenerator getSeriesToolTipGenerator(int series);

  /**
   * Sets the tool tip generator for a series and sends a
   * {@link org.jfree.chart.event.RendererChangeEvent} to all registered
   * listeners.
   *
   * @param series  the series index (zero-based).
   * @param generator  the generator (<code>null</code> permitted).
   *
   * @see #getSeriesToolTipGenerator(int)
   */
  public void setSeriesToolTipGenerator(int series,
                      CategoryToolTipGenerator generator);

  public void setSeriesToolTipGenerator(int series,
      CategoryToolTipGenerator generator, boolean notify);

  /**
   * Returns the default tool tip generator.
   *
   * @return The tool tip generator (possibly <code>null</code>).
   *
   * @see #setDefaultToolTipGenerator(CategoryToolTipGenerator)
   */
  public CategoryToolTipGenerator getDefaultToolTipGenerator();

  /**
   * Sets the default tool tip generator and sends a
   * {@link org.jfree.chart.event.RendererChangeEvent} to all registered
   * listeners.
   *
   * @param generator  the generator (<code>null</code> permitted).
   *
   * @see #getDefaultToolTipGenerator()
   */
  public void setDefaultToolTipGenerator(CategoryToolTipGenerator generator);

  public void setDefaultToolTipGenerator(CategoryToolTipGenerator generator,
      boolean notify);


  // ITEM LABEL GENERATOR

  /**
   * Returns the item label generator for the specified data item.
   *
   * @param series  the series index (zero-based).
   * @param item  the item index (zero-based).
   *
   * @return The generator (possibly <code>null</code>).
   */
  public CategoryItemLabelGenerator getItemLabelGenerator(int series,
      int item);

  /**
   * Returns the item label generator for a series.
   *
   * @param series  the series index (zero-based).
   *
   * @return The label generator (possibly <code>null</code>).
   *
   * @see #setSeriesItemLabelGenerator(int, CategoryItemLabelGenerator)
   */
  public CategoryItemLabelGenerator getSeriesItemLabelGenerator(int series);

  /**
   * Sets the item label generator for a series and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param generator  the generator.
   *
   * @see #getSeriesItemLabelGenerator(int)
   */
  public void setSeriesItemLabelGenerator(int series,
      CategoryItemLabelGenerator generator);

  public void setSeriesItemLabelGenerator(int series,
      CategoryItemLabelGenerator generator, boolean notify);

  /**
   * Returns the default item label generator.
   *
   * @return The generator (possibly <code>null</code>).
   *
   * @see #setDefaultItemLabelGenerator(CategoryItemLabelGenerator)
   */
  public CategoryItemLabelGenerator getDefaultItemLabelGenerator();

  /**
   * Sets the default item label generator and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param generator  the generator (<code>null</code> permitted).
   *
   * @see #getDefaultItemLabelGenerator()
   */
  public void setDefaultItemLabelGenerator(CategoryItemLabelGenerator generator);

  public void setDefaultItemLabelGenerator(CategoryItemLabelGenerator generator,
      boolean notify);

  // ITEM URL GENERATOR

  /**
   * Returns the URL generator for an item.
   *
   * @param series  the series index (zero-based).
   * @param item  the item index (zero-based).
   *
   * @return The item URL generator.
   */
  public CategoryURLGenerator getItemURLGenerator(int series, int item);

  /**
   * Returns the item URL generator for a series.
   *
   * @param series  the series index (zero-based).
   *
   * @return The URL generator.
   *
   * @see #setSeriesItemURLGenerator(int, CategoryURLGenerator)
   */
  public CategoryURLGenerator getSeriesItemURLGenerator(int series);

  /**
   * Sets the item URL generator for a series.
   *
   * @param series  the series index (zero-based).
   * @param generator  the generator.
   *
   * @see #getSeriesItemURLGenerator(int)
   */
  public void setSeriesItemURLGenerator(int series,
                      CategoryURLGenerator generator);

  public void setSeriesItemURLGenerator(int series,
      CategoryURLGenerator generator, boolean notify);

  /**
   * Returns the default item URL generator.
   *
   * @return The item URL generator (possibly <code>null</code>).
   *
   * @see #setDefaultItemURLGenerator(CategoryURLGenerator)
   */
  public CategoryURLGenerator getDefaultItemURLGenerator();

  /**
   * Sets the default item URL generator and sends a {@link RendererChangeEvent}
   * to all registered listeners.
   *
   * @param generator  the item URL generator (<code>null</code> permitted).
   *
   * @see #getDefaultItemURLGenerator()
   */
  public void setDefaultItemURLGenerator(CategoryURLGenerator generator);

  public void setDefaultItemURLGenerator(CategoryURLGenerator generator,
      boolean notify);


  // FIXME: ANNOTATIONS


  //// DRAWING //////////////////////////////////////////////////////////////

  /**
   * Initialises the renderer.  This method will be called before the first
   * item is rendered, giving the renderer an opportunity to initialise any
   * state information it wants to maintain. The renderer can do nothing if
   * it chooses.
   *
   * @param g2  the graphics device.
   * @param dataArea  the area inside the axes.
   * @param plot  the plot.
   * @param rendererIndex  the renderer index.
   * @param info  collects chart rendering information for return to caller.
   *
   * @return A state object (maintains state information relevant to one
   *         chart drawing).
   */
  public CategoryItemRendererState initialise(Graphics2D g2,
      Rectangle2D dataArea, CategoryPlot plot, int rendererIndex,
      PlotRenderingInfo info);

  /**
   * Draws a background for the data area.
   *
   * @param g2  the graphics device.
   * @param plot  the plot.
   * @param dataArea  the data area.
   */
  public void drawBackground(Graphics2D g2, CategoryPlot plot,
      Rectangle2D dataArea);

  /**
   * Draws an outline for the data area.
   *
   * @param g2  the graphics device.
   * @param plot  the plot.
   * @param dataArea  the data area.
   */
  public void drawOutline(Graphics2D g2, CategoryPlot plot,
      Rectangle2D dataArea);

  /**
   * Draws a single data item.
   *
   * @param g2  the graphics device.
   * @param state  state information for one chart.
   * @param dataArea  the data plot area.
   * @param plot  the plot.
   * @param domainAxis  the domain axis.
   * @param rangeAxis  the range axis.
   * @param dataset  the data.
   * @param row  the row index (zero-based).
   * @param column  the column index (zero-based).
   * @param pass  the pass index.
   */
  public void drawItem(Graphics2D g2, CategoryItemRendererState state,
      Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis,
      ValueAxis rangeAxis, CategoryDataset dataset, int row, int column,
      int pass);

  /**
   * Draws a grid line against the domain axis.
   *
   * @param g2  the graphics device.
   * @param plot  the plot.
   * @param dataArea  the area for plotting data (not yet adjusted for any
   *                 3D effect).
   * @param value  the value.
   *
   * @see #drawRangeGridline(Graphics2D, CategoryPlot, ValueAxis,
   *   Rectangle2D, double)
   */
  public void drawDomainGridline(Graphics2D g2, CategoryPlot plot,
      Rectangle2D dataArea, double value);

  /**
   * Draws a grid line against the range axis.
   *
   * @param g2  the graphics device.
   * @param plot  the plot.
   * @param axis  the value axis.
   * @param dataArea  the area for plotting data (not yet adjusted for any
   *                  3D effect).
   * @param value  the value.
   *
   * @see #drawDomainGridline(Graphics2D, CategoryPlot, Rectangle2D, double)
   */
  public void drawRangeGridline(Graphics2D g2, CategoryPlot plot,
      ValueAxis axis, Rectangle2D dataArea, double value);

  /**
   * Draws a line (or some other marker) to indicate a particular category on
   * the domain axis.
   *
   * @param g2  the graphics device.
   * @param plot  the plot.
   * @param axis  the category axis.
   * @param marker  the marker.
   * @param dataArea  the area for plotting data (not including 3D effect).
   *
   * @see #drawRangeMarker(Graphics2D, CategoryPlot, ValueAxis, Marker,
   *   Rectangle2D)
   */
  public void drawDomainMarker(Graphics2D g2, CategoryPlot plot,
      CategoryAxis axis, CategoryMarker marker, Rectangle2D dataArea);

  /**
   * Draws a line (or some other marker) to indicate a particular value on
   * the range axis.
   *
   * @param g2  the graphics device.
   * @param plot  the plot.
   * @param axis  the value axis.
   * @param marker  the marker.
   * @param dataArea  the area for plotting data (not including 3D effect).
   *
   * @see #drawDomainMarker(Graphics2D, CategoryPlot, CategoryAxis,
   *   CategoryMarker, Rectangle2D)
   */
  public void drawRangeMarker(Graphics2D g2, CategoryPlot plot,
      ValueAxis axis, Marker marker, Rectangle2D dataArea);

  /**
   * Returns the Java2D coordinate for the middle of the specified data item.
   *
   * @param rowKey  the row key.
   * @param columnKey  the column key.
   * @param dataset  the dataset.
   * @param axis  the axis.
   * @param area  the data area.
   * @param edge  the edge along which the axis lies.
   *
   * @return The Java2D coordinate for the middle of the item.
   *
   * @since 1.0.11
   */
  public double getItemMiddle(Comparable rowKey, Comparable columnKey,
      CategoryDataset dataset, CategoryAxis axis, Rectangle2D area,
      RectangleEdge edge);

}
