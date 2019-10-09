/*
 *
 */

package ext.egantt.drawing.context;

import com.egantt.awt.graphics.GraphicsContext;
import java.awt.Color;

public class DefaultColorContext
{
    public static final class LocalColorContext
        implements GraphicsContext
    {

        public Object get(Object key, Object type)
        {
            return "Paint".equals(type) ? color : null;
        }

        public Color getColor()
        {
            return color;
        }

        protected final Color color;

        public LocalColorContext(Color color)
        {
            this.color = color;
        }
    }


    public DefaultColorContext()
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
    }
}
