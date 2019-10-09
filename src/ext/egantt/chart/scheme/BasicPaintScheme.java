/*
 *
 */

package ext.egantt.chart.scheme;

import com.egantt.model.drawing.axis.AxisInterval;
import ext.egantt.drawing.painter.context.BasicPainterContext;
import java.awt.Color;

public class BasicPaintScheme extends BasicPainterContext
{

    public BasicPaintScheme()
    {
    }

    public Object get(Object key, String type)
    {
        if(type != "Paint" || !(key instanceof AxisInterval))
        {
            return super.get(key, type);
        } else
        {
            AxisInterval interval = (AxisInterval)key;
            int value = ((Long)interval.getFinish()).intValue() * 150;
            return new Color(value);
        }
    }
}
