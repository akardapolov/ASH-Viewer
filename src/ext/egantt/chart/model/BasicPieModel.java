/*
 *
 */

package ext.egantt.chart.model;

import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.interval.DoubleInterval;
import com.egantt.model.drawing.axis.interval.LongInterval;
import ext.egantt.chart.ChartModel;
import java.util.*;

public class BasicPieModel
    implements ChartModel
{

    public BasicPieModel()
    {
        map = new HashMap();
    }

    public Iterator keys()
    {
        return map.keySet().iterator();
    }

    public AxisInterval getInterval(Object axis, Object key)
    {
        return (AxisInterval)map.get(key);
    }

    public Object put(Object key, long offset)
    {
        long start = 0L;
        for(Iterator iter = map.values().iterator(); iter.hasNext();)
        {
            Number finish = (Number)((AxisInterval)iter.next()).getFinish();
            start = Math.max(start, finish.longValue());
        }

        return put(key, ((AxisInterval) (new LongInterval(start, start + offset))));
    }

    public Object put(Object key, double offset)
    {
        double start = 0.0D;
        for(Iterator iter = map.values().iterator(); iter.hasNext();)
        {
            Number finish = (Number)((AxisInterval)iter.next()).getFinish();
            start = Math.max(start, finish.doubleValue());
        }

        return put(key, ((AxisInterval) (new DoubleInterval(start, start + offset))));
    }

    public Object put(Object key, AxisInterval interval)
    {
        return map.put(key, interval);
    }

    protected Map map;
}
