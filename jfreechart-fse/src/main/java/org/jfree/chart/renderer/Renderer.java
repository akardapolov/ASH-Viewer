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
 * -------------
 * Renderer.java
 * -------------
 *
 * (C) Copyright 2012, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * ------------- JFREECHART Future State Edition (1.2.0?) ---------------------
 *               < under development > 
 * 
 */

package org.jfree.chart.renderer;

import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.event.RendererChangeEvent;
import org.jfree.chart.event.RendererChangeListener;
import org.jfree.chart.labels.ItemLabelPosition;

/**
 * A base interface for renderers.
 */
public interface Renderer extends LegendItemSource {

  /**
   * Returns the number of passes through the dataset required by the
   * renderer.  Usually this will be one, but some renderers may use
   * a second or third pass to overlay items on top of things that were
   * drawn in an earlier pass.
   *
   * @return The pass count.
   */
  public int getPassCount();

  /**
   * Add a renderer change listener.
   *
   * @param listener  the listener.
   *
   * @see #removeChangeListener(RendererChangeListener)
   */
  public void addChangeListener(RendererChangeListener listener);

  /**
   * Removes a change listener.
   *
   * @param listener  the listener.
   *
   * @see #addChangeListener(RendererChangeListener)
   */
  public void removeChangeListener(RendererChangeListener listener);

  //// VISIBLE //////////////////////////////////////////////////////////////

  /**
   * Returns a boolean that indicates whether or not the specified item
   * should be drawn.
   *
   * @param series  the series index.
   * @param item  the item index.
   *
   * @return A boolean.
   */
  public boolean getItemVisible(int series, int item);

  /**
   * Returns a boolean that indicates whether or not the specified series
   * should be drawn.
   *
   * @param series  the series index.
   *
   * @return A boolean.
   */
  public boolean isSeriesVisible(int series);

  /**
   * Returns the flag that controls whether a series is visible.
   *
   * @param series  the series index (zero-based).
   *
   * @return The flag (possibly <code>null</code>).
   *
   * @see #setSeriesVisible(int, Boolean)
   */
  public Boolean getSeriesVisible(int series);

  /**
   * Sets the flag that controls whether a series is visible and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param visible  the flag (<code>null</code> permitted).
   *
   * @see #getSeriesVisible(int)
   */
  public void setSeriesVisible(int series, Boolean visible);

  /**
   * Sets the flag that controls whether a series is visible and, if
   * requested, sends a {@link RendererChangeEvent} to all registered
   * listeners.
   *
   * @param series  the series index.
   * @param visible  the flag (<code>null</code> permitted).
   * @param notify  notify listeners?
   *
   * @see #getSeriesVisible(int)
   */
  public void setSeriesVisible(int series, Boolean visible, boolean notify);

  /**
   * Returns the default visibility for all series.
   *
   * @return The default visibility.
   *
   * @see #setDefaultSeriesVisible(boolean)
   */
  public boolean getDefaultSeriesVisible();

  /**
   * Sets the default visibility and sends a {@link RendererChangeEvent} to all
   * registered listeners.
   *
   * @param visible  the flag.
   *
   * @see #getDefaultSeriesVisible()
   */
  public void setDefaultSeriesVisible(boolean visible);

  /**
   * Sets the base visibility and, if requested, sends
   * a {@link RendererChangeEvent} to all registered listeners.
   *
   * @param visible  the visibility.
   * @param notify  notify listeners?
   *
   * @see #getDefaultSeriesVisible()
   */
  public void setDefaultSeriesVisible(boolean visible, boolean notify);  

  
  // SERIES VISIBLE IN LEGEND (not yet respected by all renderers)

  /**
   * Returns <code>true</code> if the series should be shown in the legend,
   * and <code>false</code> otherwise.
   *
   * @param series  the series index.
   *
   * @return A boolean.
   */
  public boolean isSeriesVisibleInLegend(int series);

  /**
   * Returns the flag that controls whether a series is visible in the
   * legend.  This method returns only the "per series" settings - to
   * incorporate the override and base settings as well, you need to use the
   * {@link #isSeriesVisibleInLegend(int)} method.
   *
   * @param series  the series index (zero-based).
   *
   * @return The flag (possibly <code>null</code>).
   *
   * @see #setSeriesVisibleInLegend(int, Boolean)
   */
  public Boolean getSeriesVisibleInLegend(int series);

  /**
   * Sets the flag that controls whether a series is visible in the legend
   * and sends a {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param visible  the flag (<code>null</code> permitted).
   *
   * @see #getSeriesVisibleInLegend(int)
   */
  public void setSeriesVisibleInLegend(int series, Boolean visible);

  /**
   * Sets the flag that controls whether a series is visible in the legend
   * and, if requested, sends a {@link RendererChangeEvent} to all registered
   * listeners.
   *
   * @param series  the series index.
   * @param visible  the flag (<code>null</code> permitted).
   * @param notify  notify listeners?
   *
   * @see #getSeriesVisibleInLegend(int)
   */
  public void setSeriesVisibleInLegend(int series, Boolean visible,
                                       boolean notify);

  /**
   * Returns the default visibility in the legend for all series.
   *
   * @return The base visibility.
   *
   * @see #setDefaultSeriesVisibleInLegend(boolean)
   */
  public boolean getDefaultSeriesVisibleInLegend();

  /**
   * Sets the default visibility in the legend and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param visible  the flag.
   *
   * @see #getDefaultSeriesVisibleInLegend()
   */
  public void setDefaultSeriesVisibleInLegend(boolean visible);

  /**
   * Sets the default visibility in the legend and, if requested, sends
   * a {@link RendererChangeEvent} to all registered listeners.
   *
   * @param visible  the visibility.
   * @param notify  notify listeners?
   *
   * @see #getDefaultSeriesVisibleInLegend()
   */
  public void setDefaultSeriesVisibleInLegend(boolean visible, boolean notify);


  //// PAINT /////////////////////////////////////////////////////////////////

  /**
   * Returns the paint used to fill data items as they are drawn.
   *
   * @param row  the row (or series) index (zero-based).
   * @param column  the column (or category) index (zero-based).
   *
   * @return The paint (never <code>null</code>).
   */
  public Paint getItemPaint(int row, int column);

  /**
   * Returns the paint used to fill an item drawn by the renderer.
   *
   * @param series  the series index (zero-based).
   *
   * @return The paint (possibly <code>null</code>).
   *
   * @see #setSeriesPaint(int, Paint)
   */
  public Paint getSeriesPaint(int series);

  /**
   * Sets the paint used for a series and sends a {@link RendererChangeEvent}
   * to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param paint  the paint (<code>null</code> permitted).
   *
   * @see #getSeriesPaint(int)
   */
  public void setSeriesPaint(int series, Paint paint);

  public void setSeriesPaint(int series, Paint paint, boolean notify);

  /**
   * Returns the default paint.
   *
   * @return The default paint (never <code>null</code>).
   *
   * @see #setDefaultPaint(Paint)
   */
  public Paint getDefaultPaint();

  /**
   * Sets the default paint and sends a {@link RendererChangeEvent} to all
   * registered listeners.
   *
   * @param paint  the paint (<code>null</code> not permitted).
   *
   * @see #getDefaultPaint()
   */
  public void setDefaultPaint(Paint paint);

  public void setDefaultPaint(Paint paint, boolean notify);


  //// FILL PAINT /////////////////////////////////////////////////////////

  /**
   * Returns the paint used to fill data items as they are drawn.
   *
   * @param row  the row (or series) index (zero-based).
   * @param column  the column (or category) index (zero-based).
   *
   * @return The paint (never <code>null</code>).
   */
  public Paint getItemFillPaint(int row, int column);

  /**
   * Returns the paint used to fill an item drawn by the renderer.
   *
   * @param series  the series (zero-based index).
   *
   * @return The paint (possibly <code>null</code>).
   *
   * @see #setSeriesFillPaint(int, Paint)
   */
  public Paint getSeriesFillPaint(int series);

  /**
   * Sets the paint used for a series outline and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param paint  the paint (<code>null</code> permitted).
   *
   * @see #getSeriesFillPaint(int)
   */
  public void setSeriesFillPaint(int series, Paint paint);

  public void setSeriesFillPaint(int series, Paint paint, boolean notify);

  /**
   * Returns the default fill paint.
   *
   * @return The paint (never <code>null</code>).
   *
   * @see #setDefaultFillPaint(Paint)
   */
  public Paint getDefaultFillPaint();

  /**
   * Sets the default fill paint and sends a {@link RendererChangeEvent} to
   * all registered listeners.
   *
   * @param paint  the paint (<code>null</code> not permitted).
   *
   * @see #getDefaultFillPaint()
   */
  public void setDefaultFillPaint(Paint paint);

  public void setDefaultFillPaint(Paint paint, boolean notify);

    
  //// OUTLINE PAINT /////////////////////////////////////////////////////////

  /**
   * Returns the paint used to outline data items as they are drawn.
   *
   * @param row  the row (or series) index (zero-based).
   * @param column  the column (or category) index (zero-based).
   *
   * @return The paint (never <code>null</code>).
   */
  public Paint getItemOutlinePaint(int row, int column);

  /**
   * Returns the paint used to outline an item drawn by the renderer.
   *
   * @param series  the series (zero-based index).
   *
   * @return The paint (possibly <code>null</code>).
   *
   * @see #setSeriesOutlinePaint(int, Paint)
   */
  public Paint getSeriesOutlinePaint(int series);

  /**
   * Sets the paint used for a series outline and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param paint  the paint (<code>null</code> permitted).
   *
   * @see #getSeriesOutlinePaint(int)
   */
  public void setSeriesOutlinePaint(int series, Paint paint);

  public void setSeriesOutlinePaint(int series, Paint paint, boolean notify);

  /**
   * Returns the default outline paint.
   *
   * @return The paint (never <code>null</code>).
   *
   * @see #setDefaultOutlinePaint(Paint)
   */
  public Paint getDefaultOutlinePaint();

  /**
   * Sets the default outline paint and sends a {@link RendererChangeEvent} to
   * all registered listeners.
   *
   * @param paint  the paint (<code>null</code> not permitted).
   *
   * @see #getDefaultOutlinePaint()
   */
  public void setDefaultOutlinePaint(Paint paint);

  public void setDefaultOutlinePaint(Paint paint, boolean notify);

    
  //// STROKE ////////////////////////////////////////////////////////////////

  /**
   * Returns the stroke used to draw data items.
   *
   * @param row  the row (or series) index (zero-based).
   * @param column  the column (or category) index (zero-based).
   *
   * @return The stroke (never <code>null</code>).
   */
  public Stroke getItemStroke(int row, int column);

  /**
   * Returns the stroke used to draw the items in a series.
   *
   * @param series  the series (zero-based index).
   *
   * @return The stroke (never <code>null</code>).
   *
   * @see #setSeriesStroke(int, Stroke)
   */
  public Stroke getSeriesStroke(int series);

  /**
   * Sets the stroke used for a series and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param stroke  the stroke (<code>null</code> permitted).
   *
   * @see #getSeriesStroke(int)
   */
  public void setSeriesStroke(int series, Stroke stroke);

  public void setSeriesStroke(int series, Stroke stroke, boolean notify);

  /**
   * Returns the default stroke.
   *
   * @return The default stroke (never <code>null</code>).
   *
   * @see #setDefaultStroke(Stroke)
   */
  public Stroke getDefaultStroke();

  /**
   * Sets the default stroke and sends a {@link RendererChangeEvent} to all
   * registered listeners.
   *
   * @param stroke  the stroke (<code>null</code> not permitted).
   *
   * @see #getDefaultStroke()
   */
  public void setDefaultStroke(Stroke stroke);

  public void setDefaultStroke(Stroke stroke, boolean notify);


  //// OUTLINE STROKE ////////////////////////////////////////////////////////

  /**
   * Returns the stroke used to outline data items.
   * <p>
   * The default implementation passes control to the
   * lookupSeriesOutlineStroke method.  You can override this method if you
   * require different behaviour.
   *
   * @param row  the row (or series) index (zero-based).
   * @param column  the column (or category) index (zero-based).
   *
   * @return The stroke (never <code>null</code>).
   */
  public Stroke getItemOutlineStroke(int row, int column);

  /**
   * Returns the stroke used to outline the items in a series.
   *
   * @param series  the series (zero-based index).
   *
   * @return The stroke (possibly <code>null</code>).
   *
   * @see #setSeriesOutlineStroke(int, Stroke)
   */
  public Stroke getSeriesOutlineStroke(int series);

  /**
   * Sets the outline stroke used for a series and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param stroke  the stroke (<code>null</code> permitted).
   *
   * @see #getSeriesOutlineStroke(int)
   */
  public void setSeriesOutlineStroke(int series, Stroke stroke);

  public void setSeriesOutlineStroke(int series, Stroke stroke, boolean notify);

  /**
   * Returns the default outline stroke.
   *
   * @return The stroke (never <code>null</code>).
   *
   * @see #setDefaultOutlineStroke(Stroke)
   */
  public Stroke getDefaultOutlineStroke();

  /**
   * Sets the default outline stroke and sends a {@link RendererChangeEvent} to
   * all registered listeners.
   *
   * @param stroke  the stroke (<code>null</code> not permitted).
   *
   * @see #getDefaultOutlineStroke()
   */
  public void setDefaultOutlineStroke(Stroke stroke);

  public void setDefaultOutlineStroke(Stroke stroke, boolean notify);

  
  //// SHAPE /////////////////////////////////////////////////////////////////

  /**
   * Returns a shape used to represent a data item.
   *
   * @param row  the row (or series) index (zero-based).
   * @param column  the column (or category) index (zero-based).
   *
   * @return The shape (never <code>null</code>).
   */
  public Shape getItemShape(int row, int column);

  /**
   * Returns a shape used to represent the items in a series.
   *
   * @param series  the series (zero-based index).
   *
   * @return The shape (possibly <code>null</code>).
   *
   * @see #setSeriesShape(int, Shape)
   */
  public Shape getSeriesShape(int series);

  /**
   * Sets the shape used for a series and sends a {@link RendererChangeEvent}
   * to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param shape  the shape (<code>null</code> permitted).
   *
   * @see #getSeriesShape(int)
   */
  public void setSeriesShape(int series, Shape shape);

  public void setSeriesShape(int series, Shape shape, boolean notify);

  /**
   * Returns the default shape.
   *
   * @return The shape (never <code>null</code>).
   *
   * @see #setDefaultShape(Shape)
   */
  public Shape getDefaultShape();

  /**
   * Sets the default shape and sends a {@link RendererChangeEvent} to all
   * registered listeners.
   *
   * @param shape  the shape (<code>null</code> not permitted).
   *
   * @see #getDefaultShape()
   */
  public void setDefaultShape(Shape shape);

  public void setDefaultShape(Shape shape, boolean notify);

  
  //// LEGEND ITEMS ///////////////////////////////////////////////////////////

  /**
   * Returns a legend item for a series.  This method can return
   * <code>null</code>, in which case the series will have no entry in the
   * legend.
   *
   * @param datasetIndex  the dataset index (zero-based).
   * @param series  the series (zero-based index).
   *
   * @return The legend item (possibly <code>null</code>).
   */
  public LegendItem getLegendItem(int datasetIndex, int series);


  // ITEM LABELS VISIBLE

  /**
   * Returns <code>true</code> if an item label is visible, and
   * <code>false</code> otherwise.
   *
   * @param row  the row index (zero-based).
   * @param column  the column index (zero-based).
   *
   * @return A boolean.
   */
  public boolean isItemLabelVisible(int row, int column);

  /**
   * Returns <code>true</code> if the item labels for a series are visible,
   * and <code>false</code> otherwise.
   *
   * @param series  the series index (zero-based).
   *
   * @return A boolean.
   *
   * @see #setSeriesItemLabelsVisible(int, Boolean)
   */
  public boolean isSeriesItemLabelsVisible(int series);

  /**
   * Sets a flag that controls the visibility of the item labels for a series.
   *
   * @param series  the series index (zero-based).
   * @param visible  the flag.
   *
   * @see #isSeriesItemLabelsVisible(int)
   */
  public void setSeriesItemLabelsVisible(int series, boolean visible);

  /**
   * Sets a flag that controls the visibility of the item labels for a series.
   *
   * @param series  the series index (zero-based).
   * @param visible  the flag (<code>null</code> permitted).
   *
   * @see #isSeriesItemLabelsVisible(int)
   */
  public void setSeriesItemLabelsVisible(int series, Boolean visible);

  /**
   * Sets the visibility of item labels for a series and, if requested, sends
   * a {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param visible  the visible flag.
   * @param notify  a flag that controls whether or not listeners are
   *                notified.
   *
   * @see #isSeriesItemLabelsVisible(int)
   */
  public void setSeriesItemLabelsVisible(int series, Boolean visible,
                                         boolean notify);

  /**
   * Returns the default setting for item label visibility.
   *
   * @return A flag.
   *
   * @see #setDefaultItemLabelsVisible(boolean)
   */
  public boolean getDefaultItemLabelsVisible();

  /**
   * Sets the default flag that controls whether or not item labels are visible
   * and sends a {@link RendererChangeEvent} to all registered listeners.
   *
   * @param visible  the flag.
   *
   * @see #getDefaultItemLabelsVisible()
   */
  public void setDefaultItemLabelsVisible(boolean visible);

  /**
   * Sets the default visibility for item labels and, if requested, sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param visible  the visibility flag.
   * @param notify  a flag that controls whether or not listeners are
   *                notified.
   *
   * @see #getDefaultItemLabelsVisible()
   */
  public void setDefaultItemLabelsVisible(boolean visible, boolean notify);


  //// ITEM LABEL FONT  //////////////////////////////////////////////////////

  /**
   * Returns the font for an item label.
   *
   * @param row  the row index (zero-based).
   * @param column  the column index (zero-based).
   *
   * @return The font (never <code>null</code>).
   */
  public Font getItemLabelFont(int row, int column);

  /**
   * Returns the font for all the item labels in a series.
   *
   * @param series  the series index (zero-based).
   *
   * @return The font (possibly <code>null</code>).
   *
   * @see #setSeriesItemLabelFont(int, Font)
   */
  public Font getSeriesItemLabelFont(int series);

  /**
   * Sets the item label font for a series and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param font  the font (<code>null</code> permitted).
   *
   * @see #getSeriesItemLabelFont(int)
   */
  public void setSeriesItemLabelFont(int series, Font font);

  public void setSeriesItemLabelFont(int series, Font font, boolean notify);

  /**
   * Returns the default item label font (this is used when no other font
   * setting is available).
   *
   * @return The font (<code>never</code> null).
   *
   * @see #setDefaultItemLabelFont(Font)
   */
  public Font getDefaultItemLabelFont();

  /**
   * Sets the default item label font and sends a {@link RendererChangeEvent}
   * to all registered listeners.
   *
   * @param font  the font (<code>null</code> not permitted).
   *
   * @see #getDefaultItemLabelFont()
   */
  public void setDefaultItemLabelFont(Font font);

  public void setDefaultItemLabelFont(Font font, boolean notify);


  //// ITEM LABEL PAINT  /////////////////////////////////////////////////////

  /**
   * Returns the paint used to draw an item label.
   *
   * @param row  the row index (zero based).
   * @param column  the column index (zero based).
   *
   * @return The paint (never <code>null</code>).
   */
  public Paint getItemLabelPaint(int row, int column);

  /**
   * Returns the paint used to draw the item labels for a series.
   *
   * @param series  the series index (zero based).
   *
   * @return The paint (possibly <code>null</code>).
   *
   * @see #setSeriesItemLabelPaint(int, Paint)
   */
  public Paint getSeriesItemLabelPaint(int series);

  /**
   * Sets the item label paint for a series and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series (zero based index).
   * @param paint  the paint (<code>null</code> permitted).
   *
   * @see #getSeriesItemLabelPaint(int)
   */
  public void setSeriesItemLabelPaint(int series, Paint paint);

  public void setSeriesItemLabelPaint(int series, Paint paint, boolean notify);

  /**
   * Returns the default item label paint.
   *
   * @return The paint (never <code>null</code>).
   *
   * @see #setDefaultItemLabelPaint(Paint)
   */
  public Paint getDefaultItemLabelPaint();

  /**
   * Sets the default item label paint and sends a {@link RendererChangeEvent}
   * to all registered listeners.
   *
   * @param paint  the paint (<code>null</code> not permitted).
   *
   * @see #getDefaultItemLabelPaint()
   */
  public void setDefaultItemLabelPaint(Paint paint);

  public void setDefaultItemLabelPaint(Paint paint, boolean notify);
  
  // POSITIVE ITEM LABEL POSITION...

  /**
   * Returns the item label position for positive values.
   *
   * @param row  the row index (zero-based).
   * @param column  the column index (zero-based).
   *
   * @return The item label position (never <code>null</code>).
   */
  public ItemLabelPosition getPositiveItemLabelPosition(int row, int column);

  /**
   * Returns the item label position for all positive values in a series.
   *
   * @param series  the series index (zero-based).
   *
   * @return The item label position.
   *
   * @see #setSeriesPositiveItemLabelPosition(int, ItemLabelPosition)
   */
  public ItemLabelPosition getSeriesPositiveItemLabelPosition(int series);

  /**
   * Sets the item label position for all positive values in a series and
   * sends a {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param position  the position (<code>null</code> permitted).
   *
   * @see #getSeriesPositiveItemLabelPosition(int)
   */
  public void setSeriesPositiveItemLabelPosition(int series,
                                                 ItemLabelPosition position);

  /**
   * Sets the item label position for all positive values in a series and (if
   * requested) sends a {@link RendererChangeEvent} to all registered
   * listeners.
   *
   * @param series  the series index (zero-based).
   * @param position  the position (<code>null</code> permitted).
   * @param notify  notify registered listeners?
   *
   * @see #getSeriesPositiveItemLabelPosition(int)
   */
  public void setSeriesPositiveItemLabelPosition(int series,
      ItemLabelPosition position, boolean notify);

  /**
   * Returns the default positive item label position.
   *
   * @return The position.
   *
   * @see #setDefaultPositiveItemLabelPosition(ItemLabelPosition)
   */
  public ItemLabelPosition getDefaultPositiveItemLabelPosition();

  /**
   * Sets the default positive item label position.
   *
   * @param position  the position.
   *
   * @see #getDefaultPositiveItemLabelPosition()
   */
  public void setDefaultPositiveItemLabelPosition(ItemLabelPosition position);

  /**
   * Sets the default positive item label position and, if requested, sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param position  the position.
   * @param notify  notify registered listeners?
   *
   * @see #getDefaultPositiveItemLabelPosition()
   */
  public void setDefaultPositiveItemLabelPosition(ItemLabelPosition position,
                         boolean notify);


  // NEGATIVE ITEM LABEL POSITION...

  /**
   * Returns the item label position for negative values.  This method can be
   * overridden to provide customisation of the item label position for
   * individual data items.
   *
   * @param row  the row index (zero-based).
   * @param column  the column (zero-based).
   *
   * @return The item label position.
   */
  public ItemLabelPosition getNegativeItemLabelPosition(int row, int column);

  /**
   * Returns the item label position for all negative values in a series.
   *
   * @param series  the series index (zero-based).
   *
   * @return The item label position.
   *
   * @see #setSeriesNegativeItemLabelPosition(int, ItemLabelPosition)
   */
  public ItemLabelPosition getSeriesNegativeItemLabelPosition(int series);

  /**
   * Sets the item label position for negative values in a series and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param series  the series index (zero-based).
   * @param position  the position (<code>null</code> permitted).
   *
   * @see #getSeriesNegativeItemLabelPosition(int)
   */
  public void setSeriesNegativeItemLabelPosition(int series,
                           ItemLabelPosition position);

  /**
   * Sets the item label position for negative values in a series and (if
   * requested) sends a {@link RendererChangeEvent} to all registered
   * listeners.
   *
   * @param series  the series index (zero-based).
   * @param position  the position (<code>null</code> permitted).
   * @param notify  notify registered listeners?
   *
   * @see #getSeriesNegativeItemLabelPosition(int)
   */
  public void setSeriesNegativeItemLabelPosition(int series,
      ItemLabelPosition position, boolean notify);

  /**
   * Returns the default item label position for negative values.
   *
   * @return The position.
   *
   * @see #setDefaultNegativeItemLabelPosition(ItemLabelPosition)
   */
  public ItemLabelPosition getDefaultNegativeItemLabelPosition();

  /**
   * Sets the default item label position for negative values and sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param position  the position.
   *
   * @see #getDefaultNegativeItemLabelPosition()
   */
  public void setDefaultNegativeItemLabelPosition(ItemLabelPosition position);

  /**
   * Sets the default negative item label position and, if requested, sends a
   * {@link RendererChangeEvent} to all registered listeners.
   *
   * @param position  the position.
   * @param notify  notify registered listeners?
   *
   * @see #getDefaultNegativeItemLabelPosition()
   */
  public void setDefaultNegativeItemLabelPosition(ItemLabelPosition position,
      boolean notify);

  // CREATE ENTITIES

  public boolean getItemCreateEntity(int series, int item);

  public Boolean getSeriesCreateEntities(int series);

  public void setSeriesCreateEntities(int series, Boolean create);

  public void setSeriesCreateEntities(int series, Boolean create, 
      boolean notify);

  public boolean getDefaultCreateEntities();

  public void setDefaultCreateEntities(boolean create);

  public void setDefaultCreateEntities(boolean create, boolean notify);


}
