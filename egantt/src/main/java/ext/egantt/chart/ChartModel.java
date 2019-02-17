package ext.egantt.chart;

import java.util.Iterator;

import com.egantt.model.drawing.axis.AxisInterval;

public interface ChartModel
{
	//Iterator axises();
	
	/**
	 * Returns the set of keys to query 
	 */
	Iterator keys();
	
	/**
	 * Returns the <code>AxisInterval</code> for the
	 * underlying key
	 */
	AxisInterval getInterval(Object axis, Object key);
}