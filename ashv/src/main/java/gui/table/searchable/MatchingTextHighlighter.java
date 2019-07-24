/*
 * $Id: MatchingTextHighlighter.java 1290 2011-09-26 09:15:35Z kleopatra $
 *
 * Copyright 2008 Sun Microsystems, Inc., 4150 Network Circle,
 * Santa Clara, California 95054, U.S.A. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 */
package gui.table.searchable;

import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.jdesktop.swingx.decorator.SearchPredicate;
import org.jdesktop.swingx.painter.AbstractPainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.PainterAware;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;

/**
 * <p>
 * <code>Highlighter</code> implementation that changes the background behind
 * characters that match a regular expression. The highlighting style can be
 * configured with a {@link Painter}.
 * </p>
 * <p>
 * This highlighter is designed to work with a {@link SearchPredicate}. All
 * other predicate types will be ignored and no highlighting will be performed.
 * </p>
 * 
 * <p>
 * <strong>NOTE: This highlighter is designed to work with renderers that both
 * extend {@link JLabel} and implements {@link PainterAware}. Other renderers
 * will be left undecorated.</strong>
 * </p>
 * 
 * @author gregtan
 * @author Jeanette Winzenburg
 * @author Thorsten Klimpel
 */
public class MatchingTextHighlighter extends AbstractHighlighter {

	/**
	 * Comparator that orders rectangles by their x coordinate.
	 */
	private static final Comparator<Rectangle> X_AXIS_RECTANGLE_COMPARATOR = new Comparator<Rectangle>() {
		public int compare(Rectangle o1, Rectangle o2) {
			return o1.x - o2.x;
		}
	};

	/**
	 * Painter that delegates character highlighting to {@link #painter}.
	 */
	private final DelegatingPainter delegatingPainter = new DelegatingPainter();

	/**
	 * The painter used for highlighting characters.
	 */
	private Painter<JLabel> painter;

	// Rectangles and insets fields to minimize object instantiation,
	// used in findHighlightAreas method
	private Rectangle viewR = new Rectangle();

	private Rectangle iconR = new Rectangle();

	private Rectangle textR = new Rectangle();

	private Insets insets = new Insets(0, 0, 0, 0);

	private PropertyChangeListener painterListener;

	/**
	 * Instantiates a <code>MatchingTextHighlighter</code> with no highlight
	 * predicate or painter.
	 */
	public MatchingTextHighlighter() {
		this(null, null);
	}

	/**
	 * Instantiates a <code>MatchingTextHighlighter</code> with no highlight
	 * predicate that paints with the specified painter.
	 * 
	 * @param painter the painter used to render matching text
	 */
	public MatchingTextHighlighter(Painter<JLabel> painter) {
		this(null, painter);
	}

	/**
	 * <p>
	 * Instantiates a <code>MatchingTextHighlighter</code> with the given
	 * predicate that matches text with the specified pattern with the specified
	 * highlight color.
	 * </p>
	 * 
	 * @param predicate the HighlightPredicate to use
	 * @param painter the painter used to render matching text
	 */
	public MatchingTextHighlighter(HighlightPredicate predicate,
                                   Painter<JLabel> painter) {
		super(predicate);
		setPainter(painter);
	}

	/**
	 * {@inheritDoc}
	 */
	// <snip> MatchingTextHighlighter
	// Check if Painter applicable
	@Override
	protected boolean canHighlight(Component component, ComponentAdapter adapter) {
		return component instanceof JLabel && component instanceof PainterAware
				&& painter != null
				&& getHighlightPredicate() instanceof SearchPredicate;
	}

	// </snip>

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Component doHighlight(Component component,
			ComponentAdapter adapter) {
		((PainterAware) component).setPainter(delegatingPainter);

		return component;
	}

	/**
	 * Returns the painter used for highlighting matching characters.
	 * 
	 * @return a <code>Painter</code>
	 */
	public Painter<JLabel> getPainter() {
		return painter;
	}

	/**
	 * Sets the painter used for highlighting matching characters.
	 * 
	 * @param painter a <code>Painter</code>
	 */
	public void setPainter(Painter<JLabel> painter) {
		if (areEqual(painter, this.painter)) {
			return;
		}
		uninstallPainterListener();
		this.painter = painter;
		installPainterListener();
		fireStateChanged();
	}

	/**
	 * Installs a listener to the painter if appropriate. This implementation
	 * registers its painterListener if the Painter is of type AbstractPainter.
	 */
	protected void installPainterListener() {
		if (getPainter() instanceof AbstractPainter) {
			((AbstractPainter<?>) getPainter())
					.addPropertyChangeListener(getPainterListener());
		}
	}

	/**
	 * Uninstalls a listener from the painter if appropriate. This
	 * implementation removes its painterListener if the Painter is of type
	 * AbstractPainter.
	 */
	protected void uninstallPainterListener() {
		if (getPainter() instanceof AbstractPainter) {
			((AbstractPainter<?>) getPainter())
					.removePropertyChangeListener(painterListener);
		}
	}

	/**
	 * Lazily creates and returns the property change listener used to listen to
	 * changes of the painter.
	 * 
	 * @return the property change listener used to listen to changes of the
	 *         painter.
	 */
	protected final PropertyChangeListener getPainterListener() {
		if (painterListener == null) {
			painterListener = createPainterListener();
		}
		return painterListener;
	}

	/**
	 * Creates and returns the property change listener used to listen to
	 * changes of the painter.
	 * <p>
	 * 
	 * This implementation fires a stateChanged on receiving any propertyChange,
	 * if the isAdjusting flag is false. Otherwise does nothing.
	 * 
	 * @return the property change listener used to listen to changes of the
	 *         painter.
	 */
	protected PropertyChangeListener createPainterListener() {
		PropertyChangeListener l = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO why is this commented out? // if (isAdjusting) return;
				fireStateChanged();
			}
		};
		return l;
	}

	/**
	 * Finds the rectangles that contain rendered characters that match the
	 * pattern.
	 * 
	 * @param object an optional configuration parameter. This may be null.
	 * @param width width of the area to paint.
	 * @param height height of the area to paint.
	 * @return a <code>List</code> of <code>Rectangle</code>s marking characters
	 *         to highlight
	 */
	protected List<Rectangle> findHighlightAreas(JLabel object, int width,
			int height) {
		insets = object.getInsets(insets);
		viewR.x = 0 + insets.left;
		viewR.y = 0 + insets.bottom;
		viewR.width = width - insets.right;
		viewR.height = height - insets.top;

		// Reset the text and view rectangle x any y coordinates.
		// These are not set to 0 in SwingUtilities.layoutCompoundLabel
		iconR.x = iconR.y = 0;
		textR.x = textR.y = 0;

		FontMetrics fm = object.getFontMetrics(object.getFont());
		// TODO Bug: Try to get always the current textR-Size.
		// The method SwingUtilities.layoutCompoundLabel sets the parameter
		// textR to an old value. While resizing a centered or right aligned
		// tableColumn, the calculated width of the text-rectangle seems to be
		// "one event behind". Perhaps Anti-Aliasing draws different?
		String clippedText = SwingUtilities.layoutCompoundLabel(object, fm,
				object.getText(), object.getIcon(),
				object.getVerticalAlignment(), object.getHorizontalAlignment(),
				object.getVerticalTextPosition(),
				object.getHorizontalTextPosition(), viewR, iconR, textR,
				object.getIconTextGap());

		int xOffset = calculateXOffset(object, viewR, textR, iconR,
				object.getIconTextGap());
		int yOffset = textR.y - 1;// magic -1 for a nicer look
		int highlightHeight = textR.height + 1;// magic +1 for a nicer look

		String clippedTextToSearch = clippedText;
		// Check to see if the text will be clipped
		if (!object.getText().equals(clippedText)) {
			// TODO There has to be a better way that assuming ellipsis are the
			// last characters of the text
			clippedTextToSearch = clippedText.substring(0,
					clippedText.length() - 3);
		}

		return createHighlightAreas(object.getText(), clippedTextToSearch, fm,
				xOffset, yOffset, highlightHeight);
	}

	/**
	 * Creates the rectangles that contain matched characters in the given text.
	 * <p>
	 * TODO: Improve partial clipped matches: If one of the matched characters
	 * is clipped, the remaining characters lose their highlight; just the
	 * ellipsis is highlighted.
	 * </p>
	 * 
	 * @param fullText useful for highlighting if matches exist in clipped text
	 *        and in the ellipsis
	 * @param clippedText the clipped text to search (could be the same as
	 *        fullText)
	 * @param fm the font metrics of the rendered font
	 * @param xOffset the x offset at which text rendering starts
	 * @param yOffset the y offset at which text rendering starts (e.g.
	 *        different rowHeights)
	 * @param height the height of painted highlights
	 * @return a <code>List</code> of highlight areas to paint
	 */
	protected List<Rectangle> createHighlightAreas(String fullText,
			String clippedText, FontMetrics fm, int xOffset, int yOffset,
			int height) {
		SearchPredicate predicate = (SearchPredicate) getHighlightPredicate();
		Matcher matcher = predicate.getPattern().matcher(clippedText);

		List<Rectangle> highlightAreas = null;
		int startFrom = 0;
		while (startFrom < clippedText.length() && matcher.find(startFrom)) {
			if (highlightAreas == null) {
				highlightAreas = new ArrayList<Rectangle>();
			}

			int start = matcher.start();
			int end = matcher.end();

			if (start == end) {
				// empty matcher will cause infinite loop
				break;
			}

			startFrom = end;

			int highlightx;
			int highlightWidth;

			if (start == 0) {
				// start highlight from the start of the field
				highlightx = xOffset;
			} else {
				// Calculate the width of the unhighlighted text to get the
				// start of the highlighted region.
				String strToStart = clippedText.substring(0, start);
				highlightx = fm.stringWidth(strToStart) + xOffset;
			}

			// Get the width of the highlighted region
			String highlightText = clippedText.substring(start, end);
			highlightWidth = fm.stringWidth(highlightText);

			highlightAreas.add(new Rectangle(highlightx, yOffset,
					highlightWidth, height));
		}// while ( startFrom < text.length() && matcher.find( startFrom ) )

		if (highlightAreas == null) {
			highlightAreas = Collections.emptyList();
		} else {
			coalesceHighlightAreas(highlightAreas);
		}

		return highlightAreas;
	}

	/**
	 * Joins highlight rectangles that mark adjacent horizontal areas into
	 * single rectangles. This is useful to renderers that vary horizontally,
	 * such a horizontal gradient - the gradient will not restart when there are
	 * two adjacent highlight areas.
	 * 
	 * @param highlightAreas a <code>List</code> of <code>Rectangle</code>s.
	 */
	protected void coalesceHighlightAreas(List<Rectangle> highlightAreas) {
		Collections.sort(highlightAreas, X_AXIS_RECTANGLE_COMPARATOR);

		int i = 0;
		while (i < highlightAreas.size() - 1) {
			Rectangle r1 = highlightAreas.get(i);
			Rectangle r2 = highlightAreas.get(i + 1);

			if (r1.x + r1.width == r2.x) {
				r1.width += r2.width;
				highlightAreas.remove(i + 1);
			} else {
				i++;
			}
		}
	}

	/**
	 * Calculates the x offset of highlights based on component orientation and
	 * text direction.
	 * 
	 * @param component the renderer component
	 * @param viewR the view rectangle of the renderer component
	 * @param textR the text rectangle of the renderer component
	 * @return the number of pixels to offset the highlight from the left edge
	 *         of the component
	 */
	protected int calculateXOffset(JLabel component, Rectangle viewR,
			Rectangle textR, Rectangle iconR, int iconTextGap) {
		int horizAlignment = component.getHorizontalAlignment();
		boolean leftToRight = component.getComponentOrientation()
				.isLeftToRight();

		if (horizAlignment == SwingConstants.LEFT
				|| (horizAlignment == SwingConstants.LEADING && leftToRight)
				|| (horizAlignment == SwingConstants.TRAILING && !leftToRight)) {
			return textR.x;// respect the icon and start the highlight at the
			// beginning of the text not at 0
		} else if (horizAlignment == SwingConstants.RIGHT
				|| (horizAlignment == SwingConstants.TRAILING && leftToRight)
				|| (horizAlignment == SwingConstants.LEADING && !leftToRight)) {
			int offsetWhenRight;
			if (leftToRight)
				offsetWhenRight = viewR.width - textR.width;
			else {
				int currentIconTextGap = component.getIcon() != null ? iconTextGap
						: 0;// The gap between the icon and the text of the
							// JLabel has to be included, if an icon is set
				offsetWhenRight = viewR.width - textR.width - iconR.width
						- currentIconTextGap;
			}
			return offsetWhenRight;

		} else if (horizAlignment == SwingConstants.CENTER) {
			int currentIconTextGap = component.getIcon() != null ? iconTextGap
					: 0;// The gap between the icon and the text of the JLabel
			// has to be included, if an icon is set/ visible
			int offsetWhenCentered;
			if (leftToRight)
				offsetWhenCentered = Math.round((viewR.width - textR.width
						+ iconR.width + currentIconTextGap) / 2f);// round a
			// float to prevent a jumping (see ColumnHeader for example)
			// Highlighter (most of the time...(because of Anti-Aliased-Text?
			// Even or uneven width of text to paint?))
			else
				// if the orientation is RightToLeft the icon and the gap is at
				// the right side of the text:
				offsetWhenCentered = Math.round((viewR.width - textR.width
						- iconR.width - currentIconTextGap) / 2f);// round a
			// float to prevent a jumping (see ColumnHeader for example)
			// Highlighter (most of the time...( because of Anti-Aliased-Text?
			// Even or uneven Width of text to paint?))

			return offsetWhenCentered;
		}

		throw new AssertionError("Unknown horizonal alignment "
				+ horizAlignment);
	}

	/**
	 * Painter that draws highlight rectangles at matching character positions.
	 */
	private class DelegatingPainter implements Painter<JLabel> {
		/**
		 * {@inheritDoc}
		 */
		// <snip> MatchingTextHighlighter
		// delegate to painter to paint the matches

		public void paint(Graphics2D g, JLabel object, int width, int height) {
			List<Rectangle> highlightAreas = findHighlightAreas(object, width,
					height);
			for (Rectangle r : highlightAreas) {
				Graphics2D scratchGraphics = (Graphics2D) g.create(r.x, r.y,
						r.width, r.height);
				painter.paint(scratchGraphics, object, r.width, r.height);
				scratchGraphics.dispose();
			}
		}
		// </snip>
	}

}
