package ext.egantt.chart.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.egantt.model.drawing.axis.AxisInterval;
import com.egantt.model.drawing.axis.interval.DoubleInterval;
import com.egantt.model.drawing.axis.interval.LongInterval;
import ext.egantt.chart.ChartModel;

/**
 * In the pie chart implementation values must be in the range [0..360] inclusive
 */
public class BasicPieModel implements ChartModel
{
	protected Map <Object, AxisInterval> map  = new HashMap<Object, AxisInterval>();
	
	//	------------------------------------------------------------------------
		
	public Iterator keys()
	{
		return map.keySet().iterator();
	}
	
	public AxisInterval getInterval(Object axis, Object key)
	{
		return (AxisInterval) map.get(key);
	}
	
	//	------------------------------------------------------------------------

	public Object put(Object key, long offset)
	{
		long start = 0; 		
		for (Iterator iter = map.values().iterator(); iter.hasNext();)
		{
			Number finish = (Number) ((AxisInterval) iter.next()).getFinish();
			start = Math.max(start, finish.longValue());
		}
		return put(key, new LongInterval(start, start + offset));
	}
	
	public Object put(Object key, double offset)
	{
		double start = 0; 		
		for (Iterator iter = map.values().iterator(); iter.hasNext();)
		{
			Number finish = (Number) ((AxisInterval) iter.next()).getFinish();
			start = Math.max(start, finish.doubleValue());
		}
		return put(key, new DoubleInterval(start, start + offset));
	}

	//	------------------------------------------------------------------------
	
	public Object put(Object key, AxisInterval interval)
	{
		return map.put(key, interval);
	}
}