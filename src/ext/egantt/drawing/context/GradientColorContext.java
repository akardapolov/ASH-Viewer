/*
 *
 */

package ext.egantt.drawing.context;

import com.egantt.awt.graphics.GraphicsContext;
import com.egantt.awt.paint.VerticalGradientPaint;
import java.awt.Color;
import java.awt.Paint;

import org.ash.util.Options;

public class GradientColorContext
{
    public static final class LocalColorContext
        implements GraphicsContext
    {

        public Object get(Object key, Object type)
        {
            return "Paint".equals(type) ? color : null;
        }

        public Paint getPaint()
        {
            return color;
        }

        protected final Paint color;

        public LocalColorContext(Color color)
        {
            this.color = new VerticalGradientPaint(color, color);
        }
    }


    public GradientColorContext()
    {
    }

    public static final GraphicsContext WHITE;
    public static final GraphicsContext LIGHT_GRAY;
    public static final GraphicsContext gray;
    public static final GraphicsContext GRAY;
    public static final GraphicsContext darkGray;
    public static final GraphicsContext DARK_GRAY;
    public static final GraphicsContext black;
    public static final GraphicsContext BLACK;
    public static final GraphicsContext red;
    public static final GraphicsContext RED;
    public static final GraphicsContext pink;
    public static final GraphicsContext PINK;
    public static final GraphicsContext orange;
    public static final GraphicsContext ORANGE;
    public static final GraphicsContext yellow;
    public static final GraphicsContext YELLOW;
    public static final GraphicsContext green;
    public static final GraphicsContext GREEN;
    public static final GraphicsContext magenta;
    public static final GraphicsContext MAGENTA;
    public static final GraphicsContext cyan;
    public static final GraphicsContext CYAN;
    public static final GraphicsContext blue;
    public static final GraphicsContext BLUE;
    
    public static final GraphicsContext OTHER0;
    public static final GraphicsContext CLUSTER11;
    public static final GraphicsContext QUEUEING12;
    public static final GraphicsContext NETWORK7;
    public static final GraphicsContext ADMINISTRATIVE3;
    public static final GraphicsContext CONFIGURATION2;
    public static final GraphicsContext COMMIT5;
    public static final GraphicsContext APPLICATION1;
    public static final GraphicsContext CONCURRENCY4;
    public static final GraphicsContext SYSTEMIO9;
    public static final GraphicsContext USERIO8;
    public static final GraphicsContext SCHEDULER10;
    public static final GraphicsContext CPU;
    
    static 
    {
        WHITE = new LocalColorContext(Color.white);
        LIGHT_GRAY = new LocalColorContext(Color.lightGray);
        gray = new LocalColorContext(Color.gray);
        GRAY = gray;
        darkGray = new LocalColorContext(Color.darkGray);
        DARK_GRAY = darkGray;
        black = new LocalColorContext(Color.black);
        BLACK = black;
        red = new LocalColorContext(Color.red);
        RED = red;
        pink = new LocalColorContext(Color.pink);
        PINK = pink;
        orange = new LocalColorContext(Color.orange);
        ORANGE = orange;
        yellow = new LocalColorContext(Color.yellow);
        YELLOW = yellow;
        green = new LocalColorContext(Color.green);
        GREEN = green;
        magenta = new LocalColorContext(Color.magenta);
        MAGENTA = magenta;
        cyan = new LocalColorContext(Color.cyan);
        CYAN = cyan;
        blue = new LocalColorContext(Color.blue);
        BLUE = blue;
        
        OTHER0 = new LocalColorContext(new Color(255,87,143));        
        CLUSTER11 = new LocalColorContext(new Color(117,117,117));
        QUEUEING12 = new LocalColorContext(new Color(232,232,232));        
        NETWORK7 = new LocalColorContext(new Color(156,157,108));
       	ADMINISTRATIVE3 = new LocalColorContext(new Color(84,84,29));
   		CONFIGURATION2 = new LocalColorContext(new Color(84,56,28));
   		COMMIT5 = new LocalColorContext(new Color(194,133,71));
   		APPLICATION1 = new LocalColorContext(new Color(194,71,71));
   		CONCURRENCY4 = new LocalColorContext(new Color(153,51,51));
   		SYSTEMIO9 = new LocalColorContext(new Color(0,161,230));
   		USERIO8 = new LocalColorContext(new Color(0,46,230));
   		SCHEDULER10 = new LocalColorContext(new Color(133,255,133));
   		CPU = new LocalColorContext(new Color(0,179,0));
    }
}
