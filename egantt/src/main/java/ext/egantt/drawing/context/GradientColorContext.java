package ext.egantt.drawing.context;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.paint.VerticalGradientPaint;
import com.egantt.model.drawing.painter.PainterResources;

import java.awt.*;

/**
 * <code>GradientColorContext</code>
 */
public class GradientColorContext
{
    /**
     * The color white.  In the default sRGB space.
     */  
	public final static GraphicsContext WHITE = new LocalColorContext(Color.white);

    /**
     * The color light gray.  In the default sRGB space.
     */
    public final static GraphicsContext LIGHT_GRAY = new LocalColorContext(Color.lightGray);

    /**
     * The color gray.  In the default sRGB space.
     */
    public final static GraphicsContext gray      = new LocalColorContext(Color.gray);

    /**
     * The color gray.  In the default sRGB space.
     */
    public final static GraphicsContext GRAY = gray;

    /**
     * The color dark gray.  In the default sRGB space.
     */
    public final static GraphicsContext darkGray  = new LocalColorContext(Color.darkGray);

    /**
     * The color dark gray.  In the default sRGB space.
     */
    public final static GraphicsContext DARK_GRAY = darkGray;

    /**
     * The color black.  In the default sRGB space.
     */
    public final static GraphicsContext black 	= new LocalColorContext(Color.black);
    
    /**
     * The color black.  In the default sRGB space.
     */
    public final static GraphicsContext BLACK = black;
    
    /**
     * The color red.  In the default sRGB space.
     */
    public final static GraphicsContext red  = new LocalColorContext(Color.red);

    /**
     * The color red.  In the default sRGB space.
     */
    public final static GraphicsContext RED = red;

    /**
     * The color pink.  In the default sRGB space.
     */
    public final static GraphicsContext pink      = new LocalColorContext(Color.pink);

    /**
     * The color pink.  In the default sRGB space.
     */
    public final static GraphicsContext PINK = pink;

    /**
     * The color orange.  In the default sRGB space.
     */
    public final static GraphicsContext orange 	= new LocalColorContext(Color.orange);

    /**
     * The color orange.  In the default sRGB space.
     */
    public final static GraphicsContext ORANGE = orange;

    /**
     * The color yellow.  In the default sRGB space.
     */
    public final static GraphicsContext yellow 	= new LocalColorContext(Color.yellow);

    /**
     * The color yellow.  In the default sRGB space.
     */
    public final static GraphicsContext YELLOW = yellow;

    /**
     * The color green.  In the default sRGB space.
     */
    public final static GraphicsContext green 	= new LocalColorContext(Color.green);

    /**
     * The color green.  In the default sRGB space.
     */
    public final static GraphicsContext GREEN = green;

    /**
     * The color magenta.  In the default sRGB space.
     */
    public final static GraphicsContext magenta	= new LocalColorContext(Color.magenta);

    /**
     * The color magenta.  In the default sRGB space.
     */
    public final static GraphicsContext MAGENTA = magenta;

    /**
     * The color cyan.  In the default sRGB space.
     */
    public final static GraphicsContext cyan = new LocalColorContext(Color.cyan);

    /**
     * The color cyan.  In the default sRGB space.
     */
    public final static GraphicsContext CYAN = cyan;

    /**
     * The color blue.  In the default sRGB space.
     */
    public final static GraphicsContext blue = new LocalColorContext(Color.blue);

    /**
     * The color blue.  In the default sRGB space.
     */
    public final static GraphicsContext BLUE = blue;

    /**/

    //	________________________________________________________________________
    
    public static final class LocalColorContext implements GraphicsContext {

    	protected final Paint color;
    	
    	public LocalColorContext(Color color) {   	
    		color = color.darker().darker();
    		this.color = new VerticalGradientPaint(color, color.brighter());
    	}
    	
		public Object get(Object key, Object type) {
			return (PainterResources.PAINT.equals(type))
				? color : null;
		}   	
		
		public Paint getPaint() {
			return color;
		}
    }
}