/*
 *
 */

package ext.egantt.chart;

import com.egantt.model.drawing.axis.AxisInterval;
import java.util.Iterator;

public interface ChartModel
{

    public abstract Iterator keys();

    public abstract AxisInterval getInterval(Object obj, Object obj1);
}
