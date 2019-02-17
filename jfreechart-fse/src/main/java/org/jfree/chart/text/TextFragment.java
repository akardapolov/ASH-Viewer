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
 * -----------------
 * TextFragment.java
 * -----------------
 * (C) Copyright 2003-2013, by Object Refinery Limited and Contributors.
 *
 * Original Author:  David Gilbert (for Object Refinery Limited);
 * Contributor(s):   -;
 *
 * $Id: TextFragment.java,v 1.13 2007/03/16 10:25:58 mungady Exp $
 *
 * Changes
 * -------
 * 07-Nov-2003 : Version 1 (DG);
 * 25-Nov-2003 : Fixed bug in the dimension calculation (DG);
 * 22-Dec-2003 : Added workaround for Java bug 4245442 (DG);
 * 29-Jan-2004 : Added paint attribute (DG);
 * 22-Mar-2004 : Added equals() method and implemented Serializable (DG);
 * 01-Apr-2004 : Changed java.awt.geom.Dimension2D to org.jfree.ui.Size2D
 *               because of JDK bug 4976448 which persists on JDK 1.3.1 (DG);
 * 30-Sep-2004 : Moved drawRotatedString() from RefineryUtilities
 *               --> TextUtilities (DG);
 * 16-Mar-2007 : Fixed serialization for GradientPaint (DG);
 * 17-Jun-2012 : Moved from JCommon to JFreeChart (DG);
 *
 */

package org.jfree.chart.text;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jfree.chart.ui.Size2D;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.chart.util.ParamChecks;
import org.jfree.chart.util.SerialUtilities;

/**
 * A text item, with an associated font, that fits on a single line (see
 * {@link TextLine}).  Instances of the class are immutable.
 */
public class TextFragment implements Serializable {

    /** For serialization. */
    private static final long serialVersionUID = 4465945952903143262L;

    /** The default font. */
    public static final Font DEFAULT_FONT = new Font("Serif", Font.PLAIN, 12);

    /** The default text color. */
    public static final Paint DEFAULT_PAINT = Color.BLACK;

    /** The text. */
    private String text;

    /** The font. */
    private Font font;

    /** The text color. */
    private transient Paint paint;

    /**
     * The baseline offset (can be used to simulate subscripts and
     * superscripts).
     */
    private float baselineOffset;

    /**
     * Creates a new text fragment.
     *
     * @param text  the text (<code>null</code> not permitted).
     */
    public TextFragment(String text) {
        this(text, DEFAULT_FONT, DEFAULT_PAINT);
    }

    /**
     * Creates a new text fragment.
     *
     * @param text  the text (<code>null</code> not permitted).
     * @param font  the font (<code>null</code> not permitted).
     */
    public TextFragment(String text, final Font font) {
        this(text, font, DEFAULT_PAINT);
    }

    /**
     * Creates a new text fragment.
     *
     * @param text  the text (<code>null</code> not permitted).
     * @param font  the font (<code>null</code> not permitted).
     * @param paint  the text color (<code>null</code> not permitted).
     */
    public TextFragment(String text, final Font font, final Paint paint) {
        this(text, font, paint, 0.0f);
    }

    /**
     * Creates a new text fragment.
     *
     * @param text  the text (<code>null</code> not permitted).
     * @param font  the font (<code>null</code> not permitted).
     * @param paint  the text color (<code>null</code> not permitted).
     * @param baselineOffset  the baseline offset.
     */
    public TextFragment(String text, Font font, Paint paint, float baselineOffset) {
        ParamChecks.nullNotPermitted(text, "text");
        ParamChecks.nullNotPermitted(font, "font");
        ParamChecks.nullNotPermitted(paint, "paint");
        this.text = text;
        this.font = font;
        this.paint = paint;
        this.baselineOffset = baselineOffset;
    }

    /**
     * Returns the text.
     *
     * @return The text (possibly <code>null</code>).
     */
    public String getText() {
        return this.text;
    }

    /**
     * Returns the font.
     *
     * @return The font (never <code>null</code>).
     */
    public Font getFont() {
        return this.font;
    }

    /**
     * Returns the text paint.
     *
     * @return The text paint (never <code>null</code>).
     */
    public Paint getPaint() {
        return this.paint;
    }

    /**
     * Returns the baseline offset.
     *
     * @return The baseline offset.
     */
    public float getBaselineOffset() {
        return this.baselineOffset;
    }

    /**
     * Draws the text fragment.
     *
     * @param g2  the graphics device.
     * @param anchorX  the x-coordinate of the anchor point.
     * @param anchorY  the y-coordinate of the anchor point.
     * @param anchor  the location of the text that is aligned to the anchor
     *                point.
     * @param rotateX  the x-coordinate of the rotation point.
     * @param rotateY  the y-coordinate of the rotation point.
     * @param angle  the angle.
     */
    public void draw(Graphics2D g2, float anchorX, float anchorY, 
            TextAnchor anchor, float rotateX, float rotateY, double angle) {
        g2.setFont(this.font);
        g2.setPaint(this.paint);
        TextUtilities.drawRotatedString(this.text, g2, anchorX, anchorY
                + this.baselineOffset, anchor, angle, rotateX, rotateY);
    }

    /**
     * Calculates the dimensions of the text fragment.
     *
     * @param g2  the graphics device.
     *
     * @return The width and height of the text.
     */
    public Size2D calculateDimensions(Graphics2D g2) {
        FontMetrics fm = g2.getFontMetrics(this.font);
        Rectangle2D bounds = TextUtilities.getTextBounds(this.text, g2, fm);
        Size2D result = new Size2D(bounds.getWidth(), bounds.getHeight());
        return result;
    }

    /**
     * Calculates the vertical offset between the baseline and the specified
     * text anchor.
     *
     * @param g2  the graphics device.
     * @param anchor  the anchor.
     *
     * @return the offset.
     */
    public float calculateBaselineOffset(Graphics2D g2, TextAnchor anchor) {
        float result = 0.0f;
        FontMetrics fm = g2.getFontMetrics(this.font);
        LineMetrics lm = fm.getLineMetrics("ABCxyz", g2);
        if (anchor.isTop()) {
            result = lm.getAscent();
        }
        else if (anchor.isHalfAscent()) {
            result = lm.getAscent() / 2.0f;
        }
        else if (anchor.isHalfHeight()) {
            result = lm.getAscent() / 2.0f - lm.getDescent() / 2.0f;
        }
        else if (anchor.isBottom()) {
            result = -lm.getDescent() - lm.getLeading();
        }
        return result;
    }

    /**
     * Tests this instance for equality with an arbitrary object.
     *
     * @param obj  the object to test against (<code>null</code> permitted).
     *
     * @return A boolean.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj instanceof TextFragment) {
            TextFragment tf = (TextFragment) obj;
            if (!this.text.equals(tf.text)) {
                return false;
            }
            if (!this.font.equals(tf.font)) {
                return false;
            }
            if (!this.paint.equals(tf.paint)) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a hash code for this object.
     *
     * @return A hash code.
     */
    @Override
    public int hashCode() {
        int result;
        result = (this.text != null ? this.text.hashCode() : 0);
        result = 29 * result + (this.font != null ? this.font.hashCode() : 0);
        result = 29 * result + (this.paint != null ? this.paint.hashCode() : 0);
        return result;
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the output stream.
     *
     * @throws IOException  if there is an I/O error.
     */
    private void writeObject(ObjectOutputStream stream) throws IOException {
        stream.defaultWriteObject();
        SerialUtilities.writePaint(this.paint, stream);
    }

    /**
     * Provides serialization support.
     *
     * @param stream  the input stream.
     *
     * @throws IOException  if there is an I/O error.
     * @throws ClassNotFoundException  if there is a classpath problem.
     */
    private void readObject(final ObjectInputStream stream) throws IOException, 
            ClassNotFoundException {
        stream.defaultReadObject();
        this.paint = SerialUtilities.readPaint(stream);
    }

}
