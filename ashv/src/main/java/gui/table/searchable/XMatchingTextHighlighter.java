/*
 * Created on 02.12.2008
 */
package gui.table.searchable;

import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.decorator.SearchPredicate;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.renderer.PainterAware;
import org.jdesktop.swingx.renderer.WrappingIconPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * MatchingHighlighter which marks clipped match as well. This is a hack, should
 * be supported in super.
 * 
 * @author Jeanette Winzenburg
 * @author Thorsten Klimpel
 */
public class XMatchingTextHighlighter extends MatchingTextHighlighter {
	// copy of private super field
	Rectangle myTextR;

	public XMatchingTextHighlighter() {
		this(null);
	}

	public XMatchingTextHighlighter(Painter<JLabel> painter) {
		super(painter);
	}

	@Override
	protected List<Rectangle> createHighlightAreas(String fullText,
			String clippedText, FontMetrics fm, int xOffset, int yOffset,
			int height) {
		List<Rectangle> areas = super.createHighlightAreas( fullText, 
				clippedText, fm, xOffset, yOffset, height );

		// Look for one (or more) 'ellipsed' matches of many matches. Goal is to 
		// highlight characters in visible AND clipped (ellipsis) text.
		if ( !fullText.equals( clippedText ) )
		{
			SearchPredicate predicate = (SearchPredicate) getHighlightPredicate();

			int matchesInFullText = 0;
			Matcher matcher = predicate.getPattern().matcher( fullText );
			while ( matcher.find() )
				matchesInFullText++;

			//The areas of highlighted regions could be coalesced, so the 
			// count ( areas.size() ) could be too low. So matching here again:
			int matchesInClippedText = 0;
			matcher = predicate.getPattern().matcher( clippedText );
			while ( matcher.find() )
				matchesInClippedText++;

			if ( matchesInFullText > matchesInClippedText )
			{// happens if at least one match is under the ellipsis
				if ( areas.isEmpty() )
					areas = new ArrayList<Rectangle>();

				// here we rely on the given text to not contain the ellipsis
				// PENDING JW: should be supported in super
				int beginOfEllipsis = fm.stringWidth( clippedText ) + xOffset;
				areas.add( new Rectangle( beginOfEllipsis, yOffset, 
				fm.stringWidth( "..." ), height ) );// hopefully no one ever changes 
				//the 3 points to a real "\u2026" ellipsis ;-) I think as long as we
				//are explicitly removing the last 3 characters of the String in the
				//super-class we can 'add' them here again.
			}
		}

		return areas;  
	}
	

	/**
	 * Overridden to copy super's private field.
	 */
	@Override
	protected int calculateXOffset(JLabel component, Rectangle viewR,
			Rectangle textR, Rectangle iconR, int iconTextGap) {
		myTextR = textR;
		return super.calculateXOffset(component, viewR, textR, iconR,
				iconTextGap);
	}

	@Override
	protected Component doHighlight(Component component,
			ComponentAdapter adapter) {
		if (isLabelCompatible(component))
			return super.doHighlight(component, adapter);
		((PainterAware) component).setPainter(getPainter());
		return component;
	}

	@Override
	protected boolean canHighlight(Component component, ComponentAdapter adapter) {
		return // (component instanceof JLabel ||
				// (component instanceof WrappingIconPanel)
				// &&
		component instanceof PainterAware && getPainter() != null
				&& getHighlightPredicate() instanceof SearchPredicate;
	}

	protected boolean isLabelCompatible(Component component) {
		return component instanceof JLabel
				|| (component instanceof WrappingIconPanel && ((WrappingIconPanel) component)
						.getComponent() instanceof JLabel);
	}
}

